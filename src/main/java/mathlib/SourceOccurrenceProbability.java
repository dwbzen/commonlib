package mathlib;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.relation.OccurrenceRelationBag;
import mathlib.util.IJson;

public class SourceOccurrenceProbability<K extends Comparable<K>, T extends List<K>> 
		implements IJson, Comparable<SourceOccurrenceProbability<K,T>>  {
	
	private static final long serialVersionUID = 7790448292733732535L;
	
	@JsonIgnore		private Tupple<K> key = null;
	@JsonProperty	private OccurrenceProbability occurrenceProbability = null;
	@JsonProperty	private Set<T> sources = new TreeSet<>();
	@JsonProperty	private Set<String> ids = new TreeSet<>();
	@JsonProperty	private Double averageDistance = 0D;	// average distance between K elements across all sources
	/**
	 * The containing OccurrenceRelationBag
	 */
	@JsonIgnore		private OccurrenceRelationBag<K, T, ?> occurrenceRelationBag = null;
	@JsonIgnore		private int sourceCount = 0;
	@JsonIgnore		private Double totalDistance = 0D;
	/**
	 * Dependency injection to compute distances - held by containing OccurrenceRelationBag
	 */
	@JsonIgnore		protected BiFunction<Tupple<K>, T, Double> metricFunction = null;
	
	public SourceOccurrenceProbability(Tupple<K> key) {
		this.key = key;
		occurrenceProbability = new OccurrenceProbability();
	}
	
	public Set<T> getSources() {
		return this.sources;
	}
	
	public Set<String> getIds() {
		return this.ids;
	}
	/**
	 * Adds a T source, increments sourceCount and updates averageDistance
	 * @param source
	 * @return
	 */
	public boolean addSource(T source) {
		if(metricFunction != null) {
			totalDistance += metricFunction.apply(key, source);
			sourceCount++;
			averageDistance = totalDistance / sourceCount;
		}
		return sources.add(source);
	}
	
	public boolean addSource(T source, String id) {
		addSource(source);
		return addId(id);
	}
	
	public boolean addId(String id) {
		return ids.add(id);
	}

	public OccurrenceProbability getOccurrenceProbability() {
		return occurrenceProbability;
	}

	public void setOccurrenceProbability(OccurrenceProbability occurrenceProbability) {
		this.occurrenceProbability = occurrenceProbability;
	}

	public Double getAverageDistance() {
		return averageDistance;
	}
	
	public String getAverageDistanceText() {
		return OccurrenceProbability.format.format(averageDistance);
	}

	public void setAverageDistance(Double averageDistance) {
		this.averageDistance = averageDistance;
	}

	public BiFunction<Tupple<K>, T, Double> getMetricFunction() {
		return metricFunction;
	}

	public void setMetricFunction(BiFunction<Tupple<K>, T, Double> metricFunction) {
		this.metricFunction = metricFunction;
	}

	public Tupple<K> getKey() {
		return key;
	}

	public OccurrenceRelationBag<K, T, ?> getOccurrenceRelationBag() {
		return occurrenceRelationBag;
	}

	public void setOccurrenceRelationBag(OccurrenceRelationBag<K, T, ?> occurrenceRelationBag) {
		this.occurrenceRelationBag = occurrenceRelationBag;
	}

	@Override
	public int compareTo(SourceOccurrenceProbability<K, T> other) {
		OccurrenceProbability op = other.getOccurrenceProbability();
		int compare = this.occurrenceProbability.compareTo(op);
		if(compare == 0) {
			compare = key.compareTo(other.key);
		}
		return compare;
	}

}
