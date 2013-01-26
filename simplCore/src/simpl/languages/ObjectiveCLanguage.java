package simpl.languages;

import java.util.Collection;

import simpl.exceptions.SIMPLTranslationException;


public class ObjectiveCLanguage implements ISimplLanguage{

	@Override
	public String getLanguageIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getReservedKeywords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isReservedKeyword(String keyword) {
		// TODO Auto-generated method stub
		return false;
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
