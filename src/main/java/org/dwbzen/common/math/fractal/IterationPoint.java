package org.dwbzen.common.math.fractal;

import org.dwbzen.common.math.complex.Complex;

public class IterationPoint {

	public IterationPoint(Complex pixel) {
		this.pixel = pixel;
	}
	public IterationPoint(int x, int y, Complex pixel) {
		this.x = x;
		this.y = y;
		this.pixel = pixel;
	}
	
	public IterationPoint(int x, int y, int iterations, Complex z, boolean in) {
		this.iterations = iterations;
		this.z = z;
		this.in = in;
		this.x = x;
		this.y = y;
	}
	
	private int iterations;
	private Complex z;
	public int x = 0;
	public int y = 0;
	/**
	 * The formula is iterated until the maximum iteration count is reached, 
	 * or until the bail-out condition is met. 
	 * If the bail-out condition is met, the pixel is an outside pixel. 
	 * Otherwise, it is an inside pixel.
	 */
	private boolean in;
	private Complex zMax;
	private Complex zMin;
	private Complex pixel;
	private boolean bailed;
	private boolean cycles;
	private int repeatIteration = -1;
	
	public Complex getPixel() {
		return pixel;
	}

	public void setPixel(Complex pixel) {
		this.pixel = pixel;
	}

	public int getIterations() {
		return iterations;
	}
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	public Complex getZ() {
		return z;
	}
	public void setZ(Complex z) {
		this.z = z;
	}
	public boolean isIn() {
		return in;
	}
	public void setIn(boolean in) {
		this.in = in;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public Complex getZMax() {
		return zMax;
	}

	public void setZMax(Complex max) {
		zMax = max;
	}

	public Complex getZMin() {
		return zMin;
	}

	public void setZMin(Complex min) {
		zMin = min;
	}
	
	public boolean isBailed() {
		return bailed;
	}

	public boolean isCycles() {
		return cycles;
	}

	public int getRepeatIteration() {
		return repeatIteration;
	}
	public void setRepeatIteration(int repeatIteration) {
		this.repeatIteration = repeatIteration;
	}
	
	public int getCyclePeriod() {
		return iterations - repeatIteration - 1;
	}
	
	public void update(int iters, Complex zmax, Complex zmin, Complex z, boolean bailed, boolean cycles) {
		iterations = iters;
		zMax = zmax;
		zMin = zmin;
		this.z = z;
		this.bailed = bailed;
		this.cycles = cycles;
		this.in = !bailed;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("(x,y): (" + x + ", " + y + ") pixel: " + this.pixel + " z: " + z);
		sb.append("\n iter: " + this.iterations + "\tbailed: " + bailed + "\tin: " + in + "\tcycles: " + cycles);
		sb.append("\trepeat Iteration: " + repeatIteration + "\tperiod: " + getCyclePeriod());
		sb.append("\n zMin: " + zMin + "\tzMax: " + zMax);
		return sb.toString();
	}
}
