/**
 * 
 */
package ecologylab.appframework.types;

/**
 * @author andruid
 *
 */
public class PrefInt extends Pref<Integer>
{
	private int			value;
	
	/**
	 * 
	 */
	public PrefInt()
	{
		super();
	}
	public PrefInt(int value)
	{
		super();
		this.value	= value;
	}

	/**
	 * @return	The
	 */
	@Override
	Integer getValue()
	{
		return value;
	}
	
	public void setValue(int value)
	{
		invalidate();
		this.value	= value;
	}
}
