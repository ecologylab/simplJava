package ecologylab.collections;

import ecologylab.generic.Debug;

public abstract class WeightingStrategy<E> extends Debug
{
  private boolean changed;
  
  public abstract double getWeight(E e);
  
  public void insert(E e) {
    setChanged();
  };
  
  public void remove(E e) {
    setChanged();
  };
  
  public boolean hasChanged() {
    return changed;
  }
  
  public void setChanged() {
    synchronized(this) {
      changed = true;      
    }
  }
  
  public void clearChanged() {
    synchronized(this) {
      changed = false;
    }
  }
}
