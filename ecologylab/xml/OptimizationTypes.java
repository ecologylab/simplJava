package ecologylab.xml;

public interface OptimizationTypes
{
	public static final int	UNSET_TYPE				= -99;
	/**
	 * This means we experienced an error while parsing.
	 * This should never happen.
	 */
	public static final int	BAD_FIELD				= -1;
	
	/**
	 * This means that we don't bother to parse the element,
	 * because the programmer developing ElementState subclasses did not
	 * bother to create fields that use it.
	 */
	public static final int	IGNORED_ELEMENT			= 0;

	public static final int	REGULAR_NESTED_ELEMENT	= 1;

	public static final int	LEAF_NODE_VALUE			= 2;
	
	public static final int	COLLECTION_ELEMENT		= 3;
	
	public static final int	COLLECTION_SCALAR		= 4;
	
	public static final int	MAP_ELEMENT				= 5;
	
	public static final int	MAP_SCALAR				= 6;
	
	public static final int	REGULAR_ATTRIBUTE		= 0x10;
	
	public static final int	IGNORED_ATTRIBUTE		= 0;
	
	public static final int	XMLNS_ATTRIBUTE			= 0x11;	
	
	/**
	 * These will be handled by otherNestedElement().
	 */
	public static final int	OTHER_NESTED_ELEMENT	= 9;

	
	/**
	 * Root node type: for FieldToXMLOptimizations translateTo only.
	 */
	public static final int	ROOT					= 0X20;
}
