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
public class PrefixCollection  extends Debug 
{
	HashMap<String, PrefixPhrase>	domainMap	= new HashMap<String, PrefixPhrase>();
	
	char							separator;
	
	/**
	 * 
	 */
	public PrefixCollection(char separator) 
	{
		// TODO Auto-generated constructor stub
	}

	public void add(ParsedURL purl)
	{
		PrefixPhrase domainPrefix	= getDomainPrefix(purl);
		
		domainPrefix.add(purl.directoryString(), separator);
		
		
	}

	private PrefixPhrase getDomainPrefix(ParsedURL purl)
	{
		String domain				= purl.domain();		
		PrefixPhrase domainPrefix	= domainMap.get(domain);
		
		if (domainPrefix == null)
		{
			domainPrefix	= new PrefixPhrase(null, domain);
			domainMap.put(domain, domainPrefix);
		}
		
		return domainPrefix;
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
