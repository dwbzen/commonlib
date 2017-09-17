package math;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * { "_id" : ObjectId("52e331aa781aae93331270ca"), "name" : "ifs1", "type" : "message", "command" : "SHUTDOWN" }
 * { "_id" : { "$oid" : "52e331a8781aae933310ea29"}, "name" : "ifs1", "type" : "message", "command" : "SHUTDOWN" }
 * { "name" : "ifs1", "type" : "message", "command" : "SHUTDOWN" }
 * 
 * @author don_bacon
 *
 */
public class BaseJSONObject extends JSONObject {

	private static final long serialVersionUID = -322492144640842630L;
	public static final Pattern JSON_REGEX = Pattern.compile("(_id:.+),(name:.+),(type:.+)");
	protected static final Logger log = LogManager.getLogger(BaseJSONObject.class);
	
	public static void main(String[] args) {
		if(args.length >= 0) {
			String sample = " { \"_id\" : ObjectId(\"52b30e7eba2912447205bd4e\"), \"name\" : \"ifs2\", \"type\" : \"unknown\" }";
			BaseJSONObject cm = BaseJSONObject.fromJSONString(sample);
			System.out.println(cm.toJSON());
			sample = " { \"_id\" :  { \"$oid\" : \"52e331a8781aae933310ea29\"}, \"name\" : \"ifs2\", \"type\" : \"unknown\" }";
			cm = BaseJSONObject.fromJSONString(sample);
			System.out.println(cm.toJSON());
			
		}
	}
	
	public BaseJSONObject() {
		setProperty(TYPE, UNKNOWN);
	}
	public BaseJSONObject(String name, String command) {
		setProperty(NAME, name);
		setProperty(TYPE, UNKNOWN);
	}
	
	public static BaseJSONObject fromJSONString(String jsonstr) {
		String raw = jsonstr.replaceAll("[\"\\s{}]", "");	// deletes spaces, curly braces and quotes
		Matcher m = JSON_REGEX.matcher(raw);
		BaseJSONObject cm = null;
		if(m.matches()) {
			log.debug("# groups: " + m.groupCount());
			cm = new BaseJSONObject();
			for(int i=1; i<=m.groupCount(); i++) {
				log.debug("group: " + i + "= " + m.group(i));
				BaseJSONObject.addFieldValue(cm, m.group(i));
			}
		}
		
		return cm;
	}
	public static void addFieldValue(BaseJSONObject cm, String valueString) {
		String[] fv = valueString.split(":");
		String fname = fv[0];
		String fval = (fv.length == 2) ? fv[1]  : "{" + fv[1] + ":" + fv[2] + "}";
		if(fname.equalsIgnoreCase("name")) {
			cm.setName(fval);
		}
		else if(fname.equalsIgnoreCase("_id")) {
			cm.set_id(fval);
		}
		else if(fname.equalsIgnoreCase("type")) {
			cm.setType(fval);
		}
	}
	
	@Override
	public String toJSON() {
		StringBuffer jsonstr = new StringBuffer("{");
		String name = getProperty(NAME);
		String type = getProperty(TYPE);
		if(_id != null) {
			jsonstr.append("_id:" + _id + ",");
		}
		if(name != null){
			jsonstr.append("name:\"" + name + "\",");
		}
		if(type != null) {
			jsonstr.append("type:\"" + type + "\" }");
		}
		return jsonstr.toString();

	}
	
	@Override
	public String toString() {
		return toJSON();
	}

}
