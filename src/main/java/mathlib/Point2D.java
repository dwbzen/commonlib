package mathlib;

import java.math.BigDecimal;
import java.math.MathContext;
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
	public static String OBJECT_TYPE = "point";
			
	@JsonProperty("x")	private Number x = BigDecimal.ZERO;
	@JsonProperty("y")	private Number y = BigDecimal.ZERO;

	static MathContext mathContext = MathContext.DECIMAL32;
	
	public static final Point2D<Double> ORIGIN = new Point2D<Double>(0.0, 0.0);
	public static final Pattern DECIMAL_REGEX = Pattern.compile("\\[\\s*(.+),\\s*(.+)\\s*\\]");
	public static final Pattern JSON_REGEX = Pattern.compile("(name:.+),(type:.+),(Point2D:.+)");
	
	public static void main(String[] args) {

	}
	
	public Point2D() {
		setProperty("type", OBJECT_TYPE);
	}
	
	public Point2D(Number x, Number y) {
		this();
		this.x = new BigDecimal(x.doubleValue(), mathContext);
		this.y = new BigDecimal(y.doubleValue(), mathContext);
	}
	public Point2D(double x, double y) {
		this();
		this.x = new BigDecimal(x, mathContext);
		this.y = new BigDecimal(y, mathContext);
	}
	public Point2D(int x, int y) {
		this();
		this.x = x;
		this.y = y;
	}
	public Point2D(Point2D<BigDecimal> p) {
		this(p.x.doubleValue(), p.y.doubleValue());
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
		return "[ " + x + ", " + y + " ]";
	}
	
	public String toJSON(String nameLabel, String nameValue, String type) {
		StringBuffer jsonstr = new StringBuffer("{");
		jsonstr.append( quote(nameLabel, nameValue));
		if(type != null && type.length()>0) {
			jsonstr.append(", ").append(quote("type", type));
		}
		jsonstr.append(", ").append(quote("Point2D")).append(": ").append(toString()).append("}");
		return jsonstr.toString();
	}

	public static Point2D<Number>  fromJSONString(String jsonstr) {
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
		dist = Math.abs(point.getX().doubleValue() - this.getX().doubleValue()) + 
				Math.abs(point.getY().doubleValue() - this.getY().doubleValue());
		return dist;
	}
	
	/**
	 * Modulus is the same as distance(0,0) so it depends on the metric
	 * TODO - allow pluggable metric. And compute when constructed as Double.
	 * @return
	 */
	public double mod() {
		double m = Math.abs(this.getX().doubleValue()) + Math.abs(this.getY().doubleValue());
		return m;
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
		double modMe = mod();
		double modOther = other.mod();
		return (equals(other)) ? 0 : (modMe < modOther) ? -1 : 1;
	}
	
	public boolean equals(Point2D<T> other) {
		return other.getX().equals(this.getX()) & other.getY().equals(this.getY());
	}

	@Override
	public List<Number> getCoordinates() {
		List<Number> coordinates = new ArrayList<Number>();
		coordinates.add(x);
		coordinates.add(y);
		return coordinates;
	}
}
