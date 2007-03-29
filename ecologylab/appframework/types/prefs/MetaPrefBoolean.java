/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
     * Sets the widget value/selection to the default value/selection.
     * TODO: MOVE THIS
     */
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
        Boolean defVal = (Boolean)this.getDefaultValue();

        JPanel panel = new JPanel();
        panel.setName(this.id);
        panel.setLayout(new GridBagLayout());

        ButtonGroup radioPair = new ButtonGroup();
        
        if (choices != null)
        {
        	ChoiceBoolean choice0	= (ChoiceBoolean) choices.get(0);
            boolean isDefault       = getDefaultValue().equals(choice0.getValue());
            String name	            = isDefault ? "Yes" : "No";
        	this.createRadio(panel, radioPair, isDefault, choice0.getLabel(), name, 0, 0);
        	name	                = !isDefault ? "Yes" : "No";
        	ChoiceBoolean choice1	= (ChoiceBoolean) choices.get(1);
        	this.createRadio(panel, radioPair, isDefault, choice1.getLabel(), name, 1, 0);
        }
        else
        {
            boolean yesVal  = defVal;
            boolean noVal   = !defVal;
            this.createRadio(panel, radioPair, yesVal, "Yes", "Yes", 0, 0);
            this.createRadio(panel, radioPair, noVal, "No", "No", 1, 0);
        }

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

    /**
     * Gets the Pref value for this MetaPref.
     * 
     *  TODO: MOVE THIS
     *  
     * @return Pref value
     */
    @Override
    public Boolean getPrefValue()
    {
        JRadioButton yesButton = (JRadioButton)lookupComponent(this.id+"Yes");
        return yesButton.isSelected();
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