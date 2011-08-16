package ecologylab.serialization;

public interface ISimplSerializable
{
	/**
	 * Perform custom processing immediately before translating this to XML.
	 * <p/>
	 */
	void serializationPreHook();

	/**
	 * Perform processing immediately after serializing this.
	 */
	void serializationPostHook();

	/**
	 * Perform custom processing immediately after all translation is completed. This allows
	 * a newly-created object to perform any post processing with all the data it will
	 * have from serialized representation.	
	 */
	void deserializationPostHook();

	/**
	 * Perform custom processing before the current object is populated
	 */
	void deserializationPreHook();
}
