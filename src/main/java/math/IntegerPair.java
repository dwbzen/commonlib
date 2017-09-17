package math;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

import math.util.IJson;

/**
 * 
 * @author don_bacon
 *
 */
@Entity(value="IntegerPair", noClassnameStored=true)
public class IntegerPair implements IJson {

	private static final long serialVersionUID = 8841512197660420640L;
	@Property("x")	private Integer x = 0;
	@Property("y")	private Integer y = 0;
	
	public IntegerPair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public IntegerPair() {
		this(0, 0);
	}
	
	public boolean same() {
		return getX().intValue() == getY().intValue();
	}
	
	public Integer getX() {
		return x;
	}
	public void setX(Number x) {
		this.x = x.intValue();
	}

	public Integer getY() {
		return y;
	}
	public void setY(Number y) {
		this.y = y.intValue();
	}
	
	public static IntegerPair pair(int x, int y) {
		return new IntegerPair(x,y);
	}
	
	public static IntegerPair pair(int x) {
		return new IntegerPair(x,x);
	}
	
	
	@Override
	public String toJSON() {
		StringBuffer sb = new StringBuffer("{ ");
		sb.append("\"x\" : ");
		sb.append(getX());
		sb.append(", \"y\" : ");
		sb.append(getY());
		sb.append(" }");
		
		return sb.toString();
	}
	
	public static void main(String... args) {
		IntegerPair pair = new IntegerPair(201, 301);
		// { "className" : "math.IntegerPair" , "x" : 201 , "y" : 301}
		System.out.println(pair.toJSON());
		
	}

}
