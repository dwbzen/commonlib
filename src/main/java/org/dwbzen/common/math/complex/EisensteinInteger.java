package org.dwbzen.common.math.complex;

/**
 * Eisentstein integers have the form  z = a + bw
 * where a, b are integers and w =  1/2(-1 + i(3^.5)) = exp((2 * #pi * (0,1))/3)
 * w = -.5 + (sqrt(3)/2) * i
 * a + b(-.5 + 0.86602540378443864676372317075294i) =
 * a - .5b + b0.86602540378443864676372317075294i
 * real: a - .5b
 * img:  b0.86602540378443864676372317075294i
 * @author dbacon
 *
 */
public class EisensteinInteger extends Complex {
	
	private static final long serialVersionUID = 1L;

	/**
	 * OMEGA = 1/2(-1 + i*sqrt(3)) = e^(2*pi*i/3)
	 */
	public static final Complex OMEGA = new Complex(-0.5, (Math.sqrt(3.0))/2.0);
	
	private double a;
	private double b;
	
	public EisensteinInteger(int a, int b) {
		super(a-0.5*b, b*0.86602540378443864676372317075294);
		this.a = a;
		this.b = b;
		IMAGINARY_SYMBOL = "w";
	}
	
	public static Complex asEisenstein(Complex z) {
	   	return z.asEisenstein();
	}
	public static Complex asEisenstein(Complex z, double len) {
	   	return z.asEisenstein(len);	
	}
	
	public double getA() {
		return a;
	}

	public double getB() {
		return b;
	}

	public Complex getOmega() {
		return OMEGA;
	}

}
