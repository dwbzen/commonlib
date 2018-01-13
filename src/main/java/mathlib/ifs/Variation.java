package mathlib.ifs;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import mathlib.Point2D;

/**
 * Encapsulates a non-linear variation of a linear function. 
 * This composes the afine linear function with a non-linear variation Vj.<p>
 * 
 * <b>F<sub>i</sub>(x, y) = v<sub>ij</sub> V<sub>j</sub>(a<sub>i</sub> x + b<sub>i</sub> y + c<sub>i</sub>, d<sub>i</sub> x + e<sub>i</sub> y + f<sub>i</sub>)</b></p>
 * where  v<sub>ij</sub> is a blending vector of length j such that &sum;v<sub>j</sub> = 1</p>
 * Variations are defined as follows. 
 * The numbering and definitions are defined in the paper: <A href="http://www.flam3.com/flame_draves.pdf">Fractal Flame</A></p>
 * 
   <style>
	table, th, td {
	    border: 1px solid black;
	    border-collapse: collapse;
	}
	th, td {
	    padding: 5px;
	}
	th {
	    text-align: left;
	}
	</style>
 * <table style="width:50%">
 *   <tr>
 *   	<th>Name</th>
 *   	<th>Number</th>
 *   	<th>Definition</th>
 *   </tr>
 *   <tr>
 *   	<td>linear</td><td>V<sub>0</sub>(x,y)</td><td>(x, y)</td>
 *   </tr>
 *   <tr>
 *   	<td>sinusoidal</td><td>V<sub>1</sub>(x,y)</td><td>(sin(x), sin(y))</td>
 *   </tr>
 *   <tr>
 *   	<td>spherical</td><td>V<sub>2</sub>(x, y)</td><td>(x/r<sup>2</sup>, y/r<sup>2</sup>)</td>
 *   </tr>
 *   </tr>
 *   	<td>swirl</td><td>V<sub>3</sub>(x, y)</td><td>(<i>x</i> sin(r<sup>2</sup>)-<i>y</i> cos(r<sup>2</sup>), <i>x</i> cos(r<sup>2</sup>)+<i>y</i> sin(r<sup>2</sup>)</td>
 *   </tr>
 *   <tr>
 *   	<td>horseshoe</td><td>V<sub>4</sub>(x, y)</td><td>(1/r) &#8729; ((x-y)(x+y), 2xy )</td>
 *   </tr>
 *   <tr>
 *   	<td>polar</td><td>V<sub>5</sub>(x, y)</td><td>(&theta;/&pi;, r-1)</td>
 *   </tr>
 *   <tr>
 *   	<td>handkerchief</td><td>V<sub>6</sub>(x, y)</td><td>(r*sin(&theta;+r), r*cos(&theta;-r))</td>
 *   </tr>
 *   <tr>
 *   	<td>heart</td><td>V<sub>7</sub>(x, y)</td><td>(r*sin(r&theta;), -r*cos(r&theta;))</td>
 *   </tr>
 *   <tr>
 *   	<td>disk</td><td>V<sub>8</sub>(x, y)</td><td>(&theta;/&pi;)&#8729;(sin(r&theta;), cos(r&theta;))</td>
 *   </tr>
 *   <tr>
 *   	<td>spiral</td><td>V<sub>9</sub>(x, y)</td><td>(1/r) &#8729; (cos(&theta;)+sin(r), sin(&theta;)-cos(r))</td>
 *   </tr>
 *   <tr>
 *   	<td>hyperbolic</td><td>V<sub>10</sub>(x, y)</td><td>(sin(&theta;/r), r*cos(&theta;)</td>
 *   </tr>
 *   <tr>
 *   	<td>diamond</td><td>V<sub>11</sub>(x, y)</td><td>(sin(&theta;) cos(r), cos(&theta;) sin(r))</td>
 *   </tr>
 *   <tr>
 *   	<td>julia</td><td>V<sub>13</sub>(x, y)</td><td>r<sup>.5</sup>(cos(&theta;/2), sin(&theta;/2))</td>
 *   </tr>

 *   <tr>
 *   	<td>cylinder</td><td>V<sub>29</sub>(x, y)</td><td>(sin(x), y)</td>
 *   </tr>
 * </table>
 * </p>
 * 
 * where r = (x<sup>2</sup> + y<sup>2</sup>)<sup>.5</sup>		<code>(Math.pow(Math.pow(x,2) + Math.pow(y,2), .5)</code></p>
 * and angle &theta; = arctan(y/x)</p>
 * 
 * @author don_bacon
 *
 */
