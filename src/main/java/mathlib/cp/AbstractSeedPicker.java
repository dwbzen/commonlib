package mathlib.cp;

import java.util.List;
import java.util.function.Supplier;
import mathlib.cp.MarkovChain;
import mathlib.util.INameable;

/**
 * Extend this class to create a concrete ISeedPicker instance for a given set of <K, T, R> for example<br>
 * K :: Word<br>
 * T :: Sentence<br>
 * R :: Book<br>
 * Dependency injection into a given MarkovChain<K,T,R>
 * @author don_bacon
 *
 * @param <K>
 * @param <T>
 * @param <R>
 */
public abstract class AbstractSeedPicker<K extends Comparable<K>, T extends List<K> & Comparable<T>, R extends Supplier<T> & INameable> implements ISeedPicker<K,T,R> {

	private MarkovChain<K,T,R> markovChain = null;
	private K initial = null;
	
	/**
	 * Use setter method to set markovChain
	 */
	public AbstractSeedPicker() {
	}
	
	public AbstractSeedPicker(MarkovChain<K,T,R> aMarkovChain) {
		markovChain = aMarkovChain;
	}

	@Override
	abstract public T pickSeed();

	@Override
	public void setInitial(K initialThing) {
		initial = initialThing;
	}

	@Override
	public K getInitial() {
		return initial;
	}

	public MarkovChain<K, T, R> getMarkovChain() {
		return markovChain;
	}

	public void setMarkovChain(MarkovChain<K, T, R> markovChain) {
		this.markovChain = markovChain;
	}
	
}
