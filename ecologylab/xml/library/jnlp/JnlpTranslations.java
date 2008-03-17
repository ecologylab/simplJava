/**
 * 
 */
package ecologylab.xml.library.jnlp;

import ecologylab.appframework.types.AppFrameworkTranslations;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.library.jnlp.applet.AppletDesc;
import ecologylab.xml.library.jnlp.applet.Param;
import ecologylab.xml.library.jnlp.application.ApplicationDesc;
import ecologylab.xml.library.jnlp.information.AssociationElement;
import ecologylab.xml.library.jnlp.information.Description;
import ecologylab.xml.library.jnlp.information.HomepageElement;
import ecologylab.xml.library.jnlp.information.Icon;
import ecologylab.xml.library.jnlp.information.InformationElement;
import ecologylab.xml.library.jnlp.information.MenuElement;
import ecologylab.xml.library.jnlp.information.OfflineAllowedElement;
import ecologylab.xml.library.jnlp.information.RelatedContentElement;
import ecologylab.xml.library.jnlp.information.ShortcutElement;
import ecologylab.xml.library.jnlp.resource.HrefBasedResource;
import ecologylab.xml.library.jnlp.resource.J2se;
import ecologylab.xml.library.jnlp.resource.Jar;
import ecologylab.xml.library.jnlp.resource.Nativelib;
import ecologylab.xml.library.jnlp.resource.Property;
import ecologylab.xml.library.jnlp.resource.ResourceElement;
import ecologylab.xml.library.jnlp.resource.ResourceElementArray;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class JnlpTranslations
{
	private static final String				JNLP_TRANSLATIONS_NAME	= "JNLP Translations";

	private static final String				JNLP_PACKAGE_NAME			= "ecologylab.xml.library.jnlp";

	private static final Class[]				JNLP_TRANSLATIONS			=
																						{
			AppletDesc.class, Param.class,

			ApplicationDesc.class,

			AssociationElement.class, Description.class, HomepageElement.class,
			Icon.class, InformationElement.class, MenuElement.class,
			RelatedContentElement.class, ShortcutElement.class,
			OfflineAllowedElement.class,

			AllPermissionsElement.class, HrefBasedResource.class, J2se.class,
			Jar.class, Nativelib.class, Property.class, ResourceElement.class,
			ResourceElementArray.class, JnlpState.class					};

	private static final TranslationSpace	inheritedTranslations[]	=
																						{
			AppFrameworkTranslations.get(),
			JnlpTranslations.getStudyTranslationsOnly()					};

	/**
	 * This accessor will work from anywhere, in any order, and stay efficient.
	 * 
	 * @return
	 */
	public static TranslationSpace get()
	{
		TranslationSpace result = TranslationSpace.get(JNLP_TRANSLATIONS_NAME,
				JNLP_TRANSLATIONS, inheritedTranslations);

		return result;
	}

	/**
	 * @return
	 */
	private static TranslationSpace getStudyTranslationsOnly()
	{
		TranslationSpace temp = TranslationSpace.get(JNLP_TRANSLATIONS_NAME,
				JNLP_TRANSLATIONS, JNLP_PACKAGE_NAME);

		return temp;
	}
}
