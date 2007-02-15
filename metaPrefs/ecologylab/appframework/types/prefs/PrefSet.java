/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JComponent;

import ecologylab.appframework.ObjectRegistry;
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
    
    public void processPrefs()
    {
        for (int i=0; i<size(); i++)
        {
            Pref pref = (Pref) get(i);
            registerPref(pref.name, pref);
        }
    }
    
    /**
     * 
     */
    public PrefSet() 
    {
        // TODO Auto-generated constructor stub
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
    
    protected Pref lookupPref(String name)
    {
        Pref pref = allPrefsMap().lookupObject(name);
        return pref;
    }
    
    public Integer lookupInt(String name) throws ClassCastException
    {
        return (Integer)lookupPref(name).value();
    }
    
    public Boolean lookupBoolean(String name) throws ClassCastException
    {
        return (Boolean)lookupPref(name).value();
    }
    
    public Float lookupFloat(String name) throws ClassCastException
    {
        return (Float)lookupPref(name).value();
    }
    
    public String lookupString(String name) throws ClassCastException
    {
        return (String)lookupPref(name).value();
    }
}
