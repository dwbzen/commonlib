package junit;

import junit.framework.TestCase;
import mathlib.Point2D;

public class JSONParseTest  extends TestCase {

	static Point2D<Double> point1;
	static Point2D<Double> point2;
	
	static {
		point1 = new Point2D<Double>( 0.1030576, 0.01955528);
		point2 = new Point2D<Double>(0.3064109, 0.6935743);
	}
	
	/**
	 * {"name": "sierpinski", "type": "point", "Point2D": [ 0.1030576, 0.01955528 ]}
	 *  {"name" : "ifs1", "type" : "point", "Point2D" : [  0.3064109,  0.6935743 ] }
	 */
	
	public void testParsePoint2D2() {
		String jstr1 = point1.toJson();
		//assertTrue(jstr1.contains("{\"name\":\"sierpinski\",\"type\":\"point\"},\"coordinates\":[0.1030576,0.01955528]"));
		System.out.println(jstr1);
	}
}
