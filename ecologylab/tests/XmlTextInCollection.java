/**
 * 
 */
package ecologylab.tests;

import java.util.ArrayList;

import ecologylab.serialization.library.xaml.TextBlockState;
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
		
		ArrayList<TextBlockState> textBlockCollection = new ArrayList<TextBlockState>();
		
		textBlockCollection.add(textBlock);
		
//		try
//		{
//			//System.out.println(textBlockCollection.translateToXML());
//		} catch (XMLTranslationException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
