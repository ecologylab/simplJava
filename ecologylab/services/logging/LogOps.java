package ecologylab.services.logging;

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

	public VectorState	mixedInitiativeOpSet = new VectorState();

	public void addNestedElement(ElementState elementState)
	{
		if (elementState instanceof MixedInitiativeOp)
			try {
				mixedInitiativeOpSet.addNestedElement(elementState);
			} catch (XmlTranslationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void clearSet()
	{
		mixedInitiativeOpSet.clear();
	}

	
	String getMessageString() throws XmlTranslationException
	{
		return (String)this.translateToXML(false) + "\n";
	}
	
}