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
public class PrefElementState<E extends ElementState> extends Pref<E>
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
	E getValue()
	{
		// TODO Auto-generated method stub
		return (E) get(0);
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
	 * See Pref.clone() for why this method is important.
	 * @see ecologylab.appframework.types.prefs.Pref#clone()
	 */
	@Override
	public Pref<E> clone()
	{
		Pref<E> pES = new PrefElementState(this.name);
		//TODO
		for (ElementState es : this)
		{
			pES.add(es);
		}

		return pES;
	}
}
