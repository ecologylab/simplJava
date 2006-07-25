package ecologylab.xml;

public interface ParseTableEntryTypes
{
	/**
	 * This means we experienced an error while parsing.
	 * This should never happen.
	 */
	public static int	BAD_FIELD				= -1;
	
	/**
	 * This means that we don't bother to parse the element,
	 * because the programmer developing ElementState subclasses did not
	 * bother to create fields that use it.
	 */
	public static int	IGNORED_ELEMENT			= 0;

	public static int	REGULAR_NESTED_ELEMENT	= 1;

	public static int	LEAF_NODE_VALUE			= 2;
	
	public static int	COLLECTION_ELEMENT		= 3;
	
	public static int	REGULAR_ATTRIBUTE		= 10;
	
	public static int	IGNORED_ATTRIBUTE		= 0;
	
	/**
	 * These will be handled by otherNestedElement().
	 */
	public static int	OTHER_NESTED_ELEMENT	= 4;


}
