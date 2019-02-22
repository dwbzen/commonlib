package mathlib;

import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.util.IJson;

public class PointSetStats<T extends Number> implements IJson {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty	private String type = "stats";
	@JsonProperty("minX")	public Double minXValue = Double.MAX_VALUE;
	@JsonProperty("minY")	public Double minYValue = Double.MAX_VALUE;
	@JsonProperty("maxX")	public Double maxXValue = Double.MIN_VALUE;
	@JsonProperty("maxY")	public Double maxYValue = Double.MIN_VALUE;

	@JsonProperty	public Point2D<T> minPoint = new Point2D<T>(Double.MAX_VALUE, Double.MAX_VALUE);	// determined by Point2D compare
	@JsonProperty	public Point2D<T> maxPoint = new Point2D<T>(Double.MIN_VALUE, Double.MIN_VALUE);	// determined by Point2D compare
	
	public PointSetStats() {
		
	}
	public PointSetStats(Double minX, Double maxX, Double minY, Double maxY, Point2D<T> minPoint, Point2D<T> maxPoint) {
		minXValue = minX;
		maxXValue = maxX;
		minYValue = minY;
		maxYValue = maxY;
		this.minPoint = minPoint;
		this.maxPoint = maxPoint;
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
	public Point2D<T> getMinPoint() {
		return minPoint;
	}
	public void setMinPoint(Point2D<T> minPoint) {
		this.minPoint = minPoint;
	}
	public Point2D<T> getMaxPoint() {
		return maxPoint;
	}
	public void setMaxPoint(Point2D<T> maxPoint) {
		this.maxPoint = maxPoint;
	}
	public String getType() {
		return type;
	}
	
}

