package mathlib;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.util.IJson;
import mathlib.util.INameable;

/**
 * NOTE - dropped MongoDB support for now. Need to update queries to NOT return the _id
 * 
 * @author don_bacon
 *
 */
public class JsonObject implements IJson, INameable  {

	public static final Pattern baseJsonRegex = Pattern.compile("(name:.+),(type:.+)");
	public static final String UNKNOWN = "unknown";
	public static final String QUOTE = "\"";
	public static final String TYPE = "type";
	protected static final Logger log = LogManager.getLogger(IJson.class);

	private static final long serialVersionUID = 347831929602095478L;
	
	@JsonProperty	private String name = null;
	@JsonProperty	private String type = null;
	@JsonProperty	private Map<String, String> properties = new HashMap<>();
	
	/**
	 * Derived classes will want to overload
	 */
	public String toString() {
		return "name: " + (name==null ? "n/a" : name) + (type==null ? "n/a" : type) + (properties.size()>0 ? properties.toString() : "");
	}
	
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
		Matcher m = baseJsonRegex.matcher(raw.substring(index));
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
		String[] ts = type.split(":");
		return ts.length==2 ? ts[1] : UNKNOWN;
	}

    public static JsonObject analyzeMessage(String messageText) {
        String type = JsonObject.getType(messageText);
        JsonObject obj = null;
          log.trace("process :" + messageText + "\n " + type);
          if(type.equals("message")) {
              obj = CommandMessage.fromJSONString(messageText);
          }
          else if(type.equals("point")) {
              obj = Point2D.fromJson(messageText);
          }
          else if(type.equals("stats")) {
              obj = PointSet.fromJson(messageText);
          }
          else if(type.equalsIgnoreCase(JsonObject.UNKNOWN)) {
              log.error("JSONObject: Unknown message type: " + messageText);
              obj = BaseJsonObject.fromJson(messageText);
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
	public String getJsonProperties() {
		StringBuilder builder = new StringBuilder();
		properties.entrySet().forEach(s-> builder.append(quoteString(s.getValue())).append(","));
		return builder.deleteCharAt(builder.length()-1).toString();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return  type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getProperty(String arg0) {
		return properties.get(arg0);
	}
	
	public void setProperty(String key, String value) {
		properties.put(key, value);
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}
	
}
