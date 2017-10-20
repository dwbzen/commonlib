package mathlib;


/**
 * A Plane can be bounded or unbounded.
 * @author dbacon
 *
 */
public abstract class Plane<T extends Number> {
	
	private Number center;
	private Number leftTop;
	private Number rightTop;
	private Number leftBottom;
	private Number rightBottom;
	
	private double width;
	private double height;
	
	public Number getCenter() {
		return center;
	}
	public void setCenter(Number center) {
		this.center = center;
	}
	public Number getLeftTop() {
		return leftTop;
	}
	public void setLeftTop(Number leftTop) {
		this.leftTop = leftTop;
	}
	public Number getRightTop() {
		return rightTop;
	}
	public void setRightTop(Number rightTop) {
		this.rightTop = rightTop;
	}
	public Number getLeftBottom() {
		return leftBottom;
	}
	public void setLeftBottom(Number leftBottom) {
		this.leftBottom = leftBottom;
	}
	public Number getRightBottom() {
		return rightBottom;
	}
	public void setRightBottom(Number rightBottom) {
		this.rightBottom = rightBottom;
	}
	
	public boolean isBounded() {
		return !(leftBottom == null || leftBottom == null || rightTop == null || rightBottom == null);
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	
}