public class Variation implements Function<Point2D<BigDecimal>, Point2D<BigDecimal>>{
	static Map<String, Integer> variations = new TreeMap<String, Integer>();
	static Map<Integer, Function<Point2D<BigDecimal>, Point2D<BigDecimal>>> variationFunctions = new HashMap<Integer, Function<Point2D<BigDecimal>, Point2D<BigDecimal>>>();
	static final double Pi = Math.PI;
	static {
		variations.put("linear", 0);
		variations.put("sinusoidal", 1);
		variations.put("spherical", 2);
		variations.put("swirl", 3);
		variations.put("horseshoe", 4);
		variations.put("polar", 5);
		variations.put("handkerchief", 6);
		variations.put("heart", 7);
		variations.put("disk", 8);
		variations.put("spiral", 9);
		variations.put("hyperbolic", 10);
		variations.put("diamond", 11);
		variations.put("julia", 13);
		variations.put("cylinder", 29);
	}
	static {
		Function<Point2D<BigDecimal>, Point2D<BigDecimal>> linear = p -> 
			new Point2D<BigDecimal>(p.getX().doubleValue(), p.getY().doubleValue());
			
		Function<Point2D<BigDecimal>, Point2D<BigDecimal>> sinusoidal = p -> 
			new Point2D<BigDecimal>(Math.sin(p.getX().doubleValue()), Math.sin(p.getY().doubleValue()));
			
		Function<Point2D<BigDecimal>, Point2D<BigDecimal>> spherical = p -> {
			double x = p.getX().doubleValue();
			double y = p.getY().doubleValue();
			double rsquared = Math.pow(x,2) + Math.pow(y,2);
			return new Point2D<BigDecimal>(x, y).multiply(1/rsquared);
		};
		
		Function<Point2D<BigDecimal>, Point2D<BigDecimal>> swirl = p -> {
			double x = p.getX().doubleValue();
			double y = p.getY().doubleValue();
			double rsquared = Math.pow(x,2) + Math.pow(y,2);
			return new Point2D<BigDecimal>(x*Math.sin(rsquared) - y*Math.cos(rsquared), x*Math.cos(rsquared) + y*Math.sin(rsquared));
		};
		
		Function<Point2D<BigDecimal>, Point2D<BigDecimal>> horseshoe = p -> {
			double x = p.getX().doubleValue();
			double y = p.getY().doubleValue();
			double  r = Math.pow(Math.pow(x,2) + Math.pow(y,2), .5);
			return (new Point2D<BigDecimal>( ((x-y)*(x+y)), 2*x*y)).multiply(1/r);
		};
		
		Function<Point2D<BigDecimal>, Point2D<BigDecimal>> polar = p -> {
			double x = p.getX().doubleValue();
			double y = p.getY().doubleValue();
			double theta = Math.atan(x/y);
			double  r = Math.pow(Math.pow(x,2) + Math.pow(y,2), .5);
			return new Point2D<BigDecimal>(theta / Pi, r-1);
		};
		
		Function<Point2D<BigDecimal>, Point2D<BigDecimal>> handkerchief = p -> {
			double x = p.getX().doubleValue();
			double y = p.getY().doubleValue();
			double theta = Math.atan(x/y);
			double  r = Math.pow(Math.pow(x,2) + Math.pow(y,2), .5);
			return (new Point2D<BigDecimal>( Math.sin(theta+r), Math.cos(theta-r))).multiply(r);
		};
		
		Function<Point2D<BigDecimal>, Point2D<BigDecimal>> heart = p -> {
			double x = p.getX().doubleValue();
			double y = p.getY().doubleValue();
			double theta = Math.atan(x/y);
			double  r = Math.pow(Math.pow(x,2) + Math.pow(y,2), .5);
			return (new Point2D<BigDecimal>( Math.sin(theta*r), -Math.cos(theta*r))).multiply(r);
		};
		
		Function<Point2D<BigDecimal>, Point2D<BigDecimal>> disk = p -> {
			double x = p.getX().doubleValue();
			double y = p.getY().doubleValue();
			double theta = Math.atan(x/y);
			double  r = Math.pow(Math.pow(x,2) + Math.pow(y,2), .5);
			return (new Point2D<BigDecimal>( Math.sin(theta+r), Math.cos(theta-r))).multiply(r);
		};
		
		Function<Point2D<BigDecimal>, Point2D<BigDecimal>> spiral = p -> {
			double x = p.getX().doubleValue();
			double y = p.getY().doubleValue();
			double theta = Math.atan(x/y);
			double  r = Math.pow(Math.pow(x,2) + Math.pow(y,2), .5);
			return  (new Point2D<BigDecimal>( Math.cos(theta)+Math.sin(r), Math.sin(theta)-Math.cos(r))).multiply(1/r);
		};
		
		Function<Point2D<BigDecimal>, Point2D<BigDecimal>> hyperbolic = p -> {
			double x = p.getX().doubleValue();
			double y = p.getY().doubleValue();
			double theta = Math.atan(x/y);
			double  r = Math.pow(Math.pow(x,2) + Math.pow(y,2), .5);
			return  new Point2D<BigDecimal>(Math.sin(theta)/r, r*Math.cos(theta));
		};
		
		Function<Point2D<BigDecimal>, Point2D<BigDecimal>> diamond = p -> {
			double x = p.getX().doubleValue();
			double y = p.getY().doubleValue();
			double theta = Math.atan(x/y);
			double  r = Math.pow(Math.pow(x,2) + Math.pow(y,2), .5);
			return  new Point2D<BigDecimal>(Math.sin(theta)*Math.cos(r), Math.cos(theta)*Math.sin(r));
		};
		
		Function<Point2D<BigDecimal>, Point2D<BigDecimal>> julia = p -> {
			double x = p.getX().doubleValue();
			double y = p.getY().doubleValue();
			double theta = Math.atan(x/y);
			double  r = Math.pow(Math.pow(x,2) + Math.pow(y,2), .5);
			return  (new Point2D<BigDecimal>( Math.cos(theta/2), Math.sin(theta/2))).multiply(Math.sqrt(r));
		};
		
		Function<Point2D<BigDecimal>, Point2D<BigDecimal>> cylinder = p -> 
			new Point2D<BigDecimal>(Math.sin(p.getX().doubleValue()), p.getY().doubleValue());
		
		variationFunctions.put(0, linear);
		variationFunctions.put(1, sinusoidal);
		variationFunctions.put(2, spherical);
		variationFunctions.put(3, swirl);
		variationFunctions.put(4, horseshoe);
		variationFunctions.put(5,  polar);
		variationFunctions.put(6,  handkerchief);
		variationFunctions.put(7,  heart);
		variationFunctions.put(8,  disk);
		variationFunctions.put(9,  spiral);
		variationFunctions.put(10,  hyperbolic);
		variationFunctions.put(11,  diamond);
		variationFunctions.put(13,  julia);
		variationFunctions.put(29,  cylinder);
	}
	
