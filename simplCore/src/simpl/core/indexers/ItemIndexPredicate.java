package simpl.core.indexers;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A predicate to describe indexing items for a multiindexer. 
 * @author tom
 *
 */
public abstract class ItemIndexPredicate<IndexedItem>{

	/**
	 * Gets the unique string used to identify items indexed by this predicate.
	 * @return
	 */
	public abstract String GetIndexIdentifier();
	/**
	 * OBtains a SINGLE string index for a corresponding item. Items that are indexed in this manner will typically call some method on the indexed item to get a string.
	 * For example, indexing by a class name may require calling item.getClass().getName(). 
	 * By default, this method is called by "ObtainIndexes" and wrapped as a single-element list. 
	 * Indexers that must return multiple equivilant indexes should have an empty implementation of obtainIndex and 
	 * override ObtainIndexes;
	 * @param item
	 * @return
	 */
	public abstract String ObtainIndex(IndexedItem item);
	
	public Collection<String> ObtainIndexes(IndexedItem item)
	{
		Collection<String> ourCollection = new LinkedList<String>();
		String index = ObtainIndex(item);
		if(index != null || !index.isEmpty())
		{
			ourCollection.add(index);
		}
		return ourCollection;
	}
	
}
