package ecologylab.generic;

import java.lang.reflect.Field;
import java.util.Iterator;

import simpl.descriptions.FieldDescriptor;


/**
 * Iterates through a Collection of things, and then through an Iterator
 * of such (nested) Collections of things.
 * Provides flat access to all members.
 * 
 * @author jmole, damaraju
 *
 * @param <I>   Class that we iterate over.
 * @param <O>   Class of objects that are applied in the context of what we iterate over.
 *          This typically starts as this, but shifts as we iterate through 
 *          the nested Collection of Iterators.
 */
public class ClassAndCollectionIterator<I extends FieldDescriptor, O extends Iterable<I>>
implements Iterator<O>
{
  private Iterator<I> iterator;
  private Iterator<O> collectionIterator;
  private O root;
  private O currentObject;

  /**
   * 
   * @param firstObject - The object whose elements need to be iterated over.
   */
  public ClassAndCollectionIterator(O firstObject)
  {
    root = firstObject;
    this.iterator  = firstObject.iterator();
  }

  /**
   * @return The next field in the Object.<br>
   * If the next object is a non-null collection, it iterates through the objects of that collection 
   */
  @Override
public O next() 
  {
    try
    {
      if (collectionIterator != null)
        return nextInCollection();
      
      if (iterator.hasNext())
      {
        I firstNext = iterator.next(); 
        Field field = firstNext.getField();
				if(firstNext.isCollection())
        {
        	Iterable<O> collection = (Iterable<O>)field.get(root);
        	if(collection != null)
        	{
        		collectionIterator = collection.iterator();
            return nextInCollection();
        	}
        	else
        	{
        		//Collection is null ?
        		//Debug.println("next(): Collection is null");
        		return next();
        	}
        		
        }
        O next = (O) field.get(root);
        currentObject = next;
        return next;
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  private O nextInCollection() 
  {
    if (!collectionIterator.hasNext()) {
      collectionIterator = null;
      return next();
    } 
    O next = collectionIterator.next();
    currentObject = next;
    return next;
  }

  /**
   * 
   * @return
   */
  public O currentObject()
  {
    return currentObject;
  }

  @Override
public void remove() 
  {
    throw new UnsupportedOperationException();
  }

  @Override
public boolean hasNext()
  {    
    return iterator.hasNext() || (collectionIterator != null && collectionIterator.hasNext());
  }
}
