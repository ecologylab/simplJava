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
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see ecologylab.appframework.types.prefs.Pref#getValue()
	 */
	@Override
	ElementState getValue()
	{
		// TODO Auto-generated method stub
		return get(0);
	}

	/* (non-Javadoc)
	 * @see ecologylab.appframework.types.prefs.Pref#setValue(T)
	 */
	@Override
	public void setValue(ElementState newValue)
	{
		add(newValue);
        
        this.prefUpdated();
	}

}
