/**
 * 
 */
package ecologylab.standalone;

import ecologylab.appframework.ApplicationEnvironment;
import ecologylab.appframework.types.prefs.PrefsTranslationsProvider;
import ecologylab.appframework.types.prefs.gui.PrefsEditor;
import ecologylab.xml.XMLTranslationException;

/**
 * Standalone app to open a prefs editing dialog.
 * 
 * @author Cae
 * @author andruid
 */
public class PrefsEditorAp extends ApplicationEnvironment 
{
	
	public PrefsEditorAp(String[] args) throws XMLTranslationException 
	{
		super("ecologyLabFundamental", PrefsTranslationsProvider.get(), null, args, 0);

		PrefsEditor mgr = (PrefsEditor) this.createPrefsEditor(true, true);
	}
	/**
	 * @param args
	 * @throws XMLTranslationException 
	 */
	public static void main(String[] args) throws XMLTranslationException 
	{
		new PrefsEditorAp(args);
	}

}
