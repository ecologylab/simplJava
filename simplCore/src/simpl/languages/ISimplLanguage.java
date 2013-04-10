package simpl.languages;

import java.util.Collection;

import simpl.exceptions.SIMPLTranslationException;


/**
 * An interface that represents a language supported by simpl; Should be part of a "language package" in 
 * future versions of s.im.pl. 
 * In general: Implementers of this interface represent the core unique aspects of names in a language. The class
 * should presumably hold onto some list of reserved words, provide a check for those reserved words, and also provide
 * means for translating from simpl cross platform names to the language's appropriate names (and back and forth).
 * 
 * Testing heuristics: Random strings should probably never trigger the isReserved() checks, 
 * marshalling from cross-platform name to language name and back shoudl always return the same string. 
 *
 */
public interface ISimplLanguage {
	
	/**
	 * Polymoprhic methods will use some case-insensitive identifier to identify
	 * languages; this method returns this identifier. 
	 * @return The language identifier for a language. 
	 */
	String getLanguageIdentifier();
	
	/**
	 * Obtains a list of reserved keywords for a given language. These words can often be found in a specification for 
	 * the language. Some languages may offer words that are reserved in certain contexts... those should be included
	 * in this collection.
	 * @return A Collection of reserved keywords.
	 */
	Collection<String> getReservedKeywords();
	
	/**
	 * Determines if a given string is a reserved keyword in this language
	 * @param keyword The string to check
	 * @return True if the word is a reserved word. 
	 */
	boolean isReservedKeyword(String keyword);
	
	/**
	 * Converts a string representing a simpl name (a cross platform name, if you will) to an idiomatic string in this language.
	 * @param simplName The simpl name to convert
	 * @return a language-specific name
	 * @throws SIMPLTranslationException if the name is a reserved name.
	 */
	String convertSimplNameToLanguageName(String simplName) throws SIMPLTranslationException;
	
	/**
	 * Converts a string represetning a language specific name to a simpl name (a cross platform name, if you will) 
	 * @param lanugageName The language name
	 * @return A cross-platform name
	 */
	String convertLanguageNameToSimplName(String lanugageName) throws SIMPLTranslationException;
}
