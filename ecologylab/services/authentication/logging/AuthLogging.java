package ecologylab.services.authentication.logging;

import ecologylab.services.logging.Logging;

/**
 * Interface for classes that will fire logging events based on authentication events.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public interface AuthLogging
{
	public void addLoggingListener(Logging log);

	public void fireLoggingEvent(AuthenticationOp op);
}
