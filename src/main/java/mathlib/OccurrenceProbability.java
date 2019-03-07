package mathlib;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import mathlib.util.IJson;

public class OccurrenceProbability implements IJson, Comparable<OccurrenceProbability> {
	
	private static final long serialVersionUID = 8656552141453330699L;
	
	public static final MathContext mathContext = new MathContext(5, RoundingMode.HALF_DOWN);	// precision is 5 decimal places
	public static final BigDecimal lowerLimit = new BigDecimal(1E-6);	// any number having an absolute value <= lowerLimit is set to 0.0
	
	@JsonProperty("occurrence")	 private int occurrence = 0;
	@JsonProperty	private Double probability = 0.0;
	@JsonProperty	private int[] range = {0, 0};
	/**
	 * The ordinal of this OccurrenceProbability relative to the sorting of the container
	 */
	@JsonProperty	private int rank = 0;
	@JsonIgnore		private Comparator<OccurrenceProbability> comparator = null;
	public static String formatPattern = null;
	public static DecimalFormat format = null;
	
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
		setProbability(prob);
	}

	public int getOccurrence() {
		return occurrence;
	}

	public void setOccurrence(int occurrence) {
		this.occurrence = occurrence;
	}
	
	/**
	 * Increments the total occurrences count only.
	 * The range and probability is not modified.
	 */
	public void increment() {
		occurrence++;
	}

	public double getProbability() {
		return probability;
	}
	
	@JsonIgnore
	public String getProbabilityText() {
		return format.format(probability);
	}

	public void setProbability(double prob) {
		this.probability =  BigDecimal.valueOf(prob).round(mathContext).doubleValue();
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
	
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String toString() {
		return range[0] + "," + range[1] + "\t" + getProbabilityText();
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
	
	@Override
	public String toJson(boolean pretty) {
		
		return toJson("");
	}
	
	public String toJson(String indent) {
		StringBuilder sb = new StringBuilder(indent + "\"occurrenceProbability\" : {\n");
		sb.append(indent + "  " + "\"rank\" : " + rank + "\n");
		sb.append(indent + "  " + "\"probability\" : " +  getProbabilityText() + ",\n");
		sb.append(indent + "  " + "\"range\" : [" + range[0] + ", " + range[1] + "],\n");
		sb.append(indent + "  " + "\"occurrence\" : " + occurrence + "\n");
		sb.append(indent + "}");
		return sb.toString();
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
