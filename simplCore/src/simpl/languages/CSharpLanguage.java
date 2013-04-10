package simpl.languages;

import java.util.Collection;
import java.util.HashMap;

import simpl.exceptions.SIMPLTranslationException;


public class CSharpLanguage implements ISimplLanguage {

	private static HashMap<String, String> keywords = new HashMap<String,String>();
	private static void add(String keyword)
	{
		keywords.put(keyword, keyword);
	}
	
	// Statically add all keywords for CSharp. 
	static
	{
		add("abstract");
		add("as");
		add("base");
		add("bool");
		add("break");
		add("byte");
		add("case");
		add("catch");
		add("char");
		add("checked");
		add("class");
		add("const");
		add("continue");
		add("decimal");
		add("default");
		add("delegate");
		add("do");
		add("double");
		add("else");
		add("enum");
		add("event");
		add("explicit");
		add("extern");
		add("false");
		add("finally");
		add("fixed");
		add("float");
		add("for");
		add("foreach");
		add("goto");
		add("if");
		add("implicit");
		add("in");
		add("int");
		add("interface");
		add("internal");
		add("is");
		add("lock");
		add("long");
		add("namespace");
		add("new");
		add("null");
		add("object");
		add("operator");
		add("out");
		add("override");
		add("params");
		add("private");
		add("protected");
		add("public");
		add("readonly");
		add("ref");
		add("return");
		add("sbyte");
		add("sealed");
		add("short");
		add("sizeof");
		add("stackalloc");
		add("static");
		add("string");
		add("struct");
		add("switch");
		add("this");
		add("throw");
		add("true");
		add("try");
		add("typeof");
		add("uint");
		add("ulong");
		add("unchecked");
		add("unsafe");
		add("ushort");
		add("using");
		add("virtual");
		add("void");
		add("volatile");
		add("while");
	}
	
	
	@Override
	public String getLanguageIdentifier() {
		// TODO Auto-generated method stub
		return "csharp";
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
