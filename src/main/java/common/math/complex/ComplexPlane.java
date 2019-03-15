package common.math.complex;

import common.math.Plane;

@SuppressWarnings("rawtypes")
public class ComplexPlane extends Plane {

	public ComplexPlane() {
		this(new Complex());	// center = 0,0
	}

	public ComplexPlane(Complex center) {
		super();
		setCenter(center);
	}
	
	public ComplexPlane(double width, double height) {
		this(new Complex(), width, height);	// center = 0,0
	}
	
	public ComplexPlane(Complex center, double width, double height) {
		super();
		setCenter(center);
		double scalew = width/2.0;
		double scaleh = height/2.0;
		setLeftTop(new Complex(center.x-scalew, center.y+scaleh));
		setLeftBottom(new Complex(center.x-scalew, center.y-scaleh));
		setRightTop(new Complex(center.x+scalew, center.y+scaleh));
		setRightBottom(new Complex(center.x+scalew, center.y-scaleh));
		setHeight(height);
		setWidth(width);
	}
	
	public ComplexPlane(Complex leftTop, Complex rightTop, Complex rightBottom, Complex leftBottom) {
		setLeftTop(leftTop);
		setLeftBottom(leftBottom);
		setRightTop(rightTop);
		setRightBottom(rightBottom);
		setCenter(findCenter());
	}

	/**
	 * @see math.Plane#getCenter()
	 */
	@Override
	public Complex getCenter() {
		return (Complex)super.getCenter();
	}

	public double getLeft() {
		return getLeftTop().x;
	}
	public double getTop() {
		return getLeftTop().y;
	}
	
	/**
	 * Center is defined only ifBounded()
	 * @return Complex center
	 */
	public Complex findCenter() {
		Complex c = null;
		if(isBounded()) {
			c = new Complex((getLeftTop().x + getRightTop().x) /2.0,
							(getLeftBottom().y + getRightBottom().y) /2.0);
		}
		return c;
	}
	
	/**
	 * @see math.Plane#setCenter(java.lang.Number)
	 */
	@Override
	public void setCenter(Number center) {
		super.setCenter((Complex)center);
	}

	/**
	 * @see math.Plane#getLeftBottom()
	 */
	@Override
	public Complex getLeftBottom() {
		return (Complex)super.getLeftBottom();
	}

	/**
	 * @see math.Plane#getLeftTop()
	 */
	@Override
	public Complex getLeftTop() {
		return (Complex)super.getLeftTop();
	}

	/**
	 * @see math.Plane#getRightBottom()
	 */
	@Override
	public Complex getRightBottom() {
		return (Complex)super.getRightBottom();
	}

	/**
	 * @see math.Plane#getRightTop()
	 */
	@Override
	public Complex getRightTop() {
		return (Complex)super.getRightTop();
	}

	/**
	 * @see math.Plane#setLeftBottom(java.lang.Number)
	 */
	@Override
	public void setLeftBottom(Number leftBottom) {
		super.setLeftBottom((Complex)leftBottom);
	}

	/**
	 * @see math.Plane#setLeftTop(java.lang.Number)
	 */
	@Override
	public void setLeftTop(Number leftTop) {
		super.setLeftTop((Complex)leftTop);
	}

	/**
	 * @see math.Plane#setRightBottom(java.lang.Number)
	 */
	@Override
	public void setRightBottom(Number rightBottom) {
		super.setRightBottom((Complex)rightBottom);
	}

	/**
	 * @see math.Plane#setRightTop(java.lang.Number)
	 */
	@Override
	public void setRightTop(Number rightTop) {
		super.setRightTop((Complex)rightTop);
	}

}
