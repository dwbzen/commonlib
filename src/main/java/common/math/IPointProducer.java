package common.math;


public interface IPointProducer {

	public void start();
	public boolean isComplete();
	public Point2D<Double> next(Point2D<Double> point);
	
}
