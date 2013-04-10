package ecologylab.oodss.messages;

import simpl.annotations.dbal.simpl_inherit;
import ecologylab.collections.Scope;

@simpl_inherit public class CloseMessage extends RequestMessage
{
	private static final CloseMessage	INSTANCE	= new CloseMessage();

	@Override public ResponseMessage performService(Scope objectRegistry)
	{
		System.exit(0);
		return null;
	}

	public static CloseMessage get()
	{
		return INSTANCE;
	}

}
