package ecologylab.services.messages;

/**
 * Service response message.
 * @author blake
 *
 */
public class ResponseMessage 
extends ServiceMessage
implements ResponseTypes
{
	public String response;
	
	private static ResponseMessage OKResponse 	= null;
	
	public ResponseMessage() {}
	
	public ResponseMessage(String response)
	{
		this.response = response;
	}
	
	/**
	 * Returns a ResponseMessage object representing the ReponseTypes 'OK'
	 * 
	 * @return A ReponseMessage representing 'OK'
	 */
	public static ResponseMessage OKResponse()
	{
		if (OKResponse == null)
			OKResponse = new ResponseMessage(OK);
		return OKResponse;
	}

    /**
     * @return Returns the response.
     */
    public String getResponse() {
        return response;
    }

    /**
     * @param response The response to set.
     */
    public void setResponse(String response) {
        this.response = response;
    }
}
