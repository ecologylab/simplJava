package ecologylab.services.authentication.messages;

import ecologylab.services.authentication.AuthenticationListEntry;
import ecologylab.services.messages.SendableRequest;

public interface AuthenticationRequest extends SendableRequest
{
	public AuthenticationListEntry getEntry();
}