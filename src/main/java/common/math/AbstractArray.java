package common.math;

import com.fasterxml.jackson.annotation.JsonIgnore;

import common.util.IJson;

public abstract class AbstractArray<T extends Number> implements IJson {
	
	private static final long serialVersionUID = -5630583197997148925L;

	public AbstractArray() {
	}
	
	@JsonIgnore		protected int rank = 2;
	@JsonIgnore	private String name;		// optional name used for display, maps etc.
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRank() {
		return rank;
	}
	
	protected abstract void createNew();
	public abstract Matrix<T> times(Matrix<? extends Number> other);
	public abstract T getValue(int rownum, int colnum);
	public abstract void clear();
	public abstract String toString();
}
