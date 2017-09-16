package math;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Transient;
import com.mongodb.DBObject;

@Entity(value="matrix", noClassnameStored=false)
public class Matrix<T extends Number> extends AbstractArray<Number> implements Serializable  {

	private static final long serialVersionUID = -212209984266289457L;
	@Property("rows")	protected int rows = 0;
	@Property("cols")	protected int columns = 0;
	@Property("array")	protected List<List<Number>> matrix = null;
	
	@Transient	private MathContext mathContext = MathContext.DECIMAL32;	// the default
	@Transient	private Morphia morphia = null;
	
	public static void main(String[] args) {
		Matrix<BigDecimal> matrix = new Matrix<BigDecimal>(3, 4, "A");
		Matrix<BigDecimal> mb = new Matrix<BigDecimal>(4, 2, "B");
		matrix.fill();
		mb.fill();
		
		System.out.print(matrix);
		System.out.print(mb);
		
		Matrix <Number> prod = matrix.times(mb);
		prod.setName("AxB");
		System.out.println(prod);

		System.out.println(prod.toJSON());
	}
	
	public Matrix() {
		createNew();
	}

	public Matrix(int nrows, int ncols) {
		rows = nrows;
		columns = ncols;
		createNew();
	}
	public Matrix(int nrows, int ncols, String name) {
		this(nrows, ncols);
		setName(name);
	}
	public Matrix(int ncols) {
		columns = ncols;
		matrix = new ArrayList<List<Number>>();
	}
	
	public Matrix(Matrix<? extends Number> other) {
		rows = other.rows;
		columns = other.columns;
		matrix = new ArrayList<List<Number>>();
		for(int i=0; i<rows; i++) {
			ArrayList<Number> arow = new ArrayList<Number>();
			for(int j=0; j<columns; j++) { arow.add(other.index(i, j)); }
			matrix.add(arow);
		}		
	}
	
	/**
	 * Gets the column index of the maximum value in a row.
	 * In case of duplicates, it returns the highest column index.
	 * @param irow
	 * @return int column index
	 */
	public int getMaxValueInRow(int irow) {
		int index = 0;
		int currentIndex = 0;
		Number currentMax = -Double.MIN_VALUE;
		for(Number num : row(irow)) {
			if(num.doubleValue() >= currentMax.doubleValue()) {
				currentMax = num;
				index = currentIndex;
			}
			currentIndex++;
		}
		return index;
	}
	
	/**
	 * Gets the column index of the maximum value in a row.
	 * In case of duplicates, it returns the highest column index.
	 * @param irow
	 * @return int column index
	 */
	public int getMinValueInRow(int irow) {
		int index = 0;
		int currentIndex = 0;
		Number currentMin = Double.MAX_VALUE;
		for(Number num : row(irow)) {
			if(num.doubleValue() <= currentMin.doubleValue()) {
				currentMin = num;
				index = currentIndex;
			}
			currentIndex++;
		}
		return index;
	}
	/**
	 * Returns the column index of the value provided or -1 if not present
	 * @param value
	 * @param irow
	 * @return int column index
	 */
	public int indexOf(Number value, int irow) {
		return row(irow).indexOf(value);
	}
	
	
	public void clear() {
		fill(BigInteger.ZERO);
	}
	
	public void fill(Number n) {
		for(int r=0; r<rows; r++) {
			List<Number> row = matrix.get(r);
			for(int c=0; c<columns; c++) {
				Number bd = new BigDecimal(n.doubleValue(), mathContext);
				row.set(c, bd);
			}
		}	
	}
	protected void fill(double d) {
		fill(new BigDecimal(d));
	}
	public void fill() {
		for(int r=0; r<rows; r++) {
			List<Number> row = matrix.get(r);
			for(int c=0; c<columns; c++) {
				Number bd = new BigDecimal(c + r*columns, mathContext);
				row.set(c, bd);
			}
		}	
	}
	protected void createNew() {
		matrix = new ArrayList<List<Number>>();
		for(int r=0; r<rows; r++) {
			ArrayList<Number> arow = new ArrayList<Number>();
			for(int c=0; c<columns; c++) { arow.add(BigInteger.ZERO); }
			matrix.add(arow);
		}
	}
	
	public List<Number> column(int columnNumber) {
		List<Number> column = new ArrayList<Number>();
		for(int i=0;i<rows; i++) {
			column.add(matrix.get(i).get(columnNumber));
		}
		return column;
	}
	
	public List<Number> row(int rowNumber) {
		return matrix.get(rowNumber);
	}
	
	/**
	 * Gets the element at this(i,j)
	 * @param i row
	 * @param j column
	 * @return Number
	 */
	public Number index(int irow, int icol) {
		return row(irow).get(icol);
	}
	
	public void setValue(int irow, int icol, Number element) {
		row(irow).set(icol, new BigDecimal(element.doubleValue(), mathContext));
	}
	
	public void addRow(double[] drow) {
		List<Number> rlist = new ArrayList<Number>();
		for(int i=0; i<drow.length;i++) { rlist.add(new BigDecimal(drow[i], mathContext)); }
		matrix.add(rlist);
		rows++;
	}
	
	public String toJSON() {
		if(morphia == null) {
			morphia = new Morphia();
		}
		DBObject dbo = morphia.toDBObject(this);
		return dbo.toString();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("{");
		for(int r=0; r<rows; r++) {
			sb.append( " [ ");
			List<Number> row = matrix.get(r);
			for(int c=0; c<columns; c++) {
				sb.append(row.get(c));
				if(c+1 < columns) {
					sb.append(", ");
				}
			}
			sb.append(" ]");
			if(r+1 < rows) {
				sb.append(",");
			}
		}
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * this(r, c) x B(rb, cb) -> C(r, cb)
	 * The inner dimensions of this and B must match.
	 * Precondition: c == rb
	 * @param other
	 * @return a new Matrix which is this x other
	 */
	public Matrix<Number> times(Matrix<? extends Number> other) throws  IllegalArgumentException {
		// this x other
		// assert this.columns == other.rows
		if(columns != other.rows) {
			throw new IllegalArgumentException("assertion failed: this.columns != other.rows");
		}
		Matrix<Number> result = new Matrix<Number>(rows, other.columns);
		for(int tr = 0; tr<rows; tr++) {
			List<Number> trow = matrix.get(tr);
			for(int ocol=0; ocol<other.columns; ocol++) {
				
				double sum = 0;
				for(int i=0; i<trow.size(); i++) {
					sum += trow.get(i).doubleValue() * other.index(i, ocol).doubleValue();
				}
				result.setValue(tr, ocol, new BigDecimal(sum, mathContext));
			}
		}
		return result;
	}

	public Matrix<Number> times(Number element) {
		Matrix<Number> result = new Matrix<Number>(this);
		double d = element.doubleValue();
		for(int i = 0; i<rows; i++) {
			for(int j = 0; j<columns; j++) {
				result.setValue(i, j, d * index(i,j).doubleValue());
			}
		}
		return result;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
}

