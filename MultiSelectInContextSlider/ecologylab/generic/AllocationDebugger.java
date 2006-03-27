package ecologylab.generic;

import java.util.*;

/**
 * Count allocations and finalizations of any class.
 */
public class AllocationDebugger
{
   static HashMap	stats		= new HashMap(100);

   static Comparator	comparator	= new EntryComparator();

   public final static void constructed(Object thatObject)
   {
      constructed(thatObject.getClass());
   }
   public final static void constructed(Class thatClass)
   {
      Entry entry	= getEntry(thatClass);
      entry.constructed++;
   }
   public final static void finalized(Object thatObject)
   {
      finalized(thatObject.getClass());
   }
   public final static void finalized(Class thatClass)
   {
      Entry entry	= getEntry(thatClass);
      entry.finalized++;
   }
   static final Entry getEntry(Class thatClass)
   {
      Entry	entry	= (Entry) stats.get(thatClass);
      if (entry == null)
      {
	 entry		= new Entry(thatClass);
	 stats.put(thatClass, entry);
      }
      return entry;
   }
   public static String getStats()
   {
      String result	= "AllocationDebugger\n==================\n";

      Collection values	= stats.values();
      ArrayList valuesList	= new ArrayList(values);
      Collections.sort(valuesList, comparator);
      
      for (int i=0; i<valuesList.size(); i++)
      {
	 Entry thatEntry= (Entry) valuesList.get(i);
	 if (thatEntry != null)
	 {  
	    if (thatEntry.remaining() < 20)
	       break;		   // dont bother with these punks
	    
	    // pretty print it
	    String name		= thatEntry.name;
	    result	       += name;
	    int nameLength	= name.length();
	    if (nameLength < 16)
	       result	       += "\t\t\t";
	    else if (nameLength < 24)
	       result	       += "\t\t";
	    else
	       result	       += "\t";
	    result += thatEntry.constructed + "\t"+ thatEntry.finalized + "\n";
	 }
      }
      return result;
   }
   public static void getStats(StringBuffer buffy)
   {
      buffy.append("\nAllocationDebugger\n==================\n");

      Collection values	= stats.values();
      ArrayList valuesList	= new ArrayList(values);
      Collections.sort(valuesList, comparator);
      
      for (int i=0; i<valuesList.size(); i++)
      {
	 Entry thatEntry= (Entry) valuesList.get(i);
	 if (thatEntry != null)
	 {  
	    if (thatEntry.remaining() < 20)
	       break;		   // dont bother with these punks
	    
	    // pretty print it
	    String name		= thatEntry.name;
	    buffy.append(name);
	    int nameLength	= name.length();
	    if (nameLength < 16)
	       buffy.append("\t\t\t");
	    else if (nameLength < 24)
	       buffy.append("\t\t");
	    else
	       buffy.append("\t");
	    buffy.append(thatEntry.constructed).append("\t")
		 .append(thatEntry.finalized).append("\n");
	 }
      }
   }
}
class Entry
{
   String	name;
   int		constructed;
   int		finalized;

   Entry(Class thatClass)
   {
      String className	= thatClass.toString();
      name		= className.substring(9);
   }
   int remaining()
   {
      return constructed - finalized;
   }
}
class EntryComparator
implements Comparator
{
   public int compare(Object o1, Object o2)  
   {	
      Entry e1	= (Entry) o1;
      Entry e2	= (Entry) o2;
      
      int r1	= e1.remaining();
      int r2	= e2.remaining();
      
      return (r1 == r2) ? 0 : ((r1 > r2) ? -1 : 1);
   }
}
