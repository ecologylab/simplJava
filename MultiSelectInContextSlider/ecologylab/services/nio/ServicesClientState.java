/*
 * Created on May 15, 2006
 */
package ecologylab.services.nio;

/**
 * Models the state of a ServicesClientNIO connection.
 * 
 * May be queried to determine the state of the connection.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class ServicesClientState implements BaseStates
{
    protected String state = NOT_CONNECTED;

    protected NIOClient connection = null;
    
    public ServicesClientState(NIOClient connection)
    {
        this.connection = connection;
    }
    
    /**
     * Queries the underlying connection to determine the state, sets its state, then returns it.
     * @return
     *  the state of connection.
     */
    public String getState()
    { // TODO be able to query about conecting; make connecting state non-blocking.
        if (connection.connected())
        {
            this.state = CONNECTED;
        } else {
            this.state = NOT_CONNECTED;
        }
        
        return state;
    }

}
