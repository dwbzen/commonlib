package math;


public interface IProducer {

	public void start();
	public boolean isComplete();
	public Point2D<Number> next();
	
}
