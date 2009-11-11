package ecologylab.appframework.macos;

import ecologylab.appframework.MacOSAppHandler;

/**
 * Shadowed no-op version so we can avoid compile errors on non-mac platforms.
 *
 * Make sure if you're on the mac that the ecologylabMacOS project is before ecologylabFundamental.
 * 
 * @author andrew
 *
 */
public class MacOSApp
{
	/**
	 * Shadowed no-op version so we can avoid compile errors on non-mac platforms.
	 * 
	 * @param macOSAppHandler
	 */
	public static void addListener(MacOSAppHandler macOSAppHandler)
	{
	}
}
