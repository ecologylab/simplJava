/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.xml.xml_inherit;

/**
 * Pref for a Double
 * 
 * @author Andruid Kerne (andruid@ecologylab.net)
 * @author Zachary O. Toups (zach@ecologylab.net)
 */

@xml_inherit
public class PrefDouble extends Pref<Double>
{
	/** Value of Pref */
	@xml_attribute
	double	value;

	/**
	 * 
	 */
	public PrefDouble()
	{
		super();
	}

	/**
	 * Instantiate Pref to value
	 * 
	 * @param value
	 */
	public PrefDouble(double value)
	{
		super();
		this.value = value;
	}

	public PrefDouble(String name, double value)
	{
		super();
		this.name = name;
		this.value = value;
	}

	/**
	 * Get the value of the Pref
	 * 
	 * @return The value of the Pref
	 */
	@Override
	protected Double getValue()
	{
		return value;
	}

	/**
	 * Set the value of the Pref given a Float (big F)
	 * 
	 * @param newValue
	 *          The Double value the Pref will be set to
	 */
	@Override
	public void setValue(Double newValue)
	{
		setValue(newValue.doubleValue());
	}

	/**
	 * Set the value of the Pref given a float (small f)
	 * 
	 * @param value
	 *          The double value the Pref will be set to
	 */
	public void setValue(double value)
	{
		this.value = value;

		prefChanged();
	}

	/**
	 * @see ecologylab.appframework.types.prefs.Pref#clone()
	 */
	@Override
	public Pref<Double> clone()
	{
		return new PrefDouble(this.name, this.value);
	}
}
