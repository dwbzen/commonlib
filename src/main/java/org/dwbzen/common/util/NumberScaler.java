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
	private int precision = 4;	// decimal places
	private MathContext mathContext = new MathContext(precision, RoundingMode.HALF_DOWN);
	private BigDecimal bd = null;	// for Rounding
	
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
		double xs = (point.getX() / xPointSpan * xSpan) + xRange.getFirst();
		return scaledPoint;
	}
	
	private int round(double num) {
		bd = new BigDecimal(num);
		return bd.round(mathContext).intValue();
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
	
	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
		mathContext =  new MathContext(precision, RoundingMode.HALF_DOWN);
	}

	public MathContext getMathContext() {
		return mathContext;
	}

	public static void main(String...args) {
		PointSetStats<Double> stats = new PointSetStats<>(0.02508, 1.991, 0.2074, 1.84, 
				new Point2D<Double>(0.02508, 0.2074), new Point2D<Double>(1.924, 1.803));
		OrderedPair<Integer, Integer> xRange = new OrderedPair<>(0, 1280);
		OrderedPair<Integer, Integer> yRange = new OrderedPair<>(0, 1024);
		
		NumberScaler scaler = new NumberScaler(stats, xRange, yRange);
		Point2D<Double> point = new Point2D<>(1.745, 0.7435);	// scale this
		Point2D<Integer> scaledPoint = scaler.scale(point);
		System.out.println(scaledPoint.toJson(true));
	}
	
}
