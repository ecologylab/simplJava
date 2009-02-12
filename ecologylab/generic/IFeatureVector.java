package ecologylab.generic;

import java.util.HashMap;
import java.util.Set;

public interface IFeatureVector<T>
{
	/**
	 * 
	 * @param term
	 * @return
	 */
	public double get ( T term );

	/**
	 * Calculates the dot product of this vector with another vector.
	 * 
	 * @param v
	 *            Vector to dot this Vector with.
	 */
	public double dot ( IFeatureVector<T> v );

	/**
	 * Calculates the Euclidean norm/length of this vector. The squares of each element are added
	 * together, and the square root of the result is taken and returned.
	 * 
	 * @return The norm of the vector
	 */
	public double norm ( );
	
	/**
	 * Returns the value in the vector with the largest magnitude. 
	 * ( e.g. -5 is the max of {0,2,4,-5} ) 
	 * @return the max value in the vector
	 */
	public double max ( );

	/**
	 * The set of elements which have some value associated with them in this FeatureVector.<br />
	 * By convention, this set is not backed by the FeatureVector so it may be modified without
	 * consequence.<br/>
	 * <br/>
	 * 
	 * Other implementations should try to follow this convention as well.
	 * 
	 * @return the set of elements represented in this FeatureVector.
	 */
	public Set<T> elements ( );

	/**
	 * The set of values which have some element associated with them in this FeatureVector.<br />
	 * By convention, this set is not backed by the FeatureVector so it may be modified without
	 * consequence.<br/>
	 * <br/>
	 * 
	 * Other implementations should try to follow this convention as well.
	 * 
	 * @return the set of values represented in this FeatureVector.
	 */
	public Set<Double> values ( );

	/**
	 * A reference to the underlying HashMap backing this vector
	 * 
	 * @return the HashMap representing a particular FeatureVector
	 */
	public HashMap<T, Double> map ( );

	/**
	 * Creates a new vector equivalent to this.clamp(1).  Not a real unit vector in
	 * the euclidean sense.
	 * 
	 * @return unit length FeatureVector
	 */
	public IFeatureVector<T> unit ( );

	/**
	 * Creates a new vector with the same elements as this vector, setting all the values to 1.
	 * 
	 * @return simplex FeatureVector
	 */
	public IFeatureVector<T> simplex ( );

	/**
	 * Returns the dot product of this vector and the simplex of the passed in vector.<br />
	 * <br/>
	 * 
	 * You can the average value of all the elements in this vector which are common to
	 * both by dividing this result by the commonDimensions.
	 * 
	 * @param v
	 *            vector to simplex and dot with this vector
	 * @return
	 */
	public double dotSimplex ( IFeatureVector<T> v );

	/**
	 * Returns the number of common elements in this vector and the passed in vector
	 * 
	 * @param v
	 * @return
	 */
	public int commonDimensions ( IFeatureVector<T> v );

}