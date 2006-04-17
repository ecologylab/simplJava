package ecologylab.services.logging;

import java.util.ArrayList;

import ecologylab.xml.ArrayListState;
import ecologylab.xml.VectorState;
import ecologylab.xml.ElementState;
import ecologylab.xml.XmlTranslationException;

/**
 * Send an intermediate sequence of ops to the logging server.
 * 
 * 1) Keep mixed initiative loggin data Set
 * 2) Handle recieved logging messages from client 
 * 
 * @author eunyee
 *
 */
public class LogOps extends LogRequestMessage
{
	public ArrayList set	=	new ArrayList();
	
	public void addNestedElement(ElementState elementState)
	{
		if (elementState instanceof MixedInitiativeOp)
			set.add(elementState);
	}
	
	public void clearSet()
	{
		set.clear();
	}

	/**
	 * The string that the LoggingServer will write.
	 * Eliminates the outer XML element, such as <log_request_message> or <log_ops>.
	 */
	String getMessageString() throws XmlTranslationException
	{
//		TagMapEntry	tagMapEntry	= this.getTagMapEntry(getClass(), false);
		String xmlString	= xmlString();
	
//		int start			= xmlString.indexOf(tagMapEntry.openTag) + tagMapEntry.openTag.length();
		// start of the real stuff is the end of the first tag -- whatever it is
		int start			= xmlString.indexOf('>') + 1;
		// end of the real stuff is the start of the close tag... 
		// which also should be the start of the last tag -- whatever it is
		int end				= xmlString.lastIndexOf('<');
//		int end				= xmlString.indexOf(tagMapEntry.closeTag);
		if( (start==-1) || (end==-1) )
		{
			debug("RECEIVE MESSAGE : " + xmlString);
			return "\n";
		}
		return (String) xmlString.substring(start, end) + "\n";
	}
	
}