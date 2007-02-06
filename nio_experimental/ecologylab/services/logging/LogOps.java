package ecologylab.services.logging;

import java.util.ArrayList;

import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;

/**
 * Bundle a sequence of {@link MixedInitiativeOp MixedInitiativeOp}s,
 * and send them to the logging server.
 * 
 * @author eunyee
 * @author andruid
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