package ecologylab.semantics.compiler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ecologylab.io.Files;
import ecologylab.semantics.collecting.MetaMetadataRepositoryInit;
import ecologylab.semantics.metametadata.MetaMetadataRepository;
import ecologylab.semantics.metametadata.MetaMetadataRepositoryLoader;
import ecologylab.semantics.metametadata.exceptions.MetaMetadataException;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Format;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.translators.CodeTranslator;
import ecologylab.translators.CodeTranslator.TargetLanguage;
import ecologylab.translators.java.JavaTranslator;
import ecologylab.translators.net.DotNetTranslator;

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
public class CompilerConfig extends ElementState
{

	private static final Map<TargetLanguage, CodeTranslator> registeredTranslators = new HashMap<TargetLanguage, CodeTranslator>();
	
	static
	{
		registeredTranslators.put(TargetLanguage.JAVA, new JavaTranslator());
		registeredTranslators.put(TargetLanguage.C_SHARP, new DotNetTranslator());
	}
	
	/**
	 * Provide a registering mechanism for extending translators.
	 * 
	 * @param lang
	 * @param translator
	 */
	public static void registerTranslator(TargetLanguage lang, CodeTranslator translator)
	{
		registeredTranslators.put(lang, translator);
	}

	/**
	 * The location (directory) of the repository.
	 */
	@simpl_scalar
	private File						repositoryLocation					= new File(MetaMetadataRepositoryInit.DEFAULT_REPOSITORY_LOCATION);

	/**
	 * The format in which repository is stored.
	 */
	@simpl_scalar
	private Format					repositoryFormat						= Format.XML;

	/**
	 * The location (directory) of generated semantics.
	 */
	@simpl_scalar
	private File						generatedSemanticsLocation	= new File(".." + Files.sep + "ecologylabGeneratedSemantics");

	/**
	 * The target languange.
	 */
	@simpl_scalar
	private TargetLanguage	targetLanguage							= TargetLanguage.JAVA;

	/**
	 * The repository loader. (If new loader is desired, allow injection of this field.)
	 */
	protected MetaMetadataRepositoryLoader loader				= new MetaMetadataRepositoryLoader();
	
	/**
	 * Load repository using current configs.
	 * 
	 * @return
	 */
	public MetaMetadataRepository loadRepository()
	{
		return loader.loadFromDir(repositoryLocation, repositoryFormat);
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
		CodeTranslator translator = registeredTranslators.get(targetLanguage);
		if (translator == null)
		{
			throw new MetaMetadataException("Unregistered or unknown target language: " + targetLanguage);
		}
		return translator;
	}

}
