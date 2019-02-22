package mathlib;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mathlib.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * 
 * @author dbacon
 * JSON Point3D
 * { "name" : "ifs1", "type" : "point", "Point3D" : [  0.3064109,  0.6935743, -0.39834 ] }
 * { "name" : "locns", "type" : "3Dpoint", "Point3D" : [  0.3064109,  0.6935743, -0.39834 ] }
 * { "name" : "ifs1", "type" : "point", "Point3D" : [ 2.687086E-4 , 1.829826E-4, 0.0 ] }
 * You know we could extend Point2D, but I think that would just make it more complicated.
 * 
 * @param <T>
 */
public class Point3D<T extends Number> extends JsonObject  implements Serializable, IPoint, Comparable<Point3D<T>> {

	private static final long serialVersionUID = -3612512155008547069L;
	protected static final Logger log = LogManager.getLogger(Point3D.class);
	public static String OBJECT_TYPE = "point3D";
	
	@JsonProperty	private Number x = BigDecimal.ZERO;
	@JsonProperty	private Number y = BigDecimal.ZERO;
	@JsonProperty	private Number z = BigDecimal.ZERO;

	private static MathContext mathContext = MathContext.DECIMAL32;	// the default
	public static final Point3D<Double> ORIGIN = new Point3D<Double>(0.0, 0.0, 0.0);
	public static final Pattern DECIMAL_REGEX = Pattern.compile("\\[\\s*(.+),\\s*(.+),\\s*(.+)\\s*\\]");
	public static final Pattern JSON_REGEX = Pattern.compile("(_id:.+),(name:.+),(type:.+),(Point3D:.+)");

	public static void main(String[] args) {
		if(args.length > 0) {
			Point3D<Double> point = Point3D.fromJson(args[0]);
			System.out.println("point: " + point.toJson());
		}
	}

	protected Point3D() {
		setType(OBJECT_TYPE);
	}
	
	public Point3D(Number x, Number y, Number z) {
		this();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3D(double x, double y, double z) {
		this(new BigDecimal(x, mathContext), new BigDecimal(y, mathContext), new BigDecimal(z, mathContext));
	}
	
	public Point3D(Point2D<Number> p) {
		this(p.getX().doubleValue(), p.getY().doubleValue(), 0.0);
	}
	
	public double mod() {
		double m = Math.abs(this.getX().doubleValue()) + 
				   Math.abs(this.getY().doubleValue()) +
				   Math.abs(this.getZ().doubleValue());
		return m;
	}

	public double distance(Point3D<Number> point) {
		double	dist = Math.abs(point.getX().doubleValue() - this.getX().doubleValue()) + 
					Math.abs(point.getY().doubleValue() - this.getY().doubleValue()) +
					Math.abs(point.getZ().doubleValue() - this.getZ().doubleValue());
		return dist;
	}
	
	public double distance(Point2D<Number> point) {
		double dist = 0;
			Point3D<Number> p2 = new Point3D<Number>((Point2D<Number>)point);
			dist = Math.abs(p2.getX().doubleValue() - this.getX().doubleValue()) + 
					Math.abs(p2.getY().doubleValue() - this.getY().doubleValue()) +
					Math.abs(p2.getZ().doubleValue() - this.getZ().doubleValue());
		return dist;
	}

	@Override
	public int compareTo(Point3D<T> other) {
		Double modMe = mod();
		Double modOther = other.mod();
		return modMe.compareTo(modOther);
	}

	public boolean equals(Point3D<T> other) {
		return other.getX().equals(this.getX()) && 
			   other.getY().equals(this.getY()) &&
			   other.getZ().equals(this.getZ());
	}
	
	public String toString() {
		return "[ " + x + ", " + y + ", " + z + " ]";
	}
	
	public String toJson(String nameLabel, String nameValue, String type) {
		StringBuffer jsonstr = new StringBuffer("{");
		jsonstr.append( quoteString(nameLabel, nameValue));
		if(type != null && type.length()>0) {
			jsonstr.append(", ").append(quoteString("type", type));
		}
		jsonstr.append(", ").append(quoteString("Point3D")).append(": ").append(toString()).append("}");
		return jsonstr.toString();
	}

	@SuppressWarnings("unchecked")
	public static Point3D<Double> fromJson(String jsonstr) {
		Point3D<Double> point = null;
		try {
			point = mapper.readValue(jsonstr, Point3D.class);
		} catch (JsonParseException e) {
			log.error("JsonParseException (Point3D): " + jsonstr);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.error("JsonMappingException (Point3D): " + jsonstr);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException: " + jsonstr);
			e.printStackTrace();
		}
		return point;
	}
	

	/**
	 * Format:  [ 0.9082574, 0.07519616, 0.12344 ]
	 * @param s
	 */
	public Point3D(String s) {
		String[] raw = s.split(":");
		Matcher m = DECIMAL_REGEX.matcher(raw[raw.length-1]);
		if(m.matches() &&  m.groupCount()==3) {
			x=Double.parseDouble(m.group(1));
			y=Double.parseDouble(m.group(2));
			z=Double.parseDouble(m.group(3));
		}
		else {
			throw new NumberFormatException("Unknown format " +s );
		}
	}
	
	public void addFieldValue(String valueString) {
		String[] fv = valueString.split(":");
		String fname = fv[0];
		String fval = fv[1];
		if(fname.equalsIgnoreCase("name")) {
			setName(fval);
		}
		else if(fname.equalsIgnoreCase("type")) {
			setType(fval);
		}
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

	public Number getZ() {
		return z;
	}

	public void setZ(Number z) {
		this.z = z;
	}

	
	@Override
	public List<Number> getCoordinates() {
		List<Number> coordinates = new ArrayList<Number>();
		coordinates.add(x);
		coordinates.add(y);
		coordinates.add(z);
		return coordinates;
	}

}
