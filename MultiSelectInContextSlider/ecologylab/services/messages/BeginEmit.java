package ecologylab.services.messages;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.Logging;
import ecologylab.services.LoggingDef;


/**
 * request message for the Logging server to open new log file
 * and write the header.
 * 
 * @author eunyee
 */
public class BeginEmit extends RequestMessage
{

	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		Debug.println("cf services: received BeginEmit LOG " );
		
    	FileOutputStream outFile = null;
    	if( outFile == null )
    	{
			try {
				outFile = new FileOutputStream(LoggingDef.sessionLogFile,true);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return new ResponseMessage(BADTransmission);
			}
    	}
    	
		if( outFile != null )
		{
			try {
				
				String actionStr	=	Logging.LOG_HEADER;
				System.out.println("cf services: Got It " + 
						"\n" + actionStr );
				outFile.write(actionStr.getBytes());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseMessage(BADTransmission);
			}
		}
		
		System.out.println("cf services: sending postive response");

    	return new ResponseMessage(OK);

	}
	
}