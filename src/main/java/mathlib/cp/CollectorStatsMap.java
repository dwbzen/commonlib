package mathlib.cp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import mathlib.cp.ISeedPicker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import mathlib.util.INameable;

/**
 * A TreeMap bound to a base class K and some class T that implements a List<K>
 * Some examples, 
 * K : HarmonyChord, T : ChordProgression (which implements List<HarmonyChord>
 * K : Character, T : Word (implements List<Character>)
 * K : Word, T : Sentence (implements List<Word> )
 * 
 * A Comparator must be provided so it knows how to order the elements in the TreeMap.
 * @author don_bacon
 *
 * @param <K> a base class
 * @param <T> class that implements List<K>
 */
public class CollectorStatsMap<K extends Comparable<K>, T extends List<K> & Comparable<T>, R extends Supplier<T> & INameable> extends TreeMap<T, CollectorStats<K, T, R>> {

	private static final long serialVersionUID = 4801227327750662977L;
	static ObjectMapper objectMapper = new ObjectMapper();
	
	@JsonIgnore private Map<T, Integer> summaryMap = null;
	@JsonIgnore private Map<Integer, List<T>> invertedSummaryMap = null;
	@JsonIgnore protected boolean trace = false;
	@JsonIgnore boolean pickInitialSeed = false;
	// optional bespoke class to pick seed
	@JsonIgnore Optional<ISeedPicker<K,T,R>> seedPicker;	

	protected static final Logger log = LogManager.getLogger(CollectorStatsMap.class);

	/**
	 * Creates a CollectorStatsMap with a given Comparator.
	 * @param comparator Comparator to use to order the map, if null natural ordering is used.
	 */
	public CollectorStatsMap(Comparator<? super T> comparator) {
		super(comparator);
	}
	
	public boolean isTrace() {
		return trace;
	}

	public void setTrace(boolean trace) {
		this.trace = trace;
	}

	public boolean isPickInitialSeed() {
		return pickInitialSeed;
	}

	public void setPickInitialSeed(boolean pickInitialSeed) {
		this.pickInitialSeed = pickInitialSeed;
	}

	/**
	 * @return T seed
	 */
	public T pickSeed() {
		T seed = null;
		CollectorStats<K,T,R> cstats = null;
		if(seedPicker.isPresent()) {
			ISeedPicker<K,T,R> mySeedPicker = seedPicker.get();
			return mySeedPicker.pickSeed();
		}
		while(true) {
			seed = pickCandidateSeed();
			cstats = get(seed);
			if(!pickInitialSeed || (pickInitialSeed && cstats.isInitial())) {
				break;
			}
		}
		logMessage("picked seed: '" + seed + "'");
		return seed;
	}
	
	/**
	 * Selects a random T seed
	 * @return T seed
	 */
	protected T pickCandidateSeed() {
		Set<T> keys = keySet();
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int index = random.nextInt(0, keys.size());
		T seed = keys.stream().collect(Collectors.toList()).get(index);

		log.debug("picked candidate seed: '" + seed + "'");
		return seed;
	}
	

	public ISeedPicker<K,T,R> getSeedPicker() {
		return seedPicker.get();
	}

