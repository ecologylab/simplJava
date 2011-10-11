package ecologylab.translators;

import java.util.HashMap;
import java.util.Map;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.translators.CodeTranslator.TargetLanguage;

@simpl_inherit
public class CodeTranslatorConfig extends ElementState
{
	
	@simpl_scalar
	private String generatedTranslationScopeClassPackage;
	
	@simpl_scalar
	private String generatedTranslationScopeClassSimpleName;

	protected CodeTranslatorConfig()
	{
		this("generated_translation_scope", "GeneratedTranslationScope");
	}
	
	protected CodeTranslatorConfig(String packageName)
	{
		this(packageName, "GeneratedTranslationScope");
	}
	
	protected CodeTranslatorConfig(String packageName, String simpleName)
	{
		generatedTranslationScopeClassPackage = packageName;
		generatedTranslationScopeClassSimpleName = simpleName;
	}
	
	public String getGeneratedTranslationScopeClassPackageName()
	{
		return generatedTranslationScopeClassPackage;
	}

	public String getGeneratedTranslationScopeClassSimpleName()
	{
		return generatedTranslationScopeClassSimpleName;
	}

	static Map<TargetLanguage, CodeTranslatorConfig> defaultConfigs = new HashMap<CodeTranslator.TargetLanguage, CodeTranslatorConfig>();
	
	static
	{
		defaultConfigs.put(TargetLanguage.JAVA, new CodeTranslatorConfig());
		defaultConfigs.put(TargetLanguage.C_SHARP, new CodeTranslatorConfig("GeneratedTranslationScope"));
	}
	
	public static CodeTranslatorConfig getDefaultConfig(TargetLanguage lang)
	{
		return defaultConfigs.get(lang);
	}

}
