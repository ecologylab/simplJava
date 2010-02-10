package ecologylab.services.authentication.messages;

import ecologylab.services.authentication.User;
import ecologylab.services.messages.SendableRequest;

public interface AuthenticationRequest extends SendableRequest
{
	public User getEntry();
}