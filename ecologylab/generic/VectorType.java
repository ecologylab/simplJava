package ecologylab.generic;

import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public abstract class VectorType<T> extends Observable {

	public abstract double get(T term);

	/**
	 * Calculates the dot product of this Vector with another Vector
	 * @param v Vector to dot this Vector with.
	 */
	public abstract double dot(VectorType<T> v);
	
	public abstract double idfDot(VectorType<T> v);
	
	public abstract double norm();

	public abstract Set<T> elements();

	public abstract Set<Double> values();
	
	public abstract Hashtable<T,Double> map();

	@Deprecated
	public double weight = -1;

	@Deprecated
  public double getWeight()
  {
    return weight;
  }
	@Deprecated
  public void setWeight(double weight)
  {
    this.weight = weight;
  }
	
}