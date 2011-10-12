package ecologylab.translators;

import java.io.File;
import java.io.IOException;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;

/**
 * 
 * @author quyin
 * 
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
	 * @param directoryLocation
	 *          The directory in which generated source codes will be placed.
	 * @param tScope
	 *          The translation scope from which source codes will be generated.
	 * @param config
	 * 					The configuration.
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 * @throws CodeTranslationException
	 */
	void translate(File directoryLocation, SimplTypesScope tScope, CodeTranslatorConfig config)
			throws IOException, SIMPLTranslationException, CodeTranslationException;

	/**
	 * Generates source codes taking an input {@link ClassDescriptor}.
	 * 
	 * @param classDescriptor
	 *          The descriptor for the type that needs be to translated.
	 * @param directoryLocation
	 *          The directory in which generated source codes will be placed.
	 * @param config
	 * 					The configuration.
	 * @throws IOException
	 * @throws CodeTranslationException
	 */
	void translate(ClassDescriptor classDescriptor, File directoryLocation, CodeTranslatorConfig config)
			throws IOException, CodeTranslationException;

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
