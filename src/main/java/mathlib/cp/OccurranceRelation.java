package mathlib.cp;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.Tupple;

/**
 * An OccurranceRelation encapsulates the "occurs together" relationship of objects
 * having a type K in the context of a container of type T.
 * @author don_bacon
 *
 * @param <K> the base class of elements in the relationship
 * @param <T> the containing class that is a List<K>
 * @param <S> a Supplier<T>
 */
public abstract class OccurranceRelation<K extends Comparable<K>, T extends List<K>, S extends Supplier<T>> 
		implements IRelation<K,T,S> {
	protected static final Logger log = LogManager.getLogger(OccurranceRelation.class);
	
	@JsonIgnore	private Collection<Integer> partitionKeys = new ArrayList<>();
	@JsonProperty	private Map<Tupple<K>, Integer> partitionKeyMap = new HashMap<>();
	@JsonProperty	private Set<Tupple<K>> partitions = new TreeSet<>();
	@JsonProperty	private int degree = 1;
	
	protected OccurranceRelation() {
		
	}
	
	public OccurranceRelation(T unit, int degree) {
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
			if(nbits(i) == degree) {
				Tupple<K> tupple = new Tupple<>(degree);
				int j = 0;
				do {
					if((1 & (i>>j)) == 1) {
						index = len - 1 - j;
						log.info(i + " " + j + " " + index + " (" + unit.get(index) + ")");
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

	/**
	 * Gets the number of set bits in an integer.
	 * @param n an int to evaluate
	 * @return number of set bits
	 */
	public static int nbits(int n) {
		int nbits = 0;
		int i = 0;
		do {
			nbits += (1 & (n>>i++));
		} while(Math.pow(2, i) <= n);
		return nbits;
	}

	@Override
	abstract public boolean isElement(Tupple<K> element, T unit);
	
	public static void main(String...strings) {
		for(int i=0; i<=128; i++) {
			System.out.println("i: " + i + " nbits: " + OccurranceRelation.nbits(i));
		}
	}
}
