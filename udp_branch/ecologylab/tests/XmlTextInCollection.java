/**
 * 
 */
package ecologylab.tests;

import ecologylab.generic.Debug;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.library.xaml.TextBlockState;
import ecologylab.xml.types.element.ArrayListState;

/**
 * @author awebb
 *
 */
public class XmlTextInCollection
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		TextBlockState textBlock = new TextBlockState("If you can read me, things are working. :)");
		
		ArrayListState<TextBlockState> textBlockCollection = new ArrayListState<TextBlockState>();
		
		textBlockCollection.add(textBlock);
		
		try
		{
			System.out.println(textBlockCollection.translateToXML());
		} catch (XMLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
