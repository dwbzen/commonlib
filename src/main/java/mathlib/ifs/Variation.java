package mathlib.ifs;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

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
 *   	<td>cylinder</td><td>V<sub>29</sub>(x, y)</td><td>(sin(x), y)</td>
 *   </tr>
 * </table>
 * </p>
 * 
 *  "hyperbolic"	V<sub>10</sub>(x, y)=
 *  "diamond"		V<sub>11</sub>(x, y)=
 *  "ex"			V<sub>12</sub>(x, y)=
 *  "julia"			V<sub>13</sub>(x, y)=
 * </p>
 * 
 * where r = (x<sup>2</sup> + y<sup>2</sup>)<sup>.5</sup>		<code>(Math.pow(Math.pow(x,2) + Math.pow(y,2), .5)</code></p>
 * and angle &theta; = arctan(y/x)</p>
 * 
 * @author don_bacon
 *
 */
public class Variation {
	static Map<String, Integer> variations = new TreeMap<String, Integer>();
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
		variations.put("cylinder", 29);
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
		double x = point.getX().doubleValue();
		double y = point.getY().doubleValue();
		double rsquared = Math.pow(x,2) + Math.pow(y,2);
		double r = Math.pow(rsquared, .5);
		double theta = Math.atan(x/y);
		int vnum =variations.get(name);
		Point2D<BigDecimal> result;
		
		switch(vnum) {
		case 1:	/* sinusoidal */
			result = new Point2D<BigDecimal>(Math.sin(x), Math.sin(y));
			break;
		case 2:	/* spherical */
			result = new Point2D<BigDecimal>(x/rsquared, y/rsquared);
		case 3:		/* swirl */
		case 4:		/* horseshoe */
		case 5:		/* polar */
		case 6:		/* handkerchief */
		case 7:		/* heart */
		case 8:		/* disk */
		case 9:		/* spiral */
		case 10:	/* hyperbolic */
		case 11:	/* diamond */
		case 12:	/* Ex */
		case 13:	/* Julia */
		case 0:		/* identity */
		default:
			result = new Point2D<BigDecimal>(x, y);
		}
		return result;
	}

}
