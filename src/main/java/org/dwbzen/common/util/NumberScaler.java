package org.dwbzen.common.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.dwbzen.common.math.OrderedPair;
import org.dwbzen.common.math.Point2D;
import org.dwbzen.common.math.PointSetStats;

/**
 * Scales a Point2D<Double> to a Point2D<Integer> given PointSetStats
 * or max, min x and y values, and a target range.
 * 
 * @author DBacon
 *
 */
public class NumberScaler {

	private PointSetStats<Double> pointSetStats = null;
	private double xPointSpan = 0;
	private double yPointSpan = 0;
	private OrderedPair<Integer, Integer> xRange = null;
	private int xSpan = 0;
	private OrderedPair<Integer, Integer> yRange = null;
	private int ySpan = 0;
	public static MathContext mathContext = new MathContext(4, RoundingMode.HALF_DOWN);
	
	public NumberScaler(PointSetStats<Double> stats, OrderedPair<Integer, Integer> xrange, OrderedPair<Integer, Integer> yrange) {
		pointSetStats = new PointSetStats<>(stats);
		xRange = xrange;
		yRange = yrange;
		xSpan = xRange.getSecond() - xRange.getFirst() + 1;
		ySpan = yRange.getSecond() - yRange.getFirst() + 1;
		xPointSpan = pointSetStats.maxXValue - pointSetStats.minXValue;
		yPointSpan = pointSetStats.maxYValue - pointSetStats.minYValue;
	}
	
	public Point2D<Integer> scale(Point2D<Double> point) {
		Point2D<Integer> scaledPoint = new Point2D<Integer>(0,0);
		
		return scaledPoint;
	}

	public PointSetStats<Double> getPointSetStats() {
		return pointSetStats;
	}

	public OrderedPair<Integer, Integer> getxRange() {
		return xRange;
	}

	public OrderedPair<Integer, Integer> getyRange() {
		return yRange;
	}
	
	public static void main(String...args) {
		Double d = 370.49;
		BigDecimal bd = new BigDecimal(d);
		BigDecimal r = bd.round(mathContext);
		System.out.println(d + " " + r + " " + r.intValue());
	}
	
}
