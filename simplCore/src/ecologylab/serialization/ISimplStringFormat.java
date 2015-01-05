package ecologylab.serialization;

/**
 * A given format has accepted "escaping" and "unescaping" functionality...
 * This interface represents this escaping and unescaping for a given format.
 * It is governed by a basic heuristic that a round-trip from string -> escaped -> unescaped should return
 * the original string. 
 * @author tom
 *
 */
public interface ISimplStringFormat {
	/**
	 * Escapes a string in this format. 
	 * @param unescapedString Unescaped string to escape
	 * @return The escaped string
	 */
	String escape(String unescapedString);
	
	/**
	 * Unescapes a string in this format
	 * @param escapedString An escaped string
	 * @return The unescaped string
	 */
	String unescape(String escapedString);
}
