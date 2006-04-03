package ecologylab.services.messages;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.LoggingDef;
import ecologylab.xml.XmlTranslationException;

/**
 * Save the request logging messages from the client to the file. 
 * @author eunyee
 *
 */
public class LogRequestMessage extends RequestMessage
{
	FileOutputStream outFile;
	
	/**
	 * Save the logging messages to the session log file
	 */
	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		Debug.println("cf services: received Logging Messages " );
		
		if( getOutFile() != null )
		{
			try {
				
				String actionStr	=	getMessageString();
				System.out.println("cf services: Got It " + 
						"\n" + actionStr );
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
		
		System.out.println("cf services: sending postive response");

    	return new ResponseMessage(OK);

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
	 * Create a session logging file in the specific server space
	 * @return
	 */
	FileOutputStream getOutFile()
	{
    	if( outFile == null )
    	{
			try {
				outFile = new FileOutputStream(LoggingDef.sessionLogFile,true);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
		return outFile;
	}
}