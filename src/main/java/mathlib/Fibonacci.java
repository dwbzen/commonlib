package mathlib;

import java.util.ArrayList;
import java.util.List;

import mathlib.complex.Complex;

public class Fibonacci<T extends Number> implements ISequence<T> {
	
	private List<Long> intSeries = null;
	private List<Complex> complexSeries = null;
	private int order = 2;	// for Fibonacci, for tribonacci order = 3, etc.
	private int len = 0;
	public static int DEFAULT_LEN = 10;
	
	public static <T extends Number> ISequence<T> createSequence(T[] args) {
		Fibonacci<T> fib = null;
		fib = new Fibonacci<T>();
		fib.newSequence(args);
		return fib;
	}
	
	public Fibonacci() {}
	
	public Fibonacci(int f0,int f1, int len) {
		createSeries(f0,f1, len);
	}
	public Fibonacci(Complex c0, Complex c1, int len) {
		createSeries(c0, c1, len);
	}
	
	public void newSequence(T[] args) {
		len = (args.length == 3) ? (Integer)args[2] : DEFAULT_LEN;
		order = (args.length >= 3) ? args.length - 1: 2;
	}
	
	public void createSeries(int f0,int f1, int len) {
		intSeries = new ArrayList<Long>();
		intSeries.add(new Long(f0));
		intSeries.add(new Long(f1));
		for(int i=2; i<len; i++) {
			intSeries.add(intSeries.get(i-1) + intSeries.get(i-2));
		}
		this.len = len;
	}

	public void createSeries(Complex c0, Complex c1, int len) {
		complexSeries = new ArrayList<Complex>();
		complexSeries.add(new Complex(c0));
		complexSeries.add(new Complex(c1));
		for(int i=2; i<len; i++) {
			complexSeries.add(complexSeries.get(i-1).plus(complexSeries.get(i-2)));
		}
		this.len = len;
	}
	
	public static void main(String[] args) {
		int len = 2;
		boolean complex = false;
		if(args.length < 2) {
			System.err.println("Usage: Fibonacci -len n [start start+1]");
			return;
		}
		List<Complex> temp = new ArrayList<Complex>();
		// default: 0 1 1 2 3 5 etc.
		temp.add(new Complex(0,0));
		temp.add(new Complex(1,0));
		int ind = 0;
		for(int i=0; i<args.length; i++) {
			if(args[i].equalsIgnoreCase("-len")) {
				len = Integer.parseInt(args[++i]);
			}
			else {
				Complex c = Complex.parseComplex(args[i]);
				temp.set(ind++, c);
				if(c.imag() != 0) {
					complex = true;
				}
			}
		}
		Fibonacci<?> fib = null;
		if(complex) {
			fib = new Fibonacci<Complex>();
			fib.createSeries(temp.get(0), temp.get(1), len);
		}
		else {
			fib = new Fibonacci<Long>();
			fib.createSeries((int)temp.get(0).real(), (int)temp.get(1).real(), len);
		}
		System.out.println(fib.toString());
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if(intSeries != null) {
			for(int i=0; i<len; i++ ){
				sb.append(intSeries.get(i));
				sb.append(" ");
			}
		}
		else if(complexSeries != null) {
			for(int i=0; i<len; i++ ){
				sb.append(complexSeries.get(i));
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	
	public List<Long> getIntSeries() {
		return intSeries;
	}

	public List<Complex> getComplexSeries() {
		return complexSeries;
	}

	public List<Complex> getSequence() {
		return complexSeries;
	}
	
	public int getOrder() {
		return order;
	}

	public int getLen() {
		return len;
	}
}
