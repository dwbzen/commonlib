package common.relation;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import common.math.Tupple;

/**
 * A IRelation encapsulates the mathematical definition of a Relation
 * and provides for tests whether a given subset is an element of the Relation.<br>
 * Implementations codify ideas such as "appear together in" 
 * or "is an output state of" (as in a MarkovChain).<br>
 * Relation elements are represented as Tupple<K> for a given order (degree).<br>
 * T represents a unit which can be partitioned into Tupples<K>.
 * S represents a specific instance of the universe of all T's.<br>
 * For example<br>
 * <dl>
 * <dt>K</dt><dd>Word - List&lt;Character&gt;</dd>
 * <dt>T</dt><dd>Sentence - List&ltWord&gt;</dd>
 * <dt>R</dt><dd>Book -Supplier&ltSentence&gt;</dd>
 * </dl>
 * 
 * 
 * @author don_bacon
 *
 */
public interface IRelation<K extends Comparable<K>, T extends List<K>, S extends Supplier<T>> {

	boolean isElement(Tupple<K> element, T unit);
	
	/**
	 * Generates a key for the element argument.<br>
	 * Default implementation is the hashCode of the Tupple toString().<br>
	 * Assumes that the Tupple<K> is sorted (guaranteed by the Tupple constructor).
	 * @param element the Tupple<Character> to generate a key for
	 * @return int key (hash of sorted Tupple elements)
	 * @author don_bacon
	 */
	default int getKey(Tupple<K> element) {
		return element.toString().hashCode();
	}
	
	Set<? extends Tupple<K>> partition(T unit, int degree);
	
}
