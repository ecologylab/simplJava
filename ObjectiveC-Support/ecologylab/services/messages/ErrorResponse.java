package ecologylab.services.messages;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_attribute;

/**
 * Base class for all ResponseMessages that indicate errors.
 * 
 * @author andruid
 */
@xml_inherit
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