	/**
	 * Sets the ISeedPicker to use.
	 * @throws NullPointerException if null
	 * 
	 * @param mySeedPicker non-null ISeedPicker<K,T,R> instance.
	 */
	public void setSeedPicker(ISeedPicker<K,T,R> mySeedPicker) {
		seedPicker = Optional.of(mySeedPicker);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(T key : this.keySet()) {
			CollectorStats<K, T, R> cstats = this.get(key);
			sb.append("'" + key.toString() + "'\t" + cstats.getTotalOccurrance());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	private void createSummaryMap() {
		summaryMap = new TreeMap<T, Integer>();
		for(T key : this.keySet()) {
			CollectorStats<K, T, R> cstats = this.get(key);
			Integer totalOccurrance = cstats.getTotalOccurrance();
			summaryMap.put(key, totalOccurrance);
		}
	}

	/**
	 * TODO - add toJson()
	 * 
	 * @return LinkedHashMap 
	 */
	public LinkedHashMap<T, CollectorStats<K,T,R>>  sortByValue() {
		return (LinkedHashMap<T,CollectorStats<K,T,R>> ) this.entrySet().stream()
			.sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
	
	public Map<T, Integer> getSummaryMap() {
		if(summaryMap == null) {
			createSummaryMap();
		}
		return summaryMap;
	}
	
	private void createInvertedSummaryMap() {
		invertedSummaryMap = new TreeMap<Integer, List<T>>(new MapComparator());
		for(T key : keySet()) {
			CollectorStats<K, T,R> cstats = get(key); 
			Integer totalOccurrance = cstats.getTotalOccurrance();
			if(invertedSummaryMap.containsKey(totalOccurrance)) {
				List<T> vals = invertedSummaryMap.get(totalOccurrance);
				vals.add(key);
				log.debug("T val: " + vals.toString());
			}
			else {
				List<T> vals = new ArrayList<T>();
				vals.add(key);
				invertedSummaryMap.put(totalOccurrance, vals);
			}
		}
	}
	
	public Map<Integer, List<T>> getInvertedSummaryMap() {
		if(invertedSummaryMap == null) {
			createInvertedSummaryMap();
		}
		return invertedSummaryMap;
	}
	
	public String getSummaryMapText() {
		StringBuilder sb = new StringBuilder();
		getSummaryMap();
		int totalCount = 0;
		for(T key : summaryMap.keySet()) {
			Integer count = summaryMap.get(key);
			totalCount += count;
			String text = "'" + key + "'\t" + count + "\n";
			sb.append(text);
		}
		sb.insert(0, "Total Count: " + totalCount + "\n");
		return sb.toString();
	}
	
	public String getInvertedSummaryMapText() {
		return getInvertedSummaryMapText(false);
	}
	
	public String getInvertedSummaryMapText(boolean jsonFormat) {
		return getInvertedSummaryMapText(jsonFormat, false);
	}
	
	public String getInvertedSummaryMapText(boolean jsonFormat, boolean pretty) {
		StringBuilder sb = new StringBuilder();
		String result = "";
		getInvertedSummaryMap();
		if(jsonFormat) {
			sb.append("{\n");
			for(Integer count : invertedSummaryMap.keySet()) {
				sb.append( "\"" + count + "\": [ ");
				List<T> valList = invertedSummaryMap.get(count);
				int nvals = valList.size();
				int valCount = 0;
				for(T val :  valList) {
					valCount++;
					if(pretty) {sb.append("\n    "); }
					sb.append("\"" + val + "\"" + ( valCount < nvals ? ", " : ""));
				}
				sb.append(pretty ? "\n    ],\n" : "],\n");
			}
			result = sb.substring(0, sb.length()-2) + "\n}\n";
		}
		else {
			int totalCount = 0;
			for(Integer count : invertedSummaryMap.keySet()) {
				String header = "Count: " + count + "\n";
				sb.append(header);
				List<T> valList = invertedSummaryMap.get(count);
				for(T val :  valList) {
					String text = "\t'" + val + "'\n";
					sb.append(text);
					totalCount += count;
				}
			}
			sb.insert(0, "Total Count: " + totalCount + "\n");
			result = sb.toString();
		}
		return result;
	}
	
	private void logMessage(String text) {
		log.debug(text);
		if(trace) {
			System.out.println(text);
		}
	}
}

class MapComparator implements Comparator<Integer>
{
	private boolean reverse = true;
	public MapComparator() {
		
	}
	public MapComparator(boolean reverseSortOrder) {
		reverse = reverseSortOrder;
	}
	@Override
	public int compare(Integer int1, Integer int2) {
		int result = 0;
		if(!int1.equals(int2)) {
			if(reverse) {
				result = int1.intValue() < int2.intValue() ? 1 : -1;
			}
			else {
				result = int1.intValue() < int2.intValue() ? -1 : 1;
			}
		}
		return result;
	}
	
}
