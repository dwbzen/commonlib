package common.data;

import java.util.Collection;

public interface ILayer extends IContainer {

	IDataSpace getDataSpace();
	void setDataSpace(IDataSpace dataSpace);
	
	int getLayerNumber();
	void setLayerNumber(int layerNumber);
	
	Collection<DataPoint<?>> getDataPoints();
	void setDataPoints(Collection<DataPoint<?>> dataPoints);
	boolean addDataPoint(DataPoint<?> dataPoint);
	
}
