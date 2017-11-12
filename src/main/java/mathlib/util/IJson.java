package mathlib.util;

import java.io.Serializable;

import org.mongodb.morphia.Morphia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: refactor the classes implementing this interface to use the Jackson toJson().
 * TODO: remove all Morphia dependencies for JSON.
 * 
 * @author don_bacon
 *
 */
public interface IJson extends Serializable {
	static Morphia morphia = new Morphia();
	static ObjectMapper mapper = new ObjectMapper();
	
	default String toJSON() {
		return morphia.toDBObject(this).toString();
	}
	
	default String toJson() {
		String result = null;
		try {
			result = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return result;
	}
}
