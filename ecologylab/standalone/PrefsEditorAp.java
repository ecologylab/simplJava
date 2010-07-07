/**
 * 
 */
package ecologylab.standalone;

import ecologylab.appframework.ApplicationEnvironment;
import ecologylab.appframework.types.prefs.PrefsTranslationsProvider;
import ecologylab.appframework.types.prefs.gui.PrefsEditor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;

/**
 * Standalone app to open a prefs editing dialog.
 * 
 * @author Cae
 * @author andruid
 */
public class PrefsEditorAp extends ApplicationEnvironment 
{
	
	public PrefsEditorAp(String[] args) throws SIMPLTranslationException 
	{
		super("ecologyLabFundamental", PrefsTranslationsProvider.get(), (TranslationScope) null, args, 0);

		PrefsEditor mgr = (PrefsEditor) this.createPrefsEditor(true, true);
	}
	/**
	 * @param args
	 * @throws SIMPLTranslationException 
	 */
	public static void main(String[] args) throws SIMPLTranslationException 
	{
		new PrefsEditorAp(args);
	}

}
