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
        JTextField textField = (JTextField)lookupComponent(this.id+"textField");
        textField.setText(this.getDefaultValue().toString());
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
        textField.setText(this.getDefaultValue().toString());
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
	public boolean isWithinRange(Integer newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}