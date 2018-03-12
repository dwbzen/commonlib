package mathlib.util;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 
 * @author don_bacon
 *
 */
public interface IJson extends Serializable {
	static ObjectMapper mapper = new ObjectMapper();
	static final String QUOTE = "\"";

	@Deprecated
	default String toJSON() {
		return toJson();
	}
	
	default String toJson(boolean pretty) {
		String result = null;
		if(pretty) {
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
		}
		mapper.configure(SerializationFeature.INDENT_OUTPUT, pretty);
		try {
			result = pretty ? mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this) :
							  mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			System.err.println("Cannot serialize because " + e.toString());
			e.printStackTrace();
		}
		return result;
	}
	
	default String toJson() {
		String result = null;
		try {
			result = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			System.err.println("Cannot serialize because " + e.toString());
			e.printStackTrace();
		}
		return result;
	}

	default String quote(String key, String val) {
		return QUOTE.concat(key).concat(QUOTE).concat(": ").concat(QUOTE).concat(val).concat(QUOTE);
	}
	default String quote(String key, int val) {
		return QUOTE.concat(key).concat(QUOTE).concat(": ").concat(String.valueOf(val));
	}
	default String quote(String key, Double val) {
		return QUOTE.concat(key).concat(QUOTE).concat(": ").concat(String.valueOf(val));
	}
	default String quote(String s) {
		return QUOTE.concat(s).concat(QUOTE);
	}
}
