/**
 * 
 */
package ecologylab.collections;

import java.util.ArrayList;
import java.util.HashMap;

import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;

/**
 * An optimized data structure for managing a hierarchical collection of prefixes, automatically 
 * merging and removing entries, and providing a fast matching function.
 * 
 * @author andruid
 */
public class PrefixCollection  extends PrefixPhrase 
{
	final char						separator;
	
/**
 * Construct a PrefixCollection in which each prefix can be parsed into PrefixPhrases,
 * using the separator to split the phrases.
 * 
 * @param separator
 */
	public PrefixCollection(char separator) 
	{
		super(null, null);
		this.separator				= separator;
	}

	/**
	 * Construct a PrefixCollection in which each prefix can be parsed into PrefixPhrases,
	 * using the separator to split the phrases.
	 * 
	 * @param separator
	 */
	public PrefixCollection() 
	{
		this('/');
	}
		
	public PrefixPhrase add(ParsedURL purl)
	{
		String host				= purl.url().getHost();		
		// domainPrefix is a child of this, the root (with no parent)
		PrefixPhrase hostPrefix	= getPrefix(null, host);
		
		// children of hostPrefix
		return (hostPrefix != null) ? hostPrefix.add(purl.pathDirectoryString(), separator) : lookupChild(host);
	}

	public boolean match(ParsedURL purl)
	{
		String host				= purl.url().getHost();		
		// domainPrefix is a child of this, the root (with no parent)
		PrefixPhrase hostPrefix	= lookupChild(host);
		
		// children of hostPrefix
		return (hostPrefix == null) ? false : hostPrefix.match(purl.pathDirectoryString(), separator);
	}
	
	public ArrayList<String> values()
	{
		return values(separator);
	}

	static final ParsedURL[] TEST_ADD	=
	{
//		ParsedURL.getAbsolute("http://nytimes.com"), 
//		ParsedURL.getAbsolute("http://www.nytimes.com/2008"),
		ParsedURL.getAbsolute("http://nytimes.com/pages/sports/foo/bar/baz/bloch"),
		ParsedURL.getAbsolute("http://nytimes.com/pages/sports/"),
		ParsedURL.getAbsolute("http://nytimes.com/pages/sports/foo/"),
		ParsedURL.getAbsolute("http://nytimes.com/pages/sports/foo/bar/baz/bloch"),
		ParsedURL.getAbsolute("http://nytimes.com/pages/arts/interactive"),
		ParsedURL.getAbsolute("http://nytimes.com/pages/sports/foo"),
		ParsedURL.getAbsolute("http://nytimes.com/pages/arts"),
		ParsedURL.getAbsolute("http://www.nytimes.com/2008/01/26/sports/football/26giants.html?ref=sports"),
		
	};
	
	static final ParsedURL[] TEST_MATCH	=
	{
		ParsedURL.getAbsolute("http://nytimes.com/pages/sports/hoops"),
		ParsedURL.getAbsolute("http://nytimes.com/pages/"),
		ParsedURL.getAbsolute("http://nytimes.com/pages/sports/"),
		ParsedURL.getAbsolute("http://nytimes.mom/"),
		ParsedURL.getAbsolute("http://nytimes.com/pages/arts/interactive"),
		ParsedURL.getAbsolute("http://nytimes.com/"),
		ParsedURL.getAbsolute("http://nytimes.com/pages/arts/"),
		ParsedURL.getAbsolute("http://www.nytimes.com/2008/01/26/sports/baseball"),

	};
	public static void main(String[] s)
	{
		char separator = '/';
		PrefixCollection pc	= new PrefixCollection(separator);
		
		StringBuilder buffy	= new StringBuilder(32);
		
		for (int i=0; i<TEST_ADD.length; i++)
		{
//			println(TEST[i].directoryString());
			PrefixPhrase pp	=	pc.add(TEST_ADD[i]);
			buffy.setLength(0);
			pp.toStringBuilder(buffy, separator);
			println(buffy);
		}
		println("\n");
		
		for (int i=0; i<TEST_MATCH.length; i++)
		{
			ParsedURL purl	= TEST_MATCH[i];
			
			println(purl.toString() + "\t" + pc.match(purl));
		}
		println("\n");
		
		for (String phrase : pc.values())
		{
			println(phrase);
		}
	}
	
	public char separator()
	{
		return separator;
	}
}
