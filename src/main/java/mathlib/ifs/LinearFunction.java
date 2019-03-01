package mathlib.ifs;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.Matrix;
import mathlib.Point2D;
import mathlib.util.IJson;

/**
 * A LinearFunction F is defined as a map R<sup>2</sup> -> R<sup>2</sup>:</p>
 * <b>F<sub>i</sub>(x, y) = V<sub>i</sub>(a<sub>i</sub> x + b<sub>i</sub> y + c<sub>i</sub>, d<sub>i</sub> x + e<sub>i</sub> y + f<sub>i</sub>)</b></p>
 * and is represented internally as a 2 x 3 Matrix:</p>
 * &#9474; a&emsp;b&emsp;c &#9474;<br>
 * &#9474; d&emsp;e&emsp;f&ensp;&#9474;</p>
 * 
 *
 * JSON representation of this LinearFunction example:<br>
 * 
 * { "className" : "math.ifs.LinearFunction" , "weight" : 0.0 , "name" : "f1", 
 *   "function": { "className" : "math.Matrix" , "rows" : 2 , "cols" : 3 , 
 *   "array" : [ [ 0.5 , 0 , 0] , [ 0 , 0.5 , 0.5]] , "rank" : 2 , "name" : "f1"} }
 *
 *
 */
public class LinearFunction implements IJson, Function<Point2D<BigDecimal>, Point2D<BigDecimal>>  {
	
	private static final long serialVersionUID = -4451686768008927428L;
	public static final String ObjectType = "LinearFunction";
	public static final MathContext mathContext = IteratedFunctionSystem.mathContext;
	
	public static final BigDecimal lowerLimit = IteratedFunctionSystem.lowerLimit;	// any number having an absolute value <= lowerLimit is set to 0.0
	
	@JsonProperty("type")	private String type = ObjectType;
	//  the function Matrix must be 2 x 3
	@JsonProperty			private  Matrix<BigDecimal> function = null;
	@JsonProperty("weight")	private double weight = 0;
	@JsonIgnore				private String name;
	@JsonIgnore	private List<Variation>	variations = new ArrayList<Variation>();

	public static void main(String[] args) {
		double[][] dm3 = { {.5, 0, 0}, {0, .5, .5} };
		LinearFunction f1 = new LinearFunction(dm3);
		f1.setName("f1");
		System.out.println(f1);
		System.out.println(f1.toJson());
	}
	
	public LinearFunction() {
		function = new Matrix<BigDecimal>(2,3);
	}
	public LinearFunction(Matrix<Number> function) {
		if(function.getRows()!= 2 || function.getColumns() != 3) {
			throw new IllegalArgumentException("assertion failed: must be (2,3)");
		}
		this.function = new Matrix<BigDecimal>(function);
	}
	
	/**
	 * Copy constructor (deep)
	 * @param func
	 */
	public LinearFunction(LinearFunction func) {
		this.function = new Matrix<BigDecimal>(func.getFunction());
		this.weight = func.getWeight();
		this.name = func.name;
		variations.addAll(func.getVariations());
	}
	
	public LinearFunction(double[][] array, String name) {
		// array must be 2 row x 3 column array
		if(array.length != 2 || array[0].length != 3) {
			throw new IllegalArgumentException("Wrong size array");
		}
		function = new Matrix<BigDecimal>(3);
		function.addRow(array[0]);
		function.addRow(array[1]);
		this.name = name;
	}
	public LinearFunction(double[][] array) {
		this(array, "f" + String.valueOf((new Date()).getTime()));
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		if(function.getName() == null) {
			function.setName(name);
		}
	}

	public Matrix<BigDecimal> getFunction() {
		return function;
	}
	
	public void setFunction(Matrix<BigDecimal> function) {
		this.function = function;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public List<Variation> getVariations() {
		return variations;
	}
	
	public LinearFunction addVariation(Variation v) {
		variations.add(v);
		return this;
	}
	
	public String getType() {
		return type;
	}

	protected Point2D<BigDecimal> evaluateAt(Point2D<BigDecimal> point) {
		double x =	a()*point.getX().doubleValue() +
					b()*point.getY().doubleValue() +
					c();
		double y =	d()*point.getX().doubleValue() +
					e()*point.getY().doubleValue() +
					f();
		Point2D<BigDecimal> temp = new Point2D<BigDecimal>(x,y);
		for(Variation v : variations) {
			temp = v.apply(temp);
		}
		BigDecimal xbd = temp.toBigDecimalX().round(mathContext);
		if(xbd.abs().compareTo(lowerLimit) <= 0) {
			xbd = BigDecimal.ZERO;
		}
		BigDecimal ybd = temp.toBigDecimalY().round(mathContext);
		if(ybd.abs().compareTo(lowerLimit) <= 0) {
			ybd = BigDecimal.ZERO;
		}
		Point2D<BigDecimal> result = new Point2D<BigDecimal>(xbd, ybd );
		return result;
	}
	
	public double a() { return function.index(0, 0).doubleValue(); }
	public double b() { return function.index(0, 1).doubleValue(); }
	public double c() { return function.index(0, 2).doubleValue(); }
	public double d() { return function.index(1, 0).doubleValue(); }
	public double e() { return function.index(1, 1).doubleValue(); }
	public double f() { return function.index(1, 2).doubleValue(); }

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if(name != null) { sb.append(name + ":"); }
		sb.append(function.toString());
		
		return sb.toString();
	}

	@Override
	public Point2D<BigDecimal> apply(Point2D<BigDecimal> point) {
		return evaluateAt(point);
	}
}
