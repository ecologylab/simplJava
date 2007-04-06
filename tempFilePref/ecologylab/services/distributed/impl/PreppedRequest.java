/*
 * Created on Apr 4, 2007
 */
package ecologylab.services.nio;


/**
 * Represents a RequestMessage that has been translated to XML. This object
 * encapsulates the XML String, along with the request's UID.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class PreppedRequest implements Comparable<PreppedRequest>
{
    private long   uid;

    private String request;

    /**
     * 
     */
    public PreppedRequest(String request, long uid)
    {
        this.uid = uid;
        this.request = request;
    }

    /**
     * @return the request
     */
    public String getRequest()
    {
        return request;
    }

    /**
     * @return the uid
     */
    public long getUid()
    {
        return uid;
    }

    public int compareTo(PreppedRequest arg0)
    {
        return (int) (this.uid - arg0.getUid());
    }
}
