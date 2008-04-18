/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.io.File;

import ecologylab.appframework.ApplicationPropertyNames;
import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.element.HashMapState;

/**
 * A serial set of Pref objects.
 * Used for reading and writing (load and save).
 * The static allPrefsMap in Pref is used for lookup.
 * 
 * @author Cae
 * @author andruid
 */

@xml_inherit
public class PrefSet extends HashMapState<String, Pref<?>> implements ApplicationPropertyNames
{
    /** No-argument constructor for XML translation. */
    public PrefSet() 
    {
    }

    /**
     * Register the Pref, as well as adding it to the super ArrayListState.
     * @param pref
     * @return
     */
    @Override public Pref<?> add(Pref<?> pref)
    {
    	Pref<?> result	= super.add(pref);
    	pref.register();
    	return result;
    }
    
	/**
	 * Perform custom processing on the newly created child node,
	 * just before it is added to this.
	 * <p/>
	 * This is part of depth-first traversal during translateFromXML().
	 * <p/>
	 * Add the entry to the category map.
	 * 
	 * @param child
	 */
    //TODO -- get rid of this when we make ArrayListState implement Collection!!!
    // (cause then this.add() will get called!)
	@Override protected void createChildHook(ElementState child)
	{
		Pref<?> pref	= (Pref<?>) child;
		pref.register();
	}
    /**
     * Read Pref declarations from a file or across the net.
     * 
     * @param purl
     * @param translationSpace
     * @return
     * @throws XMLTranslationException
     */
    public static PrefSet load(ParsedURL purl, TranslationScope translationSpace) 
    throws XMLTranslationException
    {
    	File file	= purl.file();
    	PrefSet pS	= null;
    	if ((file != null) && file.exists())
    		pS = (PrefSet) ElementState.translateFromXML(purl, translationSpace);
        
        return pS;
    }
    /**
     * Read Pref declarations from a file or across the net.
     * 
     * @param prefXML - Preferences in an XML format; to be translated into a PrefSet.
     * @param translationSpace
     * @return
     * @throws XMLTranslationException
     */
    public static PrefSet load(String filename, TranslationScope translationSpace)
    throws XMLTranslationException
    {
        PrefSet pS = (PrefSet) ElementState.translateFromXML(filename, translationSpace);
        
        return pS;
    }
    /**
     * Read Pref declarations from a file or across the net.
     * 
     * @param prefXML - Preferences in an XML format; to be translated into a PrefSet.
     * @param translationSpace
     * @return
     * @throws XMLTranslationException
     */
    public static PrefSet loadFromCharSequence(String prefXML, TranslationScope translationSpace)
    throws XMLTranslationException
    {
        PrefSet pS = (PrefSet) ElementState.translateFromXMLCharSequence(prefXML, translationSpace);
        
        return pS;
    }
    
    /**
     * Remove the Pref from this, and from the global set.
     * 
     * @param key
     * @return
     */
    public Pref<?> clearPref(String key)
    {
    	Pref.clearPref(key);
    	return super.remove(key);
    }

}