	private String name;
	private double blendingVectorLength;
	private int variationNumber = 0;
	
	public Variation(String name, int vnum, double length) {
		this.name = name;
		variationNumber = vnum;
		blendingVectorLength = length;
	}

	public String getName() {
		return name;
	}

	public double getBlendingVectorLength() {
		return blendingVectorLength;
	}
	
	public int getVariationNumber() {
		return variationNumber;
	}
	
	public Point2D<BigDecimal> evaluateAt(Point2D<BigDecimal> point) {
		int vnum = variations.containsKey(name) ?  variations.get(name) : 0;
		Point2D<BigDecimal> result = variationFunctions.get(vnum).apply(point).multiply(blendingVectorLength);
		return result;
	}

	@Override
	public Point2D<BigDecimal> apply(Point2D<BigDecimal> t) {
		return evaluateAt(t);
	}

	public static Function<Point2D<BigDecimal>, Point2D<BigDecimal>> getVariationFunction(int num) {
		int vnum = variationFunctions.containsKey(num) ? num : -1;
		return vnum>=0 ? variationFunctions.get(vnum) : null;
	}
	public static Function<Point2D<BigDecimal>, Point2D<BigDecimal>> getVariationFunction(String name) {
		int vnum = variations.containsKey(name) ?  variations.get(name) : -1;
		return vnum>=0 ? variationFunctions.get(vnum) : null;
	}

}
