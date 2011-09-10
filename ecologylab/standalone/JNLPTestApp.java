/**
 * 
 */
package ecologylab.standalone;

import java.io.File;

import ecologylab.generic.Debug;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.Format;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.library.jnlp.JnlpState;
import ecologylab.serialization.library.jnlp.JnlpTranslations;

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
			jnlp = (JnlpState) JnlpTranslations.get().deserialize(new File("c:\\jnlptest.jnlp"),
					Format.XML);

			Debug.println("...done.");

			Debug.println("translate to...");

			ClassDescriptor.serialize(jnlp, new File("c:\\jnlp2.txt"), Format.XML);

			Debug.println("...done.");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
