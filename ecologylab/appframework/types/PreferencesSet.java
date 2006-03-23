package cf.services.messages;

import ecologylab.xml.ArrayListState;
import ecologylab.xml.ElementState;

/**
 * A top level message between javascript and CFSessionLauncher.
 */
public class PreferencesSet extends ArrayListState
{

	public PreferencesSet()
	{
		super();
	}
	   /**
	    * overriding the addElement of ElementState class to add non-primitive objects
	    */
	   	public void addNestedElement(ElementState elementState)
	   	{
	   		if (elementState instanceof Preference)
				set.add(elementState);
	   	}

}
