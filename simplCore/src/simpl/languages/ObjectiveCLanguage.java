package simpl.languages;

import java.util.Collection;
import java.util.HashMap;

import simpl.exceptions.SIMPLTranslationException;


public class ObjectiveCLanguage implements ISimplLanguage{

	private static HashMap<String, String> keywords = new HashMap<String,String>();
	private static void add(String keyword)
	{
		keywords.put(keyword, keyword);
	}
	
	// Statically add all keywords for Objective C. 
	static
	{
		add("char");
		add("short");
		add("int");
		add("long");
		add("float");
		add("double");
		add("signed");
		add("unsigned");
		add("id");
		add("const");
		add("volatile");
		add("in");
		add("out");
		add("inout");
		add("bycopy");
		add("byref");
		add("oneway");
		add("self");
		add("super");
		// So, there's actually some back-and forth about this...
		// and really about all keywords in objective C.
		// Just inluding these for the sake of conceptual clarity, even if they should never be an issue. 
		add("@interface");
		add("@end");
		add("@implementation");
		add("@end");
		add("@interface");
		add("@end");
		add("@implementation");
		add("@end");
		add("@protoco");
		add("@end");
		add("@class");
	}
	
	
	@Override
	public String getLanguageIdentifier() {
		// TODO Auto-generated method stub
		return "objectivec";
	}

	@Override
	public Collection<String> getReservedKeywords() {
		// TODO Auto-generated method stub
		return keywords.values();
	}

	@Override
	public boolean isReservedKeyword(String keyword) {
		// TODO Auto-generated method stub
		return keywords.containsKey(keyword);
	}

	@Override
	public String convertSimplNameToLanguageName(String simplName)
			throws SIMPLTranslationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String convertLanguageNameToSimplName(String lanugageName)
			throws SIMPLTranslationException {
		// TODO Auto-generated method stub
		return null;
	}

}
