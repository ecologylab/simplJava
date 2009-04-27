/**
 * 
 */
package ecologylab.generic;

/**
 * 
 *
 * @author andruid 
 */
public class CharArrayPool extends ResourcePool<char[]>
{
	int		bufferSize;
	
	protected CharArrayPool(int initialPoolSize, int bufferSize)
	{
		super(false, initialPoolSize, NEVER_CONTRACT);
		this.bufferSize	= bufferSize;
	}

	@Override
	protected void clean(char[] objectToClean)
	{
		
	}

	@Override
	protected char[] generateNewResource()
	{
		return new char[bufferSize];
	}

}
