/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.element.ArrayListState;

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
	private static final int TEXT_FIELD_PADDING = 50;

    private static final int RIGHT_GUI_INSET = 20;

    private static final int LEFT_GUI_INSET = 15;

    private static final int TOOLTIP_WRAP_WIDTH = 80;

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
	
	@xml_attribute	boolean		requiresRestart;
    
    // have to call getWidget() for each panel;
    // if we try to do so here, everything ends up null
    public JPanel               jPanel;
    
    ObjectRegistry<JComponent>  jComponentsMap;
    
    @xml_nested ArrayListState<Choice<T>> choices = null;
    
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
    
    public String getDescription()
    {
        return description;
    }
    
    public String getHelpText()
    {
        return helpText;
    }
    
    public String getID()
    {
        return id;
    }

    public abstract JPanel getWidget();
    
    public JLabel getLabel(JPanel panel)
    {
        // pass 0,0 for row,col - it doesn't actually matter.
        return createLabel(panel, 0, 0);
    }
    
    public void print()
    {
        println(this.id + '\n' +
                this.description + '\n' +
                this.category + '\n' +
                this.helpText + '\n' +
                this.widget);
        println("" + this.getDefaultValue());
        if (choices != null)
        {
            for (Choice choice : choices)
            {
                println("Choice: " + choice.name + ", " + choice.label);
            }
        }
        println("\n");
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
	
    /**
     * Construct a new instance of the Pref that matches this.
     * 
     * @return
     */
    protected abstract Pref<T> getPrefInstance();
    
    /**
     * Construct a new instance of the Pref that matches this.
     * Use this to fill-in the default value.
     * 
     * @return
     */
    public Pref<T> getDefaultPrefInstance()
    {
    	Pref<T> result	= getPrefInstance();
    	result.name		= id;
    	
    	result.setValue(this.getDefaultValue());
    	return result;
    }
    
    /**
     * Get the Pref object associated with this.
     * That is, look for one in the registry.
     * Return it if there is one.
     * Otherwise, get a default Pref based on this,
     * register it, and return it.
     * 
     * @return	The Pref object associated with this.
     */
    public Pref<T> getAssociatedPref()
    {
    	Pref result	= Pref.lookupPref(id);
    	if (result == null)
    	{
    		result	= getDefaultPrefInstance();
    		result.register();
    	}
    	return result;
    }
    
/*
	public boolean isWithinRange(T newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
    
    public abstract void revertToDefault();
    public abstract void setWidgetToPrefValue(T prefValue);
    
    private ObjectRegistry<JComponent> jComponentsMap()
    {
        ObjectRegistry<JComponent> result   = this.jComponentsMap;
        if (result == null)
        {
            result                          = new ObjectRegistry<JComponent>();
            this.jComponentsMap             = result;
        }
        return result;
    }

    protected void registerComponent(String labelAndName, JComponent jComponent)
    {
        //println("Registering: " + this.id+labelAndName);
        jComponentsMap().registerObject(this.id+labelAndName,jComponent);
    }
    
    protected JComponent lookupComponent(String labelAndName)
    {
        //println("Trying to fetch: " + labelAndName);
        JComponent jComponent = jComponentsMap().lookupObject(labelAndName);
        return jComponent;
    }
    
    protected JRadioButton createRadio(JPanel panel, ButtonGroup buttonGroup, boolean initialValue, String label, String name, int row, int col)
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        
        JRadioButton radioButton = new JRadioButton();
        
        radioButton.setSelected(initialValue);
        radioButton.setName(name);
        radioButton.setText(label);
        c.gridx = col;
        c.gridy = row;
        c.insets = new Insets(0,0,0,RIGHT_GUI_INSET); // top,left,bottom,right
        
        buttonGroup.add(radioButton);
        
        panel.add(radioButton, c);
        registerComponent(name, radioButton);
        
        return radioButton;
    }
    
    protected JTextField createTextField(JPanel panel, String initialValue, String labelAndName, int row, int col)
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        
        JTextField textField = new JTextField();
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setText(initialValue);
        textField.setName(labelAndName);
        c.gridx = col;
        c.gridy = row;
        c.insets = new Insets(0,0,0,RIGHT_GUI_INSET); // top,left,bottom,right
        c.ipadx = TEXT_FIELD_PADDING;
        
        panel.add(textField, c);
        registerComponent(labelAndName, textField);
        
        return textField;
    }
    
    protected JLabel createLabel(JPanel panel, int row, int col)
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        
        JLabel label = new JLabel();
        String wrapText = "<html>" + description + "</html>";
        label.setText(wrapText);
        
        //nasty workaround because there is no API option to wrap tooltips
        String formattedToolTip = wrapTooltip();
        
        label.setToolTipText(formattedToolTip);
        label.setHorizontalTextPosition(SwingConstants.LEADING);
        label.setVerticalAlignment(SwingConstants.TOP);
        c.gridx = col;
        c.gridy = row;
        c.weightx = 0.5;
        c.insets = new Insets(0,LEFT_GUI_INSET,0,0); // top,left,bottom,right
        
        panel.add(label, c);
        
        return label;
    }

    /**
     * This allows you to wrap the help tooltip text, because there is no
     * way to normally do this.
     * @return
     */
    private String wrapTooltip()
    {
        String formattedToolTip = "<html>";
        if (helpText != null && helpText != "")
        {
            int tiplen = helpText.length();
            int wrapAt = TOOLTIP_WRAP_WIDTH;
            int nowAt = 0;
            int breakAt = 0;
            if (wrapAt > tiplen-1)
            {
                formattedToolTip = formattedToolTip.concat(helpText.substring(nowAt, tiplen) + "<br>");
            }
            else
            {
                do
                {
                    nowAt = breakAt;
                    breakAt = helpText.indexOf(" ", (nowAt+wrapAt));
                    if (breakAt > tiplen-1)
                    {
                        //System.out.print("breakAt " + breakAt + " is past length of string " + tiplen + "\n");
                        //System.out.print("remaining string: " + this.helpText.substring(nowAt,tiplen) + "\n");
                        formattedToolTip = formattedToolTip.concat(helpText.substring(nowAt, tiplen) + "<br>");
                    }
                    else if (breakAt > 0)
                    {
                        //System.out.print("cut is nowAt " + nowAt + " to breakAt " + breakAt + "\n");
                        formattedToolTip = formattedToolTip.concat(helpText.substring(nowAt, breakAt) + "<br>");
                    }
                    else
                    {
                        //System.out.print("remaining string: " + this.helpText.substring(nowAt,tiplen) + "\n");
                        formattedToolTip = formattedToolTip.concat(helpText.substring(nowAt, tiplen) + "<br>");
                        break;
                    }
                } while(nowAt < tiplen-1);
            }
        }
        formattedToolTip = formattedToolTip.concat("</html>");
        return formattedToolTip;
    }

    /**
     * 
     * @return
     */
    public abstract T getPrefValue();
}
