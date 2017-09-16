package math;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import math.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author dbacon
 * JSON Point3D (MongoDB)
 * { "_id" : { "$oid" : "52e331a8781aae933310ea29"}, "name" : "ifs1", "type" : "point", "Point3D" : [  0.3064109,  0.6935743, -0.39834 ] }
 * { "_id" : ObjectId("52b49229e5bb8c8c26bbc603"), "name" : "locns", "type" : "3Dpoint", "Point3D" : [  0.3064109,  0.6935743, -0.39834 ] }
 * { "_id" : { "$oid" : "52e331a8781aae933310ea29"}, "name" : "ifs1", "type" : "point", "Point3D" : [ 2.687086E-4 , 1.829826E-4, 0.0 ] }
 * You know we could extend Point2D, but I think that would just make it more complicated.
 * 
 * @param <T>
 */
public class Point3D<T extends Number> extends JSONObject  implements Serializable, IPoint, Comparable<Point3D<T>> {

	private static final long serialVersionUID = -3612512155008547069L;
	protected static final Logger log = LogManager.getLogger(Point3D.class);
	public static String OBJECT_TYPE = "point3D";
	
	private Number x = BigDecimal.ZERO;
	private Number y = BigDecimal.ZERO;
	private Number z = BigDecimal.ZERO;

	private static MathContext mathContext = MathContext.DECIMAL32;	// the default
	public static final Point3D<Double> ORIGIN = new Point3D<Double>(0.0, 0.0, 0.0);
	public static final Pattern DECIMAL_REGEX = Pattern.compile("\\[\\s*(.+),\\s*(.+),\\s*(.+)\\s*\\]");
	public static final Pattern JSON_REGEX = Pattern.compile("(_id:.+),(name:.+),(type:.+),(Point3D:.+)");

	public static void main(String[] args) {
		if(args.length > 0) {
			// assumes a JSON point from MongoDB
			Point3D<Double> point = Point3D.fromJSON(args[0]);
			System.out.println("point: " + point.toJSON());
			
		}

		String s = "[ 0.9082574,0.07519616, -0.993823 ]";
		Matcher m = DECIMAL_REGEX.matcher(s);
		boolean b = m.matches();
		int ng = m.groupCount();
		if(ng == 3) {
			String n1 = m.group(1);
			String n2 = m.group(2);
			String n3 = m.group(3);
			System.out.println("matches: " + b + " group count: " + ng);
			System.out.println("n1="+ n1 + " n2=" + n2 + " n3=" + n3);
			Point3D<Double> p3d = new Point3D<Double>(s);
			System.out.println(p3d.toString());
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

	public Point3D(int x, int y, int z) {
		this(new Integer(x), new Integer(y), new Integer(z));
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
		return other.getX().equals(this.getX()) & 
			   other.getY().equals(this.getY()) &
			   other.getZ().equals(this.getZ());
	}
	
	public String toString() {
		return "[ " + x + ", " + y + ", " + z + " ]";
	}
	
	public String toJSON() {
		StringBuffer jsonstr = new StringBuffer("{");
		String name = getProperty(NAME);
		String type = getProperty(TYPE);
		if(_id != null) {
			jsonstr.append(quote("_id", _id)).append(",");
		}
		if(name != null){
			jsonstr.append(quote("name" ,name)).append(",");
		}
		if(type != null) {
			jsonstr.append(quote("type", type)).append(",");
		}
		jsonstr.append(quote("Point3D")).append(": ").append(toString()).append("}");
		return jsonstr.toString();
	}
	
	public String toJSON(String nameLabel, String nameValue, String type) {
		StringBuffer jsonstr = new StringBuffer("{");
		jsonstr.append( quote(nameLabel, nameValue));
		if(type != null && type.length()>0) {
			jsonstr.append(", ").append(quote("type", type));
		}
		jsonstr.append(", ").append(quote("Point3D")).append(": ").append(toString()).append("}");
		return jsonstr.toString();
	}

	public static Point3D<Double> fromJSON(String jsonstr) {
		String raw = jsonstr.replaceAll("[\"\\s{}]", "");	// deletes spaces, curly braces and quotes
		Point3D<Double> point = null;
		Pattern pat = JSON_REGEX;
		Matcher m = pat.matcher(raw);
		boolean b = m.matches();
		if(b) {
			log.debug("# groups: " + m.groupCount());
			point = new Point3D<Double>(m.group(4));
			for(int i=1; i<=m.groupCount()-1; i++) {
				log.debug("group: " + i + "= " + m.group(i));
				Point3D.addFieldValue(point, m.group(i));
			}
			log.debug("point: " + point.toJSON());
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
			x=new Double(Double.parseDouble(m.group(1)));
			y=new Double(Double.parseDouble(m.group(2)));
			z=new Double(Double.parseDouble(m.group(3)));
		}
		else {
			throw new NumberFormatException("Unknown format " +s );
		}
	}
	
	public static void addFieldValue(Point3D<Double> ps, String valueString) {
		String[] fv = valueString.split(":");
		String fname = fv[0];
		String fval = fv[1];
		if(fname.equalsIgnoreCase("name")) {
			ps.setName(fval);
		}
		else if(fname.equalsIgnoreCase("_id")) {
			ps.set_id(fval);
		}
		else if(fname.equalsIgnoreCase("type")) {
			ps.setType(fval);
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
