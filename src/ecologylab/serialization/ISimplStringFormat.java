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
	String escape(String unescapedString);
	String unescape(String escapedString);
}
