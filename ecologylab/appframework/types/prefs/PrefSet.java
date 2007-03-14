/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JComponent;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.element.ArrayListState;

/**
 * @author Cae
 *
 */

@xml_inherit
public class PrefSet extends ArrayListState<Pref>
{
    ObjectRegistry<Pref> allPrefsMap;
  
    /*
    /**
     * 
     */
    public PrefSet() 
    {
    }

    private ObjectRegistry<Pref> allPrefsMap()
    {
        ObjectRegistry<Pref> result     = this.allPrefsMap;
        if (result == null)
        {
            result                      = new ObjectRegistry<Pref>();
            this.allPrefsMap            = result;
        }
        return result;
    }
    
    protected void registerPref(String name, Pref pref)
    {
        allPrefsMap().registerObject(name,pref);
    }
    
    public Pref lookupPref(String name)
    {
        Pref pref = allPrefsMap().lookupObject(name);
        return pref;
    }
    
    public Integer lookupInt2(String name) throws ClassCastException
    {
        return (Integer)lookupPref(name).value();
    }
    
    public int lookupInt(String name, int defaultValue) throws ClassCastException
    {
        PrefInt prefInt = ((PrefInt)lookupPref(name));
		return (prefInt == null) ? defaultValue : prefInt.value();
    }
    public int lookupInt(String name) throws ClassCastException
    {
        return lookupInt(name, 0);
    }
   
    public boolean lookupBoolean(String name, boolean defaultValue) throws ClassCastException
    {
        PrefBoolean prefBoolean = ((PrefBoolean)lookupPref(name));
		return (prefBoolean == null) ? defaultValue : prefBoolean.value();
    }
    public boolean lookupBoolean(String name) throws ClassCastException
    {
        return lookupBoolean(name, false);
    }
       
    public float lookupFloat(String name, float defaultValue) throws ClassCastException
    {
        PrefFloat prefFloat = ((PrefFloat)lookupPref(name));
		return (prefFloat == null) ? defaultValue : prefFloat.value();
    }
    public float lookupFloat(String name) throws ClassCastException
    {
        return lookupFloat(name, 1.0f);
    }
   
    public String lookupString(String name, String defaultValue) throws ClassCastException
    {
        PrefString prefString = ((PrefString)lookupPref(name));
		return (prefString == null) ? defaultValue : prefString.value();
    }
    public String lookupString(String name) throws ClassCastException
    {
        return lookupString(name, null);
    }
       
    public ElementState lookupElementState(String name) throws ClassCastException
    {
        return ((PrefElementState)lookupPref(name)).value();
    }
    
    public boolean hasPref(String name)
    {
        return allPrefsMap().containsKey(name);
    }

    public void modifyPref(String name, Pref newPref)
    {
        allPrefsMap().modifyObject(name, newPref);
    }
    
    /**
     * Register the Pref, as well as adding it to the super ArrayListState.
     * @param that
     * @return
     */
    public boolean add(Pref that)
    {
    	boolean result	= super.add(that);
    	registerPref(that.name, that);
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
		registerPref(pref.name, pref);
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
		 return (PrefSet) ElementState.translateFromXML(purl, translationSpace);
    	
    }
}
