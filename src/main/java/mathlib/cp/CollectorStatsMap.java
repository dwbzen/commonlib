package mathlib.cp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class CollectorStatsMap<K, T extends List<K>> extends TreeMap<T, CollectorStats<K, T>> {

	private static final long serialVersionUID = 4801227327750662977L;
	
	@JsonIgnore private Map<T, Integer> summaryMap = null;
	@JsonIgnore private Map<Integer, List<T>> invertedSummaryMap = null;
	@JsonIgnore protected boolean trace = false;
	@JsonIgnore boolean pickInitialSeed = false;

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
		CollectorStats<K,T> cstats = null;
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

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(T key : this.keySet()) {
			CollectorStats<K, T> cstats = this.get(key);
			sb.append("'" + key.toString() + "'\t" + cstats.getTotalOccurrance());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	private void createSummaryMap() {
		summaryMap = new TreeMap<T, Integer>();
		for(T key : this.keySet()) {
			CollectorStats<K, T> cstats = this.get(key);
			Integer totalOccurrance = cstats.getTotalOccurrance();
			summaryMap.put(key, totalOccurrance);
		}
	}

	public Map<T, Integer> getSummaryMap() {
		if(summaryMap == null) {
			createSummaryMap();
		}
		return summaryMap;
	}
	
	private void createInvertedSummaryMap() {
		invertedSummaryMap = new TreeMap<Integer, List<T>>(new MapComparator());
		for(T key : this.keySet()) {
			CollectorStats<K, T> cstats = this.get(key);
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
	
	public String displaySummaryMap() {
		StringBuilder sb = new StringBuilder();
		getSummaryMap();
		for(T key : summaryMap.keySet()) {
			Integer count = summaryMap.get(key);
			String text = "'" + key + "'\t" + count + "\n";
			System.out.print(text);
			sb.append(text);
		}
		return sb.toString();
	}
	
	public String displayInvertedSummaryMap() {
		StringBuilder sb = new StringBuilder();
		getInvertedSummaryMap();
		for(Integer count : invertedSummaryMap.keySet()) {
			String header = "Count: " + count + "\n";
			System.out.print(header);
			List<T> valList = invertedSummaryMap.get(count);
			for(T val :  valList) {
				String text = "\t'" + val + "'\n";
				sb.append(text);
				System.out.print(text);
			}
		}
		return sb.toString();
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

	@Override
	public int compare(Integer int1, Integer int2) {
		int result = 0;
		if(!int1.equals(int2)) {
			result = int1.intValue() < int2.intValue() ? 1 : -1;
		}
		return result;
	}
	
}
