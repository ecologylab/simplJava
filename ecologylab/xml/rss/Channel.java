package ecologylab.xml.rss;

import ecologylab.xml.*;
import ecologylab.xml.ElementState.xml_leaf;

import java.util.ArrayList;
import java.util.Collection;

public @xml_inherit class Channel extends ArrayListState
{
   @xml_leaf	public String			title;
   @xml_leaf	public String			description;
   
   static final String[]	LEAF_ELEMENT_FIELD_NAMES	= {"title", "description"};
   
   /**
    * The array of Strings with the names of the leaf elements.
    * 
    * @return
    */
   protected String[] leafElementFieldNames()
   {
	   return LEAF_ELEMENT_FIELD_NAMES;
   }
}
