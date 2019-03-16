package org.dwbzen.common.data;

/**
 * An AbstractDataPoint is an abstraction of a point that is an element of a larger data structure.
 * The "shape" of the point is given by rank which is the number of indices needed to specify the point.
 * A point could be a single number (scalar, rank 0), a list of numbers (vector, rank 1), 
 * a matrix (rank 2), or a tensor of rank k>=3.
 * 
 * Concrete classes specialize by rank since any container must maintain homogeneity,
 * that is all points having the same rank.
 * ScalarDataPoint<Number> - a scalar, just a Number, rank 0
 * VectorDataPoint<Number> - an n-element List<Number>. For example a point in 2-D has 2 elements
 * 		corresponding to x and y coordinates. A 3-D point has 3 elements for x, y, and z.
 * MatrixDataPoint<Number> - a rank 2 Matrix structure having m rows and n columns
 * 		implemented as List<List<Number>> where the inner list is one row.
 * TensorDataPointN<Number> - a rank N>= 3 structure implemented as a nested list.
 * 		So a rank 3 tensor is a List<MartixDataPoint>, a rank 4 is List<List<MartixDataPoint>>
 * 		and so on. A rank 4 Tensor is sufficient for working with general relativity spacetime models.
 * 		corresponding to TensorDataPoint3<Number> and TensorDataPoint4<Number>
 * 		The rank of a tensor is it's nesting level.
 * 
 * In all the above cases the data point components have the same type, namely Number.
 * This is convenient when representing points on a plane (x, y components) or
 * on a sphere (x, y and z). There are cases where it is necessary to hold a mix data types.
 * For example, a 4-D Spacetime coordinate (x, y, z, t).
 * 
 * @author don_bacon
 *
 */
public interface DataPackageInfo {

}
