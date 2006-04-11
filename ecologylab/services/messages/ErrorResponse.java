package ecologylab.services.messages;

/**
 * Base class for all ResponseMessages that indicate errors.
 * 
 * @author andruid
 */
public class ErrorResponse extends ResponseMessage
{
	public String explanation;
	
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

    /**
     * @return Returns the explanation.
     */
    public String getExplanation()
    {
        return explanation;
    }

}
