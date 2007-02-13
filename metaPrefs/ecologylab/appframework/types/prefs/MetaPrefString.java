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
        JLabel label = new JLabel();
        label.setBounds(new Rectangle(0, 10, 340, 32));
        String wrapText = "<html>" + this.description + "</html>";
        label.setText(wrapText);
        label.setToolTipText(this.helpText);
        label.setHorizontalTextPosition(SwingConstants.LEADING);
        
        JTextField textField = new JTextField();
        textField.setBounds(new Rectangle(410, 17, 115, 20));
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setText(this.getDefaultValue());
        textField.setName("textField");
        registerComponent("textField",textField);
        
        JPanel panel = new JPanel();
        panel.setSize(new java.awt.Dimension(586,35));
        panel.setLayout(null);
        panel.add(label);
        panel.add(textField);
        panel.setVisible(true);
        
        return panel;
    }
	
/*
	public boolean isWithinRange(String newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}