package mathlib.cp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.SerializationFeature;

import mathlib.util.IJson;

public class OccurrenceProbability implements IJson {
	
	private static final long serialVersionUID = 8656552141453330699L;
	@JsonProperty("occurrence")	 private int occurrence = 0;
	@JsonProperty	private double probability = 0.0;
	@JsonProperty	private int[] range = {0, 0};

	public OccurrenceProbability() {
	}

	public OccurrenceProbability(int occ, double prob) {
		this.occurrence = occ;
		this.probability = prob;
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
	
	public static void main(String... args) {
		OccurrenceProbability p = new OccurrenceProbability(10, .1);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		int[] range = {1,10};
		p.setRange(range);
		System.out.println(p.toJson());
	}
}
