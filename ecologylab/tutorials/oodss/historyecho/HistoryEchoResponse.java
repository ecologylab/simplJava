package ecologylab.tutorials.oodss.historyecho;
import ecologylab.collections.Scope;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;

/**
 * Implements a message that will be sent back to
 * HistoryEchoClient by the server containing the last
 * message and the penultimate one.
 */
public class HistoryEchoResponse extends ResponseMessage
{
	@xml_attribute String echo;
	@xml_attribute String prevEcho;
	
	/** 
	 * Constructor used on client.
	 * Fields populated automatically by ecologylab.xml
	 */	
	public HistoryEchoResponse() {}
	
	/** 
	 * Constructor used on server
	 * @param prevEcho a String which indicates the previous echo
	 * received by the server.
	 * 
	 * @ echo echo of the string just received by the History server.
	 */
	public HistoryEchoResponse (String prevEcho, String echo)
	{
		this.prevEcho = prevEcho;
		this.echo = echo;
	}
	
	/** 
	 * Called automatically by OODSS on client
	 * */
	@Override public void processResponse(Scope appObjScope)
	{
		System.out.println("2nd To Last Message: " + prevEcho +
				"\nLast Message: " + echo);
	}
	
	/** 
	 * Checks that the message does not have an error condition,
	 *  for now we assume it doesn't */
	@Override public boolean isOK()
	{
		return true;
	}
}
