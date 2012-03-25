package ecologylab.semantics.compiler;

import java.io.File;
import java.io.IOException;

import ecologylab.generic.Debug;
import ecologylab.semantics.metadata.MetadataClassDescriptor;
import ecologylab.semantics.metametadata.MetaMetadata;
import ecologylab.semantics.metametadata.MetaMetadataRepository;
import ecologylab.semantics.namesandnums.SemanticsNames;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.translators.CodeTranslationException;
import ecologylab.translators.CodeTranslator;

/**
 * 
 * @author quyin
 * 
 */
// TODO use ApplicationEnvironment to facilitate loading XML configs.
public class NewMetaMetadataCompiler extends Debug // ApplicationEnvironment
{
	
	public static final String	BUILTINS_CLASS_PACKAGE														= ".builtins";

	public static final String	DECLARATION_CLASS_PACKAGE													= ".declarations";

	public static final String	DECLARATION_CLASS_SUFFIX													= "Declaration";

	private static final String	META_METADATA_COMPILER_TSCOPE_NAME								= "meta-metadata-compiler-tscope";

	private static final String	META_METADATA_COMPILER_BUILTIN_DECLARATIONS_SCOPE	= "meta-metadata-compiler-builtin-declarations-scope";

	public void compile(CompilerConfig config) throws IOException, SIMPLTranslationException,
			CodeTranslationException
	{
		debug("\n\n loading repository ...\n\n");
		SimplTypesScope.enableGraphSerialization();
		MetaMetadataRepository repository = config.loadRepository();
		SimplTypesScope tscope = repository.traverseAndGenerateTranslationScope(META_METADATA_COMPILER_TSCOPE_NAME);

		CodeTranslator compiler = config.getCompiler();
		
		// generate declaration classes and scope
		SimplTypesScope builtinDeclarationsScope = SimplTypesScope.get(META_METADATA_COMPILER_BUILTIN_DECLARATIONS_SCOPE, new Class[] {});
		String builtinPackage = null;
		for (ClassDescriptor mdCD : tscope.getClassDescriptors())
		{
			MetaMetadata definingMmd = ((MetadataClassDescriptor) mdCD).getDefiningMmd();
			if (definingMmd.isBuiltIn())
			{
				ClassDescriptor declCD = (ClassDescriptor) mdCD.clone();
				String packageName = mdCD.getDescribedClassPackageName();
				String classSimpleName = mdCD.getDescribedClassSimpleName();
				if (definingMmd.isRootMetaMetadata())
				{
					packageName += BUILTINS_CLASS_PACKAGE + DECLARATION_CLASS_PACKAGE;
					classSimpleName += DECLARATION_CLASS_SUFFIX;
				}
				else
				{
					builtinPackage = packageName; // essentially, the old package name
					packageName += DECLARATION_CLASS_PACKAGE;
					classSimpleName += DECLARATION_CLASS_SUFFIX;
				}
				declCD.setDescribedClassPackageName(packageName);
				declCD.setDescribedClassSimpleName(classSimpleName);
				builtinDeclarationsScope.addTranslation(declCD);
			}
		}
//			compiler.translate(cd, config.getGeneratedBuiltinDeclarationsLocation(), config,
//					newPackageName, newSimpleName, GenerateAbstractClass.TRUE);
		CompilerConfig newConfig = (CompilerConfig) config.clone();
		newConfig.setLibraryTScopeClassPackage("ecologylab.semantics.metadata.builtins.declarations");
		newConfig.setLibraryTScopeClassSimpleName("MetadataBuiltinDeclarationsTranslationScope");
		newConfig.setGenerateAbstractClass(true);
		newConfig.setBuiltinDeclarationScopeName(SemanticsNames.REPOSITORY_BUILTIN_DECLARATIONS_TYPE_SCOPE);
		compiler.translate(config.getGeneratedBuiltinDeclarationsLocation(), builtinDeclarationsScope, newConfig);
		
		// generate normal metadata classes
		for (ClassDescriptor mdCD : tscope.getClassDescriptors())
		{
			MetaMetadata definingMmd = ((MetadataClassDescriptor) mdCD).getDefiningMmd();
			if (definingMmd.isBuiltIn())
				compiler.excludeClassFromTranslation(mdCD);
		}
		File generatedSemanticsLocation = config.getGeneratedSemanticsLocation();
		debug("\n\n compiling to " + generatedSemanticsLocation + " ...\n\n");
		compiler.translate(generatedSemanticsLocation, tscope, config);
		
			
//		CodeTranslator translator = CodeTranslatorConfig.getCodeTranslator(config.getTargetLanguage());
//		if (translator instanceof AbstractCodeTranslator)
//		{
//			File directoryLocation = new File("../ecologylabSemantics/src"); // default location
//			((AbstractCodeTranslator) translator).generateLibraryTScopeClass(directoryLocation, builtinDeclarationsScope, builtinPackage + DECLARATION_CLASS_PACKAGE, "MetadataBuiltinDeclarationsTranslationScope");
//		}
//		else
//		{
//			warning("Do not support generating declarations scope class in this target language!");
//		}

		// generate other classes and scope

		compilerHook(repository);
		debug("\n\n compiler finished.");
	}

	protected void compilerHook(MetaMetadataRepository repository)
	{

	}

	/**
	 * @param args
	 * 
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 * @throws CodeTranslationException
	 */
	public static void main(String[] args) throws IOException, SIMPLTranslationException,
			CodeTranslationException
	{
		if (args.length < 3)
		{
			error(NewMetaMetadataCompiler.class, "lacking argument(s).");
			error(NewMetaMetadataCompiler.class,
					"args: <target-language> <generated-semantics-location> <generated-builtin-declarations-location");
			error(NewMetaMetadataCompiler.class, "  - <target-language>: e.g. java or csharp (cs, c#).");
			error(NewMetaMetadataCompiler.class,
					"  - <generated-semantics-location>: the path to the location for generated semantics.");
			error(
					NewMetaMetadataCompiler.class,
					"  - <generated-builtin-declarations-location>: the path to the location for generated builtin declarations.");
			System.exit(-1);
		}

		String lang = args[0].toLowerCase();
		String semanticsLoc = args[1];
		String builtinDeclarationsLoc = args[2];

		CompilerConfig config = new CompilerConfig(lang, new File(semanticsLoc), new File(
				builtinDeclarationsLoc));
		NewMetaMetadataCompiler compiler = new NewMetaMetadataCompiler();
		compiler.compile(config);
	}

}
