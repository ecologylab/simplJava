package ecologylab.serialization;

import java.io.File;
import java.util.HashMap;

import ecologylab.net.ParsedURL;

public class SerializationContext implements ScalarUnmarshallingContext
{
	public HashMap<Integer, ElementState>	marshalledObjects				= new HashMap<Integer, ElementState>();

	public HashMap<Integer, ElementState>	visitedElements					= new HashMap<Integer, ElementState>();

	public HashMap<Integer, ElementState>	needsAttributeHashCode	= new HashMap<Integer, ElementState>();

	public HashMap<String, ElementState>		unmarshalledObjects			= new HashMap<String, ElementState>();
	
	protected ParsedURL purlContext;
	
	protected File fileContext;
	
	public SerializationContext()
	{
		
	}
	
	public SerializationContext(File fileContext)
	{
		this.fileContext = fileContext;
		this.purlContext = new ParsedURL(fileContext);
	}

	@Override
	public ParsedURL purlContext()
	{
		return (purlContext != null) ? purlContext : (fileContext != null) ? new ParsedURL(fileContext) : null;
	}

	@Override
	public File fileContext()
	{
		return (fileContext != null) ? fileContext : (purlContext != null) ? purlContext.file() : null;
	}
}