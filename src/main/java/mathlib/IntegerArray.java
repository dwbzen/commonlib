package mathlib;

import java.util.Vector;

public class IntegerArray extends Vector<Integer> {

	private static final long serialVersionUID = 2566966583854239235L;
	private int origin = 0;		// could be negative, -2 for example
	
	public IntegerArray() {
		super();
	}
	
	public IntegerArray(int orig) {
		super();
		origin = orig;
	}
	
	@Override
	public Integer elementAt(int index) {
		return (Integer)super.elementAt(index-origin);		// if origin were -2, index-origin  == index + 2
	}

	public boolean add(int value) {
		return super.add(Integer.valueOf(value));
	}
	
	public void add(int index, int value) {
		super.add(index, Integer.valueOf(value));
	}
	
	/**
	 * Adds an int value to the end of the IntegerArray
	 * @param value
	 * @return this, so operation can be chained
	 */
	public IntegerArray addElement(int value) {
		add(value);
		return this;
	}
	
	public int getElementAt(int index) {
		return elementAt(index).intValue();
	}
	
	public void setElementAt(int value, int index) {
		super.insertElementAt(Integer.valueOf(value), index-origin);
	}
}
