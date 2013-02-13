package simpl.core.indexers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * A class that indexes inserted objects by a set of multiple criteria. implementors provide the critera in the form of 
 * ItemIndexer predicates, marshalling happens via .by();
 * @author tom
 *
 */
public abstract class MultiIndexer<IndexedObject> implements Iterable<IndexedObject>{

	public final class InnerIndexer<IndexedObject> implements Iterable<IndexedObject>
	{		
		Map<String, IndexedObject> ourMap;
		
		public InnerIndexer(Map<String, IndexedObject> theMap)
		{
			this.ourMap = theMap;
		}
		
		public IndexedObject get(String indexString)
		{
			return this.ourMap.get(indexString);
		}
		
		public boolean contains(String indexString)
		{
			return this.ourMap.containsKey(indexString);
		}
		
		@Override
		public Iterator< IndexedObject > iterator() {
			// TODO Auto-generated method stub
			return this.ourMap.values().iterator();
		}
		
		public int size()
		{
			return this.ourMap.entrySet().size();
		}
	}
	
	private HashMap<String, HashMap<String, IndexedObject>> allmaps;
	private List<IndexedObject> allitems;
	
	public MultiIndexer()
	{
		this.allitems = new ArrayList<IndexedObject>(); // Not sure about this one... may revise. 
		// This is probably better as a set, but I can't decide at the moment. 
		this.allmaps  = new HashMap<String, HashMap<String, IndexedObject>>();
		
		// Initialize all of the maps in the allmaps.
		for(ItemIndexPredicate<IndexedObject> index : this.getIndexPredicates())
		{
			String indexID = index.GetIndexIdentifier();
			
			if(this.allmaps.containsKey(indexID))
			{
				throw new RuntimeException("Should not have multiple indexers of the same identifer, please check your indexers and try again.");
			}else{
				// Put a new map at the approrpiate index. 
				this.allmaps.put(indexID, new HashMap<String, IndexedObject>());
			}
		}
	}
	
	/**
	 * Obtains the list of index predicates that implementors of the MultiIndexer desire.
	 * Guides the indexing process..
	 * @return 
	 */
	public abstract List<ItemIndexPredicate<IndexedObject>> getIndexPredicates();
	
	/**
	 * Inserts an item into the MultiIndexer
	 * @param object
	 */
	public void Insert(IndexedObject object)
	{
		this.allitems.add(object);
		
		for(ItemIndexPredicate<IndexedObject> index : this.getIndexPredicates())
		{
			String indexID = index.GetIndexIdentifier();
			
			Collection<String> indexValues = index.ObtainIndexes(object);
			
			// We can index something by multiple values; consider "other tags"
			for(String indexValue : indexValues)
			{
				if(!indexValue.isEmpty())
				{
					this.allmaps.get(indexID).put(indexValue, object);
				}
			}
		}
	}
	
	/**
	 * Removes a given item from the Multiindexer
	 * @param object
	 */
	public void Remove(IndexedObject object)
	{
		this.allitems.remove(object);
		
		for(ItemIndexPredicate<IndexedObject> index : this.getIndexPredicates())
		{
			String indexID = index.GetIndexIdentifier();
			
			Collection<String> indexValues = index.ObtainIndexes(object);
			
			for(String indexValue : indexValues)
			{
				if(!indexValue.isEmpty())
				{
					this.allmaps.get(indexID).remove(indexValue);
				}
			}
		}
	}
	
	/**
	 * Takes the contents of another indexer and merges it into this one.
	 * @param otherIndexer
	 */
	public void mergeIn(MultiIndexer<IndexedObject> otherIndexer)
	{
		for(IndexedObject obj : otherIndexer)
		{
			this.Insert(obj);
		}
	}
	
	public InnerIndexer<IndexedObject> by(String indexID)
	{
		return new InnerIndexer<IndexedObject>(this.allmaps.get(indexID));
	}
	
	protected InnerIndexer<IndexedObject> by(ItemIndexPredicate<IndexedObject> predicate)
	{
		return this.by(predicate.GetIndexIdentifier());
	}

	public Integer size() 
	{
		return this.allitems.size();
	}
	
	@Override
	public Iterator<IndexedObject> iterator() 
	{
		return this.allitems.iterator();
	}
	
	public Collection<IndexedObject> getAllItems()
	{
		return this.allitems;
	}
}
