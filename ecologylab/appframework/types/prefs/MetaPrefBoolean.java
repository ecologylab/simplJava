/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ecologylab.xml.xml_inherit;

/**
 * Metadata about a Boolean Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */
@xml_inherit
public class MetaPrefBoolean extends MetaPref<Boolean>
{
    /**
     * Default value for this MetaPref
     */
	@xml_attribute	boolean		defaultValue;
	
	/**
	 * Instantiate.
	 */
	public MetaPrefBoolean()
	{
		super();
	}
	
    /**
     * Gets the default value of a MetaPref. 
     * 
     * @return Default value of MetaPref
     */
	public Boolean getDefaultValue()
	{
		return defaultValue;
	}

    /**
     * Construct a new instance of the Pref that matches this.
     * Use this to fill-in the default value.
     * 
     * @return
     */
	protected @Override Pref<Boolean> getPrefInstance()
	{
		return new PrefBoolean();
	}
    
/*
	public boolean isWithinRange(Boolean newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}