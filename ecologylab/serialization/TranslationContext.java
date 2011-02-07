package ecologylab.serialization;

import java.io.File;
import java.util.HashMap;

import ecologylab.net.ParsedURL;

public class TranslationContext implements ScalarUnmarshallingContext
{
	public HashMap<Integer, ElementState>	marshalledObjects				= new HashMap<Integer, ElementState>();

	public HashMap<Integer, ElementState>	visitedElements					= new HashMap<Integer, ElementState>();

	public HashMap<Integer, ElementState>	needsAttributeHashCode	= new HashMap<Integer, ElementState>();

	public HashMap<String, ElementState>	unmarshalledObjects			= new HashMap<String, ElementState>();

	protected ParsedURL										purlContext;

	protected File												fileContext;

	protected String											delimiter								= ",";

	public TranslationContext()
	{

	}

	public TranslationContext(File fileContext)
	{
		this.fileContext = fileContext;
		this.purlContext = new ParsedURL(fileContext);
	}

	@Override
	public ParsedURL purlContext()
	{
		return (purlContext != null) ? purlContext : (fileContext != null) ? new ParsedURL(fileContext)
				: null;
	}

	@Override
	public File fileContext()
	{
		return (fileContext != null) ? fileContext : (purlContext != null) ? purlContext.file() : null;
	}

	public String getDelimiter()
	{
		return delimiter;
	}

}