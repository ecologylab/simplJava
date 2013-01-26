/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import simpl.annotations.dbal.simpl_classes;
import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_tag;
import simpl.core.ElementState;
import simpl.core.TranslationContext;
import simpl.types.ScalarType;

import ecologylab.collections.Scope;

/**
 * Metadata about a Preference. Defines information to enable editing the Preference.
 * 
 * @author andruid
 * 
 */

@simpl_inherit
public abstract class MetaPref<T> extends ElementState implements WidgetTypes
{
	/** The global registry of Pref objects. Used for providing lookup services. */
	static final Scope<MetaPref>							allMetaPrefsMap	= new Scope<MetaPref>();

	/**
	 * Unique identifier for Preference name with convenient lookup in automatically generated
	 * HashMap.
	 */
	@simpl_tag("id")
	@simpl_scalar
	String																		m_id;

	/**
	 * This is the short text that appears in the Swing panel for editing the value.
	 */
	@simpl_scalar
	String																		description;

	/**
	 * This is longer text about that describes what the Preference does and more about the
	 * implications of its value.
	 */
	@simpl_scalar
	String																		helpText;

	/**
	 * Type of graphical user interface component used to interact with it. Must be a constant defined
	 * in the interface WidgetTypes If this value is left as null, it should default to TEXT_FIELD.
	 */
	@simpl_scalar
	String																		widget;

	/**
	 * Categories enable tabbed panes of preferences to be edited.
	 */
	@simpl_scalar
	String																		category;

	/**
	 * Whether or not the application has to restart for this pref change to take effect.
	 */
	@simpl_scalar
	boolean																		requiresRestart;

	/**
	 * optional; for preferences with three or more choices
	 */
	@simpl_collection
	@simpl_classes(
	{ ChoiceBoolean.class, ChoiceFloat.class, ChoiceInt.class })
	ArrayList<Choice<T>>											choices;

	ScalarType<T>															scalarType;

	/**
	 * LinkedHashMap to make locating exact choices easier
	 */
	private LinkedHashMap<String, Choice<T>>	choiceList;

	ValueChangedListener											valueChangedListener;

	// @xml_attribute T defaultValue;

	// @xml_nested RangeState<T> range;
	/**
	 * Instantiate.
	 * 
	 * @param scalarType
	 *          TODO
	 */
	public MetaPref(ScalarType scalarType)
	{
		super();
		this.scalarType = scalarType;
	}

	/**
	 * Gets the default value of a MetaPref; type-specific behavior.
	 * 
	 * @return Default value of MetaPref
	 */
	public abstract T getDefaultValue();

	/**
	 * Gets the min value of the range of a MetaPref.
	 * 
	 * @return Min value of a MetaPref
	 */
	public abstract T getMinValue();

