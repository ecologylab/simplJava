package ecologylab.tests;

import ecologylab.appframework.types.prefs.Pref;
import ecologylab.appframework.types.prefs.PrefString;
import ecologylab.generic.Debug;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.types.element.HashMapState;

public class TestMaps extends Debug
{

	/**
	 * @param args
	 * @throws XMLTranslationException 
	 */
	public static void main(String[] args) throws XMLTranslationException
	{
		Class[] CLASSES =
		{
				HashMapState.class, PrefString.class
		};
		TranslationScope TS	= TranslationScope.get("foo", CLASSES);
		
		HashMapState<String, Pref<?>> prefs = new HashMapState<String, Pref<?>>();
		
		prefs.add(new PrefString("1", "1a"));
		prefs.add(new PrefString("2", "2b"));
		prefs.add(new PrefString("3", "3c"));
		
		prefs.translateToXML(System.out);
		println("\n");
		ElementState.translateFromXMLCharSequence(prefs.translateToXML(), TS).translateToXML(System.out);
	}

}
