package math.complex;

import java.util.Iterator;
import java.util.LinkedHashSet;

public class ComplexSet extends LinkedHashSet<Complex> {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(Complex e) {

		return contains(e) ? false : super.add(e);
	}

	@Override
	public boolean contains(Object o) {
		for(Iterator<Complex> it=iterator(); it.hasNext();) {
			if(it.next().equals((Complex)o)) {
				return true;
			}
		}
		return false;
	}
	
	public int indexOf(Complex c) {
		int index = -1;
		int ind = 0;
		for(Iterator<Complex> it=iterator(); it.hasNext(); ind++) {
			if(it.next().equals(c)) {
				index = ind;
				break;
			}
		}
		return index;
	}
	
	public boolean add(double d) {
		return add(new Complex(d, 0.0));
	}
}
