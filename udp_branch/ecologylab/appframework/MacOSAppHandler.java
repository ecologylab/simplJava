/**
 * 
 */
package ecologylab.appframework;

/**
 * Interface for handling Mac OS X application specific events (those related to the application menu found only on Mac OS X).
 * 
 * @author andrew
 *
 */
public interface MacOSAppHandler
{
	public void handleAbout();

	public void handleOpenApplication();

	public void handleOpenFile(String filename);

	public void handlePreferences();

	public void handlePrintFile(String filename);

	public void handleQuit();
	
	public void handleReOpenApplication();
}
