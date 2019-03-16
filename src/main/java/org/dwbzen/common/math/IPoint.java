package org.dwbzen.common.math;

import java.util.List;

import org.dwbzen.common.util.IJson;

public interface IPoint extends IJson {
	List<Number> getCoordinates();
	
}
