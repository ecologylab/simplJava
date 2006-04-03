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
	private static ResponseMessage BADTransmissionResponse = null;
	
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
	 * Returns a ResponseMessage object representing the ReponseTypes 'BAD'
	 * 
	 * @return A ReponseMessage representing 'BAD'
	 */
	public static ResponseMessage BADTransmissionResponse()
	{
		if (BADTransmissionResponse == null)
			BADTransmissionResponse = new ResponseMessage(BADTransmission);
		return BADTransmissionResponse;
	}
}
