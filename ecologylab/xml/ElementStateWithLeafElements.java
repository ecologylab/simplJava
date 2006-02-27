/**
 * 
 */
package ecologylab.xml;

import java.util.HashMap;

/**
 * An ElementState object that will contain XML leaf element fields, that is, fields
 * in which primitive values are defined as text nodes, with name a singleton element,
 * rather than as attributes.
 * 
 * @author andruid
 */
public class ElementStateWithLeafElements extends ElementState
{
	/**
	 * Holds the declarations of leaf element field names, for fast lookup
	 * during translation.
	 */
	protected static final HashMap			leafElementFields	= new HashMap();
	   

	/**
	 * 
	 */
	public ElementStateWithLeafElements()
	{
		super();
	}

	/**
	 * @return  Declarations of leaf element field names, for fast lookup
	 * during translation.
	 */
   public HashMap leafElementFields()
   {
	   return leafElementFields;
   }
   
   protected static void defineLeafElementFieldNames(String[] leafElementFieldNames)
   {
	   defineLeafElementFieldNames(leafElementFields, leafElementFieldNames);
   }
}
