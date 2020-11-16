package org.dwbzen.common.util;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Single configuration class. Usage example: <br>
 * Configuration config = Configuration.getInstance("/config.properties");<br>
 * Properties properties = config.getProperties();
 * 
 * @author don_bacon
 *
 */
public class Configuration {
	protected static final Logger log = LogManager.getLogger(Configuration.class);
	/**
	 * Default configuration file
	 */
	public static final String CONFIG_FILENAME = "/config.properties";
	private static Configuration _configuration = null;
	
	private Properties properties = null;
	private String configurationFilename = null;
	
	private Configuration() {
	}
	private Configuration(String configFilename) {
		properties = new Properties();
	}
	
	public static Configuration getInstance(String configFile) {
		Configuration configuration = null;
		synchronized(Configuration.class){
				configuration = new Configuration(configFile);
				configuration.configurationFilename = configFile;
				configuration.loadProperties();
		}
		return configuration;
	}
	
	public static Configuration getInstance() {
		return getInstance(CONFIG_FILENAME);
	}
	
	private void loadProperties() {
        URL url = getClass().getResource(configurationFilename);
        if(url == null) {
            throw new IllegalArgumentException("Could not load resource: \"" + configurationFilename + "\"");
        }
        try(InputStream stream = url.openStream()) {
        	properties.load(stream);
        }
        catch(Exception e) {
        	log.error("Could not load " + configurationFilename + " " + e.toString());
        }
	}
	
	public void addConfiguration(Configuration someOtherConfiguration) {
		properties.putAll(someOtherConfiguration.getProperties());
	}
	
	public Properties getProperties() {
		return this.properties;
	}
	
	public static Configuration getConfiguration() throws IllegalAccessError {
		if(_configuration == null) {
			// not set globally - raise an exception, probably a coding error
			throw new IllegalAccessError("Global Configuration is not set");
		}
		return _configuration;
	}
	public static void setConfiguration(Configuration config) {
		_configuration = config;
	}
}
