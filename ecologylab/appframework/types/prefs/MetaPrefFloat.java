/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.GridBagLayout;
import java.awt.Rectangle;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ecologylab.appframework.types.prefs.MetaPref;
import ecologylab.xml.xml_inherit;

/**
 * Metadata about a Float Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */

@xml_inherit
public class MetaPrefFloat extends MetaPref<Float>
{
	@xml_attribute	float		defaultValue;
	
	/**
	 * 
	 */
	public MetaPrefFloat()
	{
		super();
	}
	
	public Float getDefaultValue()
	{
		return defaultValue;
	}

    public @Override
    void revertToDefault()
    {
        JTextField textField = (JTextField)lookupComponent(this.id+"textField");
        textField.setText(this.getDefaultValue().toString());
    }

    public @Override
    JPanel getWidget()
    {
        JPanel panel = new JPanel();
        panel.setName(this.id);
        panel.setLayout(new GridBagLayout());
        
        if (widgetIsTextField())
        {
            this.createTextField(panel,this.getDefaultValue().toString(),"textField", 0, 0);
        }
        else if (widgetIsRadio())
        {
            // we know here that if we are a radio, we are a mutex of 3 or more
            // because otherwise we would be a bool.
            if (choices != null)
            {
                ButtonGroup buttonGroup = new ButtonGroup();
                int rnum = 0;
                for (Choice choice : choices)
                {
                    boolean isDefault = this.getDefaultValue().equals(choice.getValue());
                    this.createRadio(panel, buttonGroup, isDefault, choice.getLabel(), choice.getName(), rnum, 0);
                    rnum++;
                }
            }
        }
        // TODO: drop-down list
        
        panel.setVisible(true);
        
        return panel;
    }

    @Override
    public void setWidgetToPrefValue(Float prefValue)
    {
        JTextField textField = (JTextField)lookupComponent(this.id+"textField");
        textField.setText(prefValue.toString());
    }

    @Override
    public Float getPrefValue()
    {
        JTextField textField = (JTextField)lookupComponent(this.id+"textField");
        return new Float(textField.getText());
    }

	protected @Override Pref<Float> getPrefInstance()
	{
		return new PrefFloat();
	}
	
/*
	public boolean isWithinRange(Float newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}