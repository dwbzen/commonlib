package mathlib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * @author dbacon
 *
 * @param <T>
 */
public class PointSet<T extends Number>  extends JsonObject {

	private static final long serialVersionUID = 7219606678524448973L;
	public static String OBJECT_TYPE = "stats";
	
	@JsonProperty private List<Point2D<T>> points = new ArrayList<Point2D<T>>();
	
	@JsonProperty("minX")	private Double minXValue = Double.MAX_VALUE;
	@JsonProperty("maxX")	private Double maxXValue = Double.MIN_VALUE;
	
	@JsonProperty("minY")	private Double minYValue = Double.MAX_VALUE;
	@JsonProperty("maxY")	private Double maxYValue = Double.MIN_VALUE;

	@JsonProperty	private Point2D<T> minPoint = new Point2D<T>(Double.MAX_VALUE, Double.MAX_VALUE);	// determined by Point2D compare
	@JsonProperty	private Point2D<T> maxPoint = new Point2D<T>(Double.MIN_VALUE, Double.MIN_VALUE);	// determined by Point2D compare
	@JsonProperty	private int n=0;
	@JsonProperty	private String linearFunction = null;
	
	public PointSet() {
		setProperty(TYPE, OBJECT_TYPE);
	}
	
	public static void main(String args[]) {

	}
	
	public List<Point2D<T>> getPoints() {
		return points;
	}
	
	@SuppressWarnings("unchecked")
	public static PointSet<Number> fromJson(String jsonstr) {
		PointSet<Number> pointSet = null;
		try {
			pointSet = mapper.readValue(jsonstr, PointSet.class);
		} catch (JsonParseException e) {
			log.error("JsonParseException (PointSet): " + jsonstr);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.error("JsonMappingException (PointSet): " + jsonstr);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException: " + jsonstr);
			e.printStackTrace();
		}
		return pointSet;
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
	public void addFieldValue(String valueString) {
		String[] fv = valueString.split(":");
		String fname = fv[0];
		String fval = (fv.length == 2) ? fv[1]  : "{" + fv[1] + ":" + fv[2] + "}";
		if(fname.equalsIgnoreCase("name")) {
			setName(fval);
		}
		else if(fname.equals("LinearFunction")) {
			setLinearFunction(valueString.substring(valueString.indexOf(":") + 1));
		}
		else if(fname.equalsIgnoreCase("type")) {
			setType(fval);
		}
		else if(fname.equalsIgnoreCase("n")) {
			setN(Integer.parseInt(fval));
		}
		else if(fname.equalsIgnoreCase("minX")) {
			minXValue = Double.parseDouble(fval);
		}
		else if(fname.equalsIgnoreCase("minY")) {
			minYValue = Double.parseDouble(fval);
		}
		else if(fname.equalsIgnoreCase("maxX")) {
			maxXValue = Double.parseDouble(fval);
		}
		else if(fname.equalsIgnoreCase("maxY")) {
			maxYValue = Double.parseDouble(fval);
		}
		else if(fname.equalsIgnoreCase("maxPoint")) {
			setMaxPoint(new Point2D<T>(fval));
		}
		else if(fname.equalsIgnoreCase("minPoint")) {
			setMinPoint(new Point2D<T>(fval));
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
	public String toString() {
		return toJson(true);
	}
	
}
