package ecologylab.translators;

import java.util.HashMap;
import java.util.Map;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.translators.java.JavaTranslator;
import ecologylab.translators.net.DotNetTranslator;

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

	public CodeTranslatorConfig()
	{
		this("library_tscope", "LibraryTranslationScope");
	}
	
	public CodeTranslatorConfig(String packageName)
	{
		this(packageName, "LibraryTranslationScope");
	}
	
	public CodeTranslatorConfig(String packageName, String simpleName)
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
	
	public static final String												JAVA						= "java";

	public static final String												CSHARP					= "csharp";

	private static final Map<String, CodeTranslator>	codeTranslators	= new HashMap<String, CodeTranslator>();

	static
	{
		CodeTranslator javaTranslator = new JavaTranslator();
		CodeTranslator csharpTranslator = new DotNetTranslator();

		registerCodeTranslator(JAVA, javaTranslator);

		registerCodeTranslator(CSHARP, csharpTranslator);
		registerCodeTranslator("c_sharp", csharpTranslator);
		registerCodeTranslator("cs", csharpTranslator);
		registerCodeTranslator("c#", csharpTranslator);
	}

	/**
	 * Provide a registering mechanism for extending translators.
	 * 
	 * @param targetLanguage
	 * @param codeTranslator
	 */
	public static void registerCodeTranslator(String targetLanguage, CodeTranslator codeTranslator)
	{
		codeTranslators.put(targetLanguage, codeTranslator);
	}
	
	public static CodeTranslator getCodeTranslator(String name)
	{
		return codeTranslators.get(name);
	}

}
