package mathlib.cp;

import java.text.DecimalFormat;
import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.SerializationFeature;

import mathlib.util.IJson;

public class OccurrenceProbability implements IJson, Comparable<OccurrenceProbability> {
	
	private static final long serialVersionUID = 8656552141453330699L;
	@JsonProperty("occurrence")	 private int occurrence = 0;
	@JsonProperty	private double probability = 0.0;
	@JsonProperty	private int[] range = {0, 0};
	@JsonIgnore		private Comparator<OccurrenceProbability> comparator = null;
	public static String formatPattern = null;
	static DecimalFormat format = null;
	
	static {
		formatPattern = "0.0####";
		format = new DecimalFormat(formatPattern);
	}
	
	public OccurrenceProbability() {
		comparator = new OccurrenceProbabilityComparator();
	}

	public OccurrenceProbability(int occ, double prob) {
		this();
		occurrence = occ;
		probability = prob;
	}

	public int getOccurrence() {
		return occurrence;
	}

	public void setOccurrence(int occurrence) {
		this.occurrence = occurrence;
	}

	public double getProbability() {
		return probability;
	}
	
	@JsonIgnore
	public String getProbabilityText() {
		return format.format(probability);
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public int[] getRange() {
		return range;
	}

	public void setRange(int[] range) {
		this.range = range;
	}
	public void setRange(int index, int val) {
		range[index] = val;
	}
	
	public String toString() {
		return range[0] + "," + range[1] + "\t" + getProbabilityText();
	}
	
	public static void main(String... args) {
		OccurrenceProbability p = new OccurrenceProbability(10, 0.01176470588);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		int[] range = {1,10};
		p.setRange(range);
		System.out.println(p.toJson());
		System.out.println(p.toString());
	}
	
	@Override
	public boolean equals(Object other) {
		boolean result = false;
		if(other instanceof OccurrenceProbability) {
			OccurrenceProbability op = (OccurrenceProbability)other;
			result = (this.occurrence == op.occurrence && this.probability == op.probability);
		}
		return result;
	}

	@Override
	public int compareTo(OccurrenceProbability o) {
		return comparator.compare(this, o);
	}
}

/**
 * Sort by occurrence in reverse order by default
 * @author don_bacon
 *
 */
class OccurrenceProbabilityComparator implements Comparator<OccurrenceProbability> {
	private boolean reverse = true;
	public OccurrenceProbabilityComparator() {
		
	}
	public OccurrenceProbabilityComparator(boolean reverseSortOrder) {
		reverse = reverseSortOrder;
	}
	
	@Override
	public int compare(OccurrenceProbability o1, OccurrenceProbability o2) {
		int result = 0;
		if(!o1.equals(o2)) {
			if(reverse) {
				result =  o1.getOccurrence() < o2.getOccurrence() ? 1 : -1;
			}
			else {
				result = o1.getOccurrence() < o2.getOccurrence() ? -1 : 1;
			}
		}
		return result;
	}
	
}
