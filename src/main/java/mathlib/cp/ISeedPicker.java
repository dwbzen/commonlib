package mathlib.cp;

import java.util.List;
import java.util.function.Supplier;

import mathlib.util.INameable;

public interface ISeedPicker<K extends Comparable<K>, T extends List<K> & Comparable<T>, R extends Supplier<T> & INameable> {
	T pickSeed();
	
	void setInitial(K initialThing);
	
	K getInitial();
}
