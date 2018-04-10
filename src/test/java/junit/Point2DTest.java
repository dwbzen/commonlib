package junit;

import junit.framework.TestCase;
import mathlib.Point2D;

public class Point2DTest  extends TestCase  {

	public void testEquals() {
		Point2D<Double> p1 = new Point2D<>(0.33333333, 0.33333);
		Point2D<Double> p2 = new Point2D<>(0.33333333, 0.33333);
		assertTrue(p1.equals(p2));
	}
}
