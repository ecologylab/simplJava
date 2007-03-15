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

        JPanel panel = new JPanel();
        // at the moment this will only be a radio button choice
        //if (widgetIsRadio())
        //{
        panel.setName(this.id);

        ButtonGroup radioPair = new ButtonGroup();

        panel.setSize(new java.awt.Dimension(586,40));

        this.createLabel(panel);
        
        if (choices != null)
        {
        	ChoiceBoolean choice0	= (ChoiceBoolean) choices.get(0);
            boolean isDefault = getDefaultValue().equals(choice0.getValue());
            String name	= isDefault ? "Yes" : "No";
        	this.createRadio(panel, radioPair, isDefault, choice0.getLabel(), name, 405);
        	name	= !isDefault ? "Yes" : "No";
        	ChoiceBoolean choice1	= (ChoiceBoolean) choices.get(1);
        	this.createRadio(panel, radioPair, isDefault, choice1.getLabel(), name, 405);
        }
        else
        {
            boolean yesVal = defVal;
            boolean noVal = !defVal;
            this.createRadio(panel, radioPair, yesVal, "Yes", "Yes", 405);
            this.createRadio(panel, radioPair, noVal, "No", "No", 484);
        }

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