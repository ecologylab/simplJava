/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.xml.ElementState;

/**
 * A preference that is an ElementState.
 * 
 * @author andruid
 */
public class PrefElementState extends Pref<ElementState>
{
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see ecologylab.appframework.types.prefs.Pref#getValue()
	 */
	@Override
	ElementState getValue()
	{
		// TODO Auto-generated method stub
		return get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ecologylab.appframework.types.prefs.Pref#setValue(T)
	 */
	@Override
	public void setValue(ElementState newValue)
	{
		add(newValue);

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
	public Pref<ElementState> clone()
	{
		Pref<ElementState> pES = new PrefElementState(this.name);

		for (ElementState e : this)
		{
			pES.add(e);
		}

		return pES;
	}
}
