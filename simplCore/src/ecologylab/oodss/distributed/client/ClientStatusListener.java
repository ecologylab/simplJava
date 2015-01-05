package ecologylab.oodss.distributed.client;

public interface ClientStatusListener
{
	/**
	 * Invoked when the client's connection status changes.
	 * 
	 * @param connect
	 *          true if the client connected; false if the client disconnected.
	 */
	public void clientConnectionStatusChanged(boolean connect);
}
