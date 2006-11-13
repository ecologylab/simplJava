package ecologylab.services.logging;

import java.util.ArrayList;

import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;

/**
 * Send an intermediate sequence of ops to the logging server.
 * 
 * 1) Keep mixed initiative loggin data Set
 * 2) Handle recieved logging messages from client 
 * 
 * @author eunyee
 *
 */
@xml_inherit
public final class LogOps extends LogRequestMessage
{
	@xml_nested protected ArrayList set	=	new ArrayList();
	
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