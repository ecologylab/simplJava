package simpl.descriptions.indexers;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import simpl.core.indexers.ItemIndexPredicate;
import simpl.core.indexers.MultiIndexer;
import simpl.descriptions.ClassDescriptor;

public class ClassDescriptorIndexer extends MultiIndexer<ClassDescriptor> {

	
	public final class IndexingShortcut
	{
		public IndexingShortcut(ClassDescriptorIndexer cdi)
		{


		}
		
	}
	
	public IndexingShortcut by;
	
	public ClassDescriptorIndexer()
	{
		super();
		this.by = new IndexingShortcut(this);
	}
	
	public List<ItemIndexPredicate<ClassDescriptor>> getIndexPredicates() {
		List<ItemIndexPredicate<ClassDescriptor>> ourList = new LinkedList<ItemIndexPredicate<ClassDescriptor>>();
		

		
		return ourList;
	}

}
