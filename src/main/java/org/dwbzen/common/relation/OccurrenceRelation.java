package org.dwbzen.common.relation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dwbzen.common.math.Tupple;
import org.dwbzen.common.util.IJson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An OccurranceRelation encapsulates the "occurs together" relationship of objects
 * having a type K in the context of a unit (key) of type T that is the
 * basis for partitions - subsets (Tupple) of degree cardinality.
 * @author don_bacon
 *
 * @param <K> the base class of elements in the relationship (Character for example)
 * @param <T> the containing class that is a List<K>  (Word is a List<Character>, Sentence is a List<Word>)
 * @param <S> a Supplier<T>	(Sentence supplies Word, Book supplies Sentence)
 */
public abstract class OccurrenceRelation<K extends Comparable<K>, T extends List<K>, S extends Supplier<T>> 
		implements IRelation<K,T,S>, IJson {

	protected static final Logger log = LogManager.getLogger(OccurrenceRelation.class);
	
	@JsonIgnore	private Collection<Integer> partitionKeys = new ArrayList<>();
	@JsonProperty	private Map<Tupple<K>, Integer> partitionKeyMap = new HashMap<>();
	@JsonProperty	private Set<Tupple<K>> partitions = new TreeSet<>();
	@JsonProperty	private int degree = 1;
	@JsonProperty	protected T unit = null;
	
	protected OccurrenceRelation() {
		
	}
	
	public OccurrenceRelation(T unit, int degree) {
		this.unit = unit;
		partition(unit, degree);
	}
	
	/**
	 * Creates a degree-subset power set of the K elements of a give T unit.
	 * And also creates a map of the keys.
	 */
	public Set<? extends Tupple<K>> partition(T unit, int degree) {
		int len = unit.size();
		int index = 0;
		this.degree = degree;
		double nSets = Math.pow(2, len);	// Power set cardinality
		for(int i = 1; i<nSets; i++) {
			if(Partitions.nbits(i) == degree) {
				Tupple<K> tupple = new Tupple<>(degree);
				int j = 0;
				do {
					if((1 & (i>>j)) == 1) {
						index = len - 1 - j;
						log.debug(i + " " + j + " " + index + " (" + unit.get(index) + ")");
						tupple.add(unit.get(index));
					}
				} while(Math.pow(2, j++) <= nSets);
				partitions.add(tupple);
				Integer key = getKey(tupple);
				partitionKeyMap.put(tupple, key);
				partitionKeys.add(key);
				log.debug("added " + tupple);
			}
		}
		return partitions;
	}

	public Map<Tupple<K>, Integer> getPartitionKeyMap() {
		return partitionKeyMap;
	}

	public Set<Tupple<K>> getPartitions() {
		return partitions;
	}

	public final Collection<Integer> getPartitionKeys() {
		return partitionKeys;
	}
	
	public int getDegree() {
		return degree;
	}
	

	public T getUnit() {
		return unit;
	}

	@Override
	abstract public boolean isElement(Tupple<K> element, T unit);
	
}
