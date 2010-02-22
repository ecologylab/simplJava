package ecologylab.tutorials.oodss.historyecho;
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
		StringBuffer prevEcho;
		String prevEchoTmp;
		
		/*
		 * Retrieve, from the object registry,
		 *	the last-sent string. In the case that 
		 * we are running a server in which echo_history
		 * has already been instantiated in the application scope
		 * then we will use and update a application history value.
		 */
		if(cSScope.get(ECHO_HISTORY) == null)
		{
			/*
			 * In the case that the sever hasn't then we instantiate our own,
			 * in the session scope.
			 */
			cSScope.put(ECHO_HISTORY, new StringBuffer());
		}
		
		prevEcho = (StringBuffer) cSScope.get(ECHO_HISTORY);
		
		// Temporarily store the previous echo string
		prevEchoTmp = new String(prevEcho);
		
		/*
		 *  replace it with the new one
		 */
		prevEcho.replace(0,prevEcho.length(),newEcho);
		
		/*
		 * use both messages to create a new response
		 */
		return new HistoryEchoResponse(prevEchoTmp, newEcho);
	}
	
	public boolean isDisposable()
	{
		return true;
	}
}
