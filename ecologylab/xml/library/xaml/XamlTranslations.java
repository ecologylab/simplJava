package ecologylab.xml.library.xaml;

import ecologylab.generic.Debug;
import ecologylab.xml.NameSpaceDecl;
import ecologylab.xml.TranslationScope;


public class XamlTranslations extends Debug
{
	private static final String TRANSLATION_SPACE_NAME	= "xaml";

	public static final Class TRANSLATIONS[]	= 
	{
		WindowState.class,
		CanvasState.class,
		TextBlockState.class,
		FrameworkElementState.class,
		PanelChildState.class
	};

	public static final NameSpaceDecl[] NAME_SPACE_DECLS				=
	{
		new NameSpaceDecl("http://schemas.microsoft.com/winfx/2006/xaml/presentation", WindowState.class, WindowState.get()),
		new NameSpaceDecl("http://schemas.microsoft.com/winfx/2006/xaml", WindowState.class, WindowState.get()),
	}; 

	/**
	 * Just prevent anyone from new'ing this.
	 */
	private XamlTranslations()
	{
	}

	public static TranslationScope get()
	{
		return TranslationScope.get(TRANSLATION_SPACE_NAME, TRANSLATIONS, null, null, NAME_SPACE_DECLS);
	   }
	
}
