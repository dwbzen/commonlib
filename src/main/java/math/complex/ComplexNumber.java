package math.complex;

import math.INumber;

public class ComplexNumber extends Complex implements INumber {

	private static final long serialVersionUID = -493360012297153883L;

	public void add(INumber number) {
		super.add(number.toComplex());
	}

	public INumber plus(INumber number) {

		return null;
	}

	public INumber div(INumber number) {
		// TODO Auto-generated method stub
		return null;
	}

	public void divideBy(INumber number) {
		// TODO Auto-generated method stub
		
	}

	public INumber minus(INumber number) {
		// TODO Auto-generated method stub
		return null;
	}

	public void mult(INumber number) {
		// TODO Auto-generated method stub
		
	}

	public void subtract(INumber number) {
		// TODO Auto-generated method stub
		
	}

	public INumber times(INumber number) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isComplex() {
		return true;
	}

	public INumber e(INumber x) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Takes the natural log of this
	 * @return the INumber ln of this
	 * @see math.INumber#ln()
	 */
	public INumber ln() {
		// TODO Auto-generated method stub
		return null;
	}

	public INumber power(INumber p) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ComplexNumber toComplex() {
		return this;
	}

}
