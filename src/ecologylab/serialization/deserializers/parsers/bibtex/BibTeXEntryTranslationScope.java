package ecologylab.serialization.deserializers.parsers.bibtex;

import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.deserializers.parsers.bibtex.entrytypes.AbstractBibTeXEntry;
import ecologylab.serialization.deserializers.parsers.bibtex.entrytypes.BibTeXArticle;
import ecologylab.serialization.deserializers.parsers.bibtex.entrytypes.BibTeXBook;
import ecologylab.serialization.deserializers.parsers.bibtex.entrytypes.BibTeXInBook;
import ecologylab.serialization.deserializers.parsers.bibtex.entrytypes.BibTeXInProceedings;
import ecologylab.serialization.deserializers.parsers.bibtex.entrytypes.BibTeXPhdThesis;
import ecologylab.serialization.deserializers.parsers.bibtex.entrytypes.BibTeXTechReport;

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
