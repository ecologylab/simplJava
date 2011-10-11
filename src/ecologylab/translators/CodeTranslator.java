package ecologylab.translators;

import java.io.File;
import java.io.IOException;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;

/**
 * 
 * @author quyin
 * 
 */
public interface CodeTranslator
{

	public static enum TargetLanguage
	{
		JAVA,
		C_SHARP,
	}
	
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
	 * 
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 * @throws CodeTranslationException
	 */
	void translate(File directoryLocation, TranslationScope tScope)
			throws IOException, SIMPLTranslationException, CodeTranslationException;

	/**
	 * Generates source codes taking an input {@link ClassDescriptor}.
	 * 
	 * @param classDescriptor
	 *          The descriptor for the type that needs be to translated.
	 * @param directoryLocation
	 *          The directory in which generated source codes will be placed.
	 *          
	 * @throws IOException
	 * @throws CodeTranslationException
	 */
	void translate(ClassDescriptor classDescriptor, File directoryLocation)
			throws IOException, CodeTranslationException;

	/**
	 * Exclude a class from translation. Useful for, e.g. excluding built-in classes.
	 * 
	 * @param classToExclude
	 */
	void excludeClassFromTranslation(ClassDescriptor classToExclude);

}
