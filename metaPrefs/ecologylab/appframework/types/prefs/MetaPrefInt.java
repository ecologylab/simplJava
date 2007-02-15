/**
 * 
 */
package ecologylab.appframework.types.prefs;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ecologylab.xml.xml_inherit;

/**
 * Metadata about an Integer Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */
@xml_inherit
public class MetaPrefInt extends MetaPref<Integer>
{
	@xml_attribute	int		defaultValue;
	
	/**
	 * 
	 */
	public MetaPrefInt()
	{
		super();
	}
	
    public Integer getDefaultValue()
	{
		return defaultValue;
	}

    public @Override
    void revertToDefault()
    {
        if (widgetIsTextField())
        {
            JTextField textField = (JTextField)lookupComponent(this.id+"textField");
            textField.setText(this.getDefaultValue().toString());
        }
        else if (widgetIsRadio())
        {
            // TODO: this is a bad assumption (value = index)
            // get default choice
            Choice choice = choices.get(this.getDefaultValue());
            // registered name
            String regName = this.id + choice.name;
            println("we think the name is: " + regName);
            JRadioButton defaultButton = (JRadioButton) lookupComponent(regName);
            ButtonModel buttonModel = defaultButton.getModel();
            buttonModel.setSelected(true);
        }
    }

    public @Override
    JPanel getWidget()
    {
        JPanel panel = new JPanel();
        
        this.createLabel(panel);
        // TODO: widget here needs to check what type of thing we are actually creating
        // ie, metapref.widget (this also needs to be done in other metapref type classes)
        if (widgetIsTextField())
        {
            this.createTextField(panel,this.getDefaultValue().toString(),"textField");
        }
        else if (widgetIsRadio())
        {
            // we know here that if we are a radio, we are a mutex of 3 or more
            // because otherwise we would be a bool.
            if (choices != null)
            {
                ButtonGroup buttonGroup = new ButtonGroup();
                int start_y = 7;
                for (Choice choice : choices)
                {
                    // TODO: there's a better way to do this than in an if-else
                    boolean thisIsDefault = getDefaultValue().equals(choice.getValue());
                    //println("this is default?: " + thisIsDefault);
                    //println("getDefaultValue: " + getDefaultValue().toString());
                    //println("choice.getValue: " + choice.getValue().toString());
                    if (thisIsDefault)
                        this.createRadio(panel, buttonGroup, true, choice.getLabel(), choice.getName(), 405, start_y);
                    else
                        this.createRadio(panel, buttonGroup, false, choice.getLabel(), choice.getName(), 405, start_y);
                    start_y += 30;
                }
            }
        }
        // TODO: drop-down list
        if (choices != null)
        {
            panel.setSize(new java.awt.Dimension(586,35*choices.size()));
        }
        else
        {
            panel.setSize(new java.awt.Dimension(586,35));
        }
        panel.setLayout(null);
        panel.setVisible(true);
        
        return panel;
    }
	
/*
	public boolean isWithinRange(Integer newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}