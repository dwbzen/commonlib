package org.dwbzen.common.math.complex;

import org.dwbzen.common.math.fractal.ComplexColoring;
import org.dwbzen.common.math.fractal.MandlebrotFractal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.dwbzen.common.math.ui.MandelCanvas;

/**
 * The Ultra Fractal code: (len is length of triangle side)
	  temp2=#z
	  temp=abs(temp2)
	  ;
	  ; represent z as Eisenstein integer: a + bw, w = -1/2 + i*sqrt(3)/2
	  ;
	  float b = 2*imag(temp)/(sqrt3*len)
	  float a = real(temp)+(len*b/2)
	  complex eis = @center + (a + b*w)
	
	  complex point1 = ceil(eis)
	
	  float rp = real(point1)
	  float ip = real(flip(point1))
	  if(real(temp2) < 0)
		rp = -rp
	  endif
	  if(real(flip(temp2)) < 0)
		ip = -ip
	  endif
	  complex c1 = c1 = (rp - (.5*ip) + im*halfsqrt3*ip)
	  
	  remain = temp2 - c1*normfac

*/
public class TriangleGridColoring extends ComplexColoring {
	static final Logger log = LogManager.getLogger(ComplexColoring.class);
	private double len = 1.0;	// length of 1 side
	private Complex center = new Complex(0,0);
    private boolean debugEnabled = false;

	public static void main(String args[]) {
		TriangleGridColoring tgc = new TriangleGridColoring();
		MandelCanvas mandel = new MandelCanvas();
		mandel.setFractalFormula(new MandlebrotFractal());
		double len = 1.0;
		for(int i=0; i<args.length; i++) {
			if(args[i].equals("-len"))  {
				len = Double.parseDouble(args[++i]);
				i++;
			}
    		Complex z = Complex.parseComplex(args[i]);
    		System.out.println("--------------");
    		tgc.setLen(len);
    		Complex c1 = tgc.atIteration(z);
    		System.out.println(z + "\t" + c1);
    		len = 1.0;
		}

	}
	
	public TriangleGridColoring() {
		super();
	}

	public Complex getCenter() {
		return center;
	}

	public void setCenter(Complex center) {
		this.center = center;
	}

	@Override
	public int afterIterations() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Complex atIteration(Complex z) {
		Complex eis = null;
		Complex z0 = z.abs();
		if(len != 1.0) {
			eis = z0.asEisenstein(len).plus(center);
		}
		else {
			eis = z0.asEisenstein().plus(center);
		}
		eis.setImaginarySymbol("w");
		Complex point2 = eis.ceil();
		Complex point1 = new Complex(Math.ceil(eis.x) , Math.ceil(eis.y));
		if(z.x < 0 ) {
			point1.x = -point1.x;
			
		}
		if(z.y < 0) {
			point1.y = -point1.y;
		}
		Complex c1 = new Complex(point1.x - point1.y*.5, point1.y*Complex.SQRT3/2);
		if(debugEnabled) {
			point1.setImaginarySymbol("w");
			log.debug("   z: " + z);
			log.debug(" eis: " + eis);
			log.debug("  p1: " + point1);
			log.debug("  p2: " + point2);
			log.debug("  c1: " + c1);
		}
		return point1;
	}

	@Override
	protected double smooth(Complex z) {
		// r= @factor*real(il*lp - il*log(log(cabs(remain))))
		// @factor is a positive double
		//  float lp = log(log(@bailout))
		//  complex il = 1/log(log(200*1/len))
		return 1.0;
	}
	
	public double getLen() {
		return len;
	}

	public void setLen(double len) {
		this.len = len;
	}

}
