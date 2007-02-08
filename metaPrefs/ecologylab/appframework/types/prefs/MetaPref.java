/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;

/**
 * Metadata about a Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */

@xml_inherit
public abstract class MetaPref<T> extends ElementState
{
	/**
	 * Unique identifier for Preference name with convenient lookup in automatically generated HashMap.
	 */
	@xml_attribute 	String		id;
	
	/**
	 * This is the short text that appears in the Swing panel for editing the value.
	 */
	@xml_attribute 	String		description;
	
	/**
	 * This is longer text about that describes what the Preference does and more about the implications
	 * of its value.
	 */
	@xml_attribute 	String		helpText;
	
	/**
	 * Type of graphical user interface component used to interact with it.
	 * Must be a constant defined in the interface WidgetTypes
	 * If this value is left as null, it should default to TEXT_FIELD.
	 */
	@xml_attribute	String		widget;
	
	/**
	 * Categories enable tabbed panes of preferences to be edited.
	 */
	@xml_attribute 	String		category;
	
//	@xml_attribute	T			defaultValue;
	
//	@xml_nested		RangeState<T>	range;
	/**
	 * 
	 */
	public MetaPref()
	{
		super();
	}
	
	abstract T getDefaultValue();

    public String getCategory()
    {
        return category;
    }

    public JComponent getWidget()
    {
        if ("RADIO".equals(category))
        {
            // TODO don't need this after figure out how to define n-radio buttons
            if (this.getDefaultValue() instanceof Boolean)
            {
                JLabel label = new JLabel();
                label.setBounds(new Rectangle(0, 4, 292, 16));
                label.setText(this.description);
                label.setToolTipText(this.helpText);
                
                JRadioButton radioYes = new JRadioButton();
                radioYes.setBounds(new Rectangle(385, 0, 46, 24));
                radioYes.setSelected(true);
                radioYes.setText("Yes");

                JRadioButton radioNo = new JRadioButton();
                radioNo.setBounds(new Rectangle(464, 0, 40, 24));
                radioNo.setName("No");
                radioNo.setText("No");
                
                JPanel panel = new JPanel();
                panel.setLayout(null);
                panel.add(label);
                panel.add(radioYes);
                panel.add(radioNo);
                
                return panel;
            }
        }
        else if ("TEXT_FIELD".equals(category))
        {
            JLabel label = new JLabel();
            label.setBounds(new Rectangle(0, 2, 292, 16));
            label.setText(this.description);
            label.setToolTipText(this.helpText);
            
            JTextField textField = new JTextField();
            textField.setBounds(new Rectangle(420, 0, 115, 20));
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setText((String)this.getDefaultValue());
            
            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.add(label);
            panel.add(textField);
            
            return panel;
        }
        // TODO Auto-generated method stub
        return null;
    }
    
    
	
/*
	public boolean isWithinRange(T newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}
