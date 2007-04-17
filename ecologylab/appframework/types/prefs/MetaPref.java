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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
    /**
     * The padding between the default value in a text field and either 
     * side of the text field.
     */
    protected static final int TEXT_FIELD_PADDING = 50;

    /**
     * The inset between the right side of the gui panel and the right 
     * side of values.
     */
    protected static final int RIGHT_GUI_INSET = 20;

    /**
     * The inset between the left side of the gui panel and the left 
     * side of the descriptions.
     */
    protected static final int LEFT_GUI_INSET = 15;

    /**
	 * Unique identifier for Preference name with convenient lookup in 
     * automatically generated HashMap.
	 */
	@xml_attribute 	String		id;
	
	/**
	 * This is the short text that appears in the Swing panel for editing the value.
	 */
	@xml_attribute 	String		description;
	
	/**
	 * This is longer text about that describes what the Preference does and 
     * more about the implications of its value.
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
	
    /**
     * Whether or not the application has to restart for this pref change to take effect.
     */
	@xml_attribute	boolean		requiresRestart;
    
    /**
     * The JPanel associated with this metaPref; this is the values the pref has.
     * You have to call getWidget() for each panel after initialization.
     * Doing so here will just give null.
     */
    public JPanel               jPanel;
    
    /**
     * The object registry which the metapref gui components are stored in.
     */
    ObjectRegistry<JComponent>  jComponentsMap;
    
    /**
     * optional; for preferences with three or more choices
     */
    @xml_nested ArrayListState<Choice<T>> choices = null;
    
//	@xml_attribute	T			defaultValue;
	
//	@xml_nested		RangeState<T>	range;
	/**
	 * Instantiate.
	 */
	public MetaPref()
	{
		super();
	}
	
    /**
     * Gets the default value of a MetaPref; type-specific behavior. 
     * 
     * @return Default value of MetaPref
     */
	public abstract T getDefaultValue();

    /**
     * Gets the category for a MetaPref.
     * 
     * @return Category of MetaPref
     */
    public String getCategory()
    {
        return category;
    }
    
    /**
     * Gets the description for a MetaPref.
     * 
     * @return Description of MetaPref
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * Gets the help text for a MetaPref.
     * 
     * @return Help Text of MetaPref
     */
    public String getHelpText()
    {
        return helpText;
    }
    
    /**
     * Gets the ID of a MetaPref. This is a unique identifier.
     * 
     * @return ID (unique) of MetaPref.
     */
    public String getID()
    {
        return id;
    }
    
    /**
     * Gets the Choices of a MetaPref. This is optional and may be null.
     * 
     * @return ID (unique) of MetaPref.
     */
    public ArrayListState<Choice<T>> getChoices()
    {
        return choices;
    }

    /**
     * Gives printed output showing id, description, category, helpText,
     * widget, default value, and choices for a MetaPref.
     */
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

    /**
     * Returns whether or not a widget uses radio buttons.
     * 
     * @return True = Uses radio buttons. False = Doesn't.
     */
    public boolean widgetIsRadio()
    {
        if ("RADIO".equals(widget))
            return true;
        return false;
    }
    
    /**
     * Returns whether or not a widget uses one or more text fields.
     * 
     * @return True = Uses text field(s). False = Doesn't.
     */
    public boolean widgetIsTextField()
    {
        if ("TEXT_FIELD".equals(widget))
            return true;
        return false;
    }
    
    /**
     * Returns whether or not a widget uses check boxes.
     * 
     * @return True = Uses check boxes. False = Doesn't.
     */
    public boolean widgetIsCheckBoxes()
    {
        if ("CHECK_BOXES".equals(widget))
            return true;
        return false;
    }
    
    /**
     * Returns whether or not a widget uses a drop down menu.
     * 
     * @return True = Uses a drop down menu. False = Doesn't.
     */
    public boolean widgetIsDropDown()
    {
        if ("DROP_DOWN".equals(widget))
            return true;
        return false;
    }
    
    /**
     * Returns whether or not a widget uses a color chooser.
     * 
     * @return True = Uses a color chooser. False = Doesn't.
     */
    public boolean widgetIsColorChooser()
    {
        if ("COLOR_CHOOSER".equals(widget))
            return true;
        return false;
    }
    
    /**
     * Returns whether or not a widget uses a file chooser.
     * 
     * @return True = Uses a file chooser. False = Doesn't.
     */
    public boolean widgetIsFileChooser()
    {
        if ("FILE_CHOOSER".equals(widget))
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
     * @return	The Pref object associated with this MetaPref.
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
    
    /**
     * Returns the ObjectRegistry for this MetaPref's jComponents.
     * 
     * @return ObjectRegistry for MetaPref's jComponents.
     */
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

    /**
     * Registers a JComponent with the ObjectRegistry
     * 
     * @param labelAndName
     * @param jComponent
     */
    protected void registerComponent(String labelAndName, JComponent jComponent)
    {
        //println("Registering: " + this.id+labelAndName);
        jComponentsMap().registerObject(this.id+labelAndName,jComponent);
    }
    
    /**
     * Returns a JComponent from the ObjectRegistry by name
     * 
     * @param labelAndName
     * @return JComponent matching labelAndName from ObjectRegistry
     */
    protected JComponent lookupComponent(String labelAndName)
    {
        //println("Trying to fetch: " + labelAndName);
        JComponent jComponent = jComponentsMap().lookupObject(labelAndName);
        return jComponent;
    }

}
