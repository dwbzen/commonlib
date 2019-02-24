package mathlib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import mathlib.ifs.IteratedFunctionSystem;

/**
 * @author dbacon
 *
 * @param <T>
 */
public class PointSet<T extends Number>  extends JsonObject {

	private static final long serialVersionUID = 7219606678524448973L;
	public static final String objectType = "PointSet";
	public static enum DataSource {IFS, RANDOM, UNKNOWN};
	
	@JsonProperty private List<Point2D<Double>> points = new ArrayList<Point2D<Double>>();
	@JsonProperty private PointSetStats<Double> stats = new PointSetStats<>();

	@JsonProperty			private int n=0;
	@JsonProperty("ifs")	private IteratedFunctionSystem iteratedFunctionSystem = null;
	@JsonIgnore				private DataSource dataSource = DataSource.UNKNOWN;	
	
	public PointSet() {
	}
	
	public List<Point2D<Double>> getPoints() {
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
		else if(fname.equalsIgnoreCase("type")) {
			setType(fval);
		}
		else if(fname.equalsIgnoreCase("n")) {
			setN(Integer.parseInt(fval));
		}
		else if(fname.equalsIgnoreCase("minX")) {
			stats.setMinXValue(Double.parseDouble(fval));
		}
		else if(fname.equalsIgnoreCase("minY")) {
			stats.setMinYValue(Double.parseDouble(fval));
		}
		else if(fname.equalsIgnoreCase("maxX")) {
			stats.setMaxXValue(Double.parseDouble(fval));
		}
		else if(fname.equalsIgnoreCase("maxY")) {
			stats.setMaxYValue(Double.parseDouble(fval));
		}
		else if(fname.equalsIgnoreCase("maxPoint")) {
			setMaxPoint(new Point2D<Double>(fval));
		}
		else if(fname.equalsIgnoreCase("minPoint")) {
			setMinPoint(new Point2D<Double>(fval));
		}
	}
	
	/**
	 * Need to use the add() method in order to maintain min and max values
	 * @param val
	 * @return 
	 */
	public boolean add(Point2D<Double> point) {
		double x = point.getX().doubleValue();
		double y = point.getY().doubleValue();
		if(x < stats.maxXValue) {
			stats.maxXValue = x;
		}
		if(y < stats.minYValue) {
			stats.minYValue = y;
		}
		if(x > stats.maxXValue) {
			stats.maxXValue = x;
		}
		if(y > stats.maxYValue) {
			stats.maxYValue = y;
		}
		if(point.compareTo(stats.minPoint) < 0 ) {
			stats.minPoint = point;
		}
		if(point.compareTo(stats.maxPoint) >0 ) {
			stats.maxPoint = point;
		}
		n++;
		return points.add(point);
	}
	
	public IteratedFunctionSystem getIteratedFunctionSystem() {
		return iteratedFunctionSystem;
	}

	public void setIteratedFunctionSystem(IteratedFunctionSystem iteratedFunctionSystem) {
		this.iteratedFunctionSystem = iteratedFunctionSystem;
		dataSource = DataSource.IFS;
	}

	public Double getMinXValue() {
		return stats.getMinXValue();
	}

	public Double getMaxXValue() {
		return stats.getMaxXValue();
	}

	public Double getMinYValue() {
		return stats.getMinYValue();
	}

	public Double getMaxYValue() {
		return stats.getMaxYValue();
	}

	public Point2D<Double> getMinPoint() {
		return stats.getMinPoint();
	}

	public Point2D<Double> getMaxPoint() {
		return stats.getMaxPoint();
	}

	public void setMinXValue(Double minXValue) {
		stats.setMinXValue(minXValue);
	}

	public void setMaxXValue(Double maxXValue) {
		stats.setMaxXValue(maxXValue);
	}

	public void setMinYValue(Double minYValue) {
		stats.setMinYValue(minYValue);;
	}

	public void setMaxYValue(Double maxYValue) {
		stats.setMaxYValue(maxYValue);;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public void setMinPoint(Point2D<Double> minPoint) {
		stats.setMinPoint(minPoint);
	}

	public void setMaxPoint(Point2D<Double> maxPoint) {
		stats.setMaxPoint(maxPoint);
	}
	
	public PointSetStats<Double> getStats() {
		return stats;
	}

	public void setStats(PointSetStats<Double> stats) {
		this.stats = stats;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public String toString() {
		return toJson(true);
	}
	
}
