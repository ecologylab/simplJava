package simpl.descriptions.indexers;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import simpl.core.indexers.ItemIndexPredicate;
import simpl.core.indexers.MultiIndexer;
import simpl.descriptions.ClassDescriptor;

public class ClassDescriptorIndexer extends MultiIndexer<ClassDescriptor<?>> {

	// Here are the different indexers we intend to index "by" 
	final class byClassSimpleName extends ItemIndexPredicate<ClassDescriptor<?>>
	{
		public String GetIndexIdentifier() {
			// TODO Auto-generated method stub
			return "javaclasssimplename";
		}

		public String ObtainIndex(ClassDescriptor<?> item) {
			// TODO Auto-generated method stub
			return item.getDescribedClassSimpleName();
		}
	}
	
	final class byClassName extends ItemIndexPredicate<ClassDescriptor<?>>
	{
		public String GetIndexIdentifier() {
			// TODO Auto-generated method stub
			return "javaclassname";
		}

		public String ObtainIndex(ClassDescriptor<?> item) {
			return item.getJavaTypeName();
		}
		
	}
	
	final class byTagName extends ItemIndexPredicate<ClassDescriptor<?>>
	{
		/**
		 * Gets the identifier for this index; this allows us to make calls to .by("") for the indexer.
		 * So indexer.by("tagname") lets us index by the ItemIndexPredicate identified as "tagname" 
		 * (This one.)
		 */
		public String GetIndexIdentifier() {
			return "tagname";
		}

		/**
		 * ObtainIndex for this indexer doesn't make sense because we 
		 * intend to index by BOTH the tag name and all "othertags"; thus
		 * we override ObtainIndexes and return a list of index strings for the given item.
		 */
		public String ObtainIndex(ClassDescriptor<?> item) {
			throw new RuntimeException("Obtainindex not supported for this indexer; use ObtainIndexes instead. \"byTagName\"");
		}
		
		@Override
		/**
		 * We override ObtainIndexes in this case because we intend to return multiple indexes for a given object. 
		 * In this case, we index by tag name and all "other tags" for a given class description (since they all 
		 * refer to the same class descriptor, after all!)
		 */
		public Collection<String> ObtainIndexes(ClassDescriptor<?> item) {
			Collection<String> ourIndexes = new LinkedList<String>();
			ourIndexes.add(item.getTagName());
			ourIndexes.addAll(item.otherTags());
			return ourIndexes;
		}
	}
	
	final class bySimplName extends ItemIndexPredicate<ClassDescriptor<?>> 
	{
		public String GetIndexIdentifier() {
			// TODO Auto-generated method stub
			return "simplname";
		}

		public String ObtainIndex(ClassDescriptor<?> item) {
			// TODO Auto-generated method stub
			//return item.getClassSimpleName();
			return ""; //TODO FIXME
		}
	}
	
	final class byObjectiveCName extends ItemIndexPredicate<ClassDescriptor<?>>
	{
		public String GetIndexIdentifier() {
			// TODO Auto-generated method stub
			return "objc";
		}

		public String ObtainIndex(ClassDescriptor<?> item) {
			// TODO Autofgenerated method stub
			//return item.getObjectiveCTypeName();
			return ""; //TODO FIXME
		}
	}
	
	public final class IndexingShortcut
	{
		public IndexingShortcut(ClassDescriptorIndexer cdi)
		{
			this.ObjectiveCName = cdi.by(new byObjectiveCName());
			this.SimplName = cdi.by(new bySimplName());
			this.ClassName = cdi.by(new byClassName());
			this.ClassSimpleName = cdi.by(new byClassSimpleName());
			this.TagName = cdi.by(new byTagName());
		}
		
		public InnerIndexer<ClassDescriptor<?>> ObjectiveCName;
		public InnerIndexer<ClassDescriptor<?>> SimplName;
		public InnerIndexer<ClassDescriptor<?>> ClassName;
		public InnerIndexer<ClassDescriptor<?>> ClassSimpleName;
		public InnerIndexer<ClassDescriptor<?>> TagName;
	}
	
	public IndexingShortcut by;
	
	public ClassDescriptorIndexer()
	{
		super();
		this.by = new IndexingShortcut(this);
	}
	
	public List<ItemIndexPredicate<ClassDescriptor<?>>> getIndexPredicates() {
		List<ItemIndexPredicate<ClassDescriptor<?>>> ourList = new LinkedList<ItemIndexPredicate<ClassDescriptor<?>>>();
		
		ourList.add(new byObjectiveCName());
		ourList.add(new bySimplName());
		ourList.add(new byClassName());
		ourList.add(new byClassSimpleName());
		ourList.add(new byTagName());
		
		return ourList;
	}

}
