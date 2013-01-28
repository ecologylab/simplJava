package simpl.descriptions.indexers;

import simpl.core.indexers.ItemIndexPredicate;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.FieldType;

public abstract class FieldTypeItemPredicate<FD extends FieldDescriptor> extends ItemIndexPredicate<FD> {

	public abstract FieldType getFieldType();
	
	@Override
	public String GetIndexIdentifier() {
		// TODO Auto-generated method stub
		return "type_"+getFieldType().toString();
	}

	@Override
	public String ObtainIndex(FD item) {
		if(item.getType().equals(getFieldType()))
		{
			return item.getName();
		}else{
			return ""; // no string will make it so that this doesn't get indexed by this indexer. 
		}
	}
	
}
