package mathlib.relation;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.OccurrenceProbability;
import mathlib.Tupple;

/**
 * 
 * @author don_bacon
 *
 * @param <K> the base class of elements in the relationship (for example, Character)
 * @param <T> the containing class that is a List<K> (for example, Word)
 * @param <S> a Supplier<T>  (for example, Sentence)
 */
public abstract class OccurrenceRelationBag<K extends Comparable<K>, T extends List<K>, S extends Supplier<T>> {
	protected static final Logger log = LogManager.getLogger(OccurrenceRelationBag.class);

	/**
	 * A Map of OccurrenceRelations keyed by instances of T (unit)
	 */
	@JsonProperty	protected Map<T, OccurrenceRelation<K,T,S>> occurrenceRelationMap = new TreeMap<>();
	/**
	 * The OccurrenceProbability of each K-Tupple
	 */
	@JsonProperty	protected Map<Tupple<K>, OccurrenceProbability> occurrenceProbabilityMap = new TreeMap<>();
	/**
	 * Maps a given K-Tupple to the source Unit(s).
	 */
	@JsonProperty	protected Map<Tupple<K>, List<T>> tuppleUnitMap = new TreeMap<>();
	@JsonProperty	private int degree = 1;
	
	protected OccurrenceRelationBag(int degree) {
		this.degree = degree;
	}
	
	public void addOccurrenceRelation(OccurrenceRelation<K,T,S> occurrenceRelation) {

	}
	
	public int getDegree() {
		return degree;
	}
	
}
