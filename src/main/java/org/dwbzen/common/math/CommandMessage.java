package org.dwbzen.common.math;

import java.io.IOException;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.dwbzen.common.util.INameable;

/**
 * { "name" : "ifs1", "type" : "message", "command" : "SHUTDOWN" }
 * 
 * @author don_bacon
 *
 */
public class CommandMessage extends BaseJsonObject {

	public static final String MESSAGE_TYPE = "message";
	public static final String objectType = MESSAGE_TYPE;
	public static final Pattern commandRegex = Pattern.compile("(name:.+),(type:.+),(command:.+)");
	private static final long serialVersionUID = -322492144640842630L;
	
	@JsonProperty	private String command = null;
	
	public CommandMessage() {
		super(INameable.DEFAULT_NAME, MESSAGE_TYPE);
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
		CommandMessage commandMessage = null;
		try {
			commandMessage = mapper.readValue(jsonstr, CommandMessage.class);
		} catch (JsonParseException e) {
			log.error("JsonParseException (CommandMessage): " + jsonstr);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.error("JsonMappingException (CommandMessage): " + jsonstr);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException: " + jsonstr);
			e.printStackTrace();
		}
		return commandMessage;
	}
	
	public void addFieldValue(String valueString) {
		String[] fv = valueString.split(":");
		String fname = fv[0];

		String fval = (fv.length == 2) ? fv[1]  : "{" + fv[1] + ":" + fv[2] + "}";
		if(fname.equalsIgnoreCase("name")) {
			setName(fval);
		}
		else if(fname.equalsIgnoreCase("type")) {
			setType(fval);
		}
		else if(fname.equalsIgnoreCase("command")) {
			setCommand(fval);
		}
	}

	@Override
	public String toString() {
		return toJson(true);
	}

}
