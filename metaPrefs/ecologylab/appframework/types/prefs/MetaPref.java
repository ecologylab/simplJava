/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.Rectangle;
import java.util.HashMap;

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
    
    ObjectRegistry<JComponent>  jComponentsMap;
    
    @xml_collection ArrayListState<Choice<T>> choices = null;
	
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

    public abstract JPanel getWidget();
    
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
    
    public abstract void revertToDefault();
    
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
        jComponentsMap().registerObject(this.id+labelAndName,jComponent);
    }
    
    protected JComponent lookupComponent(String labelAndName)
    {
        JComponent jComponent = jComponentsMap().lookupObject(labelAndName);
        return jComponent;
    }
}
