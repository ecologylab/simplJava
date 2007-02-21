/*
 * Created on Apr 5, 2006
 */
package ecologylab.services;

import ecologylab.net.NetTools;
import ecologylab.services.logging.Logging;

/**
 * A place to gather host and port assignments in the Interface Ecology Lab.
 * People using our software elsewhere may wish to change these. People in our
 * lab need to talk to each other about changes here.
 * 
 * @author andruid
 */
public interface ServicesHostsAndPorts
{
    // public static final String LOGGING_HOST = "128.194.147.49"; // ecology1
    // publicstatic final String LOGGING_HOST = "128.194.138.51"; //
    // unix.cs.tamu.edu

    // public static final String LOGGING_HOST = "128.194.147.58"; // CSDLL
    // server IP address

    public static final String LOGGING_HOST                = NetTools
                                                                   .localHost();

    public static final int    LOGGING_PORT                = 10000;

    public static final int    BROWSER_SERVICES_PORT       = 10001;

    public static final int    CF_SERVICES_PORT            = 10010;

    public static final int    ROGUE_PORT                  = 14444;

    public static final int    ROGUE_OSC_SOUND_UDP_PORT    = 14446;

    public static final int    PHYSI_ROGUE_UDP_PORT        = 14445;

    public static final int    WEB_START_APP_SERVICES_PORT = 10011;
}
