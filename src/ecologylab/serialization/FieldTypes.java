package ecologylab.serialization;

public interface FieldTypes
{
	public static final int	UNSET_TYPE								= -999;

	/**
	 * This means we experienced an error while parsing. This should never happen.
	 */
	public static final int	BAD_FIELD									= -99;

	public static final int	IGNORED_ATTRIBUTE					= -1;

	public static final int SCALAR										= 0x12;

	public static final int	COMPOSITE_ELEMENT						= 3;
	
	

	/**
	 * This means that we don't bother to parse the element, because the programmer developing
	 * ElementState subclasses did not bother to create fields that use it.
	 */
	public static final int	IGNORED_ELEMENT						= -3;

	public static final int	COLLECTION_ELEMENT				= 4;

	public static final int	COLLECTION_SCALAR					= 5;

	public static final int	MAP_ELEMENT								= 6;

	public static final int	MAP_SCALAR								= 7;


	public static final int	WRAPPER										= 0x0a;

	public static final int	PSEUDO_FIELD_DESCRIPTOR		= 0x0d;	

	public static final int	NAMESPACE_IGNORED_ELEMENT	= -2;

	public static final int	XMLNS_ATTRIBUTE						= 0x0e;

	public static final int	XMLNS_IGNORED							= 0x0f;

	public static final int	NAME_SPACE_MASK						= 0x10;

	public static final int	NAMESPACE_TRIAL_ELEMENT		= NAME_SPACE_MASK;

	public static final int	NAME_SPACE_SCALAR					= NAME_SPACE_MASK + SCALAR;

	public static final int	NAME_SPACE_NESTED_ELEMENT	= NAME_SPACE_MASK + COMPOSITE_ELEMENT;
//
//	public static final int	NAME_SPACE_LEAF_NODE			= NAME_SPACE_MASK + LEAF;

}
