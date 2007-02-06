/*
 * Created on May 15, 2006
 */
package ecologylab.services.authentication.nio;

import ecologylab.services.nio.ServicesClientState;

public class AuthClientState extends ServicesClientState implements AuthStates
{

    public AuthClientState(NIOAuthClient connection)
    {
        super(connection);
    }

    /**
     * May modify underlying connection.
     */
    public String getState()
    {
        state = super.getState();
        
        if (CONNECTED.equals(state))
        { // we are connnected, determine higher level status, such as being logged in.
            if (((NIOAuthClient)connection).isLoggingIn())
            {
                try
                {
                    ((NIOAuthClient)connection).finishLogin();
                } catch (Exception e)
                { // shouldn't happen
                    e.printStackTrace();
                }
                
                // we may have finished the login operation, check to see
                if (((NIOAuthClient)connection).isLoggingIn())
                { // still logging in, that's our state
                    state = LOGGING_IN;
                } else
                { // no longer logging in, check to see if we are logged in
                    if (((NIOAuthClient)connection).isLoggedIn())
                    {
                        state = LOGGED_IN;
                    } else
                    {
                        // TODO handle logout!
                        state = CONNECTED;
                    }
                }
            } else
            { // no longer logging in, check to see if we are logged in
                if (((NIOAuthClient)connection).isLoggedIn())
                {
                    state = LOGGED_IN;
                } else
                {
                    // TODO handle logout!
                    state = CONNECTED;
                }
            }            
        }
        
        return state;
    }
    
    

}
