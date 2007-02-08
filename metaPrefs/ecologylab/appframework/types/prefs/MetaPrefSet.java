/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.element.ArrayListState;

/**
 * @author Cae
 *
 */

@xml_inherit
public class MetaPrefSet extends ArrayListState<MetaPref>
{

	public void processMetaPrefs()
	{
		for (int i=0; i<size(); i++)
		{
			MetaPref metapref = (MetaPref) get(i);
			println(metapref.id + '\n' +
					metapref.description + '\n' +
					metapref.category + '\n' +
					metapref.helpText + '\n' +
					metapref.widget);
			println("" + metapref.getDefaultValue() + '\n');
		}
	}
	
	/**
	 * 
	 */
	public MetaPrefSet() 
	{
		// TODO Auto-generated constructor stub
	}

	
}
