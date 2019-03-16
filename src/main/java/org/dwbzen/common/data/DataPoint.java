package org.dwbzen.common.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.dwbzen.common.util.IJson;

/**
 * A DataPoint is an abstraction of a point that is an element of a larger data structure.
 * The "shape" of the point is given by dimensions which is the number of indices needed to 
 * uniquely identify the point. The rank of the point is given by depth.
 * For example a 3D point in R^3 is specified by x, y and z coordinates
 * so dimensions is 3, and depth is 1.
 * A 3x4 matrix point has dimensions {3, 4} and depth 2.
 * A scalar has an empty list as dimensions and a depth of 0.
 *
 * @param <T>
 */
public abstract class DataPoint<T extends Number> implements IJson {

	private static final long serialVersionUID = -8850267519541590336L;
	@JsonProperty	private List<Integer> dimensions = new ArrayList<Integer>();
	@JsonProperty	private int depth = 1;
	
	public List<Integer> getDimensions() {
		return dimensions;
	}
	protected void setDimensions(List<Integer> dimensions) {
		this.dimensions = dimensions;
	}
	public int getDepth() {
		return depth;
	}
	protected void setDepth(int depth) {
		this.depth = depth;
	}
	

	
}
