package mathlib;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Samples:
 * { "LinearFunction": "f1:{ [ 0.5, 0, 0 ], [ 0, 0.5, 0.5 ]},f2:{ [ 0.5, 0, 0.5 ], [ 0, 0.5, 0.5 ]},f3:{ [ 0.5, 0, 0.25 ], [ 0, 0.5, 0 ]}", "name" : "ifs2", "type" : "stats", "n" : 10, "minX" : 0.1251607, "minY" : 0.002710343, "maxX" : 0.8173904, "maxY" : 0.712295, "minPoint" : [  0.1298051,  0.08795198 ], "maxPoint" : [  0.2598582,  0.6771501 ] }
 * { "LinearFunction": "random", "type": "stats", "n": 100, "minX": 0.02218037, "minY": 0.009829026, "maxX": 0.99919, "maxY": 0.9709872, "minPoint":[ 0.09633727, 0.04835826 ], "maxPoint":[ 0.9914837, 0.9709872 ] }
 * 
 * @author dbacon
 *
 * @param <T>
 */
public class PointSet<T extends Number>  extends JSONObject {

	private static final long serialVersionUID = 7219606678524448973L;
	public static final Pattern JSON_REGEX = Pattern.compile("(LinearFunction:.+),(name:.+),(type:.+),(n:\\d+),(minX:.+),(minY:.+),(maxX:.+),(maxY:[\\d-\\.]+),(minPoint:.+),(maxPoint:.+)");
	public static String OBJECT_TYPE = "stats";
	
	private List<Point2D<T>> points = new ArrayList<Point2D<T>>();
	
	private Double minXValue = Double.MAX_VALUE;
	private Double maxXValue = Double.MIN_VALUE;
	
	private Double minYValue = Double.MAX_VALUE;
	private Double maxYValue = Double.MIN_VALUE;

	private Point2D<T> minPoint = new Point2D<T>(Double.MAX_VALUE, Double.MAX_VALUE);	// determined by Point2D compare
	private Point2D<T> maxPoint = new Point2D<T>(Double.MIN_VALUE, Double.MIN_VALUE);	// determined by Point2D compare
	private int n=0;
	private String linearFunction = null;
	
	public PointSet() {
		setProperty(TYPE, OBJECT_TYPE);
	}
	
	public static void main(String args[]) {
		// sample is JSON stats record from MongoDB
		String sample = "{\"LinearFunction\": \"f1:{ [ 0.5, 0, 0 ], [ 0, 0.5, 0.5 ]},f2:{ [ 0.5, 0, 0.5 ], [ 0, 0.5, 0.5 ]},f3:{ [ 0.5, 0, 0.25 ], [ 0, 0.5, 0 ]}\",\"name\": \"sierpinski3\",\"type\": \"stats\", \"n\": 10000,\"minX\": 9.303232E-4,\"minY\": 0.004114622,\"maxX\": 0.9985086,\"maxY\": 0.9999987,\"minPoint\":[ 0.5002074, 0.0009303232 ],\"maxPoint\":[ 0.9968433, 0.9997311 ] }";

		String raw = sample.replaceAll("[\"\\s{}]", "");	// deletes spaces, curly braces and quotes
		System.out.println("raw: " + raw);
		
		Pattern pat = JSON_REGEX;
		Matcher m = pat.matcher(raw);
		boolean b = m.matches();
		if(b) {
			PointSet<Number> ps = new PointSet<Number>();
			System.out.println("# groups: " + m.groupCount());
			for(int i=1; i<=m.groupCount(); i++) {
				System.out.println("group: " + i + "= " + m.group(i));
				PointSet.addFieldValue(ps, m.group(i));
			}
			System.out.println(ps);
		}
		PointSet<Number> ps2 = PointSet.fromJSONString(sample);
		System.out.println("\nsample:\n " + sample + "\nfromJSONString:\n " + ps2);
	}
	
	public List<Point2D<T>> getPoints() {
		return points;
	}
	
	public static PointSet<Number> fromJSONString(String jsonstr) {
		PointSet<Number> pointSet = null;
		String raw = jsonstr.replaceAll("[\"\\s{}]", "");
		Matcher m = JSON_REGEX.matcher(raw);
		if( m.matches()) {
			pointSet = new PointSet<Number>();
			for(int i=1; i<=m.groupCount(); i++) {
				PointSet.addFieldValue(pointSet, m.group(i));
			}
		}
		return pointSet;
	}
	
	public boolean isPointSetJSON(String jsonstr) {
		String raw = jsonstr.replaceAll("[\"\\s{}]", "");
		Matcher m = JSON_REGEX.matcher(raw);
		return m.matches();
	}
	
	public int size() {
		this.n = points.size();
		return n;
	}
	
