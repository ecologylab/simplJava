package ecologylab.semantics.compiler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ecologylab.generic.ReflectionTools;
import ecologylab.semantics.collecting.MetaMetadataRepositoryInit;
import ecologylab.semantics.metametadata.MetaMetadataRepository;
import ecologylab.semantics.metametadata.MetaMetadataRepositoryLoader;
import ecologylab.semantics.metametadata.exceptions.MetaMetadataException;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.formatenums.Format;
import ecologylab.translators.CodeTranslator;
import ecologylab.translators.CodeTranslatorConfig;

/**
 * The encapsulation of compiler configurations. This class provides the default configuration,
 * which loads a XML repository from a default location and generates Java source codes to another
 * prescribed location (the ecologylabGeneratedSemantics project).
 * <p />
 * New configurations could be loaded through SIMPL.
 * 
 * @author quyin
 * 
 */
@simpl_inherit
public class CompilerConfig extends CodeTranslatorConfig
{

	public static final String												JAVA						= "java";

	public static final String												CSHARP					= "csharp";

	private static final Map<String, CodeTranslator>	compilers	= new HashMap<String, CodeTranslator>();

	static
	{
		CodeTranslator javaCompiler = new MetaMetadataJavaTranslator();
		CodeTranslator csharpCompiler = new MetaMetadataDotNetTranslator();
		
		registerCompiler(JAVA, javaCompiler);

		registerCompiler(CSHARP, csharpCompiler);
		registerCompiler("c_sharp", csharpCompiler);
		registerCompiler("cs", csharpCompiler);
		registerCompiler("c#", csharpCompiler);
	}

	/**
	 * Provide a registering mechanism for extending translators.
	 * 
	 * @param targetLanguage
	 * @param codeTranslator
	 */
	public static void registerCompiler(String targetLanguage, CodeTranslator codeTranslator)
	{
		compilers.put(targetLanguage, codeTranslator);
	}

	/**
	 * The location (directory) of the repository.
	 */
	@simpl_scalar
	@simpl_hints({ Hint.XML_LEAF })
	private File													repositoryLocation = MetaMetadataRepositoryInit.findRepositoryLocation();

	/**
	 * The format in which repository is stored.
	 */
	@simpl_scalar
	@simpl_hints({ Hint.XML_LEAF })
	private Format												repositoryFormat		= Format.XML;

	/**
	 * The location (directory) of generated semantics.
	 */
	@simpl_scalar
	@simpl_hints({ Hint.XML_LEAF })
	private File													generatedSemanticsLocation;

	@simpl_scalar
	@simpl_hints({ Hint.XML_LEAF })
	private File													generatedBuiltinDeclarationsLocation;

	/**
	 * The target languange.
	 */
	@simpl_scalar
	@simpl_hints({ Hint.XML_LEAF })
	private String												targetLanguage;

	@simpl_scalar
	@simpl_hints({ Hint.XML_LEAF })
	private String												codeTranslatorClass;

	private MetaMetadataRepositoryLoader	repositoryLoader;

	private CodeTranslator								compiler;
	
	private MetaMetadataRepository				repository;

	/**
	 * Constructor for S.IM.PL.
	 */
	public CompilerConfig()
	{
		this(JAVA, new File("../ecologylabGeneratedSemantics"), new File("../ecologylabSemantics/src/ecologylab/semantics/metadata/builtins/declarations"));
	}

	/**
	 * Convenience constructor.
	 * 
	 * @param targetLanguage
	 * @param generatedSemanticsLocation
	 */
	CompilerConfig(String targetLanguage, File generatedSemanticsLocation, File generatedBuiltinDeclarationsLocation)
	{
		super("ecologylab.semantics.generated.library", "RepositoryMetadataTranslationScope");
		this.targetLanguage = targetLanguage;
		this.generatedSemanticsLocation = generatedSemanticsLocation;
		this.generatedBuiltinDeclarationsLocation = generatedBuiltinDeclarationsLocation;
	}
	
	public String getTargetLanguage()
	{
		return this.targetLanguage;
	}

	/**
	 * @return The repository loader.
	 */
	protected MetaMetadataRepositoryLoader getRepositoryLoader()
	{
		if (repositoryLoader == null)
			repositoryLoader = new MetaMetadataRepositoryLoader();
		return repositoryLoader;
	}

	/**
	 * Load repository using current configs.
	 * 
	 * @return
	 */
	public MetaMetadataRepository loadRepository()
	{
		if (repository == null)
			repository = getRepositoryLoader().loadFromDir(repositoryLocation, repositoryFormat);
		return repository;
	}

	/**
	 * @return The location (directory) generated semantics will be stored.
	 */
	public File getGeneratedSemanticsLocation()
	{
		return generatedSemanticsLocation;
	}
	
	public File getGeneratedBuiltinDeclarationsLocation()
	{
		return generatedBuiltinDeclarationsLocation;
	}

	/**
	 * @return The source code translator (which translates a SIMPL scope to a set of source code
	 *         files in the target language).
	 */
	public CodeTranslator getCompiler()
	{
		if (compiler == null)
		{
			compiler = compilers.get(targetLanguage);
			if (compiler == null && codeTranslatorClass != null)
			{
				try
				{
					Class TC =  Class.forName(codeTranslatorClass);
					compiler = ReflectionTools.getInstance(TC);
				}
				catch (ClassNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (compiler == null)
			{
				throw new MetaMetadataException("Unregistered or unknown target language: "
						+ targetLanguage);
			}
		}
		return compiler;
	}

}
