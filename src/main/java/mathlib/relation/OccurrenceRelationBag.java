package mathlib.relation;

import java.util.List;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author don_bacon
 *
 * @param <K> the base class of elements in the relationship (for example, Character)
 * @param <T> the containing class that is a List<K> (for example, Word)
 * @param <S> a Supplier<T>  (for example, Sentence)
 */
public class OccurrenceRelationBag<K extends Comparable<K>, T extends List<K>, S extends Supplier<T>> {
	protected static final Logger log = LogManager.getLogger(OccurrenceRelationBag.class);

	@JsonProperty	private OccurrenceRelation<K,T,S> occurrenceRelation = null;
	
}
