package mathlib;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.util.IJson;

public class SourceOccurrenceProbability<K extends Comparable<K>, T extends List<K>> 
		implements IJson, Comparable<SourceOccurrenceProbability<K,T>>  {
	
	private static final long serialVersionUID = 7790448292733732535L;
	
	@JsonIgnore		private Tupple<K> key = null;
	@JsonProperty	private OccurrenceProbability occurrenceProbability = null;
	@JsonProperty	private Set<T> sources = new TreeSet<>();
	@JsonProperty	private Float averageDistance = 0F;	// average distance between K elements across all sources
	/**
	 * Dependency injection to compute distances
	 */
	@JsonIgnore		protected BiFunction<Tupple<K>, T, Float> metricFunction = null;
	
	public SourceOccurrenceProbability(Tupple<K> key) {
		this.key = key;
		occurrenceProbability = new OccurrenceProbability();
	}
	
	public Set<T> getSources() {
		return this.sources;
	}
	
	public boolean addSource(T source) {
		return sources.add(source);
	}

	public OccurrenceProbability getOccurrenceProbability() {
		return occurrenceProbability;
	}

	public void setOccurrenceProbability(OccurrenceProbability occurrenceProbability) {
		this.occurrenceProbability = occurrenceProbability;
	}

	public Tupple<K> getKey() {
		return key;
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
