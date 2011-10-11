package ecologylab.translators;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * 
 * @author quyin
 *
 */
@simpl_inherit
public class CodeTranslatorConfig extends ElementState
{
	
	@simpl_scalar
	@simpl_hints({Hint.XML_LEAF})
	private String libraryTScopeClassPackage;
	
	@simpl_scalar
	@simpl_hints({Hint.XML_LEAF})
	private String libraryTScopeClassSimpleName;

	protected CodeTranslatorConfig()
	{
		this("library_tscope", "LibraryTranslationScope");
	}
	
	protected CodeTranslatorConfig(String packageName)
	{
		this(packageName, "LibraryTranslationScope");
	}
	
	protected CodeTranslatorConfig(String packageName, String simpleName)
	{
		libraryTScopeClassPackage = packageName;
		libraryTScopeClassSimpleName = simpleName;
	}
	
	public String getLibraryTScopeClassPackageName()
	{
		return libraryTScopeClassPackage;
	}

	public String getLibraryTScopeClassSimpleName()
	{
		return libraryTScopeClassSimpleName;
	}

}
