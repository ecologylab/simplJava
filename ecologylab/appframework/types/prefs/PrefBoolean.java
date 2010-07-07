/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.serialization.simpl_inherit;

/**
 * Pref for a Boolean
 * 
 * @author andruid
 * 
 */

@simpl_inherit
public class PrefBoolean extends Pref<Boolean>
{
	/**
	 * Value of Pref
	 */
	@simpl_scalar
	boolean	value;

	/**
	 * 
	 */
	public PrefBoolean()
	{
		super();
	}

	/**
	 * Instantiate Pref to value
	 * 
	 * @param value
	 */
	public PrefBoolean(boolean value)
	{
		super();
		this.value = value;
	}

	public PrefBoolean(String name, boolean value)
	{
		super(name);
		this.value = value;
	}

	/**
	 * Get the value of the Pref
	 * 
	 * @return The value of the Pref
	 */
	@Override
	protected Boolean getValue()
	{
		return value;
	}

	/**
	 * Set the value of the Pref given a Boolean (big B)
	 * 
	 * @param The
	 *          Boolean value the Pref will be set to
	 */
	@Override
	public void setValue(Boolean newValue)
	{
		setValue(newValue.booleanValue());
	}

	/**
	 * Set the value of the Pref given a boolean (small b)
	 * 
	 * @param The
	 *          boolean value the Pref will be set to
	 */
	public void setValue(boolean value)
	{
		this.value = value;

		prefChanged();
	}

	/**
	 * @see ecologylab.appframework.types.prefs.Pref#clone()
	 */
	@Override
	public Pref<Boolean> clone()
	{
		return new PrefBoolean(this.name, this.value);
	}
}
