package ecologylab.semantics.compiler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ecologylab.generic.ReflectionTools;
import ecologylab.io.Files;
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

	private static final Map<String, CodeTranslator>	codeTranslators	= new HashMap<String, CodeTranslator>();

	static
	{
		CodeTranslator javaTranslator = new MetaMetadataJavaTranslator();
		CodeTranslator csharpTranslator = new MetaMetadataDotNetTranslator();
		
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

	/**
	 * The location (directory) of the repository.
	 */
	@simpl_scalar
	@simpl_hints({ Hint.XML_LEAF })
	private File													repositoryLocation	= new File(MetaMetadataRepositoryInit.DEFAULT_REPOSITORY_LOCATION);

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

	private CodeTranslator								codeTranslator;
	
	private MetaMetadataRepository				repository;

	/**
	 * Constructor for S.IM.PL.
	 */
	public CompilerConfig()
	{
		this(JAVA, new File(".." + Files.sep + "ecologylabGeneratedSemantics"));
	}

	/**
	 * Convenience constructor.
	 * 
	 * @param targetLanguage
	 * @param generatedSemanticsLocation
	 */
	CompilerConfig(String targetLanguage, File generatedSemanticsLocation)
	{
		super("ecologylab.semantics.generated.library", "RepositoryMetadataTranslationScope");
		this.targetLanguage = targetLanguage;
		this.generatedSemanticsLocation = generatedSemanticsLocation;
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

	/**
	 * @return The source code translator (which translates a SIMPL scope to a set of source code
	 *         files in the target language).
	 */
	public CodeTranslator getCodeTranslator()
	{
		if (codeTranslator == null)
		{
			codeTranslator = codeTranslators.get(targetLanguage);
			if (codeTranslator == null && codeTranslatorClass != null)
			{
				try
				{
					Class TC =  Class.forName(codeTranslatorClass);
					codeTranslator = ReflectionTools.getInstance(TC);
				}
				catch (ClassNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (codeTranslator == null)
			{
				throw new MetaMetadataException("Unregistered or unknown target language: "
						+ targetLanguage);
			}
		}
		return codeTranslator;
	}

}