	/**
	 * Gets the max value of the range of a MetaPref.
	 * 
	 * @return Max value of a MetaPref
	 */
	public abstract T getMaxValue();

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
		String result = description;
		if (requiresRestart)
			result += "<br><weak><i>Requires restart to take effect.</i></weak>";
		return result;
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
		return m_id;
	}

	/**
	 * Gets the Choices of a MetaPref. This is optional and may be null.
	 * 
	 * @return ID (unique) of MetaPref.
	 */
	public ArrayList<Choice<T>> getChoices()
	{
		return choices;
	}

	/**
	 * Returns true if the metaPref has choices. False otherwise
	 * 
	 * @return
	 */
	public boolean hasChoices()
	{
		return choices != null;
	}

	/**
	 * Get a Choice from the list of choices, whose value matches the value passed in.
	 * 
	 * @param value
	 *          Value to find
	 * @return Choice whose value equals the passed in value
	 */
	public Choice<T> getChoiceByValue(String value)
	{
		if (choiceList == null)
			populateChoiceList();
		return choiceList.get(value);
	}

	/**
	 * Get Choice's name, for a choice whose value that matches the given value.
	 * 
	 * @param value
	 * @return Name of choice
	 */
	public String getChoiceNameByValue(String value)
	{
		return getChoiceByValue(value).getName();
	}

	/**
	 * Get Choice's name, for the choice at the given index.
	 * 
	 * @param index
	 * @return Name of choice
	 */
	public String getChoiceNameByIndex(int index)
	{
		return choices.get(index).getName();
	}

	/**
	 * Get the Choice at the given index.
	 * 
	 * @param index
	 * @return Name of choice
	 */
	public Choice<T> getChoiceByIndex(int index)
	{
		return choices.get(index);
	}

	/**
	 * Get the index of a given Choice.
	 * 
	 * @param choice
	 *          Given choice
	 * @return Index of Choice in choices
	 */
	public int getIndexByChoice(Choice<T> choice)
	{
		if (choiceList == null)
			populateChoiceList();
		return choices.indexOf(choice);
	}

	/**
	 * Get the index of a Choice with the given value.
	 * 
	 * @param value
	 * @return index of Choice with given value
	 */
	public int getIndexByValue(String value)
	{
		Choice<T> thisChoice = getChoiceByValue(value);
		return getIndexByChoice(thisChoice);
	}

	/**
	 * Populate choiceList; this allows for easy searching by value.
	 */
	private void populateChoiceList()
	{
		choiceList = new LinkedHashMap<String, Choice<T>>();
		for (Choice<T> choice : this.choices)
		{
			choiceList.put(choice.getValue().toString(), choice);
		}
	}

	/**
	 * Gives printed output showing id, description, category, helpText, widget, default value, and
	 * choices for a MetaPref.
	 */
	public void print()
	{
		println(this.m_id + '\n' + this.description + '\n' + this.category + '\n' + this.helpText
				+ '\n' + this.widget);
		println("" + this.getDefaultValue());
		if (choices != null)
		{
			for (Choice<T> choice : choices)
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
		if (RADIO_BUTTONS.equals(widget))
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
		if (SLIDER.equals(widget))
			return true;
		return false;
	}

	/**
	 * Returns whether or not a widget uses a spinner.
	 * 
	 * @return True = Uses a spinner. False = Doesn't.
	 */
	public boolean widgetIsSpinner()
	{
		if (SPINNER.equals(widget))
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
		if (TEXT_FIELD.equals(widget))
			return true;
		return false;
	}

	/**
	 * Returns whether or not a widget uses check boxes.
	 * 
	 * @return True = Uses check boxes. False = Doesn't.
	 */
	public boolean widgetIsCheckBox()
	{
		if (CHECK_BOX.equals(widget))
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
		if (DROP_DOWN.equals(widget))
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
		if (COLOR_CHOOSER.equals(widget))
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
		if (FILE_CHOOSER.equals(widget))
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
	 * Create an instance of our associated type, from a String.
	 * 
	 * @param string
	 * @return
	 */
	public T getInstance(String string)
	{
		return scalarType.getInstance(string);
	}

	public T getInstance(T value)
	{
		return value;
	}

	/**
	 * Construct a new instance of the Pref that matches this. Use this to fill-in the default value.
	 * 
	 * @return
	 */
	public Pref<T> getDefaultPrefInstance()
	{
		Pref<T> result = getPrefInstance();
		result.name = m_id;

		result.setValue(this.getDefaultValue());
		return result;
	}

	/**
	 * Get the Pref object associated with this. That is, look for one in the registry. Return it if
	 * there is one. Otherwise, get a default Pref based on this, register it, and return it.
	 * 
	 * @return The Pref object associated with this MetaPref.
	 */
	public Pref<T> getAssociatedPref()
	{
		Pref result = Pref.lookupPref(m_id);
		if (result == null)
		{
			result = getDefaultPrefInstance();
			result.register();
		}
		return result;
	}

	/**
	 * Set object that receives a callback when the value of a Pref is changed through the
	 * PrefsEditor.
	 * 
	 * @param valueChangedListener
	 */
	public void setValueChangedListener(ValueChangedListener valueChangedListener)
	{
		this.valueChangedListener = valueChangedListener;
	}

	/**
	 * Set object that receives a callback when the value of a Pref is changed through the
	 * PrefsEditor.
	 * 
	 * @param valueChangedListener
	 */
	public static void setValueChangedListener(String metaPrefId,
			ValueChangedListener valueChangedListener)
	{
		MetaPref metaPref = lookup(metaPrefId);
		if (metaPref != null)
			metaPref.setValueChangedListener(valueChangedListener);
	}

	/**
	 * Create an entry for this in the allMetaPrefsMap.
	 * 
	 */
	void register()
	{
		allMetaPrefsMap.put(this.m_id, this);
	}

	/**
	 * Look up a MetaPref by name in the map of all MetaPrefs
	 * 
	 * @param id
	 *          Name of MetaPref
	 * 
	 * @return MetaPref with the given id
	 */
	public static MetaPref lookup(String id)
	{
		MetaPref metaPref = allMetaPrefsMap.get(id);
		return metaPref;
	}

	public ValueChangedListener getValueChangedListener()
	{
		return valueChangedListener;
	}

	@Override
	public void deserializationPostHook(TranslationContext translationContext, Object object)
	{
		MetaPref metaPref = (MetaPref) object;

		if (metaPref.parent() instanceof MetaPrefSet)
		{
			((MetaPrefSet) metaPref.parent()).addEntryToCategoryMap(metaPref);
			metaPref.getAssociatedPref(); // create one if needed
		}
		else
		{
			weird("parent of metaPref should be MetaPrefSet.");
		}
	}

	/*
	 * public boolean isWithinRange(T newValue) { return (range == null) ? true :
	 * range.isWithinRange(newValue); }
	 */

}
