package ecologylab.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.google.common.primitives.Ints;

public class TestFilteredIterator
{

  @Test
  public void test()
  {
    int[] a = { 1, 2, 3, 4, 5, 6 };
    List<Integer> s = Ints.asList(a);

    FilteredIterator<Integer> fi1 = new FilteredIterator<Integer>(s.iterator())
    {
      @Override
      protected boolean keepElement(Integer element)
      {
        return element % 2 == 0;
      }
    };
    assertTrue(fi1.hasNext());
    assertEquals(2, fi1.next().intValue());
    assertTrue(fi1.hasNext());
    assertEquals(4, fi1.next().intValue());
    assertTrue(fi1.hasNext());
    assertEquals(6, fi1.next().intValue());
    assertFalse(fi1.hasNext());

    FilteredIterator<Integer> fi2 = new FilteredIterator<Integer>(s.iterator())
    {
      @Override
      protected boolean keepElement(Integer element)
      {
        return element % 2 == 1;
      }
    };
    assertTrue(fi2.hasNext());
    assertEquals(1, fi2.next().intValue());
    assertTrue(fi2.hasNext());
    assertEquals(3, fi2.next().intValue());
    assertTrue(fi2.hasNext());
    assertEquals(5, fi2.next().intValue());
    assertFalse(fi2.hasNext());
  }

}
