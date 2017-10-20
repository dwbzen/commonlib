package mathlib.fractal;

import java.util.Collection;

public interface IFractalFormula {
	
	public int iteratePoint(IterationPoint ipoint);
	
	public void addIterationListener(IterationListener listener);
	
	public Collection<IterationListener> getIterationListeners();

}
