package simpl.descriptions.indexers;

import java.util.List;

import simpl.core.indexers.ItemIndexPredicate;
import simpl.core.indexers.MultiIndexer;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.FieldType;
import java.util.List;
import java.util.LinkedList;;

public class FieldDescriptorIndexer<FD extends FieldDescriptor> extends MultiIndexer<FD>
{

	public InnerIndexer<FD> Scalars;
	public InnerIndexer<FD> ScalarCollections;
	public InnerIndexer<FD> ScalarMaps;
	public InnerIndexer<FD> Composites;
	public InnerIndexer<FD> CompositeCollections;
	public InnerIndexer<FD> CompositeMaps;
// TODO???	public InnerIndexer<FieldDescriptor> CompositesAsScalars;  //
	public InnerIndexer<FD> IgnoredElements;
	public InnerIndexer<FD> IgnoredAttributes;
	
	final class byScalars extends FieldTypeItemPredicate<FD>{
		@Override
		public FieldType getFieldType() {
			// TODO Auto-generated method stub
			return FieldType.SCALAR;
		}
	}
	
	final class byScalarCollections extends FieldTypeItemPredicate<FD>{

		@Override
		public FieldType getFieldType() {
			// TODO Auto-generated method stub
			return FieldType.COLLECTION_SCALAR;
		}
	}
	
	final class byScalarMaps extends FieldTypeItemPredicate<FD>{

		@Override
		public FieldType getFieldType() {
			// TODO Auto-generated method stub
			return FieldType.MAP_SCALAR;
		}
		
	}
	
	final class byComposites extends FieldTypeItemPredicate<FD>{

		@Override
		public FieldType getFieldType() {
			// TODO Auto-generated method stub
			return FieldType.COMPOSITE_ELEMENT;
		}
		
	}
	
	final class byCompositeCollections extends FieldTypeItemPredicate<FD>{

		@Override
		public FieldType getFieldType() {
			// TODO Auto-generated method stub
			return FieldType.COLLECTION_ELEMENT;
		}
		
	}
	
	final class byCompositeMaps extends FieldTypeItemPredicate<FD>{

		@Override
		public FieldType getFieldType() {
			// TODO Auto-generated method stub
			return FieldType.MAP_ELEMENT;
		}
		
	}
	
	final class byIgnoredAttribute extends FieldTypeItemPredicate<FD>{
		@Override
		public FieldType getFieldType(){
			return FieldType.IGNORED_ATTRIBUTE;
		}
	}
	
	final class byIgnoredElement extends FieldTypeItemPredicate<FD>{
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
	public List<ItemIndexPredicate<FD>> getIndexPredicates() {
		// TODO Auto-generated method stub
		List<ItemIndexPredicate<FD>> ourList = new LinkedList<ItemIndexPredicate<FD>>();
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
