/**
 * 
 */
package ecologylab.generic;

/**
 * @author andruid
 *
 */
public class StringBuilderBaseUtils
{

	/**
	 * string builder pool to parse link metadata
	 */
	private static final StringBuilderPool	stringBuilderPool	= new StringBuilderPool(30, 512);

	public static StringBuilder acquire()
	{
		return stringBuilderPool.acquire();
	}

	public static void release(StringBuilder buffy)
	{
		stringBuilderPool.release(buffy);
	}

}
