package common.math;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

import common.math.ifs.IteratedFunctionSystem;
import common.util.IJson;
import common.util.INameable;

/**
 * NOTE - dropped MongoDB support for now. Need to update queries to NOT return the _id
 * 
 * @author don_bacon
 *
 */
public class JsonObject implements IJson, INameable  {

	private static final long serialVersionUID = 347831929602095478L;
	public static final Pattern baseJsonRegex = Pattern.compile("(name:.+),(type:.+)");
	public static final String UNKNOWN = "unknown";
	public static final String QUOTE = "\"";
	public static final String TYPE = UNKNOWN;
	protected static final Logger log = LogManager.getLogger(IJson.class);
	public static final List<String> typeList = new ArrayList<>();
	static {
		typeList.add("\"stats\"");
		typeList.add("\"message\"");
		typeList.add("\"Point2D\"");
		typeList.add("\"IFS\"");
	}
	
	@JsonProperty	protected String name = UNKNOWN;
	@JsonProperty	protected String type = TYPE;
	
	/**
	 * Derived classes will want to overload
	 */
	public String toString() {
		return "name: " + (name==null ? "n/a" : name) + (type==null ? "n/a" : type) ;
	}
	
	/**
	 * Returns the type:<typestring> type string</br>
	 * There are 4 recognized types: message, IFS, Point2D, and stats
	 * @param jsonstr
	 * @return 
	 */
	public static String getType(String jsonstr) {
		String type = UNKNOWN;
		for(String ts : typeList) {
			if(jsonstr.contains(ts)) {
				type = ts.replaceAll("\"", "");
				break;
			}
		}
		return type;
	}

    public static JsonObject analyzeMessage(String messageText) {
        String type = JsonObject.getType(messageText);
        JsonObject obj = null;
          log.trace("process :" + messageText + "\n " + type);
          if(type.equals("message")) {
              obj = CommandMessage.fromJSONString(messageText);
          }
          else if(type.equals("Point2D")) {
              obj = Point2D.fromJson(messageText);
          }
          else if(type.equals("stats")) {
              obj = PointSetStats.fromJson(messageText);
          }
          else if(type.equals("IFS")) {
        	  obj = IteratedFunctionSystem.fromJson(messageText);
          }
          else {
              log.error("JSONObject: Unknown message type: " + messageText);
              obj = BaseJsonObject.fromJson(messageText);
          }
          return obj;
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
	
}
