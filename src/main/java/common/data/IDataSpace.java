package common.data;

import java.util.Collection;

public interface IDataSpace extends IContainer {

	public Collection<Layer> getLayers();
	public void setLayers(Collection<Layer> layers);
	public boolean addLayer(Layer l);
	
}
