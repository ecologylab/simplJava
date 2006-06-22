package ecologylab.xml;

import java.util.Vector;

/**
 * An ElementState XML tree node that supports an Vector of children (as well as
 * whatever else you add to it). <p/> In general, one should use ArrayListState
 * for this kind of functionality, but, in some cases, there may be concurrency
 * issues, in which case, this more expensive class will be required.
 * 
 * @author andruid
 */
public class VectorState extends ElementState
{
    public Vector set = new Vector();

    public VectorState()
    {
        super();
    }

    public void add(ElementState elementState)
    {
        try
        {
            addNestedElement(elementState);
        }
        catch (XmlTranslationException e)
        {
            debug("ERROR: " + e.getMessage() + " while translating");
        }
    }

    /**
     * Add an element to our Collection.
     */
    public void addNestedElement(ElementState elementState)
            throws XmlTranslationException
    {
        set.add(elementState);
    }

    /**
     * Remove all elements from our Collection.
     * 
     */
    public void clear()
    {
        set.clear();
    }

    /**
     * Get the number of elements in the set.
     * 
     * @return
     */
    public int size()
    {
        return set.size();
    }
}
