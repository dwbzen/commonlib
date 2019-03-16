package org.dwbzen.common.math;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class PointSetStats<T extends Number> extends JsonObject {
	
	private static final long serialVersionUID = 1L;
	public static final String objectType = "stats";
	
	@JsonProperty("minX")	public Double minXValue = Double.MAX_VALUE;
	@JsonProperty("minY")	public Double minYValue = Double.MAX_VALUE;
	@JsonProperty("maxX")	public Double maxXValue = Double.MIN_VALUE;
	@JsonProperty("maxY")	public Double maxYValue = Double.MIN_VALUE;

	@JsonProperty	public Point2D<Double> minPoint = new Point2D<Double>(Double.MAX_VALUE, Double.MAX_VALUE);	// determined by Point2D compare
	@JsonProperty	public Point2D<Double> maxPoint = new Point2D<Double>(Double.MIN_VALUE, Double.MIN_VALUE);	// determined by Point2D compare
	
	public PointSetStats() {
		setType(objectType);
	}
	public PointSetStats(Double minX, Double maxX, Double minY, Double maxY, Point2D<Double> minPoint, Point2D<Double> maxPoint) {
		setType(objectType);
		minXValue = minX;
		maxXValue = maxX;
		minYValue = minY;
		maxYValue = maxY;
		this.minPoint = minPoint;
		this.maxPoint = maxPoint;
	}
	
	@SuppressWarnings("unchecked")
	public static PointSetStats<Double> fromJson(String jsonstr) {
		 PointSetStats<Double> stats = null;
			try {
				stats = mapper.readValue(jsonstr, PointSetStats.class);
			} catch (JsonParseException e) {
				System.err.println("JsonParseException (PointSetStats): " + jsonstr);
				e.printStackTrace();
			} catch (JsonMappingException e) {
				System.err.println("JsonMappingException (PointSetStats): " + jsonstr);
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("IOException: " + jsonstr);
				e.printStackTrace();
			}
			return stats;
	}
	
	public Double getMinXValue() {
		return minXValue;
	}
	public void setMinXValue(Double minXValue) {
		this.minXValue = minXValue;
	}
	public Double getMaxXValue() {
		return maxXValue;
	}
	public void setMaxXValue(Double maxXValue) {
		this.maxXValue = maxXValue;
	}
	public Double getMinYValue() {
		return minYValue;
	}
	public void setMinYValue(Double minYValue) {
		this.minYValue = minYValue;
	}
	public Double getMaxYValue() {
		return maxYValue;
	}
	public void setMaxYValue(Double maxYValue) {
		this.maxYValue = maxYValue;
	}
	public Point2D<Double> getMinPoint() {
		return minPoint;
	}
	public void setMinPoint(Point2D<Double> minPoint) {
		this.minPoint = minPoint;
	}
	public Point2D<Double> getMaxPoint() {
		return maxPoint;
	}
	public void setMaxPoint(Point2D<Double> maxPoint) {
		this.maxPoint = maxPoint;
	}
	
	@Override
	@JsonIgnore
	public String getName() {
		return super.getName();
	}
	
}

