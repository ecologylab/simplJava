/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication.messages;

import ecologylab.collections.Scope;
import ecologylab.services.authentication.Authenticatable;
import ecologylab.services.authentication.AuthenticationListEntry;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.distributed.server.clientmanager.AbstractClientManager;
import ecologylab.services.messages.DisconnectRequest;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.xml_inherit;

/**
 * A Logout message indicates that the connnected client no longer wants to be connected.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
@xml_inherit public class Logout extends DisconnectRequest implements AuthMessages, AuthServerRegistryObjects
{
	@xml_nested protected AuthenticationListEntry	entry	= new AuthenticationListEntry("", "");

	/** Should not normally be used; only for XML translations. */
	public Logout()
	{
		super();
	}

	/**
	 * Creates a new Logout object using the given AuthenticationListEntry object, indicating the user that should be
	 * logged out of the server.
	 * 
	 * @param entry -
	 *           the entry to use for this Logout object.
	 */
	public Logout(AuthenticationListEntry entry)
	{
		super();
		this.entry = entry;
	}

	/**
	 * Attempts to log the user specified by entry from the system; if they are already logged in; if not, sends a
	 * failure response.
	 */
	@Override public ResponseMessage performService(Scope localScope)
	{
		Authenticatable server = (Authenticatable) localScope.get(MAIN_AUTHENTICATABLE);
		String sessionId = (String) localScope.get(AbstractClientManager.SESSION_ID);
		
		if (server.logout(entry, sessionId))
		{
			super.performService(localScope);
			return new LogoutStatusResponse(LOGOUT_SUCCESSFUL);
		}
		else
		{
			return new LogoutStatusResponse(LOGOUT_FAILED_IP_MISMATCH);
		}
	}

	/**
	 * @return Returns the entry.
	 */
	public AuthenticationListEntry getEntry()
	{
		return entry;
	}

}
