package common.math.complex;

import common.math.ui.MandelCanvas;
import common.math.fractal.ComplexColoring;
import common.math.fractal.IterationPoint;

/**
 * Used to color Mandlebrot, Julia type fractals
 * @author dbacon
 *
 */
public class StandardColoring  extends ComplexColoring {

	private MandelCanvas mandel;
	
	@Override
	public int afterIterations() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Complex atIteration(Complex z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected double smooth(Complex z) {
		// r= @factor*real(il*lp - il*log(log(cabs(remain))))
		// @factor is a positive double
		// float lp = log(log(@bailout))
		// complex il = 1/log(log(200*1/len))
		return 1.0;
	}

	public MandelCanvas getMandel() {
		return mandel;
	}

	public void setMandel(MandelCanvas mandel) {
		this.mandel = mandel;
	}

	protected int colorOutsidePoint(IterationPoint ip) {
		double lp = Math.log(Math.log(mandel.getBailout()));
		double il = 1 / Math.log(mandel.getPower().x);
		double zmod = .05 * (ip.getIterations() + il * lp - il
				* Math.log(Math.log(ip.getZ().cabs())));
		
		int p = (int)Math.round(Math.abs(zmod*getGradientDepth()));
		return p;
	}
}
