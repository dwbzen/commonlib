package math;

import java.io.Serializable;

import math.complex.ComplexNumber;

public interface INumber  extends Serializable, Comparable<Number>  {

	void add(INumber number);
	INumber plus(INumber number);
	
	void subtract(INumber number);
	INumber minus(INumber number);
	
	void mult(INumber number);
	INumber times(INumber number);
	
	void divideBy(INumber number);
	INumber div(INumber number);
	
	INumber e(INumber x);
	INumber ln();
	INumber power(INumber p);
	
	boolean isComplex();
	/**
	 * Represents this INumber as a ComplexNumber
	 * @return
	 */
	ComplexNumber toComplex();
}
