package ecologylab.standalone;

import ecologylab.appframework.types.AppFrameworkTranslations;
import ecologylab.appframework.types.prefs.Pref;
import ecologylab.appframework.types.prefs.PrefInt;
import ecologylab.appframework.types.prefs.PrefString;
import ecologylab.xml.ElementState;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.types.element.HashMapState;

public class TestMaps
{

	/**
	 * @param args
	 * @throws XMLTranslationException 
	 */
	public static void main(String[] args) throws XMLTranslationException
	{
		HashMapState<String, Pref<?>> prefs = new HashMapState<String, Pref<?>>();
		
		prefs.add(new PrefString("1", "1a"));
		prefs.add(new PrefString("2", "2b"));
		prefs.add(new PrefString("3", "3c"));
		
		prefs.translateToXML(System.out);
		ElementState.translateFromXMLCharSequence(prefs.translateToXML(), AppFrameworkTranslations.get()).translateToXML(System.out);
	}

}
