package org.dwbzen.common.math;

import java.util.regex.Matcher;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * { "name" : "ifs1", "type" : "message", "command" : "SHUTDOWN" }
 * 
 * @author don_bacon
 *
 */
public class BaseJsonObject extends JsonObject {

	private static final long serialVersionUID = 971201573697726504L;
	static ObjectMapper mapper = new ObjectMapper();
	
	public BaseJsonObject() {
		setType(UNKNOWN);
	}
	public BaseJsonObject(String name) {
		setName( name);
		setType(UNKNOWN);
	}
	public BaseJsonObject(String name, String type) {
		setName(name);
		setType(type);
	}
	
	public static BaseJsonObject fromJson(String jsonstr) {
		String raw = jsonstr.replaceAll("[\"\\s{}]", "");	// deletes spaces, curly braces and quotes
		Matcher m = baseJsonRegex.matcher(raw);
		BaseJsonObject cm = null;
		if(m.matches()) {
			log.debug("# groups: " + m.groupCount());
			cm = new BaseJsonObject();
			for(int i=1; i<=m.groupCount(); i++) {
				log.debug("group: " + i + "= " + m.group(i));
				BaseJsonObject.addFieldValue(cm, m.group(i));
			}
		}
		
		return cm;
	}
	public static void addFieldValue(BaseJsonObject cm, String valueString) {
		String[] fv = valueString.split(":");
		String fname = fv[0];
		String fval = (fv.length == 2) ? fv[1]  : "{" + fv[1] + ":" + fv[2] + "}";
		if(fname.equalsIgnoreCase("name")) {
			cm.setName(fval);
		}
		else if(fname.equalsIgnoreCase("type")) {
			cm.setType(fval);
		}
	}
	
	@Override
	public String toString() {
		return toJson(true);
	}

}
