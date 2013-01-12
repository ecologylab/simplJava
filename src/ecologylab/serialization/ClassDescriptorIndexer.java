package ecologylab.serialization;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import ecologylab.generic.ItemIndexPredicate;
import ecologylab.generic.MultiIndexer;


public class ClassDescriptorIndexer extends MultiIndexer<ClassDescriptor<?>> {

	final class bySimplName implements ItemIndexPredicate<ClassDescriptor<?>> {

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

	/*public List<ItemIndexPredicate<ClassDescriptor<?>>> getIndexPredicates() {
		List<ItemIndexPredicate<ClassDescriptor<?>>> ourList = new LinkedList<ItemIndexPredicate<ClassDescriptor<?>>>();
		
		ourList.add(new byObjectiveCName());
		ourList.add(new bySimplName());
		
		return ourList;
		
	}*/

}
