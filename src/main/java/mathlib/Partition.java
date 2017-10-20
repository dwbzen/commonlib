package mathlib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Partition implements Serializable {

	private static final long serialVersionUID = -250853716898079720L;
	private Factor factors;
	private List<Factor> partitions = new ArrayList<Factor>();
	private double partitionedNumber = 0;
	
	public Partition() { }
	
	public Partition(double d) {
		this.partitionedNumber = d;
		factors = new Factor(d);
		partition();
	}
	
	public double getPartitionedNumber() {
		return partitionedNumber;
	}
	public void setPartitionedNumber(double partitionedNumber) {
		this.partitionedNumber = partitionedNumber;
	}
	public List<Factor> getPartitions() {
		return partitions;
	}
	
	/**
	 * Partition factors into dot sets. Each composed Factor has consecutive elements in List<Integer> factors
	 * For example:
	 *  { [1, -1, -2, -3], 0 } is partitioned to:  [ {[1], 0}, { [-1, -2, -3], 2 } ]
	 *  { [1, 0, -2, -3], 1 }  is partitioned to:  [ { [1, 0], 1 }, { [-2, -3], 1 } ]
	 *  { [1, -1, -3], 0 } is partitioned to:  [{ [1], 0 },{ [-1], 0 },{ [-3], 0 }]
	 */
	protected void partition() {
		List<Integer> pf = new ArrayList<Integer>();
		partitionedNumber = 0;
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
				partitions.add(nf);
				partitionedNumber += nf.getFactoredNumber();
				pf = new ArrayList<Integer>();
				pf.add(n);
			}
		}
		if(pf.size() > 0) {
			Factor nf = new Factor(pf);
			partitions.add(nf);
			partitionedNumber += nf.getFactoredNumber();
		}
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		for(Factor f : partitions) {
			sb.append(f.toString());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
	}
	
	public static void main(String... args) {
		Partition partition = null;
		Factor factors = null;
		 if(args.length != 4) {
			 return;
		 }
		int divsPerMeasure = Integer.parseInt(args[0]);
		int beats = Integer.parseInt(args[1]);
		//int beatNote = Integer.parseInt(args[2]);	// beatNote is not used but whatever
		int noteUnits = Integer.parseInt(args[3]);
		double d = noteUnits / ((double)divsPerMeasure/beats);
		/*
		 * Try Factor only
		 */
		factors = new Factor(d);
		System.out.println("factors: " + factors.getFactoredNumber() + " " + factors.toString());
		
		partition = new Partition(d);
		System.out.println("partitions: " + partition.getPartitionedNumber() + "  " + partition.toString());
	}
	
}
