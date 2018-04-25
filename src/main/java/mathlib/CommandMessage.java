package mathlib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * { "name" : "ifs1", "type" : "message", "command" : "SHUTDOWN" }
 * 
 * @author don_bacon
 *
 */
public class CommandMessage extends BaseJSONObject {

	public static final String MESSAGE_TYPE = "message";
	public static final Pattern commandRegex = Pattern.compile("(name:.+),(type:.+),(command:.+)");
	private static final long serialVersionUID = -322492144640842630L;
	
	private String command;
	
	public CommandMessage() {
		super(NAME, MESSAGE_TYPE);
	}
	public CommandMessage(String name, String command) {
		super(name, MESSAGE_TYPE);
		this.command = command;
	}

	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
	public static CommandMessage fromJSONString(String jsonstr) {
		String raw = jsonstr.replaceAll("[\"\\s{}]", "");	// deletes spaces, curly braces and quotes
		Matcher m = commandRegex.matcher(raw);
		CommandMessage cm = null;
		if(m.matches()) {
			log.debug("# groups: " + m.groupCount());
			cm = new CommandMessage();
			for(int i=1; i<=m.groupCount(); i++) {
				log.debug("group: " + i + "= " + m.group(i));
				CommandMessage.addFieldValue(cm, m.group(i));
			}
		}
		
		return cm;
	}
	public static void addFieldValue(CommandMessage cm, String valueString) {
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
		else if(fname.equalsIgnoreCase("command")) {
			cm.setCommand(fval);
		}
	}
	
	@Override
    public String toJson() {
        StringBuilder jsonstr = new StringBuilder("{");
        String name = getProperty(NAME);
        String type = getProperty(TYPE);
        String id = getId();
        if(id != null) {
            jsonstr.append("\"_id\": " + id + ",");
        }
        if(name != null){
            jsonstr.append("\"name\": \"" + name + "\",");
        }
        if(type != null) {
            jsonstr.append("\"type\": \"" + type + "\",");
        }
        jsonstr.append("\"command\": \"" + command + "\" }" );
        return jsonstr.toString();

    }

	@Override
	public String toString() {
		return toJson();
	}

}
