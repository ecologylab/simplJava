package ecologylab.xml;

/**
 * A set of integers that represent settable types.
 */
public interface Types
{
	// built-ins handled by ecologylab.xml.ElementState
	static final int		TYPE_STRING		 = 0;
	static final int		TYPE_INT			 = 1;
	static final int		TYPE_BOOLEAN	 = 2;
	static final int		TYPE_FLOAT		 = 3;
	static final int		TYPE_DOUBLE		 = 4;
	static final int		TYPE_LONG		 = 5;
	static final int		TYPE_SHORT		 = 6;
	static final int		TYPE_BYTE		 = 7;
	static final int		TYPE_CHAR		 = 8;

	static final int		TYPE_COLOR		 = 100;
	static final int		TYPE_DATE		 = 101;

	// non built-ins
	static final int		TYPE_PURL		 = -100;
}


