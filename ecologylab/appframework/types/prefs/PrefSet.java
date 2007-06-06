/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.io.File;

import ecologylab.appframework.ApplicationEnvironment;
import ecologylab.appframework.ApplicationPropertyNames;
import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.element.ArrayListState;

/**
 * A serial set of Pref objects.
 * Used for reading and writing (load and save).
 * The static allPrefsMap in Pref is used for lookup.
 * 
 * @author Cae
 * @author andruid
 */

@xml_inherit
public class PrefSet extends ArrayListState<Pref> implements ApplicationPropertyNames
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
    public boolean add(Pref pref)
    {
    	boolean result	= super.add(pref);
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
	protected void createChildHook(ElementState child)
	{
		Pref pref	= (Pref) child;
		pref.register();
	}
    /**
     * Read MetaPref declarations from a file or across the net.
     * 
     * @param purl
     * @param translationSpace
     * @return
     * @throws XmlTranslationException
     */
    public static PrefSet load(ParsedURL purl, TranslationSpace translationSpace) 
    throws XmlTranslationException
    {
    	File file	= purl.file();
    	PrefSet pS	= null;
    	if ((file != null) && file.exists())
    		pS = (PrefSet) ElementState.translateFromXML(purl, translationSpace);
        
        return pS;
    }
    /**
     * Read MetaPref declarations from a file or across the net.
     * 
     * @param prefXML
     * @param translationSpace
     * @return
     * @throws XmlTranslationException
     */
    public static PrefSet load(String prefXML, TranslationSpace translationSpace)
    throws XmlTranslationException
    {
        PrefSet pS = (PrefSet) ElementState.translateFromXML(prefXML, translationSpace);
        
        return pS;
    }
}
