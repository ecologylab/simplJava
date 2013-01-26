package ecologylab.oodss.messages;

import simpl.annotations.dbal.simpl_inherit;
import ecologylab.collections.Scope;

/**
 * Abstract base class for asynchronous server-to-client updates.
 * 
 * @author bill
 */
@simpl_inherit abstract public class UpdateMessage<S extends Scope> extends ServiceMessage<S>
{
    public UpdateMessage()
    {
    }

    /**
     * Allows for custom processing of ResponseMessages by ServicesClient,
     * without extending that.
     * 
     * @param objectRegistry
     *            provide a context for response message processing.
     * 
     */
    public void processUpdate(S objectRegistry)
    {

    }
}
