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
import ecologylab.appframework.types.prefs.PrefSet;
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
        ParsedURL prefsPURL = this.preferencesDir().getRelative("prefs.xml", "");

		try 
		{
			println("Loading meta-preferences from: " + metaPrefsPURL);
			MetaPrefSet metaPrefSet	= (MetaPrefSet) ElementState.translateFromXML(metaPrefsPURL, PrefTranslations.get());
            println("Loading preferences from: " + prefsPURL);
            PrefSet prefSet = (PrefSet) ElementState.translateFromXML(prefsPURL, PrefTranslations.get());

			//println("metaPrefSet.size() = " + metaPrefSet.size());
			metaPrefSet.processMetaPrefs();
            //get or process prefs here
            prefSet.processPrefs();
            
            // we want to also pass in prefs to PrefWidgetManager
            PrefWidgetManager mgr = new PrefWidgetManager(metaPrefSet, prefSet, prefsPURL);
		}
		catch (XmlTranslationException e)
		{
			// TODO Auto-generated catch block
			error(metaPrefsPURL, "Caught exception while reading meta-preferences:");
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
