package common.math;

import java.util.List;

public interface ISequence<T> {

	public void newSequence(T[] args);
	
	public List<? extends Number> getSequence();
}
