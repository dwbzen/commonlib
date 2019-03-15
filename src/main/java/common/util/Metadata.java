package common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Metadata implements IJson {

	private static final long serialVersionUID = -2899190433409143596L;
	/*
	 * Some standard Keys
	 */
	public static final String NAME_KEY = "name";
	public static final String URL_KEY = "url";
	public static final String DESCRIPTION_KEY = "description";
	

	@JsonProperty	private Properties properties = new Properties();
	@JsonProperty	private Map<String, Number> numberMap = new HashMap<String, Number>();
	
	public Metadata() {
		
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
	
	public Number getNumberProperty(String key) {
		return numberMap.get(key);
	}
	
	public Integer getIntegerProperty(String key) {
		Number n = numberMap.get(key);
		return (n != null) ? Integer.valueOf(n.intValue()) : null;
	}
	
	public Double getDoubleProperty(String key) {
		Number n = numberMap.get(key);
		return (n != null) ? Double.valueOf(n.intValue()) : null;
	}
	
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
	
	public void setNumberProperty(String key, Number value) {
		numberMap.put(key, value);
	}
	
	public void setIntegerProperty(String key, Integer value) {
		numberMap.put(key, value);
	}
	
	public void setDoubleProperty(String key, Double value) {
		numberMap.put(key, value);
	}
	
	
	@Override
	public String toJson() {
		return  toJson();
	}
	
	public static void main(String...args) {
		Metadata metadata = new Metadata();
		if(args != null && args.length>0) {
			int ind = args[0].indexOf(':');
			String key = args[0].substring(0, ind);
			String val = args[0].substring(ind+1);
			metadata.setProperty(key, val);
		}
		else {
			metadata.setProperty("name", "TestMetadata");
		}
		metadata.setProperty("date", "2017-02-18");
		metadata.setDoubleProperty("pi", Double.valueOf(3.14159));
		metadata.setIntegerProperty("age", 65);
		metadata.setNumberProperty("maxNumber", Double.MAX_VALUE);
		String js = metadata.toJson(true);
		System.out.println(js);
		
	}

}
