/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.Color;

import ecologylab.services.authentication.AuthenticationList;
import ecologylab.xml.xml_inherit;

/**
 * A preference that is an AuthenticationList.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public @xml_inherit class PrefAuthList extends Pref<AuthenticationList>
{
	@xml_nested AuthenticationList	value;

	public PrefAuthList()
	{
		super();
	}

	public PrefAuthList(String name, AuthenticationList authList)
	{
		super(name);
		this.value = authList;
	}

	/**
	 * @see ecologylab.appframework.types.prefs.Pref#getValue()
	 */
	@Override AuthenticationList getValue()
	{
		return value;
	}

	/**
	 * @see ecologylab.appframework.types.prefs.Pref#setValue(T)
	 */
	@Override public void setValue(AuthenticationList newValue)
	{
		this.value = newValue;

		prefChanged();
	}

	/**
	 * @see ecologylab.appframework.types.prefs.Pref#clone()
	 */
	@Override
	public Pref<AuthenticationList> clone()
	{
		return new PrefAuthList(this.name, this.value);
	}
	
	
}