package ecologylab.services.logging.playback;

/**
 * Status indicators for log playback application.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public interface PlaybackControlCommands
{
	static final String	PLAY				= "play";

	static final String	STOP				= "stop";

	static final String	PAUSE				= "pause";

	static final String	STEP_FORWARD	= "stepForward";

	static final String	STEP_BACK		= "stepBack";
}
