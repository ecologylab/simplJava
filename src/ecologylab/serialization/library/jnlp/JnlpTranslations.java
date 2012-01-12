/**
 * 
 */
package ecologylab.serialization.library.jnlp;

import ecologylab.appframework.types.AppFrameworkTranslations;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.library.jnlp.applet.AppletDesc;
import ecologylab.serialization.library.jnlp.applet.Param;
import ecologylab.serialization.library.jnlp.application.ApplicationDesc;
import ecologylab.serialization.library.jnlp.information.AssociationElement;
import ecologylab.serialization.library.jnlp.information.Description;
import ecologylab.serialization.library.jnlp.information.HomepageElement;
import ecologylab.serialization.library.jnlp.information.Icon;
import ecologylab.serialization.library.jnlp.information.InformationElement;
import ecologylab.serialization.library.jnlp.information.MenuElement;
import ecologylab.serialization.library.jnlp.information.OfflineAllowedElement;
import ecologylab.serialization.library.jnlp.information.RelatedContentElement;
import ecologylab.serialization.library.jnlp.information.ShortcutElement;
import ecologylab.serialization.library.jnlp.resource.HrefBasedResource;
import ecologylab.serialization.library.jnlp.resource.J2se;
import ecologylab.serialization.library.jnlp.resource.Jar;
import ecologylab.serialization.library.jnlp.resource.Nativelib;
import ecologylab.serialization.library.jnlp.resource.Property;
import ecologylab.serialization.library.jnlp.resource.ResourceElement;
import ecologylab.serialization.library.jnlp.resource.ResourceElementArray;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class JnlpTranslations
{
	private static final String				JNLP_TRANSLATIONS_NAME	= "JNLP Translations";

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

	private static final SimplTypesScope	inheritedTranslations[]	=
																						{
			AppFrameworkTranslations.get(),
			JnlpTranslations.getStudyTranslationsOnly()					};

	/**
	 * This accessor will work from anywhere, in any order, and stay efficient.
	 * 
	 * @return
	 */
	public static SimplTypesScope get()
	{
		SimplTypesScope result = SimplTypesScope.get(JNLP_TRANSLATIONS_NAME,
				inheritedTranslations, JNLP_TRANSLATIONS);

		return result;
	}

	/**
	 * @return
	 */
	private static SimplTypesScope getStudyTranslationsOnly()
	{
		SimplTypesScope temp = SimplTypesScope.get(JNLP_TRANSLATIONS_NAME, JNLP_TRANSLATIONS);

		return temp;
	}
}
