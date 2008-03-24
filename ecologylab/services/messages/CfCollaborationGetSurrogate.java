package ecologylab.services.messages;


import ecologylab.collections.Scope;
import ecologylab.generic.Debug;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.xml_inherit;

@xml_inherit
public class CfCollaborationGetSurrogate extends RequestMessage {

	@xml_leaf(CDATA) protected String surrogateSetString;
	
	TranslationScope translationSpace;
	
	public CfCollaborationGetSurrogate()
	{
		super();
	}

	public String getSurrogateSetString() 
	{
		return surrogateSetString;
	}

	public void setSurrogateSetString(String surrogateSetString) 
	{
		this.surrogateSetString = surrogateSetString;
	}

	@Override
	public ResponseMessage performService(Scope objectRegistry) 
	{
		Debug.println("Received loud and clear: " + surrogateSetString);
		
		return OkResponse.get();
	}
	
	public CfCollaborationGetSurrogate (String surrogateSetString, TranslationScope translationSpace) 
	throws XMLTranslationException
	{
		this(surrogateSetString);
		this.translationSpace = translationSpace;
	}
	
	public CfCollaborationGetSurrogate(String surrogateSetString)
	{
		super();
		this.surrogateSetString = surrogateSetString;
	}

	
}
