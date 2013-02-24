package simpl.types;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import simpl.core.indexers.ItemIndexPredicate;
import simpl.core.indexers.MultiIndexer;

public class ScalarTypeIndexer extends MultiIndexer<ScalarType> {

	public ScalarTypeIndexer()
	{
		super();
		this.by = new InnerIndexingShortcut();
	}
	
	final class byScalarTypeName extends ItemIndexPredicate<ScalarType>
	{

		@Override
		public String GetIndexIdentifier() {
			// TODO Auto-generated method stub
			return "scalartypename";
		}

		@Override
		public String ObtainIndex(ScalarType item) {
			// TODO Auto-generated method stub
			return item.getClass().getSimpleName();
		}
		
	}
	
	public InnerIndexingShortcut by;
	
	public class InnerIndexingShortcut
	{
		public InnerIndexingShortcut()
		{
			this.scalarTypeName = by("scalartypename");
		}
		
		public InnerIndexer<ScalarType> scalarTypeName;
	}
	
	
	final class byJavaClassName extends ItemIndexPredicate<ScalarType>
	{
		@Override
		public String GetIndexIdentifier() {
			// TODO Auto-generated method stub
			return "javaClassName";
		}

		@Override
		public String ObtainIndex(ScalarType item) {
			// This is a multi-indexed item, so we override ObtainIndexes instead. 
			return null;
		}
		
		@Override
		public Collection<String> ObtainIndexes(ScalarType item)
		{
			Collection<Class<?>> supportedClasses = item.getSupportedTypes();
			
			Collection<String> ourCollection = new LinkedList<String>();
			for(Class<?> lass : supportedClasses)
			{
				ourCollection.add(lass.getName());
			}
			return ourCollection;
		}
	}
	
	@Override
	public List<ItemIndexPredicate<ScalarType>> getIndexPredicates() {
		List<ItemIndexPredicate<ScalarType>> ourList = new LinkedList<ItemIndexPredicate<ScalarType>>();
		ourList.add(new byJavaClassName());
		ourList.add(new byScalarTypeName());
		return ourList;
	}

	/**
	 * Overridden contains method to see if a java class is within the className indexer.
	 * @param javaClass
	 * @return
	 */
	public boolean contains(Class<?> javaClass)
	{
		return this.by("javaClassName").contains(javaClass.getName());
	}
	
	public ScalarType get(Class<?> javaClass)
	{
		return this.by("javaClassName").get(javaClass.getName());
	}
}
