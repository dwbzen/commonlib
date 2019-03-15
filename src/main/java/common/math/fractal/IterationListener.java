package common.math.fractal;

import java.util.EventListener;

import common.math.complex.Complex;

public interface IterationListener extends EventListener {
	
	public Complex atEachIteration(Complex z);
	
	public Complex atIteration(Complex z);
	 
	public int afterIterations(Object obj);
	
	public Complex getZMax();

	public Complex getZMin();


}
