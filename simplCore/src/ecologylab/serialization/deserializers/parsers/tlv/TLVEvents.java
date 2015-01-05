package ecologylab.serialization.deserializers.parsers.tlv;

/**
 * Interface which can be implemented by the class handling parsing of tlv messages.
 * 
 * @author Nabeel Shahzad
 * 
 */
public interface TLVEvents
{
	public void startTLV();

	public void startObject(String objectName);

	public void endObject(String objectName);

	public void primitive(String value);

	public void endTLV();
}
