package ecologylab.oodss.distributed.common;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.IOException;

public class LimitedInputStream extends FilterInputStream
{
	private int	limit;

	private int	left;

	public LimitedInputStream(InputStream in, int limit)
	{
		super(in);
		this.limit = this.left = limit;
	}
	
	public boolean markSupported()
	{
		return false;
	}
	
	public int read(byte[] b, int off, int len) throws IOException
	{
		if(len > left)
		{
			len = left;
		}
		
		int ret = this.in.read(b, off, len);
		if(ret > 0)
		{
			left -= ret;
		}
		
		return ret;
	}
	
	public int read(byte[] b) throws IOException
	{
		if(b.length > left)
		{
			int ret = this.in.read(b, 0, left);
			if(ret > 0)
			{
				left -= ret;
			}
			
			return ret;
		}
		else
		{
			int ret = this.in.read(b);
			if(ret > 0)
			{
				left -= ret;
			}
			return ret;
		}
	}
	
	public int read() throws IOException
	{
		int ret;
		if(left > 0)
		{
			ret = this.in.read();
			if(ret >= 0)
			{
				left--;
			}
		}
		else
		{
			ret = -1;
		}
		return ret;
	}
	
	public int available() throws IOException
	{
		return Math.min(left, in.available());
	}

	public long skip(long n) throws IOException
	{
		long ret = 0;

		if(n > left)
		{
			ret = this.in.skip(left);
		}
		else
		{
			ret = this.in.skip(n);
		}
		
		left -= ret;
		
		return ret;
	}
	
	public void mark(int readLimit)
	{
		
	}
	
	public void reset()
	{
		
	}
	
}
