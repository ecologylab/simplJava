package ecologylab.oodss.messages;


import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;
import simpl.exceptions.SIMPLTranslationException;
import ecologylab.collections.Scope;
import ecologylab.generic.Debug;
import ecologylab.serialization.SimplTypesScope;

@simpl_inherit
public class CfCollaborationGetSurrogate extends RequestMessage {

	@simpl_scalar @simpl_hints(Hint.XML_LEAF_CDATA) protected String surrogateSetString;
	
	SimplTypesScope translationScope;
	
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
	
	public CfCollaborationGetSurrogate (String surrogateSetString, SimplTypesScope translationScope) 
	throws SIMPLTranslationException
	{
		this(surrogateSetString);
		this.translationScope = translationScope;
	}
	
	public CfCollaborationGetSurrogate(String surrogateSetString)
	{
		super();
		this.surrogateSetString = surrogateSetString;
	}

	
}
