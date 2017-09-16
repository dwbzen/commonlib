package ui;

import java.awt.Color;

public class GradientPoint {

	public int x;
	public Color color;	// the Color value at point x
	
	private static int maxX = Gradient.SIZE;	// 0 <= x <= SIZE-1
	
	public GradientPoint(int x, Color c) {
		color = c;
		if(x < 0 || x > maxX) {
			throw new UnsupportedOperationException("Value of x is out of bounds");
		}
		this.x = x;
	}
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
