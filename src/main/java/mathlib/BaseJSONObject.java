package mathlib;

import java.util.regex.Matcher;

/**
 * { "_id" : ObjectId("52e331aa781aae93331270ca"), "name" : "ifs1", "type" : "message", "command" : "SHUTDOWN" }
 * { "_id" : { "$oid" : "52e331a8781aae933310ea29"}, "name" : "ifs1", "type" : "message", "command" : "SHUTDOWN" }
 * { "name" : "ifs1", "type" : "message", "command" : "SHUTDOWN" }
 * 
 * @author don_bacon
 *
 */
public class BaseJSONObject extends JSONObject {

	private static final long serialVersionUID = 971201573697726504L;
	
	public BaseJSONObject() {
		setProperty(TYPE, UNKNOWN);
	}
	public BaseJSONObject(String name) {
		setProperty(NAME, name);
		setProperty(TYPE, UNKNOWN);
	}
	public BaseJSONObject(String name, String type) {
		setProperty(NAME, name);
		setProperty(TYPE, type);
	}
	
	public static BaseJSONObject fromJSONString(String jsonstr) {
		String raw = jsonstr.replaceAll("[\"\\s{}]", "");	// deletes spaces, curly braces and quotes
		Matcher m = baseJsonRegex.matcher(raw);
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
			cm.setId(fval);
		}
		else if(fname.equalsIgnoreCase("type")) {
			cm.setType(fval);
		}
	}
	
	@Override
	public String toJson() {
		StringBuilder jsonstr = new StringBuilder("{");
		String name = getProperty(NAME);
		String type = getProperty(TYPE);
		String id = getId();
		if(id != null) {
			jsonstr.append("_id:" + id + ",");
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
		return toJson();
	}

}
