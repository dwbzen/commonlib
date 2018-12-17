package mathlib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.util.IJson;

/**
 * A Collection of n T instances where n=degree (or order) of the Tupple.<br>
 * Ordering is not important so ("A", "C", "D") is equivalent to ("C", "A", "D")<br>
 * Note that changes in the backing array are reflected in the Tupple,<br>
 * in particular elements are sorted in the constructor, resulting in the backing array and element list being sorted.<br>
 * uniqueElements is a SortedSet of the unique elements of the Tupple.
 * 
 * @author don_bacon
 *
 * @param <T>
 */
public class Tupple<T extends Comparable<T>> implements IJson, Comparable<Tupple<T>> {

	private static final long serialVersionUID = 6084450929057511808L;
	@JsonProperty	private int degree = 1; 	// a scalar
	@JsonProperty	private Collection<T> elements = null;
	@JsonProperty	private SortedSet<T> uniqueElements = new TreeSet<>();
	@JsonIgnore		private T[] ts;
	
	
	@SafeVarargs
	public Tupple(T...ts ) {
		if(ts == null || ts.length == 0) {
			throw new IllegalArgumentException("Incorrect arguments");
		}
		this.ts = ts;
		Arrays.sort(ts);
		degree = ts.length;
		elements = Arrays.asList(ts);
		uniqueElements.addAll(elements);
	}
	
	@SuppressWarnings("unchecked")
	public Tupple(Collection<T> tElements) {
		degree = tElements.size();
		elements = new ArrayList<>();
		elements.addAll(tElements);
		ts = (T[])elements.toArray();
		uniqueElements.addAll(elements);
	}

	public int getDegree() {
		return degree;
	}
	
	public Collection<T> getElements() {
		return elements;
	}
	
	public T[] getElementArray() {
		return ts;
	}
	
	public boolean isElement(T someT) {
		return elements.contains(someT);
	}
	
	public int size() {
		return elements.size();
	}
	
	public T index(int index) {
		return ts[index];
	}

	public Set<T> getUniqueElements() {
		return uniqueElements;
	}

	public String toString(boolean formatted) {
		StringBuilder sb = new StringBuilder();
		if(formatted) {
			sb.append("[ ");
			elements.forEach(t -> sb.append(t.toString() + ","));
			sb.delete(sb.length()-1, sb.length()).append(" ]");
		}
		else {
			elements.forEach(t -> sb.append(t.toString()));
		}
		return sb.toString();
	}

	public String toString() {
		return toString(false);
	}
	
	public static void main(String...args) {
		Tupple<Character> tupple = new Tupple<>('a', 'B', 'x');
		System.out.println(tupple.toJson(true));
		System.out.println(tupple.toString());
		System.out.println(tupple.toString(true));
		System.out.println(tupple.getUniqueElements());
		
		Tupple<Integer> t2 = new Tupple<>(100, 200, 300);
		System.out.println(t2.toJson(true));
		System.out.println(t2.toString());
		System.out.println(tupple.getElements());
		
		String[] array = { "Fred", "Cheryl", "Don" };
		Tupple<String> tupple3 = new Tupple<String>(array);
		System.out.println(tupple3.toJson(true));
		array[0] = "Alexander";
		System.out.println(tupple3.toJson(true));
		
	}

	@Override
	public int compareTo(Tupple<T> other) {
		int temp = 0;
		int ind = 0;
		if(size() != other.size() ) {
			temp = size() - other.size();
		}
		else {
			for(;ind<size(); ind++) {
				temp += ts[ind].compareTo(other.index(ind));
			}
		}
		return (temp < 0) ? -1 : (temp>0) ? 1 : 0;
	}
	
}
