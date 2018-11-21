package mathlib;

import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.util.IJson;

public class OrderedPair<S, T> implements IJson, Comparable<OrderedPair<S,T>> {

	private static final long serialVersionUID = -2747052681255992831L;
	@JsonProperty	private S x = null;
	@JsonProperty	private T y = null;
	
	public OrderedPair(S xval, T yval) {
		x = xval;
		y = yval;
	}

	public S getX() {
		return x;
	}

	public void setX(S x) {
		this.x = x;
	}

	public T getY() {
		return y;
	}

	public void setY(T y) {
		this.y = y;
	}
	
	public String toString() {
		return "(" + x.toString() + ", " + y.toString() + ")";
	}

	@Override
	public int compareTo(OrderedPair<S, T> other) {
		return toString().compareTo(other.toString());
	}

}
