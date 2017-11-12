package mathlib.cp;

import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.SerializationFeature;

import mathlib.util.IJson;
import mathlib.util.INameable;

/**
 * MarkovChain is an implementation of a discreet-time Markov Chain (DTMC).
 * It undergoes transitions from one state to another on a state space, 
 * with the probability distribution of the next state depending only on the current state 
 * and not on the sequence of events that preceded it.
 * The states are T instances, transitions from state T1 to state T2 are K instances
 * each transition has an associated probability and additional information
 * used by classes that generate Ts.
 * 
 * The order of the MarkovChain is the number of states that determine future states.
 *
 *
 * @author don_bacon
 * @see https://en.wikipedia.org/wiki/Markov_chain
 * @param <K> a base class
 * @param <T> class that implements List<K>
 */
public class MarkovChain<K, T extends List<K>> extends CollectorStatsMap<K,T> implements IJson, INameable {

	private static final long serialVersionUID = 8849870001304925919L;
	
	@JsonProperty	private String name = NAME;		// storage key
	@JsonProperty	private int order;
	
	static String COMMA_SPACE = ", ";

	/**
	 * Creates a MarkovChain with a given Comparator.
	 * @param comparator Comparator to use to order the map, if null natural ordering is used.
	 */
	public MarkovChain(Comparator<? super T> comparator, int order) {
		super(comparator);
		this.order = order;
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}
	
	public MarkovChain(int order) {
		this(null, order);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	@Override
	/**
	 * The "toState" uses a shorthand notation of dropping the from state tokens.
	 * For example, "ab" --> "bc" is represented as "ab" --> "c" (the "b" is implied).
	 * Another example with kelylength of 3, "acb" --> "cbx" is just "acb" --> "x"
	 * 
	 * 
	 */
	public String toJSON() {
		return toJson();
	}

}