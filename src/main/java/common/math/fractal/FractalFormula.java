package common.math.fractal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import common.math.complex.Complex;
import common.math.complex.ComplexSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class FractalFormula implements IFractalFormula {
	protected static final Logger log = LogManager.getLogger(FractalFormula.class);
    
	
	public static Complex COMPLEX_ZERO = new Complex();
	public static Complex COMPLEX_MAX = new Complex(Double.MAX_VALUE, Double.MAX_VALUE);

	protected Complex z = null;			// global z-value for performing iterations
	/**
	 * The current IterationPoint being iterated by the fractal formula
	 */
	protected IterationPoint iterationPoint;
	private Collection<IterationListener> iterationListeners = new ArrayList<IterationListener>();
	private ComplexSet zHistory = null;	// for each iterationPoint
	
	private boolean cycleCheck = false;
	protected int maxIterations = 100;
	protected int bailout = 128;

	public FractalFormula() {		

	}
	
	public FractalFormula(IterationListener listener) {
		this();
		addIterationListener(listener);
	}

	public abstract int iteratePoint(IterationPoint ipoint);

	public IterationPoint getIterationPoint() {
		return iterationPoint;
	}

	public void setIterationPoint(IterationPoint point) {
		this.iterationPoint = point;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public Complex getZ() {
		return z;
	}

	public Collection<IterationListener> getIterationListeners() {
		return iterationListeners;
	}

	public void addIterationListener(IterationListener listener) {
		iterationListeners.add(listener);
	}

	public int getBailout() {
		return bailout;
	}

	public void setBailout(int bailout) {
		this.bailout = bailout;
	}

	public boolean isCycleCheck() {
		return cycleCheck;
	}

	public void setCycleCheck(boolean cycleCheck) {
		this.cycleCheck = cycleCheck;
	}
	
	public void resetZHistory() {
		if(cycleCheck) {
			zHistory = new ComplexSet();
		}
	}
	
	public Set<Complex> getZHistory() {
		return zHistory;
	}
	
	/**
	 * Checks if the Complex number z is already present
	 * in zHistory. If so, this represents a repeated cycle.
	 * @param z
	 * @return true if z is a repeated number, false otherwise (or zHistory is null)
	 */
	public boolean cycleCheck(Complex z) {
		boolean cycles = (zHistory != null) ?  !zHistory.add(z) : false;
		if(cycles) {
			iterationPoint.setRepeatIteration(zHistory.indexOf(z));
		}
		return cycles;
	}
}
