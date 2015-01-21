package ecologylab.collections;

import java.util.Iterator;

/**
 * A filtered iterator.
 * 
 * @author quyin
 *
 * @param <E>
 */
public abstract class FilteredIterator<E> implements Iterator<E>
{

  Iterator<E> iter;

  E           nextItem;

  public FilteredIterator(Iterator<E> origIter)
  {
    super();
    this.iter = origIter;
    findNextItem();
  }

  /**
   * 
   * @param element
   * @return True if element should be kept in the filtered iterator. False otherwise.
   */
  abstract protected boolean keepElement(E element);

  @Override
  public boolean hasNext()
  {
    return nextItem != null;
  }

  @Override
  public E next()
  {
    E result = nextItem;

    findNextItem();

    return result;
  }

  private void findNextItem()
  {
    nextItem = null;
    while (iter.hasNext())
    {
      E element = iter.next();
      if (keepElement(element))
      {
        nextItem = element;
        return;
      }
    }
  }

}
