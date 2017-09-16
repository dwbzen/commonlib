package math;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

import math.util.IJson;

@Entity(value="point", noClassnameStored=true)
public class Point<T extends Number> implements IJson, Serializable {

	private static final long serialVersionUID = 8164516008279659505L;
	private static MathContext mathContext = MathContext.DECIMAL32;	// the default
	
	@Property("x")	private Number x = BigDecimal.ZERO;
	@Property("y")	private Number y = BigDecimal.ZERO;
	
	public Point(Number x, Number y) {
		this.x = x;
		this.y = y;
	}
	public Point(double x, double y) {
		this.x = new BigDecimal(x, mathContext);
		this.y = new BigDecimal(y, mathContext);
	}
	public Point(int x, int y) {
		this.x = new BigDecimal(x, mathContext);
		this.y = new BigDecimal(y, mathContext);
	}
	public Point(Point<BigDecimal> p) {
		this(p.x.doubleValue(), p.y.doubleValue());
	}
	
	public int getLowInteger() {
		return x.intValue() <= y.intValue() ? x.intValue() : y.intValue();
	}
	public int getHighInteger() {
		return x.intValue() <= y.intValue() ? y.intValue() : x.intValue();
	}
	public Number getX() {
		return x;
	}
	public void setX(Number x) {
		this.x = x;
	}
	public Number getY() {
		return y;
	}
	public void setY(Number y) {
		this.y = y;
	}
	
	public String toString() {
		return "( " + x + ", " + y + " )";
	}
	
}
