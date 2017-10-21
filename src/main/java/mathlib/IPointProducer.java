package mathlib;


public interface IPointProducer {

	public void start();
	public boolean isComplete();
	public Point2D<Number> next();
	
}
