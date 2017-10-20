package mathlib.data;
import java.io.Serializable;
import java.util.List;


/**
 * A recursive structure that gives the Class for AbstractDataPoint elements.
 * The nestingLevel specifies how deep the structure is.
 * 
 * @author don_bacon
 * @param <T>
 *
 */
public abstract class AbstractDataPrototype implements Serializable {
	private static final long serialVersionUID = 3175823219715822256L;
	protected int nestingLevel = 1;
	protected List<Serializable> template = null;	// construction delegated to concrete classes
		
	protected AbstractDataPrototype() {
	}
}
