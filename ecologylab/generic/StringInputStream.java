package cm.generic;

import java.io.*;

/**
 * A mechanism for reading <code>String</code>s from
 * <code>java.io.InputStream</code>s of various formats.
 * Used especially in drag and drop, for getting values.
 */
public class StringInputStream
extends InputStream
{
   public static final int	UTF16_LE	= 0;
   public static final int	UTF16_BE	= 1;
   public static final int	UTF16		= UTF16_BE;
   public static final int	UTF8		= 2;
   
   int	outputFormat	= UTF16_LE;
   
    /**
     * The string from which bytes are read.
     */
    protected String buffer;

    /**
     * The index of the next character to read from the input stream buffer.
     *
     * @see        java.io.StringBufferInputStream#buffer
     */
    protected int pos;

    /**
     * The number of valid characters in the input stream buffer.
     *
     * @see        java.io.StringBufferInputStream#buffer
     */
    protected int count;

    /**
     * Creates a string input stream to read data from the specified string.
     *
     * @param      s   the underlying input buffer.
     */

   public StringInputStream(String s, int format)
   {
      this(s);
      outputFormat	= format;
   }
   public StringInputStream(String s)
   {
      this.buffer = s;
      count = s.length();
   }
    /**
     * Reads the next byte of data from this input stream. The value
     * byte is returned as an <code>int</code> in the range
     * <code>0</code> to <code>255</code>. If no byte is available
     * because the end of the stream has been reached, the value
     * <code>-1</code> is returned.
     * <p>
     * The <code>read</code> method of
     * <code>StringBufferInputStream</code> cannot block. It returns the
     * low eight bits of the next character in this input stream's buffer.
     *
     * @return     the next byte of data, or <code>-1</code> if the end of the
     *             stream is reached.
     */
    public synchronized int read() {
//      Debug.println("StringInputStream.read(");
	return (pos < count) ? (int)buffer.charAt(pos++) : -1;
//	return (pos < count) ? ((int)(buffer.charAt(pos++)) & 0xFFFF) : -1;
    }

   public synchronized int read(byte buf[], int off, int len)
   {
//      Debug.println("StringInputStream.read(" + len);
      if (buf == null)
      {
	 throw new NullPointerException();
      } else if ((off < 0) || (off > buf.length) || (len < 0) ||
		 ((off + len) > buf.length) || ((off + len) < 0)) {
	 throw new IndexOutOfBoundsException();
      }
      if (pos >= count) {
	 return -1;
      }
      if (pos + len > count) {
	 len = count - pos;
      }
      if (len <= 0) {
	 return 0;
      }
      String	s = buffer;
      int cnt = len;
      while (--cnt >= 0)
      {
	 char thisChar = s.charAt(pos++);
	 // little endian reverses the byte order
	 byte b1	  = (byte) (thisChar & 0xff);
	 byte b2	  = (byte) (thisChar >> 8);
	 switch (outputFormat)
	 {
	 case UTF8:
	    buf[off++] = b1;
	    b2	       = 0;
	    break;
	 case UTF16_LE:
	    buf[off++] = b1;
	    buf[off++] = b2;
	    break;
	 case UTF16_BE:
	    buf[off++] = b2;
	    buf[off++] = b1;
	    break;
	 }	    
//	 Debug.println(pos + " " + thisChar + " " + b1 + " " + b2);
      }
      if (outputFormat != UTF8)
	 len	       *= 2;
      return len;
   }
    public int available() throws IOException
    {
       Debug.println("StringBufferInputStream.available()");
       return count - pos;
    }

}
