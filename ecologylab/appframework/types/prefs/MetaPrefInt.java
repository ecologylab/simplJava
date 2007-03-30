/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.GridBagLayout;

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
	private static final int RADIO_X = 405;
    /**
     * Default value for this MetaPref
     */
	@xml_attribute	int		defaultValue;
	
    /**
     * Instantiate.
     */
	public MetaPrefInt()
	{
		super();
	}
	
    /**
     * Gets the default value of a MetaPref. 
     * 
     * @return Default value of MetaPref
     */
    public Integer getDefaultValue()
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
        if (widgetIsTextField())
        {
            JTextField textField = (JTextField)lookupComponent(this.id+"textField");
            textField.setText(this.getDefaultValue().toString());
        }
        else if (widgetIsRadio())
        {
            /* This is an ugly way to do this, but we can't trust
             * choices to be in the right order (0-n), and we can't
             * trust the choice values to be number 0-n either.
             * We also can't get the index without the object.
             */
            // get default choice
            Integer defValue = this.getDefaultValue();
            for(Choice choice1 : choices)
            {
                if (defValue.equals(choice1.getValue()))
                {
                    // registered name
                    String regName = this.id + choice1.name;
                    //println("we think the name is: " + regName);
                    JRadioButton defaultButton = (JRadioButton)lookupComponent(regName);
                    ButtonModel buttonModel = defaultButton.getModel();
                    buttonModel.setSelected(true);
                    break;
                }
            }
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
                    boolean isDefault = getDefaultValue().equals(choice.getValue());
                    this.createRadio(panel, buttonGroup, isDefault, choice.getLabel(), choice.getName(), rnum, 0);
                    rnum++;
                }
            }
        }
        // TODO: drop-down list

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
    public void setWidgetToPrefValue(Integer prefValue)
    {
        if (widgetIsTextField())
        {
            JTextField textField = (JTextField)lookupComponent(this.id+"textField");
            textField.setText(prefValue.toString());
        }
        else if (widgetIsRadio())
        {
            /* This is an ugly way to do this, but we can't trust
             * choices to be in the right order (0-n), and we can't
             * trust the choice values to be number 0-n either.
             * We also can't get the index without the object.
             */
            // find default choice
            Integer defValue = prefValue;
            for(Choice choice1 : choices)
            {
                if (defValue.equals(choice1.getValue()))
                {
                    // registered name
                    String regName = this.id + choice1.name;
                    //println("we think the name is: " + regName);
                    JRadioButton defaultButton = (JRadioButton) lookupComponent(regName);
                    ButtonModel buttonModel = defaultButton.getModel();
                    buttonModel.setSelected(true);
                    break;
                }
            }
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
    public Integer getPrefValue()
    {
        if (widgetIsTextField())
        {
            JTextField textField = (JTextField)lookupComponent(this.id+"textField");
            return new Integer(textField.getText());
        }
        else if (widgetIsRadio())
        {
            // find the selected one and return it
            for (Choice choice: choices)
            {
                String regName = this.id + choice.name;
                JRadioButton choiceButton = (JRadioButton) lookupComponent(regName);
                if (choiceButton.isSelected())
                {
                    return (Integer)choice.getValue();
                }
            }
        }
        // if by some miracle we managed to deselect something that
        // automatically has a selected value, return the default value
        return (Integer)this.getDefaultValue();
    }

    /**
     * Construct a new instance of the Pref that matches this.
     * Use this to fill-in the default value.
     * 
     * @return
     */
	protected @Override Pref<Integer> getPrefInstance()
	{
		return new PrefInt();
	}
	
/*
	public boolean isWithinRange(Integer newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}