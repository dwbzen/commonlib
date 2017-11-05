package mathlib;

import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.mongodb.morphia.annotations.Property;

/**
 * NOTE - dropped MongoDB support for now. Need to update queries to NOT return the _id
 * TODO: rewrite using Jackson
 * 
 * @author don_bacon
 *
 */
public abstract class JSONObject implements Serializable  {

	private static final long serialVersionUID = 347831929602095478L;
	protected static final Logger log = LogManager.getLogger(JSONObject.class);

	public static final Pattern JSON_REGEX = Pattern.compile("(name:.+),(type:.+),(.+?)");
	public final static String UNKNOWN = "unknown";
	public static final String NAME = "name";	// Property
	public static final String TYPE = "type";	// Property
	static final String QUOTE = "\"";
	
	@Property	protected String _id;		// optional ID - 24 char hex string
	@Property	protected Map<String, String> properties = new HashMap<String, String>();
	
	public abstract String toJSON();
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
       		obj = Point2D.fromJSONString(messageText);
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
	
	public static void parseJSONMessage(String messageText) {
		 JsonParser parser = Json.createParser(new StringReader(messageText));
		 String eventType;
		 Event event = null;
		 String keyName;
		 String valueString;
		 while(parser.hasNext()) {
			 event = parser.next();
			 eventType = event.toString();
			 System.out.println(event.toString());
			 if(eventType.equals("KEY_NAME")) {
				 keyName = parser.getString();
				 System.out.println("   " + keyName);
			 }
			 else if(eventType.equals("VALUE_STRING")) {
				 valueString = parser.getString();
				 System.out.println("   " + valueString);
			 }
		 }
		 parser.close();
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
			sb.append(quote(key,  properties.get(key)));
			sb.append(",");
		}
		return sb.deleteCharAt(sb.length()-1).toString();
	}
	
	/**
	 * Surrounds the given string with quote characters.
	 * Suitable for use in JSON string.
	 * @param s
	 * @return
	 */
	public static String quote(String s) {
		return QUOTE.concat(s).concat(QUOTE);
	}
	
	public static String quote(String key, String val) {
		return QUOTE.concat(key).concat(QUOTE).concat(": ").concat(QUOTE).concat(val).concat(QUOTE);
	}
	public static String quote(String key, int val) {
		return QUOTE.concat(key).concat(QUOTE).concat(": ").concat(String.valueOf(val));
	}
	public static String quote(String key, Double val) {
		return QUOTE.concat(key).concat(QUOTE).concat(": ").concat(String.valueOf(val));
	}
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
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
	
	/**
	 * Test of the different message types
	 * @param args
	 */
	public static void main(String[] args) {
		// "_id" : { "$oid" : "52e331a8781aae933310ea29"}
		String sample1 = " { \"name\" : \"ifs2\", \"type\" : \"message\", \"command\" : \"SHUTDOWN\" }";
		String sample2 = " { \"name\" : \"ifs1\", \"type\" : \"point\", \"Point2D\" : [  0.1677348,  0.5156566 ] }";
		String sample3 = " {\"LinearFunction\" : \"f3:{ [ 0.5, 0, 0 ], [ 0, 0.5, 0.5 ]}\" ,\"name\" : \"ifs2\", \"type\" : \"stats\", \"n\" : 10, \"minX\" : 0.1251607, \"minY\" : 0.002710343, \"maxX\" : 0.8173904, \"maxY\" : 0.712295, \"minPoint\" : [  0.1298051,  0.08795198 ], \"maxPoint\" : [  0.2598582,  0.6771501 ] }";
		System.out.println(JSONObject.getType(sample1));	// message
		System.out.println(JSONObject.getType(sample2));	// point
		System.out.println(JSONObject.getType(sample3));	// stats

			
	}
}
