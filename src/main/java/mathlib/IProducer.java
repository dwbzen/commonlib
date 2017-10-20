package mathlib;


public interface IProducer {

	public void start();
	public boolean isComplete();
	public Point2D<Number> next();
	
}
