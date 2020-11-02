package org.dwbzen.common.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomGenerator {

	ThreadLocalRandom random = ThreadLocalRandom.current();
	
	public RandomGenerator() {
	}
	
	/**
	 * If noReplacement is true, then numberToGenerate must be <= bound-origin
	 * 
	 * @param origin lowest value returned, inclusive
	 * @param bound highest value returned, exclusive
	 * @param numberToGenerate
	 * @param noReplacement set to true for no repeated elements in the range
	 * @return
	 */
	public List<Integer> randomIntegers(int origin, int bound, int numberToGenerate, boolean noReplacement){
		if(noReplacement && numberToGenerate > bound - origin) {
			throw new IllegalArgumentException("numberToGenerate must be <= bound-origin");
		}
		List<Integer> result = new ArrayList<>();
		int count = 0;
		while(count < numberToGenerate) {
			Integer n = random.nextInt(origin, bound);
			if(noReplacement) {
				if(!result.contains(n)) {
					result.add(n);
					count++;
				}
			}
			else {
				result.add(n);
				count++;
			}
		}
		return result;
	}
	
	public static void main(String...args) {
		int origin = Integer.parseInt(args[0]);
		int bound = Integer.parseInt(args[1]);
		int numberToGenerate = Integer.parseInt(args[2]);
		boolean noReplacement = args[3].equalsIgnoreCase("true");
		
		List<Integer> result = null;
		RandomGenerator rand = new RandomGenerator();
		result = rand.randomIntegers(origin, bound, numberToGenerate, noReplacement);
		for(Integer n : result) {
			System.out.print(n+"  ");
		}
	}
}
