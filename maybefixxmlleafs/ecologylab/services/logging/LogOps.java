package ecologylab.services.logging;

import java.util.ArrayList;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.XmlTranslationException;
import ecologylab.xml.xml_inherit;

/**
 * Bundle a sequence of {@link MixedInitiativeOp MixedInitiativeOp}s, and send
 * them to the logging server.
 * 
 * @author eunyee
 * @author andruid
 */
@xml_inherit public final class LogOps extends LogRequestMessage
{
    @xml_collection protected ArrayList<String> set = new ArrayList<String>();

    /** Constructor for XML translation. */
    public LogOps()
    {
        super();
    }

    /*
     * public void addNestedElement(ElementState elementState) { if
     * (elementState instanceof MixedInitiativeOp) set.add(elementState); }
     */

    public void recordStringOp(String string)
    {
        set.add(string);
    }

    public void clearSet()
    {
        set.clear();
    }

    public ArrayList<String> getSet()
    {
        return set;
    }

    public void setSet(ArrayList<String> set)
    {
        this.set = set;
    }
}