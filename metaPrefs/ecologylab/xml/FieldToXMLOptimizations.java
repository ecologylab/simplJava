/**
 * 
 */
package ecologylab.xml;

/**
 * Small data structure used to optimize translation to XML.
 *
 * @author andruid
 */
class TagMapEntry
{
   public final String startOpenTag;
   public final String closeTag;

   TagMapEntry(String tagName)
   {
	  startOpenTag	= "<" + tagName;
	  closeTag		= "</" + tagName + ">";
   }
   public String toString()
   {
   		return "TagMapEntry" + closeTag;
   }
}