/*
 * Created on Apr 13, 2006
 */
package ecologylab.services.messages;

public class RespondWithUID extends ResponseMessage
{
    public String uid;
    
    /**
     * Constructor for XML translations only.
     */
    public RespondWithUID() { super(); }

    public RespondWithUID(String uid)
    {
        this.uid = uid;
    }
    
    public boolean isOK()
    {
        return true;
    }

}
