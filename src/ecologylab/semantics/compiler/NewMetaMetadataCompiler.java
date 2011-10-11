package ecologylab.semantics.compiler;

import java.io.File;
import java.io.IOException;

import ecologylab.generic.Debug;
import ecologylab.semantics.metadata.builtins.MetadataBuiltinsTranslationScope;
import ecologylab.semantics.metametadata.MetaMetadataRepository;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
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

	private static final String	META_METADATA_COMPILER_TSCOPE_NAME	= "meta-metadata-compiler-tscope";

	public void compile(CompilerConfig config) throws IOException, SIMPLTranslationException,
			CodeTranslationException
	{
		debug("\n\n loading repository ...\n\n");
		TranslationScope.enableGraphSerialization();
		MetaMetadataRepository repository = config.loadRepository();
		TranslationScope tscope = repository
				.traverseAndGenerateTranslationScope(META_METADATA_COMPILER_TSCOPE_NAME);
		TranslationScope metadataBuiltInTScope = MetadataBuiltinsTranslationScope.get();

		CodeTranslator translator = config.getCodeTranslator();
		for (ClassDescriptor cd : metadataBuiltInTScope.getClassDescriptors())
			translator.excludeClassFromTranslation(cd);

		File generatedSemanticsLocation = config.getGeneratedSemanticsLocation();
		debug("\n\n compiling to " + generatedSemanticsLocation + " ...\n\n");
		translator.translate(generatedSemanticsLocation, tscope, config);

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
		if (args.length < 2)
		{
			error(NewMetaMetadataCompiler.class, "lacking argument(s).");
			error(NewMetaMetadataCompiler.class, "args: <target-language> <generated-semantics-location>");
			error(NewMetaMetadataCompiler.class, "  - <target-language>: e.g. java or csharp (cs, c#).");
			error(NewMetaMetadataCompiler.class, "  - <generated-semantics-location>: the path to the location for generated semantics.");
			System.exit(-1);
		}

		String lang = args[0].toLowerCase();
		String semanticsLoc = args[1];
		
		CompilerConfig config = new CompilerConfig(lang, new File(semanticsLoc));
		NewMetaMetadataCompiler compiler = new NewMetaMetadataCompiler();
		compiler.compile(config);
	}

}
