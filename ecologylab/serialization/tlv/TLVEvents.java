package ecologylab.serialization.tlv;

public interface TLVEvents
{
	public void startTLV();

	public void startObject(String objectName);

	public void endObject(String objectName);

	public void primitive(String value);

	public void endTLV();
}
