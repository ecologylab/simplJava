package simpl.languages;

import java.util.Collection;
import java.util.HashMap;

import simpl.exceptions.SIMPLTranslationException;


public class PythonLanguage implements ISimplLanguage{

	private static HashMap<String, String> keywords = new HashMap<String,String>();
	private static void add(String keyword)
	{
		keywords.put(keyword, keyword);
	}
	
	// Statically add all keywords for Python. 
	// probably could alphabetize it. Not a high priority now, though. 
	static
	{
		add("and");
		add("del");
		add("from");
		add("not");
		add("while");
		add("as");
		add("elif");
		add("global");
		add("or");
		add("with");
		add("assert");
		add("else");
		add("if");
		add("pass");
		add("yield");
		add("break");
		add("except");
		add("import");
		add("print");
		add("class");
		add("exec");
		add("in");
		add("raise");
		add("continue");
		add("finally");
		add("is");
		add("return");
		add("def");
		add("for");
		add("lambda");
		add("try");
	}
	
	
	@Override
	public String getLanguageIdentifier() {
		// TODO Auto-generated method stub
		return "python";
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
