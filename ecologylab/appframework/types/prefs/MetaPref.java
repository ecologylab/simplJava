/**
 * 
 */
package ecologylab.appframework.types.prefs;

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
     * Returns whether or not a widget uses a slider.
     * 
     * @return True = Uses a slider. False = Doesn't.
     */
    public boolean widgetIsSlider()
    {
        if ("SLIDER".equals(widget))
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

}
