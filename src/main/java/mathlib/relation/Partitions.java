package mathlib.relation;

import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.Tupple;
import mathlib.util.IJson;

public class Partitions<K extends Comparable<K>> implements IJson {

	private static final long serialVersionUID = 1L;
	protected static final Logger log = LogManager.getLogger(Partitions.class);
	
	@JsonProperty	private Set<Tupple<K>> partitions = new TreeSet<>();
	@JsonProperty	private Tupple<K> source = null;
	@JsonProperty	private int degree = 1;
	
	public Partitions(Tupple<K> tupple, int degree) {
		this.degree = degree;
		source = tupple;
		createPartitions();
	}
	
	public Set<Tupple<K>> getPartitions() {
		return partitions;
	}

	private void createPartitions() {
		int len = source.size();
		if(len <= 2) {
			partitions.add(source);
			return;
		}
		int index = 0;
		double nSets = Math.pow(2, len);	// Power set cardinality
		for(int i = 1; i<nSets; i++) {
			if(Partitions.nbits(i) == degree) {
				Tupple<K> tupple = new Tupple<>(degree);
				int j = 0;
				do {
					if((1 & (i>>j)) == 1) {
						index = len - 1 - j;
						log.debug(i + " " + j + " " + index + " (" + source.get(index) + ")");
						tupple.add(source.get(index));
					}
				} while(Math.pow(2, j++) <= nSets);
				partitions.add(tupple);
				log.debug("added " + tupple);
			}
		}
		
	}

	/**
	 * Gets the number of set bits in an integer.
	 * @param n an int to evaluate
	 * @return number of set bits
	 */
	public static int nbits(int n) {
		int nbits = 0;
		int i = 0;
		do {
			nbits += (1 & (n>>i++));
		} while(Math.pow(2, i) <= n);
		return nbits;
	}
	
	public static void main(String...strings ) {
		Tupple<Character> charTupple = new Tupple<>('d', 'o', 'n', 'a', 'l', 'd');
		Partitions<Character> charPartition = new Partitions<>(charTupple, 2);
		System.out.println(charPartition.getPartitions());
		Partitions<Character> charPartition2 = new Partitions<>(charTupple, 3);
		System.out.println(charPartition2.getPartitions());
	}
}
