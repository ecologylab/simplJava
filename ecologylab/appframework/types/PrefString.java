/**
 * 
 */
package ecologylab.appframework.types;

/**
 * @author andruid
 *
 */
public class PrefString extends Pref<String>
{
	private String			value;
	
	/**
	 * 
	 */
	public PrefString()
	{
		super();
	}
	public PrefString(String value)
	{
		super();
		this.value	= value;
	}

	/**
	 * @return	The
	 */
	@Override
	String getValue()
	{
		return value;
	}
	
	public void setValue(String value)
	{
		invalidate();
		this.value	= value;
	}
}
