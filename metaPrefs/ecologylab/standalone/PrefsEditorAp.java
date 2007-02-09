/**
 * 
 */
package ecologylab.standalone;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;


//import cf.services.messages.CFServicesTranslations;
import ecologylab.appframework.ApplicationEnvironment;
import ecologylab.appframework.types.prefs.MetaPrefSet;
import ecologylab.appframework.types.prefs.PrefTranslations;
import ecologylab.appframework.types.prefs.gui.PrefWidgetManager;
import ecologylab.net.ParsedURL;
import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.xml.ElementState;
import ecologylab.xml.XmlTranslationException;

/**
 * @author Cae
 *
 */
public class CFPrefsEdit extends ApplicationEnvironment 
{
	
	public CFPrefsEdit(String[] args) 
	{
		super("ecologyLabFundamental", DefaultServicesTranslations.get(), args);
		
		ParsedURL metaPrefsPURL = this.preferencesDir().getRelative("metaprefs.xml", "");

		try 
		{
			println("Loading preferences from: " + metaPrefsPURL);
			MetaPrefSet metaPrefSet	= (MetaPrefSet) ElementState.translateFromXML(metaPrefsPURL, PrefTranslations.get());

			//println("metaPrefSet.size() = " + metaPrefSet.size());
			metaPrefSet.processMetaPrefs();
            
            PrefWidgetManager mgr = new PrefWidgetManager(metaPrefSet);
		}
		catch (XmlTranslationException e)
		{
			// TODO Auto-generated catch block
			error(metaPrefsPURL, "Caught exception while reading preferences:");
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		new CFPrefsEdit(args);
	}

}
