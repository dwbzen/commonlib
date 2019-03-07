package mathlib.relation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.OccurrenceProbability;
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
	public static final String indent = "      ";
	
	@JsonProperty	private int totalOccurrences = 0;
	/**
	 * The OccurrenceProbability and List of sources of each K-Tupple
	 */
	@JsonProperty	protected Map<Tupple<K>, SourceOccurrenceProbability<K, T>> sourceOccurrenceProbabilityMap = null;
	@JsonProperty	private int degree = 1;
	@JsonIgnore		private boolean open = true;
	@JsonIgnore		private boolean supressSourceOutput = false;
	
	/*
	 * optional Function injected to extract the Id from a T instance.
	 */
	@JsonIgnore		private Function<T, String> idExtractorFunction = null;
	
	/**
	 * Function to compute distances , injected into each SourceOccurrenceProbability instance.
	 */
	@JsonIgnore		private BiFunction<Tupple<K>, T, Double> metricFunction = null;
	
	protected OccurrenceRelationBag(int degree) {
		this.degree = degree;
		sourceOccurrenceProbabilityMap = new TreeMap<>();
	}
	
	public void setIdExtractorFunction(Function<T, String> function) {
		idExtractorFunction = function;
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
			sop.setOccurrenceRelationBag(this);
			sop.setMetricFunction(metricFunction);
		}
		sop.addSource(source);
		if(idExtractorFunction != null) {
			String id = idExtractorFunction.apply(source);
			sop.addId(id);
		}
		sop.getOccurrenceProbability().increment();
		totalOccurrences++;
	}
	
	
	public Map<Tupple<K>, SourceOccurrenceProbability<K, T>> sortByValue() {
		Map<Tupple<K>, SourceOccurrenceProbability<K, T>> sorted = 
			(LinkedHashMap<Tupple<K>, SourceOccurrenceProbability<K, T>>)sourceOccurrenceProbabilityMap.entrySet().stream()
			.sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return sorted;
	}
	
	public Map<Tupple<K>, SourceOccurrenceProbability<K, T>> sortByValue(boolean reverseSort) {
		Map<Tupple<K>, SourceOccurrenceProbability<K, T>> sorted = null;
		if(!reverseSort) {
			sorted = sortByValue();
		}
		else {
			sorted = 
				(LinkedHashMap<Tupple<K>, SourceOccurrenceProbability<K, T>>)sourceOccurrenceProbabilityMap.entrySet().stream()
				.sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		}
		return sorted;
	}
	
	public Map<Tupple<K>, SourceOccurrenceProbability<K, T>> getSourceOccurrenceProbabilityMap() {
		return sourceOccurrenceProbabilityMap;
	}
	

	protected void setSourceOccurrenceProbabilityMap(
			Map<Tupple<K>, SourceOccurrenceProbability<K, T>> sourceOccurrenceProbabilityMap) {
		this.sourceOccurrenceProbabilityMap = sourceOccurrenceProbabilityMap;
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
		recomputeProbabilities();
		return open;
	}
	
	public boolean recomputeProbabilities() {
		int prevEndRange = -1;
		int[] range = null;
		int rank = 1;
		for(SourceOccurrenceProbability<K,T> sop : sourceOccurrenceProbabilityMap.values()) {
			OccurrenceProbability occurrenceProb = sop.getOccurrenceProbability();
			occurrenceProb.setProbability((double)occurrenceProb.getOccurrence() / (double)totalOccurrences);
			occurrenceProb.setRank(rank++);
			range = new int[2];
			range[0] = ++prevEndRange;
			range[1] = prevEndRange + occurrenceProb.getOccurrence() - 1;
			prevEndRange = range[1];
			occurrenceProb.setRange(range);
		}
		return open;
	}
	
	public boolean open() {
		open = true;
		return open;
	}

	public boolean isOpen() {
		return open;
	}

	public int getTotalOccurrences() {
		return totalOccurrences;
	}

	public void setTotalOccurrences(int totalOccurrences) {
		this.totalOccurrences = totalOccurrences;
	}

	public BiFunction<Tupple<K>, T, Double> getMetricFunction() {
		return metricFunction;
	}

	public void setMetricFunction(BiFunction<Tupple<K>, T, Double> metricFunction) {
		this.metricFunction = metricFunction;
	}

	public boolean isSupressSourceOutput() {
		return supressSourceOutput;
	}

	public void setSupressSourceOutput(boolean supressSourceOutput) {
		this.supressSourceOutput = supressSourceOutput;
	}

}
