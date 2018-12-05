package mathlib.cp;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Supplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * used by classes that generate Ts.</p>
 * 
 * The order of the MarkovChain is the number of states that determine future states.
 *
 *
 * @author don_bacon
 * <p>See <a href="https://en.wikipedia.org/wiki/Markov_chain">Markov Chain</a> on Wikipedia
 * 
 * @param <K> a base class
 * @param <T> class that implements List<K>
 * @param <R> clas that is a Supplier of <T>
 */
public class MarkovChain<K extends Comparable<K>, T extends List<K> & Comparable<T>, R extends Supplier<T> & INameable> extends CollectorStatsMap<K,T,R> implements IJson, INameable {

	private static final long serialVersionUID = 8849870001304925919L;
	static ObjectMapper mapper = new ObjectMapper();
	
	@JsonProperty	private String name = DEFAULT_NAME;		// storage key
	@JsonProperty	private int order;
	
	static String COMMA_SPACE = ", ";
	static OutputStyle outputStyle = OutputStyle.TEXT;
	
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
	
	/**
	 * Displays the MarkovChain - text output style
	 */
	public String getMarkovChainDisplayText() {
		return getMarkovChainDisplayText(false);
	}
	
	public String getMarkovChainDisplayText(boolean showSupplierCounts) {
		StringBuilder sb = new StringBuilder();
		for(T key : this.keySet()) {
			CollectorStats<K, T, R> cstats = this.get(key);
			cstats.setShowSupplierCounts(showSupplierCounts);
			sb.append("'" + key.toString() + "'\t" + cstats.getTotalOccurrance());
			sb.append("\n");
			sb.append(cstats.toString(showSupplierCounts));
		}
		return sb.toString();
	}
	
	public String getMarkovChainDisplayText(OutputStyle outputStyle) {
		return getMarkovChainDisplayText(outputStyle, false);
	}
	
	public String getMarkovChainDisplayText(OutputStyle outputStyle, boolean showSupplierCounts) {
		String output = null;
		if(outputStyle == OutputStyle.TEXT) {
			output =  getMarkovChainDisplayText(showSupplierCounts) ;
		}
		else if(outputStyle==OutputStyle.JSON) {
			output = toJson(true);
		}
		else if(outputStyle == OutputStyle.CSV) {
			output = getMarkovChainCsv();
		}
		return output;
	}
	
	private String getMarkovChainCsv() {
		StringBuilder sb = new StringBuilder();		// rows+values

		/*
		 * Build a sorted list of column headings and values by row
		 */
		SortedSet<String> columnSet = new TreeSet<>();
		SortedSet<String> rowSet = new TreeSet<>();
		SortedMap<String, Map<String, OccurrenceProbability>> rowValues = new TreeMap<>();
		
		for(T key : this.keySet()) {
			CollectorStats<K, T, R> cstats = get(key);
			rowSet.add(key.toString());
			Map<K, OccurrenceProbability> probabilityMap = cstats.getOccurrenceProbabilityMap();
			Map<String, OccurrenceProbability> probValue = new HashMap<>();
			for(K k : probabilityMap.keySet()) {
				columnSet.add(k.toString());
				OccurrenceProbability occurrenceProbability = probabilityMap.get(k);
				probValue.put(k.toString(), occurrenceProbability);
				rowValues.put(key.toString(), probValue);
			}
		}
		/*
		 * Create the spreadsheet from rowValues map
		 */
		sb.append("key");
		for(String columnKey : columnSet) { // row 1 column headings
			sb.append("," + columnKey);
		}
		sb.append("\n");
		OccurrenceProbability zeroProb = new OccurrenceProbability();
		for(String rowKey : rowValues.keySet()) {
			Map<String, OccurrenceProbability> probValue = rowValues.get(rowKey);
			sb.append(rowKey);
			for(String columnKey : columnSet) {
				OccurrenceProbability op = zeroProb;
				if(probValue.containsKey(columnKey)) {
					 op = probValue.get(columnKey);
				}
				sb.append("," + op.getProbabilityText());
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public String getSortedDisplayText(OutputStyle outputStyle) {
		return getSortedDisplayText(outputStyle, false);
	}
	
	public String getSortedDisplayText(OutputStyle outputStyle, boolean showSupplierCounts) {
		if(outputStyle==OutputStyle.CSV) {
			return getMarkovChainCsv();
		}
		StringBuilder sb = new StringBuilder();
		LinkedHashMap<T, CollectorStats<K,T, R>> sortedChain = (LinkedHashMap<T, CollectorStats<K,T,R>>) sortByValue();
		if(outputStyle==OutputStyle.JSON) {
			sb.append("[\n");
		}
		for(T t : sortedChain.keySet()) {
			CollectorStats<K, T, R>  cstats = (CollectorStats<K, T, R>) sortedChain.get(t);
			Map<K, OccurrenceProbability> sortedStats = (Map<K, OccurrenceProbability>) cstats.sortByValue();
			if(outputStyle==OutputStyle.TEXT) {
				sb.append(t.toString() + "\t" + cstats.getTotalOccurrance());
				sb.append("\n");
			}
			else if(outputStyle==OutputStyle.JSON) {
				sb.append("{ " + "\"" + t.toString() + "\" : \n  {\n" );
				sb.append("  \"totalOccurrence\" : " + cstats.getTotalOccurrance() + ",\n");
			}
			for(K key2 : sortedStats.keySet()) {
				OccurrenceProbability op = sortedStats.get(key2);
				if(outputStyle==OutputStyle.TEXT) {
					sb.append("  " + key2 + "\t" + op.getOccurrence() + "\t" + op.getProbabilityText());
					if(showSupplierCounts) {
						cstats.getSupplierCountsString(key2, sb);
					}
					sb.append("\n");
				}
				else if(outputStyle==OutputStyle.JSON) {
					sb.append("   \"" + key2 + "\": {\n" 
							+ "      \"occurrence\" : "  + op.getOccurrence() + ",\n"
							+ "      \"probability\" : " + op.getProbabilityText());
					sb.append("\n    },\n");
				}
			}
			if(outputStyle==OutputStyle.JSON) {
				sb.deleteCharAt(sb.length()-2);	// trailing comma
				sb.append("  }\n},\n");		// closing braces
			}
		}
		if(outputStyle==OutputStyle.JSON) {
			sb.deleteCharAt(sb.length()-2);	// trailing comma
			sb.append("]");
		}
		return sb.toString();
	}
	
}



