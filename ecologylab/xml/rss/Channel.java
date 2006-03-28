package ecologylab.xml.rss;

import ecologylab.xml.*;
import java.util.ArrayList;

public class Channel extends ElementStateWithLeafElements
{
   public String			title;
   public String			description;
   
   public ArrayList itemSet	=	new ArrayList();
	
   static final String[]		LEAF_ELEMENT_FIELD_NAMES	= {"title", "description"};
   
   static
   {
	   defineLeafElementFieldNames(LEAF_ELEMENT_FIELD_NAMES);
   }
   
   public void addNestedElement(ElementState elementState)
	   throws XmlTranslationException
   	{
   		if (elementState instanceof Item)
		   itemSet.add(elementState);
		else
		   super.addNestedElement(elementState);
   	}
   
}
