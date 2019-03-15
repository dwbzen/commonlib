package common.math;

import java.util.ArrayList;
import java.util.List;

import common.util.INameable;
import common.util.Index;

public class StringMatrix implements INameable {

	private static final long serialVersionUID = -6465670794003676185L;
	private int rows = 0;
	private int columns = 0;
	private int rank = 2;
	private int origin = 0;
	
	private String name = EMPTY_STRING;	// optional name used for display, maps etc.
	private List<List<String>> matrix = null;
	private static int[] tempind1 =  {3, 0, 1, 2};
	private static int[] tempind2 = {1, 2, 3, 0};

	public static String BLANK = " ";
	public static String EMPTY_STRING = "";
	
	public static void main(String[] args) {
		StringMatrix sm = new StringMatrix(5,5);
		sm.setOrigin(1);
		sm.fill();
		//sm.show();
		
		sm.rotateCell(1, 1, 1);
		//sm.show();
		sm.rotateCell(1, 1, -1);
		//sm.show();
		
		StringMatrix puzzle = new StringMatrix(5,5);
		puzzle.set(1, 1, "S");
		puzzle.set(1, 2, "D");
		puzzle.set(1, 3, "I");
		puzzle.set(2, 1, "M");
		puzzle.set(2, 2, "A");
		puzzle.set(2, 3, "I");
		puzzle.set(3, 1, "M");
		puzzle.set(3, 2, "C");
		puzzle.set(3, 3, "N");
		puzzle.show();
		
		puzzle.rotateCell(2, 2);
		puzzle.show();
		puzzle.rotateCell(2, 2);
		puzzle.show();
		puzzle.rotateCell(3, 1);
		puzzle.show();
	}
	
	public StringMatrix() {
		createNew();
	}

	public StringMatrix(int nrows, int ncols) {
		rows = nrows;
		columns = ncols;
		createNew();
	}

	protected void createNew() {
		matrix = new ArrayList<List<String>>();
		for(int r=0; r<rows; r++) {
			ArrayList<String> arow = new ArrayList<String>();
			for(int c=0; c<columns; c++) { arow.add(EMPTY_STRING); }
			matrix.add(arow);
		}
	}

	public void fill(String s) {
		for(int r=0; r<rows; r++) {
			List<String> row = matrix.get(r);
			for(int c=0; c<columns; c++) {
				row.set(c, s);
			}
		}	
	}
	
	public void fill() {
		for(int r=0; r<rows; r++) {
			List<String> row = matrix.get(r);
			for(int c=0; c<columns; c++) {
				String bd = String.valueOf(origin + c + r*columns);
				row.set(c, bd);
			}
		}	
	}
	
	public void clear() {
		fill(EMPTY_STRING);
	}
	
	public String get(int r, int c) {
		return row(r).get(c);
	}
	
	public String get(Index ind) {
		if(!ind.isValid()) {
			throw new java.lang.ArrayIndexOutOfBoundsException(ind.toString());
		}
		return get(ind.getX(), ind.getY());
	}
	
	public void set(int r, int c, String s) {
		matrix.get(r).set(c, s);
	}
	
	public void set(Index ind, String s) {
		if(!ind.isValid()) {
			throw new java.lang.ArrayIndexOutOfBoundsException(ind.toString());
		}
		set(ind.getX(), ind.getY(), s);
	}
	
	public List<String> column(int columnNumber) {
		List<String> column = new ArrayList<String>();
		for(int i=0;i<rows; i++) {
			column.add(matrix.get(i).get(columnNumber));
		}
		return column;
	}
	
	public List<String> row(int rowNumber) {
		return matrix.get(rowNumber);
	}
	
	public void rotateCell(int row, int col) {
		rotateCell(row, col, 1);
	}
	
	public void rotateCell(int row, int col, int amount) {
		int n = Math.abs(amount);
		List<Index> indicies = new ArrayList<Index>();
		int[] tempind = (amount > 0) ? tempind1 : tempind2;
		indicies.add(new Index(row-1, col));	// these are in clockwise order
		indicies.add(new Index(row, col+1));
		indicies.add(new Index(row+1, col));
		indicies.add(new Index(row, col-1));
		for(int nr=0; nr<n; nr++) {
			String[] temp = new String[4];
			int ind = 0;
			for(Index index : indicies) {
				temp[ind++] = index.isValid() ? get(index) : EMPTY_STRING;
			}
			for(int i=0; i<4; i++) {
				Index index = indicies.get(i);
				if(index.isValid())
					set(index, temp[tempind[i]]);
			}
		}
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if(getName() != null) {
			sb.append(getName() + "\n");
		}
		for(int r=0; r<rows; r++) {
			sb.append(r + ": [ ");
			List<String> row = matrix.get(r);
			for(int c=0; c<columns; c++) {
				sb.append(row.get(c));
				if(c+1 < columns) {
					sb.append(" ");
				}
			}
			sb.append(" ]\n");
		}
		return sb.toString();
	}
	
	public void show() {
		System.out.println(toString());
	}
	
	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRank() {
		return rank;
	}

	public int getOrigin() {
		return origin;
	}

	public void setOrigin(int origin) {
		this.origin = origin;
	}
	
}
