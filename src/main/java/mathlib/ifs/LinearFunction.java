package mathlib.ifs;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.Matrix;
import mathlib.Point2D;
import mathlib.util.IJson;

/**
 * A LinearFunction F is defined as a map R^2 -> R^2:
 * F(x,y) = (ax + by + c, dx + ey + f)
 * and is represented internally as a 2 x 3 Matrix
 * 
 * @author dbacon
 *
 */
public class LinearFunction implements IJson {
	
	private static final long serialVersionUID = -4451686768008927428L;
	
	/**
	 * the function Matrix must be 2 x 3
	 */
	@JsonIgnore				private  Matrix<BigDecimal> function = null;
	@JsonProperty("weight")		private double weight = 0;
	@JsonProperty("name")		private String name;

	public static void main(String[] args) {
		double[][] dm3 = { {.5, 0, 0}, {0, .5, .5} };
		LinearFunction f1 = new LinearFunction(dm3);
		f1.setName("f1");
		System.out.println(f1);
		System.out.println(f1.toJSON());
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
	
	/**
	 * JSON representation of this LinearFunction.
	 * example:
	 * { "className" : "math.ifs.LinearFunction" , "weight" : 0.0 , "name" : "f1", 
	 *   "function": { "className" : "math.Matrix" , "rows" : 2 , "cols" : 3 , 
	 *   "array" : [ [ 0.5 , 0 , 0] , [ 0 , 0.5 , 0.5]] , "rank" : 2 , "name" : "f1"} }
	 *
	 * @return JSON String
	 */
	public String toJSON() {
		return toJson();
	}
	
	public Point2D<BigDecimal> evaluateAt(Point2D<BigDecimal> point, boolean createNew) {
		double x =	a()*point.getX().doubleValue() +
					b()*point.getY().doubleValue() +
					c();
		double y =	d()*point.getX().doubleValue() +
					e()*point.getY().doubleValue() +
					f();
		
		Point2D<BigDecimal> result = point;
		if(createNew) {
			result = new Point2D<BigDecimal>(x,y);
		}
		else {
			point.setX(x);
			point.setY(y);
		}
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
}
