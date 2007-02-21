/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.Rectangle;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import ecologylab.appframework.types.prefs.MetaPref;
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
	@xml_attribute	boolean		defaultValue;
	
	/**
	 * 
	 */
	public MetaPrefBoolean()
	{
		super();
	}
	
	public Boolean getDefaultValue()
	{
		return defaultValue;
	}

    public @Override
    void revertToDefault()
    {
        // get button
        JRadioButton yesButton = (JRadioButton)lookupComponent(this.id+"Yes");
        ButtonModel yesModel = yesButton.getModel();
        boolean yesVal = this.getDefaultValue();
        
        if (yesVal)
        {
            yesModel.setSelected(true);
        }
        else
        {
            JRadioButton noButton = (JRadioButton)lookupComponent(this.id+"No");
            ButtonModel noModel = noButton.getModel();
            noModel.setSelected(true);
        }
    }

    public @Override
    JPanel getWidget()
    {
        Boolean defVal = (Boolean)this.getDefaultValue();
        boolean yesVal = defVal;
        boolean noVal = !defVal;

        JPanel panel = new JPanel();
        // at the moment this will only be a radio button choice
        //if (widgetIsRadio())
        //{
        panel.setName(this.id);

        ButtonGroup radioPair = new ButtonGroup();

        panel.setSize(new java.awt.Dimension(586,40));

        this.createLabel(panel);
        this.createRadio(panel, radioPair, yesVal, "Yes", "Yes", 405);
        this.createRadio(panel, radioPair, noVal, "No", "No", 484);
        //}

        panel.setLayout(null);
        panel.setVisible(true);
        
        return panel;
    }

    @Override
    public void setWidgetToPrefValue(Boolean prefValue)
    {
        if (prefValue)
        {
            JRadioButton yesButton = (JRadioButton)lookupComponent(this.id+"Yes");
            ButtonModel yesModel = yesButton.getModel();
            yesModel.setSelected(true);
        }
        else
        {
            JRadioButton noButton = (JRadioButton)lookupComponent(this.id+"No");
            ButtonModel noModel = noButton.getModel();
            noModel.setSelected(true);
        }
    }

    @Override
    public Boolean getPrefValue()
    {
        JRadioButton yesButton = (JRadioButton)lookupComponent(this.id+"Yes");
        return yesButton.isSelected();
    }
    
/*
	public boolean isWithinRange(Boolean newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}