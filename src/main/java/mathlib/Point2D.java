package mathlib;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author dbacon
 * JSON Point2D
 * { "name" : "ifs1", "type" : "point", "Point2D" : [  0.3064109,  0.6935743 ] }
 * { "name" : "ifs1", "type" : "point", "Point2D" : [  0.3064109,  0.6935743 ] }
 * { "name" : "ifs1", "type" : "point", "Point2D" : [ 2.687086E-4 , 1.829826E-4] }
 * 
 * 
 * @param <T>
 */
public class Point2D<T extends Number> extends JSONObject  implements IPoint, Comparable<Point2D<T>>  {

	private static final long serialVersionUID = 7492212210472351442L;
	protected static final Logger log = LogManager.getLogger(Point2D.class);
	public static final String ObjectType = "point";
			
	@JsonProperty("x")	private Number x = BigDecimal.ZERO;
	@JsonProperty("y")	private Number y = BigDecimal.ZERO;
	
	public static final Point2D<Double> ORIGIN = new Point2D<>(0.0, 0.0);
	public static final Pattern DECIMAL_REGEX = Pattern.compile("\\[\\s*(.+),\\s*(.+)\\s*\\]");
	public static final Pattern JSON_REGEX = Pattern.compile("(name:.+),(type:.+),(Point2D:.+)");
	
	protected Point2D() {
		setProperty("type", ObjectType);
	}
	
	public Point2D(Double x, Double y) {
		this();
		this.x = x;
		this.y = y;
	}
	public Point2D(double x, double y) {
		this();
		this.x = BigDecimal.valueOf(x);
		this.y = BigDecimal.valueOf(y);
	}
	public Point2D(int x, int y) {
		this();
		this.x = BigDecimal.valueOf(x);
		this.y = BigDecimal.valueOf(y);
	}
	public Point2D(Point2D<BigDecimal> p) {
		x = p.x;
		y = p.y;
	}
	/**
	 * Format:  [ 0.9082574, 0.07519616 ]
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

	public static void addFieldValue(Point2D<Number> ps, String valueString) {
		String[] fv = valueString.split(":");
		String fname = fv[0];
		String fval = (fv.length == 2) ? fv[1]  : "{" + fv[1] + ":" + fv[2] + "}";
		ps.getProperties().put(fname, fval);
	}

	public Number getX() {
		return x;
	}

	public Number getY() {
		return y;
	}
	
	public String toString() {
		return "[ " + x + ", " + y + " ]";
	}
	
	public String toJSON(String nameLabel, String nameValue, String type) {
		StringBuilder jsonstr = new StringBuilder("{");
		jsonstr.append( quoteString(nameLabel, nameValue));
		if(type != null && type.length()>0) {
			jsonstr.append(", ").append(quoteString("type", type));
		}
		jsonstr.append(", ").append(quoteString("Point2D")).append(": ").append(toString()).append("}");
		return jsonstr.toString();
	}

	public static Point2D<Number>  fromJson(String jsonstr) {
		String raw = jsonstr.replaceAll("[\"\\s{}]", "");	// deletes spaces, curly braces and quotes
		Matcher m = JSON_REGEX.matcher(raw);
		Point2D<Number> point = null;
		if(m.matches()) {
			log.debug("# groups: " + m.groupCount());
			point = new Point2D<Number>(m.group(3));
			for(int i=1; i<=m.groupCount()-1; i++) {
				log.debug("group: " + i + "= " + m.group(i));
				Point2D.addFieldValue(point, m.group(i));
			}
			log.debug("point: " + point.toJson());
		}
		return point;
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
	public boolean equals(Object other) {
		boolean equals = false;
		if(other instanceof Point2D) {
			equals = ((Point2D<?>)other).getX().equals(getX()) && ((Point2D<?>)other).getY().equals(getY());
		}
		return equals;
	}
	
	  @Override
	  public int hashCode() {
		  return (x.toString() + y.toString()).hashCode();
	  }

	@Override
	public List<Number> getCoordinates() {
		List<Number> coordinates = new ArrayList<Number>();
		coordinates.add(x);
		coordinates.add(y);
		return coordinates;
	}
}
