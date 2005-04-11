/**
 * Interace for implementing an interactive console input debug mode for
 * any class.
 */
package cm.generic;

/**
 * Interace for implementing an interactive console input debug mode for
 * any class.
 * 
 * @author blake
 */
public interface DebugMode 
{
	public void setDebugMode(boolean doDebug);
	public void toggleDebugMode(); //inverts the debug mode
	public boolean getDebugMode();
}
