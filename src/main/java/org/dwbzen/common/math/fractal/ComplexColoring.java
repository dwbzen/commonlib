package org.dwbzen.common.math.fractal;

import org.dwbzen.common.math.complex.Complex;

/**
 * Base class for coloring algorithm for fractals
 * @author dbacon
 *
 */
abstract public class ComplexColoring extends GenericIterationListener {
	
	private Complex zIter = null;
	private int maxIterations;
	private int minIterations;
	private double totalDistance = 0.0;		// from the origin
	private double averageDistance = 0.0;	// totalDistance / #iterations
	private boolean useSmoothing = false;	// set to true if there is a smoothing algorithm
	private double bailout = 128.0;

	public static int INDEX_MIN = 0;
	public static int INDEX_MAX = 400;
	
	public ComplexColoring() {
	}
	
	/**
	 * This method should be called once per iteration
	 * using the current value of z.
	 * It updates zMax, zMin and returns a Complex value that
	 * may also be used in subsequent iterations.
	 * 
	 * @return
	 */
	public Complex atEachIteration(Complex z) {
		Complex remain = atIteration(z);	// provided by specific coloring algorithm
		iterations++;
		double dist;
		if(isUseSmoothing()) {
			dist = smooth(remain);
		}
		else {
			dist = remain.cabs();
		}
		totalDistance += dist;
		averageDistance = totalDistance / iterations;
		if(dist < minDistance) {
			minDistance = dist;
			minIterations = iterations;
			zMin.assign(z);
		}
		if(dist > maxDistance) {
			maxDistance = dist;
			maxIterations = iterations;
			zMax.assign(z);
		}
		return remain;
	}
	
	public abstract Complex atIteration(Complex z);
	
	/**
	 * An alternative distance method. Replaces cabs(remain)
	 * where remain is the value returned by atIteration(z)
	 * 
	 * @param z
	 * @return
	 */
	protected abstract double smooth(Complex z);
	
	/**
	 * Computes a final index value after all iterations are completed
	 * Range is INDEX_MAX, INDEX_MIN, usually 0 to 400.
	 * @return
	 */
	abstract public int afterIterations();
	
	public int getGradientDepth() {
		return INDEX_MAX;
	}

	public Complex getZIter() {
		return zIter;
	}

	public void setZIter(Complex iter) {
		zIter = iter;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public double getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(double totalDistance) {
		this.totalDistance = totalDistance;
	}

	public double getAverageDistance() {
		return averageDistance;
	}

	public void setAverageDistance(double averageDistance) {
		this.averageDistance = averageDistance;
	}

	public boolean isUseSmoothing() {
		return useSmoothing;
	}

	public void setUseSmoothing(boolean useSmoothing) {
		this.useSmoothing = useSmoothing;
	}

	public int getMinIterations() {
		return minIterations;
	}

	public void setMinIterations(int minIterations) {
		this.minIterations = minIterations;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}

	public double getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(double minDistance) {
		this.minDistance = minDistance;
	}
	
	public double getBailout() {
		return bailout;
	}

	public void setBailout(double bailout) {
		this.bailout = bailout;
	}
}
