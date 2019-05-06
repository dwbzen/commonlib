package org.dwbzen.common.math;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used to scale Point2D<Double> to Point2D<Integer> for example mapping points to a screen range
 * such as 1024 x 768. </br>The scaled boolean indicates to a client whether the scale should be applied or not.
 * 
 * @author don_bacon
 *
 */
public class ScaleFactor extends JsonObject {

	private static final long serialVersionUID = 1L;
	public static final String objectType = "scaleFactor";
	
	@JsonProperty	private OrderedPair<Integer, Integer> xRange = null;
	@JsonProperty	private OrderedPair<Integer, Integer> yRange = null;
	@JsonProperty	private boolean scaled = false;
	
	public ScaleFactor() {
		setType(objectType);
		xRange = new  OrderedPair<Integer, Integer>(0,0);
		yRange = new  OrderedPair<Integer, Integer>(0,0);
	}
	
	public ScaleFactor( OrderedPair<Integer, Integer> xrange, OrderedPair<Integer, Integer> yrange, boolean isScaled) {
		setType(objectType);
		xRange = xrange;
		yRange = yrange;
		scaled = isScaled;
	}
	
	/**
	 * Assumes 0-origin
	 * 
	 * @param xyrange
	 * @param isScaled
	 */
	public ScaleFactor(OrderedPair<Integer, Integer> xyrange, boolean isScaled) {
		setType(objectType);
		xRange = new  OrderedPair<Integer, Integer>(0,xyrange.getFirst());
		yRange = new  OrderedPair<Integer, Integer>(0,xyrange.getSecond());
		scaled = isScaled;
	}
	
	public ScaleFactor(OrderedPair<Integer, Integer> xyrange) {
		this(xyrange, true);
	}

	public OrderedPair<Integer, Integer> getxRange() {
		return xRange;
	}

	public void setxRange(OrderedPair<Integer, Integer> xRange) {
		this.xRange = xRange;
	}

	public OrderedPair<Integer, Integer> getyRange() {
		return yRange;
	}

	public void setyRange(OrderedPair<Integer, Integer> yRange) {
		this.yRange = yRange;
	}

	public boolean isScaled() {
		return scaled;
	}

	public void setScaled(boolean scaled) {
		this.scaled = scaled;
	}

}
