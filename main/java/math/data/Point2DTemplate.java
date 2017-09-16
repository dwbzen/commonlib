package math.data;

import java.util.ArrayList;

public class Point2DTemplate extends AbstractDataPrototype  {

	private static final long serialVersionUID = -2582641722779787664L;
	private static final Double prototype = Double.valueOf(0);
	
	public Point2DTemplate() {
		super();
		template = new ArrayList<>();
		template.add(prototype);
		template.add(prototype);
		nestingLevel = 0;
	}

}
