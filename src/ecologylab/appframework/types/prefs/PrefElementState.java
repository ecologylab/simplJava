/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.serialization.ElementState;

/**
 * A preference that is an ElementState.
 * 
 * @author andruid
 */
public class PrefElementState<T extends ElementState> extends Pref<T>
{

	T	elementStatePref;

	/**
	 * 
	 */
	public PrefElementState()
	{
		super();
	}

	public PrefElementState(String name)
	{
		super();
		this.name = name;
	}

	/** 
	 * @see ecologylab.appframework.types.prefs.Pref#getValue()
	 */
	@Override
	protected T getValue()
	{
		return elementStatePref;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ecologylab.appframework.types.prefs.Pref#setValue(T)
	 */
	@Override
	public void setValue(T newValue)
	{
		elementStatePref = newValue;
		prefChanged();
	}

	/**
	 * XXX NOTE: THIS IS AN UNSAFE CLONE. IF THE VALUE OF THIS PREFERENCE IS TO BE MODIFIED, THIS
	 * METHOD MUST BE RECONSIDERED. A very cool and proper way to do this would be to translate value
	 * to and from XML, but this is impossible without the correct translation scope.
	 * 
	 * @see ecologylab.appframework.types.prefs.Pref#clone()
	 */
	@Override
	public PrefElementState<T> clone()
	{
		PrefElementState<T> pES = new PrefElementState(this.name);

		return pES;
	}
}
