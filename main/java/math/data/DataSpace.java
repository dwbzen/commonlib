package math.data;

import java.util.Collection;

import math.util.Metadata;

/**
 * A Data Space is the highest level of Container.
 * 
 * @author bacond6
 *
 */
public abstract class DataSpace implements IDataSpace {

	private static final long serialVersionUID = 2914310861586426580L;
	private Collection<Layer> layers = null;
	private Metadata metadata = new Metadata();
	private final int depth = 1;
	protected int size = 0;
	
	/**
	 * Concrete classes need to initialize the layers Collection.
	 */
	protected DataSpace() {
		initialize();
	}

	abstract void initialize();
	
	public abstract boolean addLayer(Layer l);

	public Collection<Layer> getLayers() {
		return layers;
	}

	public void setLayers(Collection<Layer> layers) {
		this.layers = layers;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	protected void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public final int getDepth() {
		return depth;
	}
	
	public int getSize() {
		return size;
	}
}
