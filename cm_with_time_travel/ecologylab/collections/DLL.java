/*
 * Copyright 1996-2002 by Andruid Kerne. All rights reserved.
 * CONFIDENTIAL. Use is subject to license terms.
 */
package cm.generic;
import java.awt.*;

/**
 * Doubly linked list of GUI components. Maintains family tree.
 */
public class DLL
extends Debug
{
   private DLL			prevSib;
   private DLL			nextSib;

   private Object		object;

   public DLL(Object objectArg)
   {
      object	= objectArg;
   }
/**
 * Add sibs after ourself.
 */
   public synchronized void addAfter(DLL sibs)
   {
      sibs.setNext(nextSib);
      addToEnd(sibs);
   }
/**
 * Add sibs to the end of this.
 * 
 * @param sibs	DLL for object added to the end of this.
 */
   public void addToEnd(DLL sibs)
   {
   		if (sibs == this)
   		{
   			debug("ERROR!!!! adding to end of self!!!!");
   			Toolkit.getDefaultToolkit().beep();
   			Thread.dumpStack();
   		}
   		
      nextSib	= sibs;		   // ??? should this be setNext(sibs) -- sync
//      setNext(sibs);
      sibs.setPrev(this);
   }
/**
 * Remove I and I from the dll of sibs.
 */
   public synchronized void remove()
   {
      if (nextSib != null)
	 nextSib.setPrev(prevSib);
      if (prevSib != null)
	 prevSib.setNext(nextSib);
      prevSib	= null;
      nextSib	= null;
//      object	= null;		   // encourage gc
   }
/**
 * Remove the relations of this node, while it is being manipulated in
 * the midst of mergeSort().
 */
   public void clear()
   {
      prevSib	= null;
      nextSib	= null;
//      remove();
      object	= null;
   }
/**
 * Remove the relations of this node, while it is being deleted.
 */
   public void clearStronger()
   {
      remove();
      object	= null;
   }
/**
 * Remove the relations of this node, while it is being rebuilt, and
 * its associations are meaningless.
 */
   public void clearRelations()
   {
      prevSib	= null;
      nextSib	= null;
   }
/**
 * Not usually for public consumption. Only available for special sorts.
 * With a getKey interface, the sort could be brought here, which will
 * be better oo design.
 */
   public synchronized void setNext(DLL next)
   {
      nextSib	= next;
   }
   public synchronized void setPrev(DLL prev)
   {
      prevSib	= prev;
   }
   public Object getThis()
   {
      return object;
   }
   public synchronized Object getNext()
   {
      return (nextSib == null) ? null : nextSib.object;
   }
   public synchronized Object getPrev()
   {
      return (prevSib == null) ? null : prevSib.object;
   }
   public DLL prev()
   {
      return prevSib;
   }
   public DLL next()
   {
      return nextSib;
   }
   public DLL mergeSortDescending(MergeD mergeD, DLL zSibs)
   {
      DLL c		= this;
      int n		= 1;
      DLL head		= new DLL(null);
      head.setNext(c);
      DLL a;
      do 
      {
	 DLL todo	= head.next();
	 c		= head;
	 int toMove	= n - 1;
//	 System.out.println("");
//	 visualize.CollageElement.printPieces(c.next(), zSibs);
//	 System.out.println("start inner loop moving " + toMove);
	 do
	 {
	    // merge lists of size n
	    DLL t	= todo;
	    a		= t;
	    for (int i=1; i<=toMove; i++)
	       t	= t.next();
	    DLL b	= t.next();
	    t.setNext(zSibs);
	    t		= b;
	    for (int i=1; i<=toMove; i++)
	       t	= t.next();
	    todo	= t.next();
	    t.setNext(zSibs);
//	    System.out.println("mergeDescending(a: ");
//	    visualize.CollageElement.printPieces(a, zSibs);
//	    System.out.println("mergeDescending(b: ");
//	    visualize.CollageElement.printPieces(b, zSibs);
	    c.setNext(mergeD.mergeDescending(a,b));
//	    System.out.println("returns: ");
//	    visualize.CollageElement.printPieces(c.next(), zSibs);
	    int twoN	= n + n;
	    for (int i=1; i<=twoN; i++)
	       c	= c.next();
	 } while (todo != zSibs);
	 n	       += n;
      } while (a != head.next());

      return head.next();
   }
}
