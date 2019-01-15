package mathlib;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.util.IJson;

public class SourceOccurrenceProbability<K extends Comparable<K>, T extends List<K>> implements IJson {
	private static final long serialVersionUID = 7790448292733732535L;
	
	@JsonProperty	private Tupple<K> key = null;
	@JsonProperty	private OccurrenceProbability occurrenceProbability = null;
	@JsonProperty	private Set<T> sources = new TreeSet<>();
	
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

}
