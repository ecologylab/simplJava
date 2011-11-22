package ecologylab.serialization;

/**
 * The class abstracting retrieving method of field values. Can be used to customize this process.
 * 
 * @author quyin
 */
public interface FieldValueRetriever
{

	/**
	 * The method used to retrieve the value of a field, from a context.
	 * 
	 * @param fd
	 * @param context
	 * @return
	 */
	Object getValue(FieldDescriptor fd, Object context);
	
}
