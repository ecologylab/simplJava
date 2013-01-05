package ecologylab.serialization;

/**
 * Represents an interfaces for classes that can marshal 
 * a given object to a "Simpl" string representation and from a representation to an object.
 * This has the implicit round-trip heuristic that an object marshalled to a string, then unmarshalled should be equviliant to the original object.
 * @author tom
 *
 */
public interface ISimplStringMarshaller {
	String marshal(Object object) throws SIMPLTranslationException;
	Object unmarshal(String string) throws SIMPLTranslationException; 
}
