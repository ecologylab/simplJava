package ecologylab.serialization.deserializers.parsers.bibtex;

import ecologylab.serialization.ElementState;

public interface BibTeXEvents
{

	void startBibTeX();

	void startEntity(String typeName);

	void key(String key);

	void startTag(String tagName);

	void endTag();

	void value(String value);

	void endEntity();

	void endBibTeX();
	
	ElementState getBibTeXObject();

}
