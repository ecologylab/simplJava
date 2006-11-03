package ecologylab.services.messages.studies;

import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.xml.TranslationSpace;

public class StudiesServicesTranslations extends DefaultServicesTranslations
{
	public static final Class[] STUDY_TRANSLATIONS = {
		ecologylab.services.messages.studies.ReadyMessage.class,
		ecologylab.services.messages.studies.SubmitMessage.class
	};
	/**
	 * This accessor will work from anywhere, in any order, and stay efficient.
	 * @return
	 */
	public static TranslationSpace get()
	{
		TranslationSpace tSpace = TranslationSpace.get(NAME, PACKAGE_NAME, TRANSLATIONS);
		tSpace.addTranslations(STUDY_TRANSLATIONS);
		
		//System.out.println("package name: " + STUDY_TRANSLATIONS[0].getPackage().getName());
		
		return tSpace;
	}
}
