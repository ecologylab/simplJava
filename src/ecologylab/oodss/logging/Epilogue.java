package ecologylab.oodss.logging;

import ecologylab.serialization.ElementState;

/**
 * Request message about letting the server to write closing block for xml logs and close the log file.
 * 
 * Subclasses may provide additional information that is written to the end of a log.
 * 
 * @author eunyee
 */
public class Epilogue extends ElementState
{
	/*
	 * Constructor for automatic translation;
	 */
	public Epilogue()
	{
		super();
	}
}
