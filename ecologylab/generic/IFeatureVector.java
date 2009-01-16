package ecologylab.generic;

import java.util.HashMap;
import java.util.Set;

public interface IFeatureVector<T>
{

	public abstract double get ( T term );

	/**
	 * Calculates the dot product of this Vector with another Vector
	 * 
	 * @param v
	 *            Vector to dot this Vector with.
	 */
	public abstract double dot ( IFeatureVector<T> v );

	public abstract double norm ( );

	public abstract Set<T> elements ( );

	public abstract Set<Double> values ( );

	public abstract HashMap<T, Double> map ( );

	public abstract IFeatureVector<T> unit ( );

	public abstract IFeatureVector<T> simplex ( );

	public abstract double dotSimplex ( IFeatureVector<T> v );

	public abstract int commonDimensions ( IFeatureVector<T> v );

}