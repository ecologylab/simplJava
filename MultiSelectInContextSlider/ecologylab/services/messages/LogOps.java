package ecologylab.services.messages;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.LoggingDef;
import ecologylab.services.MixedInitiativeOp;
import ecologylab.xml.ArrayListState;
import ecologylab.xml.ElementState;
import ecologylab.xml.XmlTranslationException;

/**
 * Send an intermediate sequence of ops to the logging server.
 * 
 * 1) Keep mixed initiative loggin data Set
 * 2) Handle recieved logging messages from client 
 * 
 * @author eunyee
 *
 */
public class LogOps extends RequestMessage
{

	public ArrayListState	mixedInitiativeOpSet = new ArrayListState();
	
	public void addNestedElement(ElementState elementState)
	{
		if (elementState instanceof MixedInitiativeOp)
			try {
				mixedInitiativeOpSet.addNestedElement(elementState);
			} catch (XmlTranslationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void clearSet()
	{
		mixedInitiativeOpSet.clear();
	}

	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		Debug.println("cf services: received LOG " );
		
//	    MixedInitiativeOpSet opSet = (MixedInitiativeOpSet) requestMessage;
	    if (mixedInitiativeOpSet != null)
	    {
	    	FileOutputStream outFile = null;
	    	if( outFile == null )
	    	{
				try {
					outFile = new FileOutputStream(LoggingDef.sessionLogFile,true);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    	}
	    	
			if( outFile != null )
			{
				try {
					
					String actionStr	=	(String)this.translateToXML(false) + "\n";
					outFile.write(actionStr.getBytes());
					System.out.println("cf services: sending postive response"); 
					
				} catch (XmlTranslationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

	    	return new ResponseMessage(OK);
	    }
	    else
	    {
	    	System.out.println("cf services: PREFS FAILED! sending NEGATIVE response");
	    	return new ResponseMessage(BADTransmission);
	    }
	}
	
}