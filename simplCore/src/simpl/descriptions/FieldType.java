package simpl.descriptions;

import simpl.annotations.dbal.simpl_scalar;

public enum FieldType {

	UNSET_TYPE(-999),
	BAD_FIELD(-99),
	IGNORED_ATTRIBUTE(-1),
	SCALAR(0x12),
	COMPOSITE_ELEMENT(3),
	/**
	 * This means that we don't bother to parse the element, because the programmer developing
	 * ElementState subclasses did not bother to create fields that use it.
	 */
	IGNORED_ELEMENT(-3),
	COLLECTION_ELEMENT(4),
	COLLECTION_SCALAR(5),
	MAP_ELEMENT(6),
	MAP_SCALAR(7),
	WRAPPER(0x0a),
	PSEUDO_FIELD_DESCRIPTOR(0x0d),	
	NAMESPACE_IGNORED_ELEMENT(-2),
	XMLNS_ATTRIBUTE(0x0e),
	XMLNS_IGNORED(0x0f),
	NAME_SPACE_MASK(0x10),
	/**
	 * These are all masked; this is a legacy decision we're carrying over. 
	 */
	NAMESPACE_TRIAL_ELEMENT(NAME_SPACE_MASK.getTypeID()),
	NAME_SPACE_SCALAR(NAME_SPACE_MASK.getTypeID() + SCALAR.getTypeID()),
	NAME_SPACE_NESTED_ELEMENT(NAME_SPACE_MASK.getTypeID()+ COMPOSITE_ELEMENT.getTypeID());	

	@simpl_scalar
	private final int typeID;

	/**
	 * Gets the ID for the type. Currently really just for backward compatability with old code. 
	 * @return
	 */
	public int getTypeID()
	{
		return this.typeID;
	}

	/**
	 * Obtains a FieldTyep from a given ID
	 * @param typeID
	 * @return
	 */
	public static FieldType fromTypeID(int typeID)
	{
		for(FieldType ft : FieldType.values())
		{
			if(ft.getTypeID()== typeID)
			{
				return ft;
			}
		}
		// Just default to this. 
		return FieldType.BAD_FIELD;
	}

	/**
	 * Creates a field type with a corresponding type ID number
	 * @param typeID
	 */
	FieldType(int typeID)
	{
		this.typeID = typeID;
	}
}