package simpl.translation.api;

/**
 * An interface that represents core aspects of a given language.
 * Primarily just checks keywords
 *
 */
public interface LanguageCore {
	/**
	 * Determines if a String S is a reserved keyword in this language
	 * @param s The string to check
	 * @return True if it is a keyword, false otherwise. 
	 * 
	 */
	boolean isKeyword(String s);
}
