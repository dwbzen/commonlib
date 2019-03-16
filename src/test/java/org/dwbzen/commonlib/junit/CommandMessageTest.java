package org.dwbzen.commonlib.junit;

import junit.framework.TestCase;
import org.dwbzen.common.math.CommandMessage;

public class CommandMessageTest   extends TestCase {

	public void testToJson() {
		//{"name":"ifs2","type":"message","command":"START"}
		String sample = " {\"name\" : \"ifs2\", \"type\" : \"message\", \"command\" : \"SHUTDOWN\" }";
		CommandMessage cm = CommandMessage.fromJSONString(sample);
		String expected = "{\"name\":\"ifs2\",\"type\":\"message\",\"command\":\"SHUTDOWN\"}";
		assertEquals(expected, cm.toJson());
	}
	
	public void testFromJson() {
		//{"name":"ifs2","type":"message","command":"START"}
		String sample2 = " {\"name\" : \"ifs2\", \"type\" : \"message\", \"command\" : \"START\" }";
		CommandMessage cm = CommandMessage.fromJSONString(sample2);
		String expected = "{\"name\":\"ifs2\",\"type\":\"message\",\"command\":\"START\"}";
		assertEquals(expected, cm.toJson());
	}
	
	public void testCreateCommandMessage() {
		CommandMessage cmShutdown = new CommandMessage("test", "SHUTDOWN");
		String expected = "{\"name\":\"test\",\"type\":\"message\",\"command\":\"SHUTDOWN\"}";
		assertEquals(expected, cmShutdown.toJson());
	}

}
