package ecologylab.services.logging;

import java.util.ArrayList;

import ecologylab.xml.ElementState;

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
    public void addNestedElement(String string)
    {
        set.add(string);
    }
	
	public void clearSet()
	{
		set.clear();
	}
	
}