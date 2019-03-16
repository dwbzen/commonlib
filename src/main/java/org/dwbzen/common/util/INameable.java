package org.dwbzen.common.util;

import java.io.Serializable;

/**
 * Predictably enough, something that has a name.
 * @author DBacon
 *
 */
public interface INameable extends Serializable {
	
	public static final String DEFAULT_NAME = "Unnamed";
	
	void setName(String name);
	String getName();

}
