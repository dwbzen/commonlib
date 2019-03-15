package common.math;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import common.util.IJson;

/**
 * A sorted Collection of n number of T instances where n=degree (or order) of the Tupple.<br>
 * The Tupple is maintained as a List<T> that is backed by a T[].
 * Changes in the backing array are reflected in the Tupple.<br>
 * In particular, elements are sorted in the constructor, or if added one at a time,<br>
 * when the Tupple is complete resulting in the backing array and element list being sorted.<br>
 * uniqueElements is a SortedSet of the unique elements of the Tupple.
 * 
 * @author don_bacon
 *
 * @param <T>
 */
public class Tupple<T extends Comparable<T>> implements IJson, Comparable<Tupple<T>> {

	private static final long serialVersionUID = 6084450929057511808L;
	static String[] displayBraces = {"[ ", " ]" };
	@JsonProperty	private int degree = 1; 	// a scalar
	@JsonProperty	protected List<T> elements = null;
	@JsonIgnore		protected SortedSet<T> uniqueElements = new TreeSet<>();
	@JsonProperty	protected Integer key = Integer.MIN_VALUE;
	@JsonIgnore		protected T[] ts;
	
	/**
	 * An Empty Tupple where elements are added one at a time.
	 * Tupple is complete when #elements == degree
	 * 
	 * @param degree
	 */
	public Tupple(int degree) {
		this.degree = degree;
		elements = new ArrayList<T>(degree);
	}
	
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
	
	public Tupple(List<T> tElements) {
		degree = tElements.size();
		elements = new ArrayList<>();
		createTArrayAndList(tElements);
		uniqueElements.addAll(elements);
	}

	@SuppressWarnings("unchecked")
	private void createTArrayAndList(List<T> tElements) {
		@SuppressWarnings("rawtypes")
		Class<? extends Comparable> tClass = tElements.iterator().next().getClass();
		ts = (T[])Array.newInstance(tClass, degree);
		for(int i=0;i<degree;i++) {
			ts[i] = tElements.get(i);
		}
		Arrays.sort(ts);
		elements = Arrays.asList(ts);
	}

	public Tupple<T> add(T t) {
		if(elements.size() < degree) {
			elements.add(t);
			if(elements.size()==degree) {
				createTArrayAndList(elements);
			}
		}
		return this;
	}
	
	public int getDegree() {
		return degree;
	}
	
	public Collection<T> getElements() {
		return elements;
	}
	
	@JsonIgnore
	public T[] getElementArray() {
		return ts;
	}
	
	public T get(int index) {
		return elements.get(index);
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
			sb.append(displayBraces[0]);
			elements.forEach(t -> sb.append(t.toString() + ","));
			sb.delete(sb.length()-1, sb.length()).append(displayBraces[1]);
		}
		else {
			elements.forEach(t -> sb.append(t.toString()));
		}
		return sb.toString();
	}

	public String toString() {
		return toString(true);
	}
	
	public int getKey() {
		if(key.equals(Integer.MIN_VALUE)) {
			key = toString().hashCode();
		}
		return key;
	}
	
	public boolean equals(Tupple<T> other) {
		return compareTo(other) == 0 ? true : false;
	}

	@Override
	public int compareTo(Tupple<T> other) {
		int temp = 0;
		if(size() != other.size() ) {
			temp = size() - other.size();
		}
		else {
			for(int ind = 0;ind<size(); ind++) {
				temp = ts[ind].compareTo(other.index(ind));
				if(temp != 0) {
					break;
				}
			}
		}
		return temp;
	}
	
	public static void main(String...strings ) {
		Tupple<Character> tupple = new Tupple<>('a', 'b');
		System.out.println(tupple.toString(true));
		System.out.println(tupple.toJson());
	}
	
}
