package org.dwbzen.common.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MathUtil {
	
	/**
	 * Factors units to powers of 2 for a given time signature & measure divisions
	 * 
	 * @param divsPerMeasure #divisions per measure
	 * @parem beats #beats per measure
	 * @param beatNote note value that has the beat
	 * @param noteUnits units for this note to factor
	 * @return List<Integer> of factors. Elements are powers of 2 (-6 <= n <= 6)
	 * Time signature is beats/beatNote (4/4 or 3/8 for example)
	 * Note values are:  1=whole, 2=half, 4=quarter, 8=eighth, 16=sixteenth, 32=32nd, 64=64th
	 * Constraint - #divisions per beat must be an integer.
	 * Example: noteUnits=19, 4/4 time, 24 divisions/measure = 3.166666, factors as [1, 0, -3, -5]
	 * or 2 + 1 + 1/8 + 1/32 = 3.15625 (close enough!)
	 * Range is 2^6 (64) to 2^-6 (1/64)
	 */
	public static List<Integer> factor(int divsPerMeasure, int beats, int beatNote, int noteUnits) {
		return factor(noteUnits / ((double)divsPerMeasure/beats));
	}
	
	public static List<Integer> factor(double d) {
		return factor(d, 6);
	}
	
	/**
	 * Factor a number as powers of 2.
	 * @param d
	 * @param places
	 * @return List<Integer> in sorted decreasing order
	 *
	 */
	public static List<Integer> factor(double d, int places) {
		List<Integer> factors = new ArrayList<Integer>();
		List<Integer> negFactors = new ArrayList<Integer>();	// for factors < 0
		int id = (int)d;
		double remaining = d-id;
		//  the number of zero bits preceding the highest-order ("leftmost") one-bit 
		//  in the two's complement binary representation of the specified int value. 
		int lim = 32-Integer.numberOfLeadingZeros(id);
		for(int i=0; i<lim; i++) {
			if( (1 & id) == 1) {
				factors.add(i);
			}
			id = id>>1;
		}
		if(remaining > 0) {
			for(int i=1; i<=places; i++) {
				double fact = 1.0/Math.pow(2, i);
				if(fact <= remaining) {
					negFactors.add(-i);
					remaining -= fact;
				}
			}
		}
		
		factors.addAll(negFactors);
		factors.sort((a,b) -> (a==b) ? 0 : ((a<b) ? 1 : -1) );
		return factors;
	}
	
	/**
	 * Partition factors into dot sets
	 * For example:
	 *  { [1, -1, -2, -3], 0 } can be partitioned to:  [ {[1], 0}, { [-1, -2, -3], 2 } ]
	 *  { [1, 0, -2, -3], 1 }  can be partitioned to:  [ { [1, 0], 1 }, { [-2, -3], 1 } ]
	 */
	public static Partition partition(Factor factors) {
		List<Integer> pf = new ArrayList<Integer>();
		Partition partition = new Partition();
		double partitionedNumber = 0;
		for(int i=0; i<factors.size(); i++) {
			if(i == 0) {
				pf.add(factors.get(0));
				continue;
			}
			int n = factors.get(i);
			if(n == (factors.get(i-1) -1 )) {
				if(pf == null) { pf = new ArrayList<Integer>(); }
				pf.add(n);
			}
			else {
				Factor nf = new Factor(pf);
				partitionedNumber += nf.getFactoredNumber();
				partition.getPartitions().add(nf);
				pf = new ArrayList<Integer>();
				pf.add(n);
			}
		}
		if(pf.size() > 0) {
			Factor nf = new Factor(pf);
			partitionedNumber += nf.getFactoredNumber();
			partition.getPartitions().add(nf);
		}
		partition.setPartitionedNumber(partitionedNumber);
		return partition;
	}
	
	/**
	 * integer Log base 2 of an integer
	 */
	public static int log2(int n){
	    if(n <= 0) throw new IllegalArgumentException();
	    return 31 - Integer.numberOfLeadingZeros(n);
	}
	
	/**
	 * Log base b of a given double
	 * @param num
	 * @param base
	 * @return
	 */
	public static double logb( double num, double base ) {
	     return Math.log(num) / Math.log(base);
	}

	/**
	 * double Log base 2 of a given double
	 * @param num
	 * @return
	 */
	public static double log2( double num ) {
	     return logb(num, 2);
	}


	/**
	 * a "dot" adds half the value to a note, ".." adds 1/2 + 1/8 and so forth.
	 * Given a List of factors sorted in decreasing value, this returns the #dots needed.
	 * It's like rounding down. For example, [1, 0, -3, -5] would be 0 dots
	 * factors: [1, -1, -2, -4, -6] = 2 dots. (missing the -3)
	 * factors: [1, -1] = 0 dot (missing the 0)
	 * factors: [1, 0, -1, -3] = 2 dots
	 * factors: [1, 0, -1, -2, -3] = 4 dots
	 * dots :: number of consecutive powers of 2 in a List<Integer> factors
	 * @param factors
	 * @return
	 */
	public static int dots(List<Integer> factors) {
		int ndots = 0;
		for(int i=1; i<factors.size(); i++) {
			int n = factors.get(i);
			if(n == (factors.get(i-1) -1 )) {
					ndots++;
			}
			else {	break;	}
		}
		return ndots;
	}
	
	public static int convertHexStringToInt(String hexString) throws NumberFormatException {
		BigInteger hexbig = new BigInteger(hexString, 16);
		int hexnum = hexbig.intValue();
		return hexnum;
	}
	
	public static void main(String[] args) {
		String factorsOf = "factors of ";
		if(args.length == 4) {
			// "Usage: factor(int divsPerMeasure, int beats, int beatNote, int noteUnits"
			int divsPerMeasure = Integer.parseInt(args[0]);
			int beats = Integer.parseInt(args[1]);
			int beatNote = Integer.parseInt(args[2]);
			int noteUnits = Integer.parseInt(args[3]);
			
			List<Integer> factors = MathUtil.factor(divsPerMeasure, beats, beatNote, noteUnits);
			int ndots = MathUtil.dots(factors);
			System.out.println("factors: " + factors + " dots: " + ndots);
			
			System.out.println("log base 2(" + noteUnits + ") = " + log2(noteUnits));
			System.out.println("log base 2(" + noteUnits + ") = " + log2((double)noteUnits));
			
			factors = factor(noteUnits);
			System.out.println(factorsOf + noteUnits + " = " + factors);
			double beatNotes = noteUnits / ((double)divsPerMeasure/beats);
			System.out.println(factorsOf + beatNotes + " = " + factor(beatNotes));
			
			double d = 7 + (1.00/3.00);
			System.out.println(factorsOf + d + " = " +  factor(d) + " " + factor(d, 10));
		}
		else if(args.length == 3) {
			// make a table of factors
			int divsPerMeasure = Integer.parseInt(args[0]);
			int beats = Integer.parseInt(args[1]);
			int beatNote = Integer.parseInt(args[2]);
			for(int i=1; i<=divsPerMeasure; i++) {
				List<Integer> factors = MathUtil.factor(divsPerMeasure, beats, beatNote,i);
				int ndots = dots(factors);
				System.out.print(divsPerMeasure + "\t" +  beats + "\t" +
						beatNote + "\t" + i + "\t" + factors + "\t" + ndots);
				Factor factor = new Factor(factors);
				Partition part = MathUtil.partition(factor);
				System.out.println("\t" + part.getPartitionedNumber() + "\t" + part.toString());
			}
		}
	}
}
