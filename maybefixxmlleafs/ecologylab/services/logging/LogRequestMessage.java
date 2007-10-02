package ecologylab.services.logging;

import ecologylab.xml.xml_inherit;
import java.io.FileOutputStream;
import java.io.IOException;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.generic.Debug;
import ecologylab.services.messages.OkResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.XmlTranslationException;

/**
 * Save the request logging messages from the client to the file. 
 * @author eunyee
 *
 */
@xml_inherit
public class LogRequestMessage extends RequestMessage
{	
	protected String			xmlString;
	FileOutputStream outFile;
	
	/**
	 * Save the logging messages to the session log file
	 */
	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		Debug.println("services: received Logging Messages " );
        try
        {
            Debug.println("contents: "+this.getMessageString());
        }
        catch (XmlTranslationException e1)
        {
            e1.printStackTrace();
        }
//		FileOutputStream outFile = (FileOutputStream) objectRegistry.lookupObject(LoggingDef.keyStringForFileObject);
		
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
		
		debug("services: sending OK response");
		
    	return OkResponse.get();

	}
	
	/**
	 * Get message string to be saved in the session log file 
	 * This method is overrided by the sub-classes to handle specific messages for each request message.
	 * 
	 * @return
	 * @throws XmlTranslationException
	 */
/*	String getMessageString() throws XmlTranslationException 
	{
		String xmlString	= xmlString();
		
		return (xmlString != null) ? xmlString : this.translateToXML(false);
	}
*/
	/**
	 * The string that the LoggingServer will write.
	 * Uses substring() to peel the inner message out of the LogRequestApplication.
	 * 
	 * Eliminates the outer XML element, such as <log_request_message> or <log_ops>.
	 */
	protected String getMessageString() throws XmlTranslationException
	{
//		TagMapEntry	tagMapEntry	= this.getTagMapEntry(getClass(), false);
		String xmlString	= xmlString();
	
		// if not on server, do normal translate to XML
		if (xmlString == null)
			xmlString		= this.translateToXML(false);

		// if on server, peel message(s) out of the XML we received, without parsing it!
		
//		int start			= xmlString.indexOf(tagMapEntry.openTag) + tagMapEntry.openTag.length();
		// start of the real stuff is the end of the first tag -- whatever it is
		int start			= xmlString.indexOf('>') + 1;
		// end of the real stuff is the start of the close tag... 
		// which also should be the start of the last tag -- whatever it is
		int end				= xmlString.lastIndexOf('<');
//		int end				= xmlString.indexOf(tagMapEntry.closeTag);
		if( (start==0) || (end==-1) )
		{
			debug("RECEIVE MESSAGE : " + xmlString);
			return "\n";
		}
		return (String) xmlString.substring(start, end) + "\n";
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
	
	public void setOutputStream(FileOutputStream outputStream)
	{
		this.outFile = outputStream;
	}

}