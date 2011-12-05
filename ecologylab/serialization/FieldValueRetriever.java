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
	Object getFieldValue(FieldDescriptor fd, Object context);
	
	/**
	 * This method returns the true value from a proxy object. If the input is not a proxy object,
	 * this method should return it untouched.
	 * 
	 * @param proxy
	 * @return
	 */
	Object getTrueValueFromProxy(Object proxy);
	
}
