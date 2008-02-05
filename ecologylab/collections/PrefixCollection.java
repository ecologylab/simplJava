/**
 * 
 */
package ecologylab.collections;

import java.util.ArrayList;
import java.util.HashMap;

import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;

/**
 * A map 
 * @author andruid
 */
public class PrefixCollection  extends PrefixPhrase 
{
	final char						separator;
	
	/**
	 * 
	 */
	public PrefixCollection(char separator) 
	{
		super(null, null);
		this.separator				= separator;
	}

	public void add(ParsedURL purl)
	{
		// domainPrefix is a child of this, the root (with no parent)
		PrefixPhrase domainPrefix	= getDomainPrefix(purl);
		
		// children of domainPrefix
		if (domainPrefix != null)
			domainPrefix.add(this, purl.directoryString(), separator);
		
		
	}

	/**
	 * Seek the PrefixPhrase corresponding to the argument.
	 * If it does not exist, return it.
	 * <p/>
	 * If it does exist, does it have 0 children?
	 * 		If so, return null. No need to insert for the argument's phrase.
	 * 		If not, return it.
	 * 
	 * @param purl
	 * @return
	 */
	private PrefixPhrase getDomainPrefix(ParsedURL purl)
	{
		String domain				= purl.domain();		
		return getPrefix(null, domain);
	}
	
	static final ParsedURL[] TEST	=
	{
		ParsedURL.getAbsolute("http://nytimes.com"), 
		ParsedURL.getAbsolute("http://nytimes.com/pages/sports/"),
		ParsedURL.getAbsolute("http://www.nytimes.com/2008/01/26/sports/football/26giants.html?ref=sports"),
		
	};
	
	public static void main(String[] s)
	{
		for (int i=0; i<TEST.length; i++)
			println(TEST[i].directoryString());
		
	}
}
