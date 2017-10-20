package mathlib.fractal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mathlib.complex.Complex;

public class JuliaFractal extends FractalFormula {
	static final Logger log = LogManager.getLogger(JuliaFractal.class);
	
	public JuliaFractal() {}
	
	public JuliaFractal(ComplexColoring cc) {
		super(cc);
	}
	private Complex power = new Complex(2,0);
	private Complex seed = new Complex(-1.25, 0);
	
	public int iteratePoint(IterationPoint ipoint) {
		return iteratePoint(ipoint, false);
	}

	/**
	 * init: z = #pixel
	 * loop: z = z^@power + @seed
	 */
	public int iteratePoint(IterationPoint ipoint, boolean checkCycle) {
		setIterationPoint(ipoint);
		//Complex point = ipoint.getPixel();
		
		int iters = 0;
		Complex zmax = COMPLEX_ZERO.clone();
		Complex zmin = COMPLEX_MAX.clone();

		boolean bails = false;
		boolean cycles = false;
		z =  ipoint.getPixel().clone();

		while(iters <= maxIterations ) {
			// TODO: handle complex cases and when power != 2
			if(power.y == 0) {
				z = z.times(z).plus(seed);

				for(IterationListener iterationListener : getIterationListeners()) {
					log.debug(iters + " " + iterationListener.atEachIteration(z));
				}
			}
			iters++;
			cycles = cycleCheck(z);
			if(z.compareTo(zmax) == 1)
				zmax.assign(z);
			if(z.compareTo(zmin) == -1)
				zmin.assign(z);
			if(z.mod() >= bailout) {
				bails = true;
				break;
			}
		}
		iterationPoint.update(iters, zmax, zmin, z, bails, cycles);
		for(IterationListener iterationListener : getIterationListeners()) {
			 iterationListener.afterIterations(iterationPoint);
		}
		return iters;
	}

	public Complex getPower() {
		return power;
	}

	public void setPower(Complex power) {
		this.power = power;
	}

	public Complex getSeed() {
		return seed;
	}

	public void setSeed(Complex seed) {
		this.seed = seed;
	}

}
