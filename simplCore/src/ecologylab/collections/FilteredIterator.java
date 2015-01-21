package ecologylab.collections;

import java.util.Iterator;

/**
 * A filtered iterator.
 * 
 * @author quyin
 *
 * @param <E>
 */
public class FilteredIterator<E> implements Iterator<E>
{

  Iterator<E> iter;

  E           nextItem;

  public FilteredIterator(Iterator<E> origIter)
  {
    this.iter = origIter;
    this.nextItem = this.iter.next();
  }

  /**
   * 
   * @param element
   * @return True if element should be kept in the filtered iterator. False otherwise.
   */
  protected boolean keepElement(E element)
  {
    // by default, not filtering anything.
    return true;
  }

  @Override
  public boolean hasNext()
  {
    return nextItem != null;
  }

  @Override
  public E next()
  {
    E result = nextItem;

    while (true)
    {
      if (iter.hasNext())
      {
        E element = iter.next();
        if (keepElement(element))
        {
          nextItem = element;
          break;
        }
      }
      else
      {
        nextItem = null;
        break;
      }
    }

    return result;
  }

}
