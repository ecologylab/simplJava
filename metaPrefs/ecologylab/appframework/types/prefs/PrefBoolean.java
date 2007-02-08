/**
 * 
 */
package ecologylab.appframework.types.prefs;

/**
 * @author andruid
 *
 */
public class PrefBoolean extends Pref<Boolean>
{
	private boolean			value;
	
	/**
	 * 
	 */
	public PrefBoolean()
	{
		super();
	}
	public PrefBoolean(boolean value)
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
	
	public void setValue(Boolean newValue)
	{
		setValue(newValue.booleanValue());
	}
	public void setValue(boolean value)
	{
		invalidate();
		this.value	= value;
	}
}
