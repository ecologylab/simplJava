package simpl.annotations.dbal;

/**
 * Hints for styles when deserializing scalar fields.
 * 
 * For instance, deserializing inner text content can be done through XML_TEXT. This can be used
 * together with other scalars (attributes / leafs).
 */
public enum Hint
{
	XML_ATTRIBUTE, XML_LEAF, XML_LEAF_CDATA, XML_TEXT, XML_TEXT_CDATA, UNDEFINED,
}
