package ecologylab.services.authentication.messages;

import ecologylab.services.authentication.AuthenticationListEntry;

public interface AuthenticationRequest
{
	public AuthenticationListEntry getEntry();
}