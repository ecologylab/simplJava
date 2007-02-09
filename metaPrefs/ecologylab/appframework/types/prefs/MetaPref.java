/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;

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
    
    public JPanel               jPanel = this.getWidget();
	
//	@xml_attribute	T			defaultValue;
	
//	@xml_nested		RangeState<T>	range;
	/**
	 * 
	 */
	public MetaPref()
	{
		super();
	}
	
	public abstract T getDefaultValue();

    public String getCategory()
    {
        return category;
    }

    public JPanel getWidget()
    {
        //println("getting widget: ");
        if ("RADIO".equals(widget))
        {
            // TODO don't need this after figure out how to define n-radio buttons
            if (this.getDefaultValue() instanceof Boolean)
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
                
                JRadioButton radioYes = new JRadioButton();
                radioYes.setBounds(new Rectangle(405, 7, 46, 32));
                radioYes.setSelected(yesVal);
                radioYes.setName("Yes");
                radioYes.setText("Yes");

                JRadioButton radioNo = new JRadioButton();
                radioNo.setBounds(new Rectangle(484, 7, 40, 32));
                radioNo.setSelected(noVal);
                radioNo.setName("No");
                radioNo.setText("No");
                
                ButtonGroup radioPair = new ButtonGroup();
                radioPair.add(radioYes);
                radioPair.add(radioNo);
                
                JPanel panel = new JPanel();
                panel.setSize(new java.awt.Dimension(586,40));
                panel.setLayout(null);
                panel.add(label);
                panel.add(radioYes);
                panel.add(radioNo);
                panel.setVisible(true);
                
                return panel;
            }
        }
        else if ("TEXT_FIELD".equals(widget))
        {
            //println("Generating text fields");
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
            
            JPanel panel = new JPanel();
            panel.setSize(new java.awt.Dimension(586,35));
            panel.setLayout(null);
            panel.add(label);
            panel.add(textField);
            panel.setVisible(true);
            
            return panel;
        }
        // TODO Auto-generated method stub
        return null;
    }
    
    public void print()
    {
        println(this.id + '\n' +
                this.description + '\n' +
                this.category + '\n' +
                this.helpText + '\n' +
                this.widget);
        println("" + this.getDefaultValue() + '\n');
    }

    public boolean widgetIsRadio()
    {
        if ("RADIO".equals(widget))
            return true;
        return false;
    }
    
    public boolean widgetIsTextField()
    {
        if ("TEXT_FIELD".equals(widget))
            return true;
        return false;
    }
	
/*
	public boolean isWithinRange(T newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}
