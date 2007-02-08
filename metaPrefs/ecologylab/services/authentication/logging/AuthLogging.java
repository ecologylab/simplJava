package ecologylab.services.authentication.logging;

import ecologylab.services.logging.Logging;

public interface AuthLogging
{
    public void addLoggingListener(Logging log);

    public void fireLoggingEvent(AuthenticationOp op);
}
