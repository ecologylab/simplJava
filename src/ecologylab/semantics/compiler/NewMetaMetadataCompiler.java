package ecologylab.semantics.compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ecologylab.appframework.PropertiesAndDirectories;
import ecologylab.generic.Debug;
import ecologylab.io.Files;
import ecologylab.semantics.metadata.builtins.MetadataBuiltinsTranslationScope;
import ecologylab.semantics.metametadata.MetaMetadata;
import ecologylab.semantics.metametadata.MetaMetadataRepository;
import ecologylab.semantics.namesandnums.SemanticsNames;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.translators.java.JavaTranslationException;
import ecologylab.translators.java.JavaTranslationUtilities;

public class NewMetaMetadataCompiler extends Debug
{

	private static final String	REPOSITORY_METADATA_TRANSLATION_SCOPE_PACKAGE_NAME	= "ecologylab.semantics.generated.library";

	private static final String	REPOSITORY_METADATA_TRANSLATION_SCOPE_CLASS_NAME		= "RepositoryMetadataTranslationScope";

	private static final String	META_METADATA_COMPILER_TSCOPE_NAME									= "meta-metadata-compiler-tscope";

	public void compile(CompilerConfig config) throws IOException, SIMPLTranslationException,
			JavaTranslationException
	{
		debug("\n\nloading repository ...\n\n");
		MetaMetadataRepository repository = config.loadRepository();
		TranslationScope tscope = repository
				.traverseAndGenerateTranslationScope(META_METADATA_COMPILER_TSCOPE_NAME);
		TranslationScope.setGraphSwitch();
		TranslationScope metadataBuiltInTScope = MetadataBuiltinsTranslationScope.get();
		
		System.out.println(tscope.serialize());

		MetaMetadataJavaTranslator jt = config.createJavaTranslator();
		for (ClassDescriptor cd : metadataBuiltInTScope.getClassDescriptors())
			jt.excludeClassFromTranslation(cd);

		String generatedSemanticsLocation = config.getGeneratedSemanticsLocation();
		debug("\n\ncompiling to " + generatedSemanticsLocation + " ...\n\n");
		jt.translateToJava(new File(generatedSemanticsLocation), tscope);
		createTranslationScopeClass(generatedSemanticsLocation,
				REPOSITORY_METADATA_TRANSLATION_SCOPE_PACKAGE_NAME, repository);
		
		compilerHook(repository);
	}
	
	protected void compilerHook(MetaMetadataRepository repository)
	{
		
	}

	public void createTranslationScopeClass(String generatedSemanticsRootDir, String packageName,
			MetaMetadataRepository repository) throws IOException
	{
		File rootDir = PropertiesAndDirectories.createDirsAsNeeded(new File(generatedSemanticsRootDir));
		File packageDir = PropertiesAndDirectories.createDirsAsNeeded(new File(rootDir, packageName
				.replace('.', Files.sep)));
		File file = new File(packageDir, REPOSITORY_METADATA_TRANSLATION_SCOPE_CLASS_NAME + ".java");
		PrintWriter printWriter = new PrintWriter(new FileWriter(file));

		// Write the package
		printWriter.println("package " + packageName + ";\n");

		// write java doc comment
		printWriter.println(JavaTranslationUtilities
				.getJavaClassComments(REPOSITORY_METADATA_TRANSLATION_SCOPE_CLASS_NAME));
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
		printWriter.print("public class ");
		printWriter.print(REPOSITORY_METADATA_TRANSLATION_SCOPE_CLASS_NAME);
		printWriter.print("\n{\n");
		printWriter.print("\tprotected static final Class TRANSLATIONS[] =\n\t{\n");
		List<String> classes = new ArrayList<String>(); 
		if (repository.values() != null)
			for (MetaMetadata mmd : repository.values())
				if (mmd.isNewMetadataClass())
				{
					ClassDescriptor cd = mmd.getMetadataClassDescriptor();
					classes.add("\t\t" + cd.getDescribedClassPackageName() + "."
							+ cd.getDescribedClassSimpleName() + ".class,\n");
				}
		Collections.sort(classes);
		for (String classDef : classes)
			printWriter.println(classDef);
		printWriter.println("\t};\n");

		// Write get() method
		printWriter.println("\tpublic static TranslationScope get()\n\t{");
		printWriter
				.println("\t\treturn TranslationScope.get(SemanticsNames.REPOSITORY_METADATA_TRANSLATIONS, MetadataBuiltinsTranslationScope.get(), TRANSLATIONS);");
		printWriter.println("\t}\n");

		// End the class
		printWriter.println("}");

		printWriter.close();
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 * @throws JavaTranslationException
	 */
	public static void main(String[] args) throws IOException, SIMPLTranslationException,
			JavaTranslationException
	{
		CompilerConfig config = new DefaultCompilerConfig();
		NewMetaMetadataCompiler compiler = new NewMetaMetadataCompiler();
		compiler.compile(config);
	}

}
