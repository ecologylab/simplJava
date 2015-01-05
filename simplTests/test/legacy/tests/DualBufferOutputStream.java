package legacy.tests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Dual buffer to store data as string and array of bytes. 
 * not efficient, just used to output data on console. 
 * @author nabeel
 */
public class DualBufferOutputStream extends OutputStream
{
	private StringBuilder sb = new StringBuilder();
	private ByteArrayOutputStream bs = new ByteArrayOutputStream();
	
	@Override
	public void write(int b) throws IOException
	{
		sb.append((char) b);
		bs.write((byte) b);		
	}
	
	public String toString()
	{
		return sb.toString();
	}
	
	public byte[] toByte()
	{
		return this.bs.toByteArray();
	}

}
