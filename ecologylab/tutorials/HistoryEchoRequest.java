package ecologylab.tutorials;
import ecologylab.collections.Scope;
import ecologylab.services.messages.RequestMessage;

/**
 * Implements a message that will be sent to
 * HistoryEchoServer so that it may be echoed back by
 * the server in a HistoryEchoResponse message.
 */
public class HistoryEchoRequest extends RequestMessage
{
	/*
	 * ECHO_HISTORY binds to a String representing the previous string
	 * sent from the client application
	 */
	final static String ECHO_HISTORY = "ECHO_HISTORY";
	@xml_attribute String newEcho;
	
	/** 
	 * Constructor used on server.
	 * Fields populated automatically by ecologylab.xml
	*/
	public HistoryEchoRequest() {}
	
	/** Constructor used on client.
	 * @param newEcho a String that will be passed to the server to echo
	 * */
	public HistoryEchoRequest(String newEcho)
	{
		this.newEcho = newEcho;
	}
	
	/** 
	 * Called automatically by LSDCS on server
	 */
	@Override public HistoryEchoResponse performService(Scope cSScope)
	{
		/*
		 * retrieve, from the session object registry,
		 *	the last-sent string
		 */
		String prevEcho = (String) cSScope.get(ECHO_HISTORY);
		
		/*
		 *  replace it with the new one
		 */
		cSScope.put(ECHO_HISTORY, newEcho);
		
		/*
		 * use both to create a new response
		 */
		return new HistoryEchoResponse(prevEcho, newEcho);
	}
}
