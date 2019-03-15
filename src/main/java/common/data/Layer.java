package common.data;

import java.util.Collection;

import common.util.Metadata;

/**
 * A Layer is a Container for DataPoints.
 * 
 * @author bacond6
 * @param <T>
 * @param <T>
 *
 */
public abstract class Layer implements ILayer {

	private static final long serialVersionUID = 2569308639395361306L;
	private Metadata metadata = new Metadata();
	private IDataSpace dataSpace = null;	// DataSpace that contains this Layer
	private Collection<DataPoint<?>> dataPoints = null;
	private int layerNumber = 0;	// concrete instances start at 1
	protected int depth = 1;
	protected int size = 0;
	
	protected Layer() {
		
	}
	
	public Metadata getMetadata() {
		return metadata;
	}

	protected void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getDepth() {
		return depth;
	}

	public int getSize() {
		return size;
	}

	public Collection<DataPoint<?>> getDataPoints() {
		return dataPoints;
	}

	public void setDataPoints(Collection<DataPoint<?>> dataPoints) {
		this.dataPoints = dataPoints;
	}
	
	public abstract boolean addDataPoint(DataPoint<?> dataPoint);

	public IDataSpace getDataSpace() {
		return dataSpace;
	}

	public void setDataSpace(IDataSpace dataSpace) {
		this.dataSpace = dataSpace;
	}

	public int getLayerNumber() {
		return layerNumber;
	}

	public void setLayerNumber(int layerNumber) {
		this.layerNumber = layerNumber;
	}

}
