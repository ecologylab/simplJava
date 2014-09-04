package ecologylab.translators;

import java.io.File;
import java.io.IOException;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;

/**
 * A general interface for translating code.
 * 
 * @author quyin
 */
public interface CodeTranslator
{

	/**
	 * Generate source codes taking an input translation scope.
	 * <p>
	 * This function internally calls {@code translate(ClassDescriptor, directoryLocation)} to
	 * generate the required source codes.
	 * </p>
	 * 
	 * @param destDir
	 *          The directory in which generated source codes will be placed.
	 * @param typeScope
	 *          The translation scope from which source codes will be generated.
	 * @param config
	 *          The configuration.
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 * @throws CodeTranslationException
	 */
	void translate(File destDir, SimplTypesScope typeScope, CodeTranslatorConfig config)
			throws IOException, SIMPLTranslationException, CodeTranslationException;

	/**
	 * Generates source codes taking an input {@link ClassDescriptor}.
	 * 
	 * @param destDir
	 *          The directory in which generated source codes will be placed.
	 * @param classToTranslate
	 *          The descriptor for the type that needs be to translated.
	 * @param config
	 *          The configuration.
	 * 
	 * @throws IOException
	 * @throws CodeTranslationException
	 */
	void translate(File destDir, ClassDescriptor classToTranslate, CodeTranslatorConfig config)
			throws IOException, SIMPLTranslationException, CodeTranslationException;

	/**
	 * Exclude a class from translation. Useful for, e.g. excluding built-in classes.
	 * 
	 * @param classToExclude
	 */
	void excludeClassFromTranslation(ClassDescriptor classToExclude);

	/**
	 * @return The target language name.
	 */
	String getTargetLanguage();

}
