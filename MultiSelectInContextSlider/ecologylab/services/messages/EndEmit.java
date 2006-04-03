package ecologylab.services.messages;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.Logging;
import ecologylab.services.LoggingDef;

/**
 * 
 * Request message about letting the server to write closing block for xml logs 
 * and close the log file. 
 * 
 * @author eunyee
 */
public class EndEmit extends RequestMessage
{

	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		Debug.println("cf services: received EndEmit LOG " );
		
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
				
				String actionStr	=	Logging.LOG_CLOSING;
				outFile.write(actionStr.getBytes());
				System.out.println("cf services: sending postive response" + 
						"\n" + actionStr );

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseMessage(BADTransmission);
			}
		}

    	return new ResponseMessage(OK);
	}
	
}