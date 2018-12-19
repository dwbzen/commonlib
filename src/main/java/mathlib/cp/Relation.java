package mathlib.cp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

import mathlib.Tupple;

public class Relation<K extends Comparable<K>, T extends List<K>, S extends Supplier<T>> implements IRelation<K,T,S> {
	
	public Map<Tupple<K>, Integer> createPartitionKeys(T unit, int degree) {
		Map<Tupple<K>, Integer> partitionKeyMap = new HashMap<>();
		Set<Tupple<K>> partitions = partition(unit, degree);
		partitions.forEach(tupple -> partitionKeyMap.put(tupple, getKey(tupple)));
		return partitionKeyMap;
	}
	
	public Set<Tupple<K>> partition(T unit, int degree) {
		Set<Tupple<K>> partitions = new TreeSet<>();
		int n = unit.size();
		double nSets = Math.pow(2, n);	// Power set cardinality
		for(int i = 1; i<nSets; i++) {
			if(nbits(i) == degree) {
				Tupple<K> tupple = new Tupple<>(degree);
				for(int j=0; j<i; j++) {
					if((1 & (i>>j)) == 1) {
						tupple.add(unit.get(j));
					}
				}
				partitions.add(tupple);
			}
		}
		
		return partitions;
	}
	
	public int nbits(int n) {
		int nbits = 0;
		for(int i=0; i<n; i++) {
			nbits += (1 & (n>>i));
		}
		return nbits;
	}

	@Override
	public boolean isElement(Tupple<K> element, T unit) {
		return false;
	}
	
	public static void main(String...strings) {
		
	}
}
