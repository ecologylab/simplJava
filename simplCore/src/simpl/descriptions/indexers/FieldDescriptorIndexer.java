package simpl.descriptions.indexers;

import java.util.List;

import simpl.core.indexers.ItemIndexPredicate;
import simpl.core.indexers.MultiIndexer;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.FieldType;
import simpl.descriptions.beiber.IFieldDescriptor;

import java.util.List;
import java.util.LinkedList;;

public class FieldDescriptorIndexer extends MultiIndexer<IFieldDescriptor>
{
	public InnerIndexer<IFieldDescriptor> Scalars;
	public InnerIndexer<IFieldDescriptor> ScalarCollections;
	public InnerIndexer<IFieldDescriptor> ScalarMaps;
	public InnerIndexer<IFieldDescriptor> Composites;
	public InnerIndexer<IFieldDescriptor> CompositeCollections;
	public InnerIndexer<IFieldDescriptor> CompositeMaps;
// TODO???	public InnerIndexer<FieldDescriptor> CompositesAsScalars;  //
	public InnerIndexer<IFieldDescriptor> IgnoredElements;
	public InnerIndexer<IFieldDescriptor> IgnoredAttributes;
	
	final class byScalars extends FieldTypeItemPredicate{
		@Override
		public FieldType getFieldType() {
			// TODO Auto-generated method stub
			return FieldType.SCALAR;
		}
	}
	
	final class byScalarCollections extends FieldTypeItemPredicate{

		@Override
		public FieldType getFieldType() {
			// TODO Auto-generated method stub
			return FieldType.COLLECTION_SCALAR;
		}
	}
	
	final class byScalarMaps extends FieldTypeItemPredicate{

		@Override
		public FieldType getFieldType() {
			// TODO Auto-generated method stub
			return FieldType.MAP_SCALAR;
		}
		
	}
	
	final class byComposites extends FieldTypeItemPredicate{

		@Override
		public FieldType getFieldType() {
			// TODO Auto-generated method stub
			return FieldType.COMPOSITE_ELEMENT;
		}
		
	}
	
	final class byCompositeCollections extends FieldTypeItemPredicate{

		@Override
		public FieldType getFieldType() {
			// TODO Auto-generated method stub
			return FieldType.COLLECTION_ELEMENT;
		}
		
	}
	
	final class byCompositeMaps extends FieldTypeItemPredicate{

		@Override
		public FieldType getFieldType() {
			// TODO Auto-generated method stub
			return FieldType.MAP_ELEMENT;
		}
		
	}
	
	final class byIgnoredAttribute extends FieldTypeItemPredicate{
		@Override
		public FieldType getFieldType(){
			return FieldType.IGNORED_ATTRIBUTE;
		}
	}
	
	final class byIgnoredElement extends FieldTypeItemPredicate{
		@Override
		public FieldType getFieldType(){
			return FieldType.IGNORED_ELEMENT;
		}
	}
	
	// -- here are the other indexers, by tag name, etc. 
	
	
	final class byFieldName extends ItemIndexPredicate<FieldDescriptor>
	{

		@Override
		public String GetIndexIdentifier() {
			// TODO Auto-generated method stub
			return "fieldName";
		}

		@Override
		public String ObtainIndex(FieldDescriptor item) {
			// TODO Auto-generated method stub
			return item.getField().getName();
		}
	}
	
	final class byTagName extends ItemIndexPredicate<FieldDescriptor>
	{

		@Override
		public String GetIndexIdentifier() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String ObtainIndex(FieldDescriptor item) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	
	public FieldDescriptorIndexer()
	{
	// These are all of the type indexers... for use in the de/serialization process.
		this.ScalarCollections = this.by(new byScalarCollections());
		this.Scalars = this.by(new byScalars());
		this.ScalarMaps = this.by(new byScalarMaps());
		this.Composites = this.by(new byComposites());
		this.CompositeCollections = this.by(new byCompositeCollections());
		this.CompositeMaps = this.by(new byCompositeMaps());
		this.IgnoredAttributes = this.by(new byIgnoredAttribute());
		this.IgnoredElements = this.by(new byIgnoredElement());
		
	}
	
	@Override
	public List<ItemIndexPredicate<IFieldDescriptor>> getIndexPredicates() {
		// TODO Auto-generated method stub
		List<ItemIndexPredicate<IFieldDescriptor>> ourList = new LinkedList<ItemIndexPredicate<IFieldDescriptor>>();
		ourList.add(new byScalars());
		ourList.add(new byScalarCollections());
		ourList.add(new byScalarMaps());
		ourList.add(new byComposites());
		ourList.add(new byCompositeCollections());
		ourList.add(new byCompositeMaps());
		ourList.add(new byIgnoredAttribute());
		ourList.add(new byIgnoredElement());
		return ourList;
	}
}
