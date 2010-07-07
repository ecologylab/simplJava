/**
 * 
 */
package ecologylab.standalone;

import ecologylab.generic.Debug;
import ecologylab.xml.ElementState;
import ecologylab.xml.SIMPLTranslationException;
import ecologylab.xml.library.jnlp.JnlpState;
import ecologylab.xml.library.jnlp.JnlpTranslations;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 *
 */
public class JNLPTestApp
{

	/**
	 * @param args
	 * @throws SIMPLTranslationException 
	 */
	public static void main(String[] args)
	{
		Debug.println("translate from...");
		JnlpState jnlp;
		try
		{
			jnlp = (JnlpState) ElementState.translateFromXML("c:\\jnlptest.jnlp", JnlpTranslations.get());

			Debug.println("...done.");

			Debug.println("translate to...");
			jnlp.serialize("c:\\jnlp2.txt");
			Debug.println("...done.");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
