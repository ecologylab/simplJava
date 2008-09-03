package ecologylab.collections;

import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import ecologylab.generic.VectorType;

public class VectorWeightStrategy<E extends FloatSetVectorElement> implements GetWeightStrategy<E>, Observer
{
  @SuppressWarnings("unchecked")
  private Hashtable<VectorType, Double> cachedWeights = new Hashtable<VectorType, Double>();
  @SuppressWarnings("unchecked")
  VectorType referenceVector;

  @SuppressWarnings("unchecked")
  public VectorWeightStrategy(VectorType v) {
    referenceVector = v;
    v.addObserver(this);
  }

  @SuppressWarnings("unchecked")
  public float getWeight(E e) {
    VectorType termVector = e.vector();
    if (termVector == null)
      return -1;
    Hashtable<VectorType, Double> cachedWeights = this.cachedWeights;
    synchronized(cachedWeights) {
      if (cachedWeights.containsKey(termVector))
        return cachedWeights.get(termVector).floatValue();
      double weight = termVector.idfDot(referenceVector);
      cachedWeights.put(termVector, weight);
      termVector.setWeight(weight);
      return (float)weight;
    }
  }

  public void insert(E e) {
    if (e.vector() != null)
      e.vector().addObserver(this);
  }

  public void remove(E e) {
    if (e.vector() != null)
      e.vector().deleteObserver(this);
  }

  @SuppressWarnings("unchecked")
  public void update(Observable o, Object arg) {
    Hashtable<VectorType, Double> cachedWeights = this.cachedWeights;
    synchronized(cachedWeights) {
      if (o == referenceVector)
        cachedWeights.clear();
      else
        cachedWeights.remove(o);
    }
  }

}