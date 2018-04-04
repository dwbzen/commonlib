package mathlib;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Factor encapsulates the List<Integer> factors of a factoredNumber in powers of 2
 * including  "dots" which is the #of consecutive powers of 2 in the factors.
 * places is the max. number of negative powers of 2 to use in factoring.
 * so places = 6 is 1/(2^6) or 1/64.
 * 
 * @author don_bacon
 *
 */
public class Factor implements Serializable {

	private static final long serialVersionUID = 2788855972961763480L;
	private static MathContext mathContext = new MathContext(4);	// 4-places rounding half-up
	private List<Integer> factors = new ArrayList<>();
	private int dots = 0;
	private double factoredNumber = 0;
	private double numberToFactor = 0;
	private double remainder = 0;
	private int places = 6;
	
	public Factor() {
	}
	
	public Factor(List<Integer> f) {
		setFactors(f);
	}
	
	public Factor(double numToFactor) {
		this(numToFactor, 6);
	}
	
	public Factor(double numToFactor, int places) {
		this();
		this.numberToFactor = numToFactor;
		this.places = places;
		factors = new ArrayList<>();
		this.factors.addAll(MathUtil.factor(numberToFactor, places));
		setDots(MathUtil.dots(factors));
		factoredNumber = BigDecimal.valueOf(getFactoredNumber(factors)).round(mathContext).doubleValue();
		remainder = numberToFactor - factoredNumber;
	}
	
	private double getFactoredNumber(List<Integer> factors2) {
		double fn = 0;
		for(int fact : factors2) {
			fn += Math.pow(2, fact);
		}
		return fn;
	}

	public String toString() {
		return "{ " + factors.toString() + ", " + dots + " }";
	}

	public int getDots() {
		return dots;
	}
	public void setDots(int dots) {
		this.dots = dots;
	}
	
	public void setDots() {
		if(factors != null && !factors.isEmpty()) {
			this.dots = MathUtil.dots(factors);
		}
	}

	public List<Integer> getFactors() {
		return factors;
	}

	/**
	 * Sets List<Integer> factors and also computes and saves the factoredNumber and dots
	 * 
	 * @param f List<Integer> of factors
	 */
	public void setFactors(List<Integer> f) {
		factors = new ArrayList<>();
		factors.addAll(f);
		factoredNumber = 0;
		for(int n : factors) {
			factoredNumber += Math.pow(2, n);
		}
		numberToFactor = factoredNumber;
		setDots();
	}
	
	public double getFactoredNumber() {
		return factoredNumber;
	}

	public void setNumberToFactor(double d) {
		this.numberToFactor = d;
		if(factors == null) {
			factors = new ArrayList<>();
		}
		else if(!factors.isEmpty()) {
			factors.removeIf(a -> true);
		}
		factors.addAll(MathUtil.factor(d, places));
		setDots(MathUtil.dots(factors));
	}

	public int size() {
		return factors.size();
	}
	public Integer get(int index) {
			return factors.get(index);
	}
	
	public int getPlaces() {
		return places;
	}

	public void setPlaces(int places) {
		this.places = places;
	}

	public double getNumberToFactor() {
		return numberToFactor;
	}

	public double getRemainder() {
		return remainder;
	}
	
}
