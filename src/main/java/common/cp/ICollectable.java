package common.cp;

public interface ICollectable<T> {
	T getTerminal();
	T getNullValue();
}
