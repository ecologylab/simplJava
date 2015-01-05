package ecologylab.generic;

/**
 * A predicate to describe indexing items for a multiindexer. 
 * @author tom
 *
 */
public interface ItemIndexPredicate<IndexedItem>{

	/**
	 * Gets the unique string used to identify items indexed by this predicate.
	 * @return
	 */
	String GetIndexIdentifier();
	/**
	 * OBtains a string index for a corresponding item. Items that are indexed in this manner will typically call some method on the indexed item to get a string.
	 * For example, indexing by a class name may require calling item.getClass().getName(). 
	 * @param item
	 * @return
	 */
	String ObtainIndex(IndexedItem item);
}
