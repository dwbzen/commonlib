package mathlib;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.util.IJson;

/**
 * NOTE - dropped MongoDB support for now. Need to update queries to NOT return the _id
 * TODO: rewrite using Jackson
 * 
 * @author don_bacon
 *
 */
public abstract class JSONObject implements IJson  {

	private static final long serialVersionUID = 347831929602095478L;
	protected static final Logger log = LogManager.getLogger(JSONObject.class);

	public static final Pattern JSON_REGEX = Pattern.compile("(name:.+),(type:.+),(.+?)");
	public final static String UNKNOWN = "unknown";
	public static final String NAME = "name";	// Property
	public static final String TYPE = "type";	// Property
	static final String QUOTE = "\"";
	
	@JsonIgnore	private String id;		// optional ID
	@JsonProperty	private Map<String, String> properties = new HashMap<>();
	
	public abstract String toString();
	
	/**
	 * Returns the type:<typestring> type string
	 * 
	 * @param jsonstr
	 * @return
	 */
	public static String getType(String jsonstr) {
		String type = null;
		String raw = jsonstr.replaceAll("[\"\\s{}]", "");	// deletes spaces, curly braces and quotes
		int n = raw.indexOf("name");
		int index = (n<=0) ? 0 : n;
		Matcher m = JSON_REGEX.matcher(raw.substring(index));
		int ind = 0;
		if(m.matches()) {
			type = m.group(2);
			if( (ind=type.indexOf(',')) > 0) {
				type = type.substring(0, ind);
			}
		}
		else {
			type = "type:" + UNKNOWN;
		}
		String ts[] = type.split(":");
		return ts.length==2 ? ts[1] : UNKNOWN;
	}

    public static JSONObject analyzeMessage(String messageText) {
        String type = JSONObject.getType(messageText);
        JSONObject obj = null;
          log.trace("process :" + messageText + "\n " + type);
          if(type.equals("message")) {
              obj = CommandMessage.fromJSONString(messageText);
          }
          else if(type.equals("point")) {
              obj = Point2D.fromJson(messageText);
          }
          else if(type.equals("stats")) {
              obj = PointSet.fromJSONString(messageText);
          }
          else if(type.equalsIgnoreCase(JSONObject.UNKNOWN)) {
              log.error("JSONObject: Unknown message type: " + messageText);
              obj = BaseJSONObject.fromJSONString(messageText);
          }
          return obj;
   }

	/**
	 * Represents properties as JSON string.
	 * Key names are quoted as are key values.
	 * Key value pairs separated by comma as in: "key1": "val1", "key2": "val2" etc.
	 * No comma appended to the last element.
	 * @return JSON String
	 */
	public String getJSONProperties() {
		StringBuffer sb = new StringBuffer();
		for(String key : properties.keySet()) {
			sb.append(quoteString(key,  properties.get(key)));
			sb.append(",");
		}
		return sb.deleteCharAt(sb.length()-1).toString();
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return properties.get(NAME);
	}
	public void setName(String name) {
		properties.put(NAME, name);
	}
	public String getType() {
		return  properties.get(TYPE);
	}
	
	protected void setType(String type) {
		properties.put(TYPE, type);
	}

	public String getProperty(String arg0) {
		return properties.get(arg0);
	}
	
	public void setProperty(String key, String value) {
		properties.put(key, value);
	}
	
	protected Map<String, String> getProperties() {
		return properties;
	}
	
}
