package ecologylab.xml.rss;

import ecologylab.xml.*;
import java.util.ArrayList;

public class Channel extends ElementState
{
   public ElementState			title;
   public ElementState			description;
   
   public ArrayList itemSet	=	new ArrayList();
	
   	public void addNestedElement(ElementState elementState)
	   throws XmlTranslationException
   	{
   		if (elementState instanceof Item)
		   itemSet.add(elementState);
		else
		   super.addNestedElement(elementState);
   	}
   
}
