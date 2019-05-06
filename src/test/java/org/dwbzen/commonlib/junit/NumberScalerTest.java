package org.dwbzen.commonlib.junit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dwbzen.common.math.OrderedPair;
import org.dwbzen.common.math.Point2D;
import org.dwbzen.common.math.PointSetStats;
import org.dwbzen.common.math.ScaleFactor;
import org.dwbzen.common.util.NumberScaler;
import org.junit.Test;

import junit.framework.TestCase;

public class NumberScalerTest   extends TestCase  {

	static final Logger log = LogManager.getLogger(NumberScaler.class);
	static Point2D<Double> minPoint = new Point2D<Double>(0.02587, 0.2078);
	static Point2D<Double> maxPoint = new Point2D<Double>(1.986, 1.681);
	static PointSetStats<Double> stats = new PointSetStats<>(0.02587, 2.003, 0.2071, 1.774, minPoint, maxPoint);
	
	static OrderedPair<Integer, Integer> xyRange = new OrderedPair<>(1280, 1024);
	static ScaleFactor sf = new ScaleFactor(xyRange, true);
	static NumberScaler scaler = new NumberScaler(stats, sf);
	
	@Test
	public void testLowerBoundry() {
		Point2D<Double> point2 = new Point2D<>(0.02587, 0.2071);
		Point2D<Integer> scaledPoint2 = scaler.scale(point2);
		log.info(scaledPoint2.toJson(true));
		assertEquals(scaledPoint2.getX(), 0.0);
		assertEquals(scaledPoint2.getY(), 0.0);
	}
	
	@Test
	public void testUpperBoundry() {
		
		Point2D<Double> point1 = new Point2D<>(2.003, 1.774);	// scale this
		Point2D<Integer> scaledPoint1 = scaler.scale(point1);
		log.info(scaledPoint1.toJson(true));
		assertEquals(scaledPoint1.getX(), 1280.0);
		assertEquals(scaledPoint1.getY(), 1024.0);
	}
}
