/**
 * 
 */
package ecologylab.services;

/**
 * @author Zach Toups
 * 
 */
public class ServerEvent
{
    public static final String SERVER_STOPPED       = "server stopped";

    public static final String SERVER_STARTED       = "server started";

    public static final String SERVER_SHUTTING_DOWN = "server shutting down";

    String                     event;

    ServicesServerBase         server;

    public ServerEvent(String event, ServicesServerBase server)
    {
        this.event = event;
        this.server = server;
    }

    /**
     * @return the event
     */
    public String getEvent()
    {
        return event;
    }

    /**
     * @return the server
     */
    public ServicesServerBase getServer()
    {
        return server;
    }

}
