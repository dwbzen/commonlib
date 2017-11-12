package mathlib.cp;

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
	@JsonIgnore protected boolean trace = false;

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

	/**
	 * @return T seed
	 */
	public T pickSeed() {
		T seed = null;
		CollectorStats<K,T> cstats = null;
		do {
			seed = pickCandidateSeed();
			cstats = get(seed);
		} while(cstats.isTerminal());
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
	
	public void display() {
		for(T key : this.keySet()) {
			CollectorStats<K, T> cstats = this.get(key);
			System.out.println("'" + key.toString() + "'\t" + cstats.getTotalOccurrance());
			System.out.print(cstats.toString());
		}
	}

	public Map<T, Integer> getSummaryMap() {
		if(summaryMap == null) {
			summaryMap = new TreeMap<T, Integer>();
		}
		for(T key : this.keySet()) {
			CollectorStats<K, T> cstats = this.get(key);
			summaryMap.put(key, cstats.getTotalOccurrance());
		}
		return summaryMap;
	}
	
	public void displaySummaryMap() {
		getSummaryMap();
		for(T key : summaryMap.keySet()) {
			Integer count = summaryMap.get(key);
			System.out.println("'" + key + "'\t" + count);
		}
	}
	
	private void logMessage(String text) {
		log.debug(text);
		if(trace) {
			System.out.println(text);
		}
	}
}