package ecologylab.oodss.messages;

import ecologylab.serialization.annotations.simpl_inherit;

/**
 * Base class for all ResponseMessages that indicate errors.
 * 
 * @author andruid
 */
@simpl_inherit
public class ErrorResponse extends ExplanationResponse
{
	public ErrorResponse()
	{
		super();
	}

	public ErrorResponse(String response)
	{
		this();
		this.explanation	= response;
	}

	public boolean isOK()
	{
		return false;
	}
}
