package ecologylab.oodss.messages;

import ecologylab.collections.Scope;
import ecologylab.serialization.simpl_inherit;

/**
 * Abstract base class for ecologylab.oodss DCF response messages.
 * 
 * @author blake
 * @author andruid
 */
@simpl_inherit abstract public class ResponseMessage<S extends Scope> extends ServiceMessage<S>
{
    public ResponseMessage()
    {
    }

    /**
     * Let's the client easily test for OK or error.
     * 
     * @return	true if the response is not an error of some kind.
     */
    public abstract boolean isOK();

    /**
     * Allows for custom processing of ResponseMessages by ServicesClient,
     * without extending that.
     * 
     * @param objectRegistry
     *            provide a context for response message processing.
     * 
     */
    public void processResponse(S objectRegistry)
    {

    }
}
