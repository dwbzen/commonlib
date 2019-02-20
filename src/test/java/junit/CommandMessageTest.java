package junit;

import junit.framework.TestCase;
import mathlib.BaseJsonObject;
import mathlib.CommandMessage;

public class CommandMessageTest   extends TestCase {

	public void testToJson() {
		String sample = " {\"name\" : \"ifs2\", \"type\" : \"message\", \"command\" : \"SHUTDOWN\" }";
		CommandMessage cm = CommandMessage.fromJSONString(sample);
		String expected = "{\"name\": \"ifs2\",\"type\": \"message\",\"command\": \"SHUTDOWN\" }";
		assertEquals(expected, cm.toJson());
	}
	
	public void testFromJson() {
		String sample2 = " {\"name\" : \"ifs2\", \"type\" : \"message\", \"command\" : \"START\" }";
		CommandMessage cm = CommandMessage.fromJSONString(sample2);
		String expected = "{\"name\": \"ifs2\",\"type\": \"message\",\"command\": \"START\" }";
		assertEquals(expected, cm.toJson());
	}
	
	public void testCreateCommandMessage() {
		CommandMessage cmShutdown = new CommandMessage("test", "SHUTDOWN");
		String expected = "{\"name\": \"test\",\"type\": \"message\",\"command\": \"SHUTDOWN\" }";
		assertEquals(expected, cmShutdown.toJson());
	}
	
	public void testBsonObject() {
		String sample = " { \"_id\" : \"52b30e7eba2912447205bd4e\", \"name\" : \"ifs2\", \"type\" : \"unknown\" }";
		BaseJsonObject cm = BaseJsonObject.fromJson(sample);
		String expected = "{_id:52b30e7eba2912447205bd4e,name:\"ifs2\",type:\"unknown\" }";
		assertEquals(expected, cm.toJson());
	}
}