	/**
	 * Format is <field>:<value>
	 * where <field> is the name of an instance field (variable)
	 * and <value> is an appropriate value
	 * Some examples: name:ifs2, minX:0.123
	 * 
	 * @param valueString
	 */
	public static void addFieldValue(PointSet<Number> ps, String valueString) {
		String[] fv = valueString.split(":");
		String fname = fv[0];
		String fval = (fv.length == 2) ? fv[1]  : "{" + fv[1] + ":" + fv[2] + "}";
		if(fname.equalsIgnoreCase("name")) {
			ps.setName(fval);
		}
		else if(fname.equalsIgnoreCase("_id")) {
			ps.set_id(fval);
		}
		else if(fname.equals("LinearFunction")) {
			ps.setLinearFunction(valueString.substring(valueString.indexOf(":") + 1));
		}
		else if(fname.equalsIgnoreCase("type")) {
			ps.setType(fval);
		}
		else if(fname.equalsIgnoreCase("n")) {
			ps.setN(Integer.parseInt(fval));
		}
		else if(fname.equalsIgnoreCase("minX")) {
			ps.minXValue = Double.parseDouble(fval);
		}
		else if(fname.equalsIgnoreCase("minY")) {
			ps.minYValue = Double.parseDouble(fval);
		}
		else if(fname.equalsIgnoreCase("maxX")) {
			ps.maxXValue = Double.parseDouble(fval);
		}
		else if(fname.equalsIgnoreCase("maxY")) {
			ps.maxYValue = Double.parseDouble(fval);
		}
		else if(fname.equalsIgnoreCase("maxPoint")) {
			ps.setMaxPoint(new Point2D<Number>(fval));
		}
		else if(fname.equalsIgnoreCase("minPoint")) {
			ps.setMinPoint(new Point2D<Number>(fval));
		}
	}
	
	/**
	 * Need to use the add() method in order to maintain min and max values
	 * @param val
	 * @return 
	 */
	public boolean add(Point2D<T> point) {
		double x = point.getX().doubleValue();
		double y = point.getY().doubleValue();
		if(x < minXValue) {
			minXValue = x;
		}
		if(y < minYValue) {
			minYValue = y;
		}
		if(x > maxXValue) {
			maxXValue = x;
		}
		if(y > maxYValue) {
			maxYValue = y;
		}
		if(point.compareTo(minPoint) < 0 ) {
			minPoint = point;
		}
		if(point.compareTo(maxPoint) >0 ) {
			maxPoint = point;
		}
		n++;
		return points.add(point);
	}
	
	public Double getMinXValue() {
		return minXValue;
	}

	public Double getMaxXValue() {
		return maxXValue;
	}

	public Double getMinYValue() {
		return minYValue;
	}

	public Double getMaxYValue() {
		return maxYValue;
	}

	public Point2D<T> getMinPoint() {
		return minPoint;
	}

	public Point2D<T> getMaxPoint() {
		return maxPoint;
	}

	public void setMinXValue(Double minXValue) {
		this.minXValue = minXValue;
	}

	public void setMaxXValue(Double maxXValue) {
		this.maxXValue = maxXValue;
	}

	public void setMinYValue(Double minYValue) {
		this.minYValue = minYValue;
	}

	public void setMaxYValue(Double maxYValue) {
		this.maxYValue = maxYValue;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public void setMinPoint(Point2D<T> minPoint) {
		this.minPoint = minPoint;
	}

	public void setMaxPoint(Point2D<T> maxPoint) {
		this.maxPoint = maxPoint;
	}

	public String getLinearFunction() {
		return linearFunction;
	}

	public void setLinearFunction(String linearFunction) {
		this.linearFunction = linearFunction;
	}

	@Override
	public String toJSON() {
		StringBuffer jsonstr = new StringBuffer("{ ");
		if(linearFunction != null) {
			jsonstr.append(quote("LinearFunction", linearFunction)).append(", ");
		}
		jsonstr.append(getJSONProperties()  + ", ");
		if(_id != null) {
			jsonstr.append( quote("_id:", _id)).append(", ");
		}
		
		jsonstr.append(quote("n", n)).append(", ");
		jsonstr.append(quote("minX", minXValue)).append(", ");
		jsonstr.append(quote("minY", minYValue)).append(", ");
		jsonstr.append(quote("maxX", maxXValue )).append(", ");
		jsonstr.append(quote("maxY", maxYValue)).append(", ");
		jsonstr.append(quote("minPoint")).append(":").append(minPoint.toString()).append("," );
		jsonstr.append(quote("maxPoint")).append(":").append(maxPoint.toString()).append(" }" );
		return jsonstr.toString();
	}
	
	@Override
	public String toString() {
		return toJSON();
	}
	
}
