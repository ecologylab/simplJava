package ecologylab.serialization.deserializers.parsers.bibtex;


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
	
	Object getBibTeXObject();

}
