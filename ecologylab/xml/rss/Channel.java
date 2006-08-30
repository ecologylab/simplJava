package ecologylab.xml.rss;

import ecologylab.xml.*;
import java.util.ArrayList;
import java.util.Collection;

public class Channel extends ArrayListState
{
   public String			title;
   public String			description;
   
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
