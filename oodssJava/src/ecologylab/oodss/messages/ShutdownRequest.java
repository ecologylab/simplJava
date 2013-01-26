/**
 * 
 */
package ecologylab.oodss.messages;

import simpl.annotations.dbal.simpl_inherit;
import ecologylab.collections.Scope;
import ecologylab.oodss.distributed.common.SessionObjects;
import ecologylab.oodss.distributed.impl.Shutdownable;

/**
 * A message indicating that the server should shut down it's associated
 * application. Before the shutdown sequence is complete, the server should
 * respond to the client.
 * 
 * Generally, the response should be an OkResponse, indicating that the server
 * has complied and is beginning the shutdown sequence. An ErrorResponse
 * generally means that the server does not support this message.
 * 
 * Subclasses could have the server return certain data before shutting down,
 * such as a log file.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
@simpl_inherit public class ShutdownRequest extends RequestMessage implements
		SessionObjects
{

	/**
	 * 
	 */
	public ShutdownRequest()
	{
	}

	/**
	 * @see ecologylab.oodss.messages.RequestMessage#performService(ecologylab.collections.Scope)
	 */
	@Override public ResponseMessage performService(Scope objectRegistry)
	{
		try
		{
			Shutdownable s = (Shutdownable) objectRegistry
					.get(MAIN_SHUTDOWNABLE);

			if (s != null)
			{
				s.shutdown();

				return OkResponse.reusableInstance;
			}
			
			// else fall through to error response
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return new ErrorResponse("Attempt to shut down server failed.");
	}
}
