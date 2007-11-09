/*
 * Created on Apr 5, 2006
 */
package ecologylab.services;

import ecologylab.net.NetTools;

/**
 * A place to gather host and port assignments in the Interface Ecology Lab. People using our software elsewhere may
 * wish to change these. People in our lab need to talk to each other about changes here.
 * 
 * @author andruid
 */
public interface ServicesHostsAndPorts
{
	// public static final String LOGGING_HOST = "128.194.147.49"; // ecology1
	// publicstatic final String LOGGING_HOST = "128.194.138.51"; //
	// unix.cs.tamu.edu

	/** The CSDLL host. */
	public static final String	CSDLL_HOST											= "128.194.147.58";		// CSDLL

	/** Normal place to look for a logging server; currently localhost. */
	public static final String	LOGGING_HOST										= NetTools.localHost();

	/** Normal port for handling remote logging. */
	public static final int		LOGGING_PORT										= 10201;

	/** Normal port for browser services. */
	public static final int		BROWSER_SERVICES_PORT							= 10001;

	/** Normal port for combinFormation services. */
	public static final int		CF_SERVICES_PORT									= 10010;

	/** Normal port for Rogue Signals network communication. */
	public static final int		ROGUE_PORT											= 14444;

	/** Normal port for Rogue Signals to communicate with sound patches. */
	public static final int		ROGUE_OSC_SOUND_PATCH_LISTENER_PORT			= 14446;

	/** Normal port for Rogue Signals to communicate with sound patches. */
	public static final int		ROGUE_OSC_SOUND_RECORDER_ACKNOWLEDGE_PORT	= 14450;

	/** Normal port for Rogue Signals to communicate with physiological sensors. */
	public static final int		PHYSI_ROGUE_UDP_PORT								= 14445;

	/** Normal port for web application services. */
	public static final int		WEB_START_APP_SERVICES_PORT					= 10011;
}
