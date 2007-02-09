/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
    
	public void processMetaPrefs()
	{
		for (int i=0; i<size(); i++)
		{
			MetaPref metapref = (MetaPref) get(i);
			/* println(metapref.id + '\n' +
					metapref.description + '\n' +
					metapref.category + '\n' +
					metapref.helpText + '\n' +
					metapref.widget);
			println("" + metapref.getDefaultValue() + '\n'); */
            metapref.jPanel = metapref.getWidget();
            ArrayList<MetaPref> metaPrefList = categoryToMetaPrefs.get(metapref.getCategory());
            if (metaPrefList == null)
            {
                metaPrefList = new ArrayList<MetaPref>();
                categoryToMetaPrefs.put(metapref.category, metaPrefList);
            }
            metaPrefList.add(metapref);
		}
	}
	
	/**
	 * 
	 */
	public MetaPrefSet() 
	{
		// TODO Auto-generated constructor stub
	}

    public Set getCategories()
    {
        return categoryToMetaPrefs.keySet();
    }

	
}
