package ecologylab.serialization.bibtex;

import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.bibtex.entrytypes.AbstractBibTeXEntry;
import ecologylab.serialization.bibtex.entrytypes.BibTeXArticle;
import ecologylab.serialization.bibtex.entrytypes.BibTeXBook;
import ecologylab.serialization.bibtex.entrytypes.BibTeXInBook;
import ecologylab.serialization.bibtex.entrytypes.BibTeXInProceedings;
import ecologylab.serialization.bibtex.entrytypes.BibTeXPhdThesis;
import ecologylab.serialization.bibtex.entrytypes.BibTeXTechReport;

public class BibTeXEntryTranslationScope
{
	
	private static Class[] bibtexEntryTypes = {
		AbstractBibTeXEntry.class,
		BibTeXArticle.class,
		BibTeXBook.class,
		BibTeXInBook.class,
		BibTeXInProceedings.class,
		BibTeXPhdThesis.class,
		BibTeXTechReport.class,
	};
			
	public static TranslationScope get()
	{
		return TranslationScope.get("bibtex_entry_types", bibtexEntryTypes);
	}
	
}
