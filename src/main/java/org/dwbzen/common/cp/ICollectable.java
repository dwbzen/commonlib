package org.dwbzen.common.cp;

public interface ICollectable<T> {
	T getTerminal();
	T getNullValue();
}
