package ecologylab.generic;

/**
 * A Runnable that, like most, can be start()ed and stop()ped.
 * 
 * @author andruid
 */
public interface StartAndStoppable extends Runnable
{
	void start();

	void stop();
}
