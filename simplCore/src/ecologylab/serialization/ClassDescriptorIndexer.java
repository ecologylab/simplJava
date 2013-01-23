package ecologylab.serialization;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import ecologylab.generic.ItemIndexPredicate;
import ecologylab.generic.MultiIndexer;


public class ClassDescriptorIndexer extends MultiIndexer<ClassDescriptor<?>> {

	
	final class byTagName implements ItemIndexPredicate<ClassDescriptor<?>>
	{

		@Override
		public String GetIndexIdentifier() {
			// TODO Auto-generated method stub
			return "tagname";
		}

		@Override
		public String ObtainIndex(ClassDescriptor<?> item) {
			// TODO Auto-generated method stub
			return item.getTagName();
		}
	}
	

	final class byClassSimpleName implements ItemIndexPredicate<ClassDescriptor<?>>
	{

		@Override
		public String GetIndexIdentifier() {
			// TODO Auto-generated method stub
			return "javaclasssimplename";
		}

		@Override
		public String ObtainIndex(ClassDescriptor<?> item) {
			// TODO Auto-generated method stub
			return item.getDescribedClassSimpleName();
		}
		
	}
	
	final class byClassName implements ItemIndexPredicate<ClassDescriptor<?>>
	{

		@Override
		public String GetIndexIdentifier() {
			// TODO Auto-generated method stub
			return "javaclassname";
		}

		@Override
		public String ObtainIndex(ClassDescriptor<?> item) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	final class byTLVId implements ItemIndexPredicate<ClassDescriptor<?>>
	{

		@Override
		public String GetIndexIdentifier() {
			// TODO Auto-generated method stub
			return "tlvid";
		}

		@Override
		public String ObtainIndex(ClassDescriptor<?> item) {
			// TODO Auto-generated method stub
			return Integer.toString(item.getTagName().hashCode());
		}
		
	}
	
	final class bySimplName implements ItemIndexPredicate<ClassDescriptor<?>> {

		public bySimplName(){}
		@Override
		public String GetIndexIdentifier() {
			// TODO Auto-generated method stub
			return "simplname";
		}

		@Override
		public String ObtainIndex(ClassDescriptor<?> item) {
			// TODO Auto-generated method stub
			return item.getClassSimpleName();
		}
		
	}
	
	final class byObjectiveCName implements ItemIndexPredicate<ClassDescriptor<?>>
	{
		public byObjectiveCName(){}
		@Override
		public String GetIndexIdentifier() {
			// TODO Auto-generated method stub
			return "objc";
		}

		@Override
		public String ObtainIndex(ClassDescriptor<?> item) {
			// TODO Autofgenerated method stub
			return item.getObjectiveCTypeName();
		}
		
	}
	
	final class byBibtexType implements ItemIndexPredicate<ClassDescriptor<?>>
	{
		@Override
		public String GetIndexIdentifier() {
			// TODO Auto-generated method stub
			return "bibtextype";
		}
		

		@Override
		public String ObtainIndex(ClassDescriptor<?> item) {
			// TODO Auto-generated method stub
			return item.getBibtexType();
		}	
	}

	public List<ItemIndexPredicate<ClassDescriptor<?>>> getIndexPredicates() {
		List<ItemIndexPredicate<ClassDescriptor<?>>> ourList = new LinkedList<ItemIndexPredicate<ClassDescriptor<?>>>();
		
		ourList.add(new byObjectiveCName());
		ourList.add(new bySimplName());
		ourList.add(new byBibtexType());
		ourList.add(new byClassName());
		ourList.add(new byClassSimpleName());
		ourList.add(new byTLVId());
		ourList.add(new byTagName());
		
		//TODO: OH SHIT. MULTI INDEXING ... OTHER TAGS. MAKE INDEX RETURN LIST? :(
		return ourList;
		
	}

}
