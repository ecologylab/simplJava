package ecologylab.services.logging;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.messages.OKResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.XmlTranslationException;

/**
 * Save the request logging messages from the client to the file. 
 * @author eunyee
 *
 */
public class LogRequestMessage extends RequestMessage
{	
	protected String			xmlString;
	
	/**
	 * Save the logging messages to the session log file
	 */
	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		Debug.println("cf services: received Logging Messages " );
		FileOutputStream outFile = (FileOutputStream) objectRegistry.lookupObject(LoggingDef.keyStringForFileObject);
		
		if( outFile != null )
		{
			try 
			{	
				String actionStr	=	getMessageString();
				outFile.write(actionStr.getBytes());
			} 
			catch (XmlTranslationException e) 
			{
				e.printStackTrace();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		else
			debug("ERROR: Can't log because FileOutputStream has not been created: " + outFile );
		
		debug("cf services: sending OK response");

    	return OKResponse.get();

	}
	
	/**
	 * Get message string to be saved in the session log file 
	 * This method is overrided by the sub-classes to handle specific messages for each request message.
	 * 
	 * @return
	 * @throws XmlTranslationException
	 */
	String getMessageString() throws XmlTranslationException 
	{
		return "";
	}

	/**
	 * Full XML for the message to be logged.
	 * 
	 * @return
	 */
	public String xmlString()
	{
		return xmlString;
	}

	public void setXmlString(String xmlString)
	{
		this.xmlString = xmlString;
	}

}