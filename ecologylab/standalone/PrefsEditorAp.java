/**
 * 
 */
package ecologylab.standalone;

import ecologylab.appframework.ApplicationEnvironment;
import ecologylab.appframework.types.prefs.PrefTranslations;
import ecologylab.appframework.types.prefs.gui.PrefsEditor;
import ecologylab.xml.XmlTranslationException;

/**
 * Standalone app to open a prefs editing dialog.
 * 
 * @author Cae
 * @author andruid
 */
public class PrefsEditorAp extends ApplicationEnvironment 
{
	
	public PrefsEditorAp(String[] args) throws XmlTranslationException 
	{
		super("ecologyLabFundamental", PrefTranslations.get(), args, 0);

		PrefsEditor mgr = (PrefsEditor) this.createPrefsEditor(true, true);

	}
	/**
	 * @param args
	 * @throws XmlTranslationException 
	 */
	public static void main(String[] args) throws XmlTranslationException 
	{
		new PrefsEditorAp(args);
	}

}
