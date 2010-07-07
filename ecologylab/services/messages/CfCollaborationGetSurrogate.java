package ecologylab.services.messages;


import ecologylab.collections.Scope;
import ecologylab.generic.Debug;
import ecologylab.xml.Hint;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.simpl_inherit;

@simpl_inherit
public class CfCollaborationGetSurrogate extends RequestMessage {

	@simpl_scalar @simpl_hints(Hint.XML_LEAF_CDATA) protected String surrogateSetString;
	
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
