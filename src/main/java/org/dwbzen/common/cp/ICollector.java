package org.dwbzen.common.cp;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * 
 * @param <T> - the base class that is collected. The implementing class
 * 				is properly named <T>Collector. T implements List<R>
 * @param <R> -  the class of atomic object that make up T
 * @param <K> - a class that implements Supplier<T>
 * 
 * The implementing class will @Override the functional interface Consumer<K>, void accept(K supplierClassInstance)
 * and @Override Function<T,R> interface apply(T collectedClassInstance)
 * Collectors (classes that implement ICollector<T, R, K>) work with
 * Producers (classes that implement IProducer<T,R>) taking the output of the collector
 * to produce a Set<R> based on a starting seed R.
 * 
 * Example: CharacterCollector - collects stats on Characters within Words. 
 *  ICollector<Word, MarkovChain<Character, Word>, Sentence> implements ICollector<T, R, K>
 *  T :: Word
 *  R :: Character
 *  K :: Sentence
 *  
 *  Word implements List<Character> and Comparable<Word>
 *  Sentence implements Supplier<Word>, overriding Word get();
 *  CollectorStatsMap<Character, Word> extends TreeMap<Character, CollectorStats<Character, Word>>
 *  The corresponding IProducer class is WordProducer.
 *  
 * Example: WordCollector - collects stats on Words within Sentences.
 *  Would implement ICollector<Sentence, MarkovChain<Word, Sentence>, Book> where
 *  Sentence implements List<Word> and Comparable<Sentence>
 *  Book implements Supplier<Sentence>, overriding Sentence get();
 *  CollectorStatsMap<Word, Sentence> extends TreeMap<Word, CollectorStats<Word, Sentence>>
 *  The Corresponding IProducer class is SentenceProducer.
 *  
 * Example: ICollector<ChordProgression, MarkovChain<HarmonyChord, ChordProgression>, Song>
 *  ChordProgression implements List<HarmonyChord> and Comparable<ChordProgression>
 *  Song implements Supplier<ChordProgression>, overriding ChordProgression get();
 *  CollectorStatsMap<HarmonyChord, ChordProgression> extends TreeMap<HarmonyChord, CollectorStats<HarmonyChord, ChordProgression>
 *  Collector-Producer pair is HarmonyChordCollector, ChordProgressionProducer
 *  
 *  In general, given the ICollector interface ICollector<T, R, K> extends Function<T, R> , Consumer<K>
 *  the corresponding IProducer interface is IProducer<T, R>  extends Function<T, R>
 *  and the Collector-Producer pair is <T>Collector, <R>Producer
 *  
 *  
 * @author don_bacon
 */
public interface ICollector<T, K, L> extends Function<T, K>, Consumer<L> {
	void collect();
	
	default void collect(List<T> listOfT) {
		// override this as default is a no-op
	}
	
	/**
	 * Implements Consumer<K>
	 */
	default void accept(L thing) {
		// override this as default is a no-op
	}
	/**
	 * Implements Function<T, R>
	 */
	default K apply(T thingToApply) {
		// override this
		return null;
	}
}
