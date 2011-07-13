package ecologylab.translators.metametadata.compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import ecologylab.appframework.PropertiesAndDirectories;
import ecologylab.generic.Debug;
import ecologylab.io.Files;
import ecologylab.semantics.collecting.MetaMetadataRepositoryInit;
import ecologylab.semantics.metadata.builtins.MetadataBuiltinsTranslationScope;
import ecologylab.semantics.metametadata.MetaMetadataRepository;
import ecologylab.semantics.namesandnums.SemanticsNames;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.translators.java.JavaTranslationException;
import ecologylab.translators.java.JavaTranslationUtilities;
import ecologylab.translators.metametadata.MetaMetadataJavaTranslator;

public class NewMetaMetadataCompiler extends Debug
{

	private static final String	GENERATED_METADATA_TRANSLATION_SCOPE_PACKAGE_NAME	= "ecologylab.semantics.generated.library";
	
	private static final String	GENERATED_METADATA_TRANSLATION_SCOPE_CLASS_NAME	= "GeneratedMetadataTranslationScope";

	public static final String	META_METADATA_COMPILER_TSCOPE_NAME		= "meta-metadata-compiler-tscope";

	public void compile(CompilerConfig config) throws IOException, SIMPLTranslationException, JavaTranslationException
	{
		MetaMetadataRepository repository = MetaMetadataRepositoryInit.getRepository();
		TranslationScope tscope = repository.traverseAndGenerateTranslationScope(META_METADATA_COMPILER_TSCOPE_NAME);
		TranslationScope.setGraphSwitch();
		TranslationScope metadataBuiltInTScope = MetadataBuiltinsTranslationScope.get();
		
		MetaMetadataJavaTranslator jt = new MetaMetadataJavaTranslator();
		for (ClassDescriptor cd : metadataBuiltInTScope.getClassDescriptors())
			jt.excludeClassFromTranslation(cd);
		
		String generatedSemanticsLocation = config.getGeneratedSemanticsLocation();
		jt.translateToJava(new File(generatedSemanticsLocation), tscope);
		createTranslationScopeClass(generatedSemanticsLocation, GENERATED_METADATA_TRANSLATION_SCOPE_PACKAGE_NAME, tscope);
	}

	public void createTranslationScopeClass(String generatedSemanticsRootDir, String packageName, TranslationScope compilerTScope) throws IOException
	{
		File rootDir = PropertiesAndDirectories.createDirsAsNeeded(new File(generatedSemanticsRootDir));
		File packageDir = PropertiesAndDirectories.createDirsAsNeeded(new File(rootDir, packageName.replace('.', Files.sep)));
		File file = new File(packageDir, GENERATED_METADATA_TRANSLATION_SCOPE_CLASS_NAME + ".java");
		PrintWriter printWriter = new PrintWriter(new FileWriter(file));
		
		// Write the package
		printWriter.println("package " + packageName + ";\n");

		// write java doc comment
		printWriter.println(JavaTranslationUtilities.getJavaClassComments(GENERATED_METADATA_TRANSLATION_SCOPE_CLASS_NAME));
		printWriter.println("\n");

		// Write the import statements
		Class[] basicImports = {
				TranslationScope.class,
				SemanticsNames.class,
				MetadataBuiltinsTranslationScope.class,
		};
		for (Class clazz : basicImports)
			printWriter.println("import " + clazz.getName() + ";\n");
		
		// Write java-doc comments
		printWriter.println("/**\n * This is the tranlation scope class for generated files.\n */\n");

		// Write the class
		printWriter.print("public class GeneratedMetadataTranslationScope\n{\n");
		printWriter.print("\tprotected static final Class TRANSLATIONS[] =\n\t{\n");
		TranslationScope metadataBuiltInTScope = MetadataBuiltinsTranslationScope.get();
		for (ClassDescriptor cd : compilerTScope.getClassDescriptors())
			if (!metadataBuiltInTScope.getClassDescriptors().contains(cd))
				printWriter.println("\t\t" + cd.getDescribedClassPackageName() + "." + cd.getDescribedClassSimpleName() + ".class,\n");
		printWriter.println("\t};\n");
		
		
		// Write get() method
		printWriter.println("\tpublic static TranslationScope get()\n\t{");
		printWriter.println("\t\treturn TranslationScope.get(SemanticsNames.GENERATED_METADATA_TRANSLATIONS, MetadataBuiltinsTranslationScope.get(), TRANSLATIONS);");
		printWriter.println("\t}\n");
		
		// End the class
		printWriter.println("}");
		
		printWriter.close();
	}
	
	/**
	 * @param args
	 * @throws JavaTranslationException 
	 * @throws SIMPLTranslationException 
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws IOException, SIMPLTranslationException, JavaTranslationException, InstantiationException, IllegalAccessException
	{
		Class<? extends CompilerConfig> configClass = DefaultCompilerConfig.class;
		int i = 0;
		while (i < args.length)
		{
			if ("-orm".equals(args[i]))
			{
				configClass = ORMCompilerConfig.class;
			}
			++i;
		}
		
		CompilerConfig config = configClass.newInstance();
		NewMetaMetadataCompiler compiler = new NewMetaMetadataCompiler();
		compiler.compile(config);
	}

}
