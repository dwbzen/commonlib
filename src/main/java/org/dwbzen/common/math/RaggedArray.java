package org.dwbzen.common.math;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An array (List) where each element is a List<R>
 * 
 * @author don_bacon
 *
 * @param <R> a base class, for example HarmonyChord
 * @param <T> a class that extends List<R>, for example ChordProgression
 */
public class RaggedArray<R, T extends List<R>> extends ArrayList<T> {
	
	private static final long serialVersionUID = -349834989199312258L;

	public RaggedArray() {
		super();
	}
	
	/**
	 * Flattens the elements of T into a single T, delimiting the
	 * elements by the R provided, or nothing if delim is null.
	 * If not null, a delimiter is also tacked onto the end of each element.
	 * @param delim R instance to use to delimit T elements
	 * @return T instance
	 */
	public T flatten(R delim) {
		T ret = null;
		Iterator<T> iter = this.iterator();
		while(iter.hasNext()) {
			if(ret == null) {
				ret = iter.next();
			}
			else {
				if(delim != null) {
					ret.add(delim);
				}
				ret.addAll(iter.next());
			}
		}
		if(delim != null) {
			ret.add(delim);
		}
		return ret;
	}
	
	/**
	 * Flattens the elements of T into a single T
	 * @return T instance
	 */
	public T flatten() {
		return flatten(null);
	}
}
