package mathlib.cp;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import mathlib.util.IJson;

/**
 * 
 * @author don_Bacon
 *
 */
public class CollectorStats<K, T extends List<K> & Comparable<T>> implements IJson, Comparable<CollectorStats<K, T>> {

	private static final long serialVersionUID = 9036890665958155561L;

	ObjectMapper mapper = new ObjectMapper();
	
	@JsonProperty	private int subsetLength;
	@JsonProperty	private T subset;				// List of length subsetLength
	@JsonProperty	private int totalOccurrance;	// total #times subset occurs
	@JsonProperty	private Map<K, OccurrenceProbability> occurrenceProbabilityMap = new TreeMap<K, OccurrenceProbability>();
	
	public static final int LOW = 0;
	public static final int HIGH = 1;
	
	@JsonProperty	private boolean terminal = false;	// true if this is a terminal state
	@JsonProperty	private boolean initial = false;	// true if this is an initial state
	
	public CollectorStats() {
	}
	
	public CollectorStats(int sublen) {
		this.subsetLength = sublen;
	}
	
	public CollectorStats(T sub) {
		setSubset(sub);
	}

	public int getSubsetLength() {
		return subsetLength;
	}

	public void setSubsetLength(int subsetLength) {
		this.subsetLength = subsetLength;
	}
	
	public T getSubset() {
		return subset;
	}

	public void setSubset(T sub) {
		this.subset = sub;
		this.subsetLength = sub.size();
	}

	public boolean isTerminal() {
		return terminal;
	}

	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}

	public boolean isInitial() {
		return initial;
	}

	public void setInitial(boolean initial) {
		this.initial = initial;
	}

	public Map<K, OccurrenceProbability> getOccurrenceProbabilityMap() {
		return occurrenceProbabilityMap;
	}
	
	public void addOccurrence(K toccur) {
		if(occurrenceProbabilityMap.containsKey(toccur)) {
			OccurrenceProbability op = occurrenceProbabilityMap.get(toccur);
			op.setOccurrence(op.getOccurrence() + 1);
		}
		else {
			occurrenceProbabilityMap.put(toccur, new OccurrenceProbability(1, 1.0));
		}
		recomputeProbabilitie();
	}

	/**
	 * example: occur		range
	 * 			-----		-----
	 * 			  3			  1,3  (1,2,3 selects first entry)
	 * 			  5			  4,8  (4 - 8 selects the second)
	 * 			  1			  9,9  (9 selects the third)
	 * 			  2			10,11   etc.
	 */
	private void recomputeProbabilitie() {
		totalOccurrance = 0;
		Collection<OccurrenceProbability> opcollection = occurrenceProbabilityMap.values();
		for(OccurrenceProbability op : opcollection) {
			totalOccurrance+= op.getOccurrence();
		}
		Set<K> keyset = occurrenceProbabilityMap.keySet(); 
		int[] prevRange = null;
		for(K key : keyset) {
			OccurrenceProbability op = occurrenceProbabilityMap.get(key);
			int occur = op.getOccurrence();
			if(totalOccurrance > 0) {
				op.setProbability(((double)op.getOccurrence()) / ((double)totalOccurrance));
			}
			if(prevRange == null) {
				op.setRange(LOW, 1);
				op.setRange(HIGH, occur);
			}
			else {
				op.setRange(LOW, prevRange[HIGH] + 1);
				op.setRange(HIGH, prevRange[HIGH] + occur);
			}
			prevRange = op.getRange();
		}
	}
	
	public int size() {
		return occurrenceProbabilityMap.size();
	}
	
	public int getTotalOccurrance() {
		return totalOccurrance;
	}

	public void setTotalOccurrance(int totalOccurrance) {
		this.totalOccurrance = totalOccurrance;
	}

	public String toString(boolean totalsOnly) {
		StringBuffer sb = new StringBuffer();
		for(K key : occurrenceProbabilityMap.keySet()) {
			OccurrenceProbability op = occurrenceProbabilityMap.get(key);
			sb.append("   '" + key.toString() + "'\t" + op.getOccurrence());
			if(!totalsOnly) {
				sb.append("\t" + op.toString());
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public String toString() {
		return toString(false);
	}
	
	@Override
	public String toJson() {
		String result = null;
		try {
			result = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int compareTo(CollectorStats<K, T> other) {
		int result = 0;
		T subset = getSubset();
		T otherSubset = other.getSubset();
		assert(subset instanceof Comparable<?> && otherSubset instanceof Comparable<?>);
		if( other.getTotalOccurrance() == totalOccurrance ) {
			result = subset.compareTo(otherSubset);
		}
		else {
			result =  other.getTotalOccurrance() == totalOccurrance ? 0 : (other.getTotalOccurrance() < totalOccurrance) ? -1 : 1;
		}
		return result;
	}
	
	public LinkedHashMap<K, OccurrenceProbability> sortByValue() {
		return (LinkedHashMap<K, OccurrenceProbability>) this.occurrenceProbabilityMap.entrySet().stream()
			.sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

}

class CollectorStatsComparator<K extends Comparable<K>,T extends List<K> &  Comparable<T>> implements Comparator<CollectorStats<K,T>> {
	private boolean reverse = true;
	public CollectorStatsComparator() {
		
	}
	public CollectorStatsComparator(boolean reverseSortOrder) {
		reverse = reverseSortOrder;
	}
	
	@Override
	public int compare(CollectorStats<K,T> o1, CollectorStats<K,T> o2) {
		int result = 0;
		if(o1.getTotalOccurrance() == o2.getTotalOccurrance()) {
			
		}
		else {
			if(reverse) {
				result =  o1.getTotalOccurrance() < o2.getTotalOccurrance() ? 1 : -1;
			}
			else {
				result = o1.getTotalOccurrance() < o2.getTotalOccurrance() ? -1 : 1;
			}
		}
		return result;
	}

}

