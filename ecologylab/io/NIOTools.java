package ecologylab.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

import ecologylab.generic.*;
import ecologylab.xml.XMLTools;
/**
 * Utility class for managing memory mapped buffers. 
 * 
 * @author wolf
 */
public class NIOTools extends Debug{
	/**
	 * The logger for reporting io problems
	 */
    private static boolean warned;
	
	/**
     * Really closes a MappedByteBuffer without the need to wait for
     * garbage collection. Any problems with closing
     * a buffer on Windows (the problem child in this case) will be logged as
     * SEVERE to the logger of the package name. To force logging of errors, 
     * set the System property "org.geotools.io.debugBuffer" to "true".
     * @param buffer
     * See MappedByteBuffer
     * @return true if the operation was successful, false otherwise.
     */
	public static boolean clean(final java.nio.ByteBuffer buffer) 
	{
        if (buffer == null || ! buffer.isDirect() ) 
            return false;
        
		Boolean b = (Boolean) AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() 
			{
                Boolean success = Boolean.FALSE;
				try 
				{
					Method getCleanerMethod =
						buffer.getClass().getMethod("cleaner", null);
					getCleanerMethod.setAccessible(true);
                    Object cleaner = getCleanerMethod.invoke(buffer,  null);
                    Method clean = cleaner.getClass().getMethod("clean", null);
                    clean.invoke(cleaner, null);
                    success = Boolean.TRUE;
				} catch (Exception e)
                {
                    if (!warned)
                        log(e,buffer);
				}
				return success;
			}
		});
        
        return b.booleanValue();
	}
    
    private static void log(Exception e,java.nio.ByteBuffer buffer) 
    {
        warned = true;
        String message = "NIOTools: Error attempting to close a mapped byte buffer : " + buffer.getClass().getName();
        message += "\n JVM : " + System.getProperty("java.version") + " " + System.getProperty("java.vendor");
        Debug.println(message);
    }
    
    public static boolean writeMemoryMapped(File file, StringBuilder buffy)
    {
    	boolean ok					= true;
    	RandomAccessFile rFile		= null;
    	MappedByteBuffer mMapBuffer	= null;
		try
		{
	    	rFile		    		= new RandomAccessFile(file, "rw");
	    	FileChannel fileChannel = rFile.getChannel();
	    	
	    	int	length				= buffy.length();
	    	
			mMapBuffer				= fileChannel.map(FileChannel.MapMode.PRIVATE, 0, length);
			fileChannel.close();
			//TODO could write XmlTools.xmlHeader() here
			
			for (int i=0; i<length; i++)
				mMapBuffer.put((byte) buffy.charAt(i));
			
			rFile.close();
			clean(mMapBuffer);
		} catch (IOException e)
		{
			e.printStackTrace();
			ok					= false;
		}
/*		finally
		{
			try
			{
				if (rFile != null)
					rFile.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			if (mMapBuffer != null)
				clean(mMapBuffer);
		}
*/
		return ok;
    }
    static final int BUFFER_SIZE	= 1024;
    static final ByteBuffer byteBuffer	= ByteBuffer.allocateDirect(BUFFER_SIZE);
    
    public static boolean writeFile(File file, StringBuilder buffy)
    {
    	boolean ok					= true;
    	FileOutputStream oStream	= null;

    	try
		{
	    	oStream					= new FileOutputStream(file);
			FileChannel fileChannel = oStream.getChannel();
	    	
	    	int	length				= buffy.length();
			//TODO could write XmlTools.xmlHeader() here
			
	    	int buffyIndex=0;
			for (buffyIndex=0; buffyIndex<length; buffyIndex+= BUFFER_SIZE)
			{
				writeABuffer(fileChannel, buffy, buffyIndex, BUFFER_SIZE);
				//TODO -- deal with how this will drop bytes at the end
			}
			if (buffyIndex > length)
			{
				buffyIndex	-= BUFFER_SIZE;
				int lastCount= length - buffyIndex;
				//writeABuffer(fileChannel, buffy, buffyIndex, lastCount);
			}
			fileChannel.close();
		} catch (IOException e)
		{
			e.printStackTrace();
			ok					= false;
		}
		return ok;
    }
    private static void writeABuffer(FileChannel fileChannel, StringBuilder buffy, int index, int length)
    throws IOException
    {
		for (int i=0; i<length; i++)
			byteBuffer.put((byte) buffy.charAt(index));
		
		fileChannel.write(byteBuffer);
		byteBuffer.clear();
    }
}
