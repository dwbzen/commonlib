package org.dwbzen.common.math.ifs;

import java.util.List;

import org.dwbzen.common.math.Point2D;
import org.dwbzen.common.util.IJson;

public interface IIteratedFunctionSystem extends IJson {

	Point2D<Double> getRandomPoint();
	
	LinearFunction pickFunction() ;
	List<LinearFunction> getFunctions();
	int addFunction(LinearFunction f, double weight);
	int addFunction(LinearFunction f);
	
}
