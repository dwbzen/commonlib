package common.math.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A color gradient created from point, color combinations
 * To make scaling easy, the gradient width is fixed at 400.
 * The gradient is created from n-points, each with a Color
 * Each point is an x-value from 0 to 400.
 * 
 * @author dbacon
 *
 */
public class Gradient {
	
	public final static int SIZE = 400; // 0 to 399, index 400 is the default color
	private boolean cyclic = true;
	private Color defaultColor = Color.BLACK;	// used when not cyclic
	private SortedSet<GradientPoint> gradientPoints = null;
	private List<Color> gradient = null;
	
	public Gradient(SortedSet<GradientPoint> points) {
		this(points, true);
	}
	
	public Gradient(SortedSet<GradientPoint> points, boolean cyclic) {
		this.gradientPoints = points;
		this.cyclic = cyclic;
		createGradient();
	}
	
	public Gradient(int x1, Color c1, int x2, Color c2) {
		gradientPoints = new TreeSet<GradientPoint>(new GradientPointComparator());
		gradientPoints.add(new GradientPoint(x1, c1));
		gradientPoints.add(new GradientPoint(x2, c2));
		gradientPoints.add(new GradientPoint(SIZE-1, c1));
		createGradient();
	}
	
	/**
	 * A simple 2-color gradient for x=0 to x=SIZE-1
	 * The colors goes from c1 to c2 then back to c1
	 * @param c1 the Color for x=0
	 * @param c2 the Color for x=SIZE/2
	 */
	public Gradient(Color c1, Color c2) {
		gradientPoints = new TreeSet<GradientPoint>();
		gradientPoints.add(new GradientPoint(0, c1));
		gradientPoints.add(new GradientPoint(SIZE/2, c2));
		gradientPoints.add(new GradientPoint(SIZE - 1, c1));
		createGradient();
	}
	/**
	 * A Color in a point in RGB space, 0-255 for each.
	 * The gradient points between two color points c1 and c2
	 * are determined by connecting c1, c2 with an imaginary line.
	 * Then for each point on that line, calculate the color value
	 * through interpolation.
	 * The resulting gradient is a List<Color> that has SIZE elements.
	 */
	private void createGradient() {
		gradient = new ArrayList<Color>();
		GradientPoint gp1 = null;
		GradientPoint gp2 = null;
		Iterator<GradientPoint> it = gradientPoints.iterator();
		while(it.hasNext()) {
			gp1 = gp2;
			gp2 = it.next();
			if(gp1 == null)
				continue;
			//
			// connect gp1 to gp2
			float npoints = gp2.x - gp1.x;
			Color acolor = gp1.color;
			float deltaR = ((float)(gp2.color.getRed() - gp1.color.getRed()))/(npoints-2);	// could be negative
			float deltaG = ((float)(gp2.color.getGreen() - gp1.color.getGreen()))/(npoints-2);
			float deltaB = ((float)(gp2.color.getBlue() - gp1.color.getBlue()))/(npoints-2);
			//System.out.println(deltaR + " " + deltaG + " " + deltaB);
			gradient.add(gp1.color);
			for(int i=1; i<npoints-1; i++) {
				int red =   incrementColor(acolor.getRed(), deltaR);
				int green = incrementColor(acolor.getGreen(), deltaG);
				int blue =  incrementColor(acolor.getBlue(), deltaB);
				//System.out.println(red + " " + green + " " + blue);
				acolor = new Color(red, green, blue);
				gradient.add(acolor);
			}
			gradient.add(gp2.color);
		}
		gradient.add(defaultColor);
	}

	private int incrementColor(int val, float delta) {
		int nval =  Math.round(val + delta);
		nval = (nval < 0) ? 0 : nval;
		nval = (nval > 255) ? 255: nval;
		return nval;
	}
	
	public void addGradientPoint(GradientPoint pnew) {
		Iterator<GradientPoint> gpit = gradientPoints.iterator();
		boolean replaced = false;
		while(gpit.hasNext()) {
			GradientPoint p = gpit.next();
			if(pnew.x == p.x) {
				p.color = pnew.color;
				replaced = true;
			}
		}
		if(!replaced) {
			gradientPoints.add(pnew);
		}
		createGradient();	// need to recreate each time
	}
	
	public void addGradientPoint(int x1, Color c1) {
		addGradientPoint(new GradientPoint(x1, c1));
	}
	
	public boolean isCyclic() {
		return cyclic;
	}

	public void setCyclic(boolean cyclic) {
		this.cyclic = cyclic;
	}

	public SortedSet<GradientPoint> getGradientPoints() {
		return gradientPoints;
	}
	
	/**
	 * 0 <= point <= SIZE
	 * if < 0, use abs
	 * if >= SIZE, wrap around if cyclic, else use defaultColor
	 * @param point
	 * @return
	 */
	public Color getColor(int point) {
		Color color = defaultColor;
		int p = (point < 0) ? Math.abs(point) : point;
		if(p >= SIZE) {
			p = cyclic ? p%SIZE : SIZE;
		}
		color = gradient.get(p);
		return color;
	}
	
	/**
	 * 0 <= point < 1
	 * @param point
	 * @return
	 */
	public Color getColor(float point) {
		int p = Math.round(Math.abs(point*SIZE));
		return getColor(p);
	}
	
	public Color getColor(double point) {
		int p =  (int)Math.round(Math.abs(point*SIZE));
		return getColor(p);
	}

	public Color getDefaultColor() {
		return defaultColor;
	}

	public void setDefaultColor(Color defaultColor) {
		this.defaultColor = defaultColor;
	}
	
}

/**
 * Sorted by X-value only
 * @author dbacon
 *
 */
class GradientPointComparator implements Comparator<GradientPoint> {

	/*
	public int compare(Object op1, Object op2) {
		GradientPoint p1 = (GradientPoint)op1;
		GradientPoint p2 = (GradientPoint)op2;
		return compare(p1, p2);
	}
	*/
	
	public int compare(GradientPoint p1, GradientPoint p2) {
		return p1.x == p2.x ? 0 : ( p1.x < p2.x ? -1 : 1);
	}
}
