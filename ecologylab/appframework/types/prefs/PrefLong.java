/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.xml.xml_inherit;

/**
 * Pref for a Long
 * 
 * @author andruid, Zachary O. Toups (zach@ecologylab.net)
 * 
 */

@xml_inherit
public class PrefLong extends Pref<Long>
{
	/** Value of Pref */
	@xml_attribute
	long	value;

	/**
	 * 
	 */
	public PrefLong()
	{
		super();
	}

	public PrefLong(String name, long value)
	{
		super(name);

		this.value = value;
	}

	/**
	 * Instantiate Pref to value
	 * 
	 * @param value
	 */
	public PrefLong(long value)
	{
		this(null, value);
	}

	/**
	 * @return The
	 */
	@Override
	protected Long getValue()
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
	public void setValue(Long newValue)
	{
		setValue(newValue.longValue());
	}

	/**
	 * Set the value of the Pref given an int
	 * 
	 * @param The
	 *          int value the Pref will be set to
	 */
	public void setValue(long value)
	{
		this.value = value;

		prefChanged();
	}

	/**
	 * @see ecologylab.appframework.types.prefs.Pref#clone()
	 */
	@Override
	public Pref<Long> clone()
	{
		return new PrefLong(this.name, this.value);
	}
}
