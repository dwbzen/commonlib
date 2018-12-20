package mathlib.cp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mathlib.Tupple;

public class Relation<K extends Comparable<K>, T extends List<K>, S extends Supplier<T>> implements IRelation<K,T,S> {
	protected static final Logger log = LogManager.getLogger(Relation.class)
			;
	public Map<Tupple<K>, Integer> createPartitionKeys(T unit, int degree) {
		Map<Tupple<K>, Integer> partitionKeyMap = new HashMap<>();
		Set<Tupple<K>> partitions = partition(unit, degree);
		partitions.forEach(tupple -> partitionKeyMap.put(tupple, getKey(tupple)));
		return partitionKeyMap;
	}
	
	public Set<Tupple<K>> partition(T unit, int degree) {
		Set<Tupple<K>> partitions = new TreeSet<>();
		int len = unit.size();
		int index = 0;
		double nSets = Math.pow(2, len);	// Power set cardinality
		for(int i = 1; i<nSets; i++) {
			if(nbits(i) == degree) {
				Tupple<K> tupple = new Tupple<>(degree);
				int j = 0;
				do {
					if((1 & (i>>j)) == 1) {
						index = len - 1 - j;
						log.debug(i + " " + j + " " + index);
						tupple.add(unit.get(index));
					}
				} while(Math.pow(2, j++) <= nSets);
				partitions.add(tupple);
				log.debug(tupple);
			}
		}
		return partitions;
	}
	
	public static int nbits(int n) {
		int nbits = 0;
		int i = 0;
		do {
			nbits += (1 & (n>>i++));
		} while(Math.pow(2, i) <= n);
		return nbits;
	}

	@Override
	public boolean isElement(Tupple<K> element, T unit) {
		return false;
	}
	
	public static void main(String...strings) {
		for(int i=0; i<=128; i++) {
			System.out.println("i: " + i + " nbits: " + Relation.nbits(i));
		}
	}
}
