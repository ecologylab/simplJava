/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * Pref for an Integer
 * 
 * @author andruid
 * 
 */

@simpl_inherit
public class PrefInt extends Pref<Integer>
{
	/**
	 * Value of Pref
	 */
	@simpl_scalar
	int	value;

	/**
	 * 
	 */
	public PrefInt()
	{
		super();
	}

	public PrefInt(String name, int value)
	{
		super(name);

		this.value = value;
	}

	/**
	 * Instantiate Pref to value
	 * 
	 * @param value
	 */
	public PrefInt(int value)
	{
		this(null, value);
	}

	/**
	 * @return The
	 */
	@Override
	protected Integer getValue()
	{
		return value;
	}

	/**
	 * Set the value of the Pref given an Integer
	 * 
	 * @param The
	 *          Integer value the Pref will be set to
	 */
	@Override
	public void setValue(Integer newValue)
	{
		setValue(newValue.intValue());
	}

	/**
	 * Set the value of the Pref given an int
	 * 
	 * @param The
	 *          int value the Pref will be set to
	 */
	public void setValue(int value)
	{
		this.value = value;

		prefChanged();
	}

	/**
	 * @see ecologylab.appframework.types.prefs.Pref#clone()
	 */
	@Override
	public Pref<Integer> clone()
	{
		return new PrefInt(this.name, this.value);
	}
}
