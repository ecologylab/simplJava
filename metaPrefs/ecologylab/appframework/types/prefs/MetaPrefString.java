/**
 * 
 */
package ecologylab.appframework.types.prefs;

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
	@xml_attribute	String		defaultValue;
	
	/**
	 * 
	 */
	public MetaPrefString()
	{
		super();
	}
	
    public String getDefaultValue()
	{
		return defaultValue;
	}

    public @Override
    void revertToDefault()
    {
        JTextField textField = (JTextField)lookupComponent(this.id+"textField");
        textField.setText(this.defaultValue);
    }

    public @Override
    JPanel getWidget()
    {
        JPanel panel = new JPanel();
        panel.setName(this.id);
        
        this.createLabel(panel);
        this.createTextField(panel,this.getDefaultValue(),"textField");
        
        panel.setSize(new java.awt.Dimension(586,35));
        panel.setLayout(null);
        panel.setVisible(true);
        
        return panel;
    }

    @Override
    public void setWidgetToPrefValue(String prefValue)
    {
        JTextField textField = (JTextField)lookupComponent(this.id+"textField");
        textField.setText(prefValue);
    }

    @Override
    public String getPrefValue()
    {
        JTextField textField = (JTextField)lookupComponent(this.id+"textField");
        return textField.getText();
    }
	
/*
	public boolean isWithinRange(String newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}