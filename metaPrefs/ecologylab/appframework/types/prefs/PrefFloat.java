/**
 * 
 */
package ecologylab.appframework.types.prefs;

/**
 * @author andruid
 *
 */
public class PrefFloat extends Pref<Float>
{
	private float			value;
	
	/**
	 * 
	 */
	public PrefFloat()
	{
		super();
	}
	public PrefFloat(float value)
	{
		super();
		this.value	= value;
	}

	/**
	 * @return	The
	 */
	@Override
	Float getValue()
	{
		return value;
	}
	
	public void setValue(Float newValue)
	{
		setValue(newValue.floatValue());
	}
	public void setValue(float value)
	{
		invalidate();
		this.value	= value;
	}
}
