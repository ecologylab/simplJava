/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.GridBagLayout;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ecologylab.appframework.types.prefs.MetaPref;
import ecologylab.xml.xml_inherit;

/**
 * Metadata about a String Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */

@xml_inherit
public class MetaPrefString extends MetaPref<String>
{
    /**
     * Default value for this MetaPref
     */
	@xml_attribute	String		defaultValue;
	
    /**
     * Instantiate.
     */
	public MetaPrefString()
	{
		super();
	}
	
    /**
     * Gets the default value of a MetaPref. 
     * 
     * @return Default value of MetaPref
     */
    public String getDefaultValue()
	{
		return defaultValue;
	}

    /**
     * Sets the widget value/selection to the default value/selection.
     * TODO: MOVE THIS
     */
    public @Override
    void revertToDefault()
    {
        JTextField textField = (JTextField)lookupComponent(this.id+"textField");
        textField.setText(this.defaultValue);
    }

    /**
     * Gets the JPanel containing the gui components for the choices 
     * or fields associated with a MetaPref.
     * 
     *  TODO: MOVE THIS
     * 
     * @return JPanel of choices/values JComponents.
     */
    public @Override
    JPanel getWidget()
    {
        JPanel panel = new JPanel();
        // at the moment this will only be a text field
        //if (widgetIsTextField())
        //{
        panel.setName(this.id);
        panel.setLayout(new GridBagLayout());

        this.createTextField(panel,this.getDefaultValue(),"textField", 0, 0);
        //}
        
        panel.setVisible(true);
        
        return panel;
    }

    /**
     * Sets the widget value/selection to the value/selection of the Pref.
     * 
     *  TODO: MOVE THIS
     * 
     * @param prefValue     Value of Pref
     */
    @Override
    public void setWidgetToPrefValue(String prefValue)
    {
        JTextField textField = (JTextField)lookupComponent(this.id+"textField");
        textField.setText(prefValue);
    }

    /**
     * Gets the Pref value for this MetaPref.
     * 
     *  TODO: MOVE THIS
     *  
     * @return Pref value
     */
    @Override
    public String getPrefValue()
    {
        JTextField textField = (JTextField)lookupComponent(this.id+"textField");
        return textField.getText();
    }

    /**
     * Construct a new instance of the Pref that matches this.
     * Use this to fill-in the default value.
     * 
     * @return
     */
	protected @Override Pref<String> getPrefInstance()
	{
		return new PrefString();
	}
	
/*
	public boolean isWithinRange(String newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}