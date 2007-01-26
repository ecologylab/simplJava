/**
 * 
 */
package ecologylab.appframework.types;

/**
 * @author andruid
 *
 */
public class PrefBoolean extends Pref<Boolean>
{
	private Boolean			value;
	
	/**
	 * 
	 */
	public PrefBoolean()
	{
		super();
	}
	public PrefBoolean(Boolean value)
	{
		super();
		this.value	= value;
	}

	/**
	 * @return	The
	 */
	@Override
	Boolean getValue()
	{
		return value;
	}
	
	public void setValue(Boolean value)
	{
		invalidate();
		this.value	= value;
	}
}
