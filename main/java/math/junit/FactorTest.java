package math.junit;

import junit.framework.TestCase;
import math.Factor;

public class FactorTest extends TestCase {

	public void testFactor1() {
		Factor factor = new Factor(2.631);
		System.out.println(factor);
		System.out.println(" factoredNumber: " + factor.getFactoredNumber());
		System.out.println(" remainder: " + factor.getRemainder());
	}
	
	public void testFactor2() {
		Factor factor = new Factor(3.7651);
		System.out.println(factor);
		System.out.println(" factoredNumber: " + factor.getFactoredNumber());
		System.out.println(" remainder: " + factor.getRemainder());
	}
}
