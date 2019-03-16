package org.dwbzen.common.math;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectReader;

/**
 * 
 * @author dbacon
 * JSON Point2D
 * {"name":"unknown","type":"Point2D","coordinates":[0.99201,-4.333]}
 * 
 * TODO This needs to be refactored to be polymorphic.
 * TODO The Double attributes should be T. Same applies to Point3D.
 * 
 * @param <T>
 */
public class Point2D<T extends Number> extends JsonObject  implements IPoint, Comparable<Point2D<T>>  {

	private static final long serialVersionUID = 7492212210472351442L;
	protected static final Logger log = LogManager.getLogger(Point2D.class);
	public static final String ObjectType = "Point2D";
			
	@JsonProperty	private Double x = BigDecimal.ZERO.doubleValue();
	@JsonProperty	private Double y = BigDecimal.ZERO.doubleValue();
	@JsonProperty	private int	count = 1;
	
	public static final Point2D<Double> ORIGIN = new Point2D<>(0.0, 0.0);
	public static final Pattern DECIMAL_REGEX = Pattern.compile("\\[\\s*(.+),\\s*(.+)\\s*\\]");
	
	protected Point2D() {
		setType(ObjectType);
	}
	
	public Point2D(Double x, Double y) {
		this();
		this.x = x;
		this.y = y;
	}
	public Point2D(double x, double y) {
		this();
		this.x = x;
		this.y = y;
	}
	public Point2D(int x, int y) {
		this();
		this.x = Double.valueOf(x);
		this.y = Double.valueOf(y);
	}
	public Point2D(Point2D<BigDecimal> p) {
		this();
		x = p.x;
		y = p.y;
		count = p.count;
	}
	
	public Point2D(BigDecimal xbd, BigDecimal ybd) {
		this();
		x = xbd.doubleValue();
		y = ybd.doubleValue();
	}
	
	/**
	 * Format:  [ 0.9082574, 0.07519616 ] or coordinates : [ 0.9082574, 0.07519616 ] 
	 * @param s
	 */
	public Point2D(String s) {
		this();
		String[] raw = s.split(":");
		Matcher m = DECIMAL_REGEX.matcher(raw[raw.length-1]);
		if(m.matches() &&  m.groupCount()==2) {
			x= Double.parseDouble(m.group(1));
			y= Double.parseDouble(m.group(2));
		}
		else {
			throw new NumberFormatException("Unknown format " +s );
		}
	}

	public Double getX() {
		return x;
	}
	
	public BigDecimal toBigDecimalX() {
		return new BigDecimal(x);
	}
	
	public Double getY() {
		return y;
	}
	
	public BigDecimal toBigDecimalY() {
		return new BigDecimal(y);
	}

	/**
	 * count is the #times this point is accessed in a given context.
	 * This can be mult-purposed to represent a color (for example) as a gradient index.
	 * @return count
	 */
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String toString() {
		return "[ " + x + ", " + y + " ]";
	}

	public static Point2D<Double>  fromJson(String jsonstr) {
		Point2D<Double> point = null;
		try {
			ObjectReader reader = mapper.readerFor(ORIGIN.getClass());
			point = reader.readValue(jsonstr);
		} catch (JsonParseException e) {
			log.error("JsonParseException (Point2D): " + jsonstr);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.error("JsonMappingException (Point2D): " + jsonstr);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException: " + jsonstr);
			e.printStackTrace();
		}
		return point;
	}
	
	public String toJsonString(String nameLabel, String nameValue, String type) {
		StringBuilder jsonstr = new StringBuilder("{");
		jsonstr.append( quoteString(nameLabel, nameValue));
		if(type != null && type.length()>0) {
			jsonstr.append(", ").append(quoteString("type", type));
		}
		jsonstr.append(", ").append(quoteString("Point2D")).append(": ").append(toString()).append("}");
		return jsonstr.toString();
	}
	
	/**
	 * Manhattan metric
	 * @param point
	 * @return
	 */
	public double distance(Point2D<Number> point) {
		double dist = 0;
		dist = Math.abs(point.getX().doubleValue() - getX().doubleValue()) + 
				Math.abs(point.getY().doubleValue() - getY().doubleValue());
		return dist;
	}
	
	/**
	 * Modulus is the same as distance(0,0) so it depends on the metric
	 * @return
	 */
	public double mod() {
		return Math.abs(getX().doubleValue()) + Math.abs(getY().doubleValue());
	}
	
	/**
	 * Multiplies this Point2D by a double.
	 * @param num
	 * @return new Point2D
	 */
	public Point2D<T> multiply(double num) {
		return new Point2D<T>(getX().doubleValue()*num, getY().doubleValue()*num);
	}
	
	@Override
	public int compareTo(Point2D<T> other) {
		if(equals(other)) {
			return 0;
		}
		double modMe = mod();
		double modOther = other.mod();
		return (modMe < modOther) ? -1 : 1;
	}
	
	@Override
	/**
	 * Points are equal if the respective X and Y values are equal.
	 * Point count is not a factor in determining equality.
	 * Use the identical method to also consider count.
	 */
	public boolean equals(Object other) {
		boolean equals = false;
		if(other instanceof Point2D) {
			equals = ((Point2D<?>)other).getX().equals(getX()) && ((Point2D<?>)other).getY().equals(getY());
		}
		return equals;
	}
	
	public boolean identical(Object other) {
		boolean equals = false;
		if(other instanceof Point2D) {
			equals = ((Point2D<?>)other).getX().equals(getX()) && ((Point2D<?>)other).getY().equals(getY());
			if(equals) {
				equals &= ((Point2D<?>)other).getCount() == ((Point2D<?>)other).getCount();
			}
		}
		return equals;
	}
	
	@Override
	  public int hashCode() {
		  return (x.toString() + y.toString()).hashCode();
	  }

	@Override
	@JsonIgnore
	public List<Number> getCoordinates() {
		List<Number> coordinates = new ArrayList<Number>();
		coordinates.add(x);
		coordinates.add(y);
		return coordinates;
	}
	
	public static void main(String[] args) {
		Point2D<Double> p2d = new Point2D<Double>(.99201, -4.333);
		String jsonstr = p2d.toJson();
		System.out.println(jsonstr);
		System.out.println(p2d.toJson(true));
		
		Point2D<Double> point = Point2D.fromJson(jsonstr);
		System.out.println(point.toString());
		
		if(args.length > 0) {
			Point2D<Double> point2 = Point2D.fromJson(args[0]);
			System.out.println("point: " + point2.toJson());
		}
	}
}
