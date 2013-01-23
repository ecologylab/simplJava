package ecologylab.generic;

import java.io.ByteArrayOutputStream;

/**
 * 
 * @author quyin
 *
 */
public class ByteArrayOutputStreamPool extends ResourcePool<ByteArrayOutputStream>
{
	
	public static int DEFAULT_BYTE_ARRAY_SIZE = 1024 * 16; // default size: 16k

	protected ByteArrayOutputStreamPool()
	{
		super(1, 1);
	}
	
	@Override
	protected ByteArrayOutputStream generateNewResource()
	{
		return new ByteArrayOutputStream(DEFAULT_BYTE_ARRAY_SIZE);
	}
	
	@Override
	protected void clean(ByteArrayOutputStream objectToClean)
	{
		objectToClean.reset();
	}
	
	private static ByteArrayOutputStreamPool singleton = new ByteArrayOutputStreamPool();
	
	public static ByteArrayOutputStreamPool get()
	{
		return singleton;
	}

}
