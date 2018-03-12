package junit;

import junit.framework.TestCase;
import mathlib.Point2D;

public class JSONParseTest  extends TestCase {

	static Point2D<Double> point1;
	static Point2D<Double> point2;
	
	static {
		point1 = new Point2D<Double>();
		point1.setProperty("name", "sierpinski");
		point1.setProperty("type", "point");
		point1.setX( 0.1030576);
		point1.setY(0.01955528);
		
		point2 = new Point2D<Double>();
		point2.setProperty("name", "ifs1");
		point2.setProperty("type", "point");
		point2.setX( 0.3064109);
		point2.setY(0.6935743);
	}
	
	/**
	 * {"name": "sierpinski", "type": "point", "Point2D": [ 0.1030576, 0.01955528 ]}
	 *  {"name" : "ifs1", "type" : "point", "Point2D" : [  0.3064109,  0.6935743 ] }
	 */
	
	public void testParsePoint2D2() {
		//String jstr1 = point1.toJson();
		System.out.println("Fix Me");
	}
}
