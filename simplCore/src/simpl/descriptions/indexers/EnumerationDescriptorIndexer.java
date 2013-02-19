package simpl.descriptions.indexers;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import simpl.core.indexers.ItemIndexPredicate;
import simpl.core.indexers.MultiIndexer;
import simpl.descriptions.EnumerationDescriptor;

public class EnumerationDescriptorIndexer extends MultiIndexer<EnumerationDescriptor> {

	final class byTagName extends ItemIndexPredicate<EnumerationDescriptor>
	{
		public String GetIndexIdentifier() {
			// TODO Auto-generated method stub
			return "tagname";
		}

		/**
		 * ObtainIndex for this indexer doesn't make sense because we 
		 * intend to index by BOTH the tag name and all "othertags"; thus
		 * we override ObtainIndexes and return a list of index strings for the given item.
		 */
		public String ObtainIndex(EnumerationDescriptor item) {
			throw new RuntimeException("Obtain index not supported for this indexer; use ObtainIndexes instead. \"byTagName\"");
		}
		
		/**
		 * We override OBtainIndexes in this case because we intend to return multiple indexes for a given object. 
		 * In this case, we index by tag name and all "other tags" for a given class description.
		 */
		@Override
		public Collection<String> ObtainIndexes(EnumerationDescriptor item) {
			Collection<String> ourIndexes = new LinkedList<String>();
			
//			ourIndexes.add(item.getTagName());
			for(String s : item.otherTags())
			{
				ourIndexes.add(s);
			}
			
			return ourIndexes;
		}
	}
	
	public final class IndexingShortcut
	{
		public IndexingShortcut(EnumerationDescriptorIndexer edi)
		{
			this.TagName = edi.by(new byTagName());
		}
		
		public InnerIndexer<EnumerationDescriptor> TagName;
	}
	
	public IndexingShortcut by;
	
	public EnumerationDescriptorIndexer()
	{
		this.by = new IndexingShortcut(this);
	}
	
	@Override
	public List<ItemIndexPredicate<EnumerationDescriptor>> getIndexPredicates() {

		List<ItemIndexPredicate<EnumerationDescriptor>> ourList = new LinkedList<ItemIndexPredicate<EnumerationDescriptor>>();
		ourList.add(new byTagName());
		return ourList;
	}

}
