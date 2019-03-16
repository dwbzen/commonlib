package org.dwbzen.common.data;

import java.io.Serializable;

import org.dwbzen.common.util.Metadata;

/**
 * A Container, as the name suggests, contains other objects including other Containers.
 * It has Metadata and a size metric that measures how big it is.
 * Also has a depth which indicates if it's a root or node, that is
 * does it contain other Containers and if so how deeply nested?
 * By convention, a root level Container such as a DataSpace has a depth of 1.
 * 
 * @author bacond6
 *
 */
public interface IContainer extends Serializable {
	public Metadata getMetadata();
	
	public int getSize();
	
	public int getDepth();
	
}
