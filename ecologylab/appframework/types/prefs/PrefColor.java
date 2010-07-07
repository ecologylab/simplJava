/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.Color;

/**
 * A preference that is a Color.
 * 
 * @author awebb
 * 
 */
public class PrefColor extends Pref<Color>
{
	@simpl_scalar
	Color	value;

	public PrefColor()
	{
		super();
	}

	public PrefColor(Color value)
	{
		super();
		this.value = value;
	}

	public PrefColor(String name, Color value)
	{
		this.name = name;
		this.value = value;
	}

	/**
	 * @see ecologylab.appframework.types.prefs.Pref#getValue()
	 */
	@Override
	protected Color getValue()
	{
		return value;
	}

	/**
	 * @see ecologylab.appframework.types.prefs.Pref#setValue(T)
	 */
	@Override
	public void setValue(Color newValue)
	{
		this.value = newValue;

		prefChanged();
	}

	/**
	 * @see ecologylab.appframework.types.prefs.Pref#clone()
	 */
	@Override
	public Pref<Color> clone()
	{
		return new PrefColor(this.name, this.value);
	}
}