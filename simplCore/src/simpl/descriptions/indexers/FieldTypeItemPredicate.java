package simpl.descriptions.indexers;

import simpl.core.indexers.ItemIndexPredicate;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.FieldType;

public abstract class FieldTypeItemPredicate extends ItemIndexPredicate<FieldDescriptor> {

	public abstract FieldType getFieldType();
	
	@Override
	public String GetIndexIdentifier() {
		// TODO Auto-generated method stub
		return "type_"+getFieldType().toString();
	}

	@Override
	public String ObtainIndex(FieldDescriptor item) {
		if(item.getFieldType().equals(getFieldType()))
		{
			return item.getName();
		}else{
			return ""; // no string will make it so that this doesn't get indexed by this indexer. 
		}
	}
	
}
