package common.math.ui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import common.math.complex.Complex;
import common.math.complex.ComplexPlane;
import common.math.complex.TriangleGridColoring;
import common.math.fractal.ComplexColoring;
import common.math.fractal.FractalFormula;
import common.math.fractal.IterationPoint;
import common.math.fractal.MandlebrotFractal;

public class MandelCanvas {

	private int bailout = 4;
	private int maxIterations = 100;
	private Complex start = new Complex(0,0);
	private Complex power = new Complex(2,0);
	
	// defaults
	private double centerX = -0.5;
	private double centerY = 0.0;
	
	private Complex center = new Complex(centerX, centerY);
	private Complex leftTop = new Complex(-2.0 + centerX, 1.5 + centerY);
	private Complex rightTop = new Complex(2.0 + centerX, leftTop.y);
	private Complex leftBottom = new Complex(leftTop.x, -leftTop.y);
	private Complex rightBottom = new Complex(rightTop.x, -rightTop.y);
	private ComplexPlane canvas;
	
	private Dimension windowSize = null;
	
	private Complex z = null;			// global z-value for performing iterations
	private Complex zmax = new Complex(0,0);
	private Complex zmin = new Complex(Double.MAX_VALUE, Double.MAX_VALUE);
	private ComplexColoring coloringAlgorithm = null;
	
	private List<IterationPoint> iterationPoints = new ArrayList<IterationPoint>();
	private FractalFormula fractalFormula = null;
	
	private int totalIn, totalOut = 0;	// total #points in/out of the set

	
	public static final int DEFAULT_WIDTH = 502;
	public static final int DEFAULT_HEIGHT = 502;
	
	static final Logger log = LogManager.getLogger(MandelCanvas.class);
    
	
	public static void main(String[] args){
	
		MandelCanvas mandel = new MandelCanvas();
		mandel.setFractalFormula(new MandlebrotFractal());
		mandel.setColoringAlgorithm(new TriangleGridColoring());
		
		mandel.iterate();
	}

	public MandelCanvas() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public MandelCanvas(int width, int height) {
		windowSize = new Dimension(width, height);
		canvas = new ComplexPlane(leftTop, rightTop, rightBottom, leftBottom);
	}
	
	public int iterate() {
		int numIterations = 0;
		int totalIterations = 0;
		//
		// for each point, compute z from fractal formula
		// until max iterations is reached or bailout condition
		//
		double hincr = (rightTop.real() - leftTop.real())/windowSize.width;
		double vincr = (rightTop.imag() - rightBottom.imag())/windowSize.height;
		log.debug("hincr = " + hincr + " vincr = " + vincr);

		Complex point = new Complex(leftTop);
		int ix, iy;		// from top, left
		int x, y = 0;	// relative to center to map with Ultra fractal x,y
		int xstart = (windowSize.width / -2);
		int ystart = (windowSize.height / 2);
		y = ystart;
		boolean in = false;
		for(iy = 0; iy<windowSize.height; iy++) {
			//logger.debug(iy);
			x = xstart;
			for(ix = 0; ix<=windowSize.width; ix++) {
				IterationPoint ipoint = new IterationPoint(ix, iy, point);
				numIterations = fractalFormula.iteratePoint(ipoint);
				zmax = ipoint.getZMax();
				zmin = ipoint.getZMin();
				z = ipoint.getZ();
				if(numIterations >= maxIterations) {
					totalIn++;
					ipoint.setIn(true);
					iterationPoints.add(ipoint);
					//logger.info("in (" + x + ", " + y + ") :" + point.toString() + " #iter: " + numIterations + " z: " + z.toString());
				}
				else {
					totalOut++;
					ipoint.setIn(false);
					iterationPoints.add(ipoint);
					if(z.mod() > zmax.mod()) {
						zmax.assign(z);
					}
					if(z.mod() < zmin.mod()) {
						zmin.assign(z);
					}
					//logger.info("out (" + x + ", " + y + ") :" + point.toString() + " #iter: " + numIterations + " z: " + z.toString());
				}
				
				totalIterations += numIterations;
				point.add(hincr, 0);
				x++;	// left to right
			}
			point.setReal(leftTop.real());
			point.subtract(0, vincr);
			y--;	// top to bottom
		}
		log.info("total in: " + totalIn + " total out: " + totalOut);
		log.info("zmax = " + zmax + "  zmin = " + zmin);
		return totalIterations;
	}
		
	public int getBailout() {
		return bailout;
	}

	public void setBailout(int bailout) {
		this.bailout = bailout;
	}

	public Complex getLeftBottom() {
		return leftBottom;
	}

	public void setLeftBottom(Complex leftBottom) {
		this.leftBottom = leftBottom;
	}

	public Complex getLeftTop() {
		return leftTop;
	}

	public void setLeftTop(Complex leftTop) {
		this.leftTop = leftTop;
	}

	public Complex getRightBottom() {
		return rightBottom;
	}

	public void setRightBottom(Complex rightBottom) {
		this.rightBottom = rightBottom;
	}

	public Complex getRightTop() {
		return rightTop;
	}

	public void setRightTop(Complex rightTop) {
		this.rightTop = rightTop;
	}

	public Dimension getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(Dimension windowSize) {
		this.windowSize = windowSize;
	}

	public Complex getStart() {
		return start;
	}

	public void setStart(Complex start) {
		this.start = start;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public List<IterationPoint> getIterationPoints() {
		return iterationPoints;
	}

	public Complex getPower() {
		return power;
	}

	public void setPower(Complex power) {
		this.power = power;
	}

	public Complex getCenter() {
		return center;
	}

	public void setCenter(Complex center) {
		this.center = center;
	}

	public ComplexColoring getColoringAlgorithm() {
		return coloringAlgorithm;
	}

	public void setColoringAlgorithm(ComplexColoring coloringAlgorithm) {
		deleteColoringAlgorithm();
		this.coloringAlgorithm = coloringAlgorithm;
		fractalFormula.addIterationListener(coloringAlgorithm);
	}
	public void deleteColoringAlgorithm() {
		if(coloringAlgorithm != null) {
			coloringAlgorithm = null;
		}
	}

	public FractalFormula getFractalFormula() {
		return fractalFormula;
	}

	public void setFractalFormula(FractalFormula fractalFormula) {
		this.fractalFormula = fractalFormula;
		fractalFormula.setMaxIterations(maxIterations);
	}

	public ComplexPlane getCanvas() {
		return canvas;
	}

	public void setCanvas(ComplexPlane canvas) {
		this.canvas = canvas;
		setLeftTop(canvas.getLeftTop());
		setRightTop(canvas.getRightTop());
		setLeftBottom(canvas.getLeftBottom());
		setRightBottom(canvas.getRightBottom());
		setCenter(canvas.getCenter());
	}
	
}
