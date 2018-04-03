package mathlib;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.util.IJson;

/**
 * 
 * @author don_bacon
 *
 */
public class IntegerPair implements IJson {

	private static final long serialVersionUID = 8841512197660420640L;
	@JsonIgnore	private String xLabel = "x";
	@JsonIgnore private String yLabel = "y";
	
	@JsonProperty	private Integer x = 0;
	@JsonProperty	private Integer y = 0;
	
	public IntegerPair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public IntegerPair(String xlabel, int x, String ylabel, int y) {
		xLabel = xlabel;
		yLabel = ylabel;
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
	
	public String getxLabel() {
		return xLabel;
	}
	public void setxLabel(String xLabel) {
		this.xLabel = xLabel;
	}
	public String getyLabel() {
		return yLabel;
	}
	public void setyLabel(String yLabel) {
		this.yLabel = yLabel;
	}
	
	public static IntegerPair pair(int x, int y) {
		return new IntegerPair(x,y);
	}
	
	public static IntegerPair pair(int x) {
		return new IntegerPair(x,x);
	}
	

	@Override
	public String toJson() {
		return toJson();
	}
	
	public static void main(String... args) {
		IntegerPair pair = new IntegerPair(201, 301);
		// { "className" : "math.IntegerPair" , "x" : 201 , "y" : 301}
		System.out.println(pair.toJson());
	}

}
