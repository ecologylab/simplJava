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
public final class LogOps extends LogRequestMessage
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
	
}