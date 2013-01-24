package legacy.tests.net;

import java.util.HashMap;

import legacy.tests.TestCase;

import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.SIMPLTranslationException;

public class ParsedURLMapTest extends Debug implements TestCase
{
	private HashMap<ParsedURL, Integer>	urlSpanMap;

	public ParsedURLMapTest()
	{
	}

	@Override
	public void runTest() throws SIMPLTranslationException
	{
		urlSpanMap = new HashMap<ParsedURL, Integer>();
		
		ParsedURL purl = ParsedURL.getAbsolute("http://dl.acm.org/citation.cfm?id=1871437.1871580");
		if (purl == null)
			return;
		
		urlSpanMap.put(purl, (int)(Math.random()*100));
		if (!urlSpanMap.containsKey(purl))
			warning("containsKey() fails for ParsedURL Map");
		if (urlSpanMap.get(purl) == null)
			warning("get() fails for ParsedURL Map");
		
		debug("containsKey() and get() tested for ParsedURL Maps");
	}
}
