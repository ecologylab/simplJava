package cm.generic;


import java.util.*;
import cm.media.html.*;

public class StringBuffersPool 
{
	public static int POOL_SIZE	=	64;
	public static Vector bufferPool = new Vector();
	
	static
	{
		
		for(int i = 0 ; i < POOL_SIZE ; i++)
		{
			bufferPool.add(new StringBuffer(Scan.BUFFER_SIZE));
		}	
	}
	
	public static StringBuffer nextBuffer()
	{
		int freeIndex = bufferPool.size() - 1;
		
		if (freeIndex == -1)
		{
			return (new StringBuffer(Scan.BUFFER_SIZE));
		}
		StringBuffer b = (StringBuffer)bufferPool.get(freeIndex);
		bufferPool.removeElementAt(freeIndex);
		return b;
	}
	
	public static void release(StringBuffer b)
	{
		bufferPool.add(b);		
	}
}
