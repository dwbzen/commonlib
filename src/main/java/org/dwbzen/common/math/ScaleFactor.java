package org.dwbzen.common.math;

/**
 * Used to scale Point2D<Double> to Point2D<Integer> for example mapping points to a screen range
 * such as 1024 x 768. </br>The scaled boolean indicates to a client whether the scale should be applied or not.
 * 
 * @author DBacon
 *
 */
public class ScaleFactor {
	private OrderedPair<Integer, Integer> xRange = null;
	private OrderedPair<Integer, Integer> yRange = null;
	private boolean scaled = false;
	
	public ScaleFactor() {
		xRange = new  OrderedPair<Integer, Integer>(0,0);
		yRange = new  OrderedPair<Integer, Integer>(0,0);
	}
	
	public ScaleFactor( OrderedPair<Integer, Integer> xrange, OrderedPair<Integer, Integer> yrange, boolean isScaled) {
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
		xRange = new  OrderedPair<Integer, Integer>(0,xyrange.getFirst());
		yRange = new  OrderedPair<Integer, Integer>(0,xyrange.getSecond());
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
