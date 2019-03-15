package common.math.complex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * Eisentstein integers have the form  z = a + bW
 * where a, b are integers and w =  1/2(-1 + i(3^.5)) = exp((2 * #pi * (0,1))/3)
 * W = -.5 + sqrt(3)/2 i
 * @author dbacon
 *
 */
public class EisensteinIntegerList {
	
	public static void main(String[] args) {
		EisensteinIntegerList ei = new EisensteinIntegerList(-10, 10);
		List<EisensteinInteger> eilist = ei.getEisensteinIntegers();
		Iterator<EisensteinInteger> it = eilist.iterator();
		while(it.hasNext()) {
			EisensteinInteger c = it.next();
			System.out.println(c.getA() + "," + c.getB() + ","  + c.x + "," + c.y);
		}
		
	}
	
	private List<EisensteinInteger> eisensteinIntegers = new ArrayList<EisensteinInteger>();
	public static final Complex W = new Complex(-0.5, 0.86602540378443864676372317075294);
	private int low;
	private int high;

	/**
	 * Creates a list of Eisenstein Integers with both real and imaginary
	 * parts having a range of low to high inclusive
	 * @param low
	 * @param high
	 */
	public EisensteinIntegerList(int low, int high) {
		this.low = low;
		this.high = high;
		for(int a=low; a<=high; a++) {
			for(int b=low; b<=high; b++) {
				eisensteinIntegers.add(new EisensteinInteger(a, b));
			}
		}
	}

	public List<EisensteinInteger> getEisensteinIntegers() {
		return eisensteinIntegers;
	}

	public int getLow() {
		return low;
	}

	public int getHigh() {
		return high;
	}

}
