package ecologylab.collections;

public interface GetWeightStrategy<E extends FloatSetElement>
{
  
  public float getWeight(E e);

}
