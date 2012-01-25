package ecologylab.semantics.compiler;

import java.io.File;
import java.io.IOException;

import ecologylab.generic.Debug;
import ecologylab.semantics.metadata.MetadataClassDescriptor;
import ecologylab.semantics.metametadata.MetaMetadata;
import ecologylab.semantics.metametadata.MetaMetadataRepository;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.translators.AbstractCodeTranslator;
import ecologylab.translators.CodeTranslationException;
import ecologylab.translators.CodeTranslator;
import ecologylab.translators.CodeTranslator.GenerateAbstractClass;
import ecologylab.translators.CodeTranslator.GeneratePackageStructure;
import ecologylab.translators.CodeTranslatorConfig;

/**
 * 
 * @author quyin
 * 
 */
// TODO use ApplicationEnvironment to facilitate loading XML configs.
public class NewMetaMetadataCompiler extends Debug // ApplicationEnvironment
{
	
	public static final String	BUILTINS_CLASS_PACKAGE							= ".builtins";

	public static final String	DECLARATION_CLASS_PACKAGE						= ".declarations";

	public static final String	DECLARATION_CLASS_SUFFIX						= "Declaration";

	private static final String	META_METADATA_COMPILER_TSCOPE_NAME	= "meta-metadata-compiler-tscope";

	public void compile(CompilerConfig config) throws IOException, SIMPLTranslationException,
			CodeTranslationException
	{
		debug("\n\n loading repository ...\n\n");
		SimplTypesScope.enableGraphSerialization();
		MetaMetadataRepository repository = config.loadRepository();
		SimplTypesScope tscope = repository
				.traverseAndGenerateTranslationScope(META_METADATA_COMPILER_TSCOPE_NAME);
		// SimplTypesScope metadataBuiltInTScope = MetadataBuiltinsTranslationScope.get();

		CodeTranslator compiler = config.getCompiler();
		
		// generate declaration classes and scope
		SimplTypesScope builtinDeclarationsScope = SimplTypesScope.get("metadata-builtin-declarations-scope", new Class[] {});
		String builtinPackage = null;
		for (ClassDescriptor cd : tscope.getClassDescriptors())
		{
			ClassDescriptor clonedCd = (ClassDescriptor) cd.clone();
			
			String newPackageName = cd.getDescribedClassPackageName();
			String newSimpleName = cd.getDescribedClassSimpleName();

			MetaMetadata definingMmd = ((MetadataClassDescriptor) cd).getDefiningMmd();
			if (definingMmd.isRootMetaMetadata())
			{
				newPackageName += BUILTINS_CLASS_PACKAGE + DECLARATION_CLASS_PACKAGE;
				newSimpleName += DECLARATION_CLASS_SUFFIX;
			}
			else if (definingMmd.isBuiltIn())
			{
				builtinPackage = newPackageName; // essentially, the old package name
				newPackageName += DECLARATION_CLASS_PACKAGE;
				newSimpleName += DECLARATION_CLASS_SUFFIX;
			}
			else
			{
				continue;
			}
			clonedCd.setDescribedClassPackageName(newPackageName);
			clonedCd.setDescribedClassSimpleName(newSimpleName);
			
			compiler.excludeClassFromTranslation(cd);
			builtinDeclarationsScope.addTranslation(clonedCd);
			compiler.translate(cd, config.getGeneratedBuiltinDeclarationsLocation(), config,
					GeneratePackageStructure.FALSE, newPackageName, newSimpleName, GenerateAbstractClass.TRUE);
		}
		CodeTranslator translator = CodeTranslatorConfig.getCodeTranslator(config.getTargetLanguage());
		if (translator instanceof AbstractCodeTranslator)
		{
			File directoryLocation = new File("../ecologylabSemantics/src"); // default location
			((AbstractCodeTranslator) translator).generateLibraryTScopeClass(directoryLocation, builtinDeclarationsScope, builtinPackage + DECLARATION_CLASS_PACKAGE, "MetadataBuiltinDeclarationsTranslationScope");
		}
		else
		{
			warning("Do not support generating declarations scope class in this target language!");
		}

		// generate other classes and scope
		File generatedSemanticsLocation = config.getGeneratedSemanticsLocation();
		debug("\n\n compiling to " + generatedSemanticsLocation + " ...\n\n");
		compiler.translate(generatedSemanticsLocation, tscope, config);

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
