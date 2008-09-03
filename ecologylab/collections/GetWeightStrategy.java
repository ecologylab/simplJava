package ecologylab.collections;

public interface GetWeightStrategy<E extends FloatSetElement>
{
  public float getWeight(E e);
  
  public void insert(E e);
  
  public void remove(E e);
}
