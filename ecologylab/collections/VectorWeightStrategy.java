package ecologylab.collections;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import ecologylab.generic.VectorType;


@SuppressWarnings("unchecked")
public class VectorWeightStrategy<E extends VectorSetElement> extends WeightingStrategy<E> implements Observer
{
  
  private Hashtable<VectorType, Double> cachedWeights = new Hashtable<VectorType, Double>();
  private VectorType referenceVector;

  public VectorWeightStrategy(VectorType v) {
    referenceVector = v;
    v.addObserver(this);
  }

  public double getWeight(E e) {
    VectorType termVector = e.vector();
    double weight = -1;
    if (termVector != null) {
      Hashtable<VectorType, Double> cachedWeights = this.cachedWeights;
      synchronized(cachedWeights) {
        if (cachedWeights.containsKey(termVector))
          weight = cachedWeights.get(termVector).floatValue();
        else {
          weight = termVector.idfDot(referenceVector);
          cachedWeights.put(termVector, weight);
        }
      }
    }
    if (weight > 0)
      termVector.setWeight(weight);
    e.weight = weight;
    return weight;
  }

  public void insert(E e) {
    if (e.vector() != null)
      e.vector().addObserver(this);
    super.insert(e);
  }

  public void remove(E e) {
    if (e.vector() != null)
      e.vector().deleteObserver(this);
    super.remove(e);
  }

  
  public void update(Observable o, Object arg) {
    Hashtable<VectorType, Double> cachedWeights = this.cachedWeights;
    synchronized(cachedWeights) {
      if (o == referenceVector)
        cachedWeights.clear();
      else
        cachedWeights.remove(o);
    }
    setChanged();
  }

  
}