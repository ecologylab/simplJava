package ecologylab.generic;

import java.io.UnsupportedEncodingException;


/**
 * Help for working with Sun's attrocious drag and drop and clipboard "support".
 *
 * @author andruid
 */
public class DataTransferTools extends Debug
{
	
	public DataTransferTools()
	{
		super();
		
	}
	
	static final String EOL = "\r\n";
	
	static final String HTML_CLIPBOARD_FORMAT_HEADER = "Version:1.0"+ EOL +
		"StartHTML:00000000000"+ EOL +
		"EndHTML:00000000000"+ EOL +
		"StartFragment:00000000000"+ EOL +
		"EndFragment:00000000000" + EOL ;
	
	
	static final int HEADER_LENGTH = HTML_CLIPBOARD_FORMAT_HEADER.length();

	/**
	 * surrounds the html with this envelope, ready for
	 * windows clipboard (jdk support can be made better)
	 * <pre>
	 * Version:1.0
	 * StartHTML:00000000000
	 * EndHTML:00000000000
	 * StartFragment:00000000000
	 * EndFragment:00000000000
	 * &lt;!--StartFragment--&gt;
	 * ...
	 * &lt;!-- EndFragment-- &gt;
	 * </pre>
	 * We have to return a byte array 'cause in Windows the html needs to be utf-8
	 * encoded. And because we have to calculate char-offsets, we encode it here.
	 * @param html
	 * @return byte[]
	 */
	public static byte[] convertToMSCfHtml(String html) throws UnsupportedEncodingException
	{
		html = "<!--StartFragment-->" + html + "<!--EndFragment-->\r\n\0";
		
		byte[] bHtml = html.getBytes("UTF-8");// encode first 'cause it may grow
		int htmlLen = bHtml.length;
		
		StringBuffer buf = new  StringBuffer(HTML_CLIPBOARD_FORMAT_HEADER);
		setValueInHeader( buf, "StartHTML", HEADER_LENGTH-1);
		setValueInHeader( buf, "EndHTML", HEADER_LENGTH + htmlLen-1);
		setValueInHeader( buf, "StartFragment", HEADER_LENGTH-1);
		setValueInHeader( buf, "EndFragment", HEADER_LENGTH + htmlLen-1);
		byte[] bHeader = buf.toString().getBytes("UTF-8");// should stay the same (no nonASCII chars in header)
		
		byte result[] = new byte[HEADER_LENGTH + htmlLen ];
		System.arraycopy(bHeader, 0, result, 0, bHeader.length);
		System.arraycopy(bHtml, 0, result, bHeader.length, bHtml.length);
		
		return result;
	}
	
	/**
	 * Replaces name+":00000000000" with name+":xxxxxxxxxxx" where xxx... is the '0' padded value.
	 * Value can't be to long, since maxint can be displayed with 11 digits. If value is below zero
	 * there is enough place (10 for the digits 1 for sign).<br>
	 * If the search is not found nothing is done.
	 * @param src
	 * @param name
	 * @param value
	 */
	private static void setValueInHeader( StringBuffer src, String name, int value)
	{
		String search = name+":00000000000";
		int pos = src.indexOf(search);
		if (pos ==-1) return;// not found, do nothing
		
		boolean belowZero = value<0;
		if (belowZero) value = -value;
		
		src.replace(pos+search.length()-(value+"").length(), pos+search.length(), value+"");
		if (belowZero) src.setCharAt(pos+name.length()+1,'-'); // +1 'cause of ':' in "SearchMe:"
	}
}
