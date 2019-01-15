package mathlib.relation;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.SourceOccurrenceProbability;
import mathlib.Tupple;
import mathlib.util.IJson;

/**
 * 
 * @author don_bacon
 *
 * @param <K> the base class of elements in the relationship (for example, Character)
 * @param <T> the containing class that is a List<K> (for example, Word). These are the sources.
 * @param <S> a Supplier<T>  (for example, Sentence)
 */
public abstract class OccurrenceRelationBag<K extends Comparable<K>, T extends List<K>, S extends Supplier<T>> implements IJson {

	private static final long serialVersionUID = 2823871802307514005L;

	protected static final Logger log = LogManager.getLogger(OccurrenceRelationBag.class);
	
	/**
	 * The OccurrenceProbability and List of sources of each K-Tupple
	 */
	@JsonProperty	protected Map<Tupple<K>, SourceOccurrenceProbability<K, T>> sourceOccurrenceProbabilityMap = null;
	@JsonProperty	private int degree = 1;
	@JsonIgnore		private boolean open = true;
	
	protected OccurrenceRelationBag(int degree) {
		this.degree = degree;
		sourceOccurrenceProbabilityMap = new TreeMap<>();
	}
	
	public boolean addOccurrenceRelation(OccurrenceRelation<K,T,S> occurrenceRelation) {
		if(open) {
			T source = occurrenceRelation.getUnit();
			occurrenceRelation.getPartitions().forEach(tupple -> addPartitionTupple(source, tupple));
		}
		return open;
	}
	
	protected void addPartitionTupple(T source, Tupple<K> tupple) {
		SourceOccurrenceProbability<K,T> sop = null;
		if(sourceOccurrenceProbabilityMap.containsKey(tupple)) {
			sop = sourceOccurrenceProbabilityMap.get(tupple);
		}
		else {
			sop = new SourceOccurrenceProbability<>(tupple);
			sourceOccurrenceProbabilityMap.put(tupple, sop);
		}
		sop.addSource(source);
		sop.getOccurrenceProbability().increment();
	}
	
	
	public Map<Tupple<K>, SourceOccurrenceProbability<K, T>> getSourceOccurrenceProbabilityMap() {
		return sourceOccurrenceProbabilityMap;
	}

	public int getDegree() {
		return degree;
	}
	
	/**
	 * Closes the OccurrenceRelationBag. This recomputes the probabilities 
	 * and prevents adding additional OccurrenceRelations unless re-opened.
	 */
	public boolean close() {
		open = false;
		return open;
	}
	
	public boolean open() {
		open = true;
		return open;
	}

	public boolean isOpen() {
		return open;
	}
	
}
