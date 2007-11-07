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
	
	public static final int	IGNORED_ATTRIBUTE		= 0;
	
	public static final	int NAMESPACE_IGNORED_ELEMENT	= 0;
	
	public static final int	REGULAR_ATTRIBUTE		= 1;

	public static final int	REGULAR_NESTED_ELEMENT	= 2;

	public static final int	LEAF_NODE_VALUE			= 3;
	
	public static final int	COLLECTION_ELEMENT		= 4;
	
	public static final int	COLLECTION_SCALAR		= 5;
	
	public static final int	MAP_ELEMENT				= 6;
	
	public static final int	MAP_SCALAR				= 7;
	
	/**
	 * Root node type: for FieldToXMLOptimizations translateTo only.
	 */
	public static final int	ROOT					= 8;
	
	/**
	 * This undocumented feature forms an ElementState subclass instance using lookup by class,
	 * if that works, and then calls addNestedElement(), which the user can override, to decide what
	 * do to with it.
	 */
	public static final int	OTHER_NESTED_ELEMENT	= 9;
	
	public static final int	XMLNS_ATTRIBUTE			= 0x0a;	
	
	
	public static final int NAME_SPACE_MASK			= 0x10;
	
	public static final int	NAME_SPACE_ATTRIBUTE		= NAME_SPACE_MASK + REGULAR_ATTRIBUTE;

	public static final int	NAME_SPACE_NESTED_ELEMENT	= NAME_SPACE_MASK + REGULAR_NESTED_ELEMENT;
	
	public static final int NAME_SPACE_LEAF_NODE		= NAME_SPACE_MASK + LEAF_NODE_VALUE;
	
	
}
