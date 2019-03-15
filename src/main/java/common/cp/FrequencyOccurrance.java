package common.cp;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

import common.math.OccurrenceProbability;

/**
 * A FrequencyOccurranceMap records the frequency of n instances (called the order)
 * of K occur in a a given Collection of T's. </p>
 * For example.
 * K : Word, T : Book (implements List<Word> )</p>
 * A Book is just a collection of Sentences which is a Collection<Word>.
 * We impose an additional constraint that the K's in T (Words in the Sentence) be ordered.
 * This is to insure key primacy.
 * 
 * 
 * @author don_bacon
 *
 */
public class FrequencyOccurrance<K, T extends Collection<K>> {

	protected static final Logger log = LogManager.getLogger(FrequencyOccurrance.class);
	
	@JsonProperty	private Map<T, OccurrenceProbability> frequencyProbabilityMap = null;
	@JsonProperty	private int order;		// must be >=2
	
	public FrequencyOccurrance(int order) {
		this.order = order;
		frequencyProbabilityMap = Collections.synchronizedMap(new TreeMap<>());
	}
	
	
	
}
