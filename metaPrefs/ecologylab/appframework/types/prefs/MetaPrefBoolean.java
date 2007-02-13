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
            yesModel.setSelected(yesVal);
        }
        else
        {
            JRadioButton noButton = (JRadioButton)lookupComponent(this.id+"No");
            ButtonModel noModel = noButton.getModel();
            noModel.setSelected(!yesVal);
        }
    }

    public @Override
    JPanel getWidget()
    {
        Boolean defVal = (Boolean)this.getDefaultValue();
        boolean yesVal = defVal;
        boolean noVal = !defVal;
        
        //println("Generating boolean radio button");
        JLabel label = new JLabel();
        label.setBounds(new Rectangle(0, 4, 340, 32));
        // this is to make the text automatically wrap
        String wrapText = "<html>" + this.description + "</html>";
        label.setText(wrapText);
        label.setToolTipText(this.helpText);
        label.setHorizontalTextPosition(SwingConstants.LEADING);
        
        ButtonGroup radioPair = new ButtonGroup();
        
        JPanel panel = new JPanel();
        panel.setSize(new java.awt.Dimension(586,40));
        panel.setLayout(null);
        panel.add(label);

        this.createRadio(panel, radioPair, yesVal, "Yes", 405);
        this.createRadio(panel, radioPair, noVal, "No", 484);

        panel.setVisible(true);
        
        return panel;
    }
	
    protected JRadioButton createRadio(JPanel panel, ButtonGroup buttonGroup, boolean initialValue, String labelAndName, int x)
    {
        JRadioButton radioButton = new JRadioButton();
        radioButton.setBounds(new Rectangle(x, 7, 46, 32));
        radioButton.setSelected(initialValue);
        radioButton.setName(labelAndName);
        radioButton.setText(labelAndName);
        
        buttonGroup.add(radioButton);
        
        panel.add(radioButton);
        registerComponent(labelAndName, radioButton);
        
        return radioButton;
    }
    
/*
	public boolean isWithinRange(Boolean newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}