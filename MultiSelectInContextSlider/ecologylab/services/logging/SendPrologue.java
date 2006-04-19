package ecologylab.services.logging;

public final class SendPrologue extends LogRequestMessage
{
	public Prologue		prologue;
	
	public SendPrologue(Prologue prologue)
	{
		super();
		this.prologue	= prologue;
	}

	public SendPrologue()
	{
		super();
	}

}
