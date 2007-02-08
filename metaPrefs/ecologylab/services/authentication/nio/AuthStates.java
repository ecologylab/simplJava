/*
 * Created on May 15, 2006
 */
package ecologylab.services.authentication.nio;

import ecologylab.services.authentication.messages.AuthMessages;
import ecologylab.services.nio.BaseStates;

public interface AuthStates extends BaseStates, AuthMessages
{
    public static final String LOGGING_IN = "Logging in.";
    public static final String LOGGED_IN = "Logged in to server.";
}
