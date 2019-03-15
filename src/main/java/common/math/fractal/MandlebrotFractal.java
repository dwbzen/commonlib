package common.math.fractal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import common.math.complex.Complex;

public class MandlebrotFractal extends FractalFormula {
	static final Logger log = LogManager.getLogger(MandlebrotFractal.class);

	public MandlebrotFractal() {}
	
	public MandlebrotFractal(ComplexColoring cc) {
		super(cc);
	}
	
	public static void main(String[] args){
		
		Complex zarg = null;
		int iterations = -1;
		for(int i=0; i<args.length; i++) {
			if(args[i].equalsIgnoreCase("-iter")) {
				iterations = Integer.parseInt(args[++i]);
			}
			else {
	    		zarg = Complex.parseComplex(args[i]);
			}
		}
		
		if(zarg == null) {
			System.err.println("No complex argument present");
			System.exit(-1);
		}
		IterationPoint ip = new IterationPoint(zarg);
		FractalFormula ff = new MandlebrotFractal();
		if(iterations > 0) {
			ff.setMaxIterations(iterations);
		}
		IterationListener listener = new GenericIterationListener();
		ff.addIterationListener(listener);
		ff.setCycleCheck(true);
		System.out.println(ip.toString());
		
	}
	
	private Complex power = new Complex(2,0);
	private Complex start = COMPLEX_ZERO;
	
	/**
	 * init:  z = @start
	 * loop:  z = z^@power + #pixel
	 */
	public int iteratePoint(IterationPoint ipoint) {
		setIterationPoint(ipoint);
		Complex point = ipoint.getPixel();
		resetZHistory();	// only if cycleCheck is true
		
		int iters = 0;
		Complex zmax = COMPLEX_ZERO;
		Complex zmin = COMPLEX_MAX;
		boolean bails = false;
		boolean cycles = false;
		z = getStart();
		
		while(iters <= maxIterations ) {
			// TODO: handle complex cases and when power != 2
			if(power.y == 0) {
				z = z.times(z).plus(point);
				
				for(IterationListener iterationListener : getIterationListeners()) {
					log.debug(iters + " " + iterationListener.atEachIteration(z));
				}
			}
			iters++;
			cycles = cycleCheck(z);
			bails = z.mod() >= bailout;
			if(z.compareTo(zmax) == 1)
				zmax.assign(z);
			if(z.compareTo(zmin) == -1)
				zmin.assign(z);
			if(cycles || bails)
				break;
		}
		iterationPoint.update(iters, zmax, zmin, z, bails, cycles);
		for(IterationListener iterationListener : getIterationListeners()) {
			 iterationListener.afterIterations(iterationPoint);
		}
		return iters;
	}

	public Complex getStart() {
		return start;
	}

	public void setStart(Complex start) {
		this.start = start;
	}

	public Complex getPower() {
		return power;
	}

	public void setPower(Complex power) {
		this.power = power;
	}

}
