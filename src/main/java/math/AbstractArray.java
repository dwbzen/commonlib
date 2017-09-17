package math;

import org.mongodb.morphia.annotations.Property;

public abstract class AbstractArray<T extends Number> {
	
	public AbstractArray() {
	}
	
	@Property	protected String _id;		// optional ID - 24 char hex string
	@Property	protected int rank = 2;
	@Property	private String name;	// optional name used for display, maps etc.
	
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
	public abstract Matrix<Number> times(Matrix<? extends Number> other) throws  IllegalArgumentException;
	public abstract void clear();
	public abstract String toString();
}
