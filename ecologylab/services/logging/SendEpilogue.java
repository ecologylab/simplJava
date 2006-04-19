package ecologylab.services.logging;

public final class SendEpilogue extends LogRequestMessage
{
	public Epilogue		epilogue;
	
	public SendEpilogue(Epilogue epilogue)
	{
		super();
		this.epilogue	= epilogue;
	}

	public SendEpilogue()
	{
		super();
	}

}
