package org.dwbzen.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 
 * @author don_bacon
 *
 */
public interface IJson {
	static ObjectMapper mapper = new ObjectMapper();
	static final String QUOTE = "\"";
	
	default String toJson(boolean pretty) {
		String result = null;
		if(pretty) {
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
		}
		mapper.configure(SerializationFeature.INDENT_OUTPUT, pretty);
		mapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, false);
		mapper.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, false);
		mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
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
		mapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, false);
		try {
			result = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			System.err.println("Cannot serialize because " + e.toString());
			e.printStackTrace();
		}
		return result;
	}

	default String quoteString(String key, String val) {
		return QUOTE.concat(key).concat(QUOTE).concat(": ").concat(QUOTE).concat(val).concat(QUOTE);
	}
	default String quoteString(String key, int val) {
		return QUOTE.concat(key).concat(QUOTE).concat(": ").concat(String.valueOf(val));
	}
	default String quoteString(String key, Double val) {
		return QUOTE.concat(key).concat(QUOTE).concat(": ").concat(String.valueOf(val));
	}
	default String quoteString(String s) {
		return QUOTE.concat(s).concat(QUOTE);
	}
}
