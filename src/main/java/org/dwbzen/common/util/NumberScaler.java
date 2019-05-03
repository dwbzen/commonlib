package org.dwbzen.common.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dwbzen.common.math.OrderedPair;
import org.dwbzen.common.math.Point2D;
import org.dwbzen.common.math.PointSetStats;
import org.dwbzen.common.math.ScaleFactor;

/**
 * Scales a Point2D<Double> to a Point2D<Integer> given PointSetStats
 * or max, min x and y values, and a target range.
 * 
 * @author DBacon
 *
 */
public class NumberScaler {

	static final Logger log = LogManager.getLogger(NumberScaler.class);
	private PointSetStats<Double> pointSetStats = null;
	private ScaleFactor scaleFactor = null;
	private double xPointSpan = 0;
	private double yPointSpan = 0;
	private OrderedPair<Integer, Integer> xRange = null;
	private int xSpan = 0;
	private OrderedPair<Integer, Integer> yRange = null;
	private int ySpan = 0;
	private int precision = 4;	// decimal places
	private MathContext mathContext = new MathContext(precision, RoundingMode.HALF_DOWN);
	private BigDecimal bd = null;	// for Rounding
	
	public NumberScaler(PointSetStats<Double> stats, ScaleFactor scaleFactor) {
		pointSetStats = new PointSetStats<>(stats);
		this.scaleFactor = scaleFactor;
		xRange = scaleFactor.getxRange();
		yRange = scaleFactor.getyRange();
		xSpan = xRange.getSecond() - xRange.getFirst();
		ySpan = yRange.getSecond() - yRange.getFirst();
		xPointSpan = pointSetStats.maxXValue - pointSetStats.minXValue;
		yPointSpan = pointSetStats.maxYValue - pointSetStats.minYValue;
	}
	
	public Point2D<Integer> scale(Point2D<Double> point) {
		Double xs = ((point.getX() - pointSetStats.minXValue)/xPointSpan * xSpan) + xRange.getFirst();
		Double ys = ((point.getY() - pointSetStats.minYValue)/yPointSpan * ySpan) + yRange.getFirst();
		int xsint = xs.intValue();
		int ysint = ys.intValue();
		return new Point2D<Integer>(xsint, ysint);
	}
	
	public int round(double num) {
		bd = new BigDecimal(num);
		return bd.round(mathContext).intValue();
	}

	public PointSetStats<Double> getPointSetStats() {
		return pointSetStats;
	}

	public ScaleFactor getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(ScaleFactor scaleFactor) {
		this.scaleFactor = scaleFactor;
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
		Point2D<Double> minPoint = new Point2D<Double>(0.02587, 0.2078);
		Point2D<Double> maxPoint = new Point2D<Double>(1.986, 1.681);
		PointSetStats<Double> stats = new PointSetStats<>(0.02587, 2.003, 0.2071, 1.774, minPoint, maxPoint);

		OrderedPair<Integer, Integer> xyRange = new OrderedPair<>(1280, 1024);
		ScaleFactor sf = new ScaleFactor(xyRange, true);
		NumberScaler scaler = new NumberScaler(stats, sf);
		
		Point2D<Double> point2 = new Point2D<>(0.02587, 0.2071);
		Point2D<Integer> scaledPoint2 = scaler.scale(point2);
		log.info(scaledPoint2.toJson(true));
		
		Point2D<Double> point1 = new Point2D<>(2.003, 1.774);	// scale this
		Point2D<Integer> scaledPoint1 = scaler.scale(point1);
		System.out.println(scaledPoint1.toJson(true));
		
	}
	
}
