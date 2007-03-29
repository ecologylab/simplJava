/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
public class MetaPrefSet extends ArrayListState<MetaPref>
{
    public HashMap<String, ArrayList<MetaPref>> categoryToMetaPrefs = new HashMap<String, ArrayList<MetaPref>>();
    private ArrayList<String> orderOfTabs = new ArrayList<String>();
	/**
	 * Perform custom processing on the newly created child node,
	 * just before it is added to this.
	 * <p/>
	 * This is part of depth-first traversal during translateFromXML().
	 * <p/>
	 * Add the entry to the category map.
	 * <p/>
	 * Also, create a Pref to match the MetaPref child, if there isn't one already.
	 * 
	 * @param child
	 */
	protected void createChildHook(ElementState child)
	{
		MetaPref metaPref = (MetaPref) child;
		addEntryToCategoryMap(metaPref);
		metaPref.getAssociatedPref();	// create one if needed
	}
	/**
	 * Add entry
	 * @param metaPref
	 */
	void addEntryToCategoryMap(MetaPref metaPref)
	{
        metaPref.jPanel = metaPref.getWidget();
        ArrayList<MetaPref> metaPrefList = categoryToMetaPrefs.get(metaPref.getCategory());
        if (metaPrefList == null)
        {
            metaPrefList = new ArrayList<MetaPref>();
            categoryToMetaPrefs.put(metaPref.category, metaPrefList);
            orderOfTabs.add(metaPref.category);
        }
        metaPrefList.add(metaPref);		
	}
	/**
	 * 
	 */
	public MetaPrefSet() 
	{

	}

    public String[] getOrderedTabNames(String[] tabList)
    {
        return orderOfTabs.toArray(tabList);
    }
    
    public int getNumberOfTabs()
    {
        return orderOfTabs.size();
    }
    
    public Set getCategories()
    {
        return categoryToMetaPrefs.keySet();
    }

    
    /**
     * Read MetaPref declarations from a file or across the net.
     * 
     * @param purl
     * @param translationSpace
     * @return
     * @throws XmlTranslationException
     */
    public static MetaPrefSet load(File file, TranslationSpace translationSpace) 
    throws XmlTranslationException
    {
		 return load(new ParsedURL(file), translationSpace);
    	
    }
	
    /**
     * Read MetaPref declarations from a file or across the net.
     * 
     * @param purl
     * @param translationSpace
     * @return
     * @throws XmlTranslationException
     */
    public static MetaPrefSet load(ParsedURL purl, TranslationSpace translationSpace) 
    throws XmlTranslationException
    {
		 return (MetaPrefSet) ElementState.translateFromXML(purl, translationSpace);
    	
    }
}
