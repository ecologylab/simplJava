package ecologylab.serialization;

import java.util.Collection;
import java.util.HashMap;

public class JavaLanguage implements ISimplLanguage{

	private static HashMap<String, String> keywords = new HashMap<String, String>();
	
	static
	{
		keywords.put("abstract", "abstract");
		keywords.put("continue","continue");
		keywords.put("for","for");
		keywords.put("new","new");
		keywords.put("switch","switch");
		keywords.put("assert","assert");
		keywords.put("default","default");
		keywords.put("package","package");
		keywords.put("synchronized","synchronized");
		keywords.put("boolean","boolean");
		keywords.put("do","do");
		keywords.put("if","if");
		keywords.put("private","private");
		keywords.put("this","this");
		keywords.put("break","break");
		keywords.put("double","double");
		keywords.put("implements","implements");
		keywords.put("protected","protected");
		keywords.put("throw","throw");
		keywords.put("byte","byte");
		keywords.put("else","else");
		keywords.put("import","import");
		keywords.put("public","public");
		keywords.put("throws","throws");
		keywords.put("case","case");
		keywords.put("enum","enum");
		keywords.put("instanceof","instanceof");
		keywords.put("return","return");
		keywords.put("transient","transient");
		keywords.put("catch","catch");
		keywords.put("extends","extends");
		keywords.put("int","int");
		keywords.put("short","short");
		keywords.put("try","try");
		keywords.put("char","char");
		keywords.put("final","final");
		keywords.put("interface","interface");
		keywords.put("static","static");
		keywords.put("void","void");
		keywords.put("class","class");
		keywords.put("finally","finally");
		keywords.put("long","long");
		keywords.put("strictfp","strictfp");
		keywords.put("volatile","volatile");
		keywords.put("const","const");
		keywords.put("float","float");
		keywords.put("native ","native ");
		keywords.put("super","super");
		keywords.put("while","while");
		keywords.put("goto","goto");
	}
	
	@Override
	public String getLanguageIdentifier() {
		return "java";
	}

	@Override
	public Collection<String> getReservedKeywords() {
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
