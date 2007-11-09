package ecologylab.services.logging;

import ecologylab.xml.xml_inherit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.generic.Debug;
import ecologylab.services.messages.OkResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.ElementState.xml_leaf;

/**
 * Save the request logging messages from the client to the file. 
 * @author eunyee
 *
 */
@xml_inherit
abstract public class LogRequestMessage extends RequestMessage
{	
    @xml_leaf(CDATA) protected StringBuilder	bufferToLog;

	Writer 										outputStreamWriter;
	
	public LogRequestMessage()
	{
		
	}
	public LogRequestMessage(int bufferSize)
	{
		bufferToLog		= new StringBuilder(bufferSize);
	}
	/**
	 * Save the logging messages to the session log file
	 */
	@Override public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		debug("services: received Logging Messages " );
/*
		try
        {
            debug(this.translateToXML());
        }
        catch (XmlTranslationException e1)
        {
            e1.printStackTrace();
        }
	*/	
		if (outputStreamWriter != null )
		{
			try 
			{	
				final StringBuilder bufferToLog = bufferToLog();
                
                debug(bufferToLog);
                
				outputStreamWriter.append(bufferToLog);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		else
			error("Can't log because there is no outputStreamWriter.");
		
		debug("services: sending OK response");
		
    	return OkResponse.get();

	}
	
	public void setWriter(OutputStreamWriter outputStreamWriter)
	{
		this.outputStreamWriter = outputStreamWriter;
	}

    public void appendToBuffer(StringBuilder opsBuffer)
    {
    	bufferToLog.append(opsBuffer);
    }
    public void setBuffer(StringBuilder bufferToLog)
    {
    	this.bufferToLog	= bufferToLog;
    }
	/**
	 * Stuff to write to the log file based on the contents of this message.
	 * 
	 * @return	ops, the buffer in which MixedInitiativeOps have been passed here.
	 */
	protected StringBuilder bufferToLog()
	{
		return bufferToLog;
	}    
    /**
     * Clear the buffer for re-use, presumably after sending it.
     */
    public void clear()
    {
//    	bufferToLog.setLength(0);
    	bufferToLog	= null;
    }

}