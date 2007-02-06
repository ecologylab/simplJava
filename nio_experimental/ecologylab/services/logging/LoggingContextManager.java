/**
 * 
 */
package ecologylab.services.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SocketChannel;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.services.nio.ContextManager;
import ecologylab.services.nio.NIOServerBackend;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;

/**
 * @author Zach
 *
 */
public class LoggingContextManager extends ContextManager
{
    FileOutputStream outputStream;

    String logFilesPath;
    
    boolean end;
    
    /**
     * @param token
     * @param server
     * @param socket
     * @param translationSpace
     * @param registry
     */
    public LoggingContextManager(Object token, NIOServerBackend server,
            SocketChannel socket, TranslationSpace translationSpace,
            ObjectRegistry registry, String logFilesPath)
    {
        super(token, server, socket, translationSpace, registry);
        
        this.logFilesPath = logFilesPath;
    }
    
    FileOutputStream getFile(String fileName)
    {
        if( outputStream == null )
        {
            try {
                File file   = new File(fileName);
                String dirPath  = file.getParent();
                File dir    = new File(dirPath);
                if (!dir.exists())
                    dir.mkdirs();
                //TODO what if (file.exists()) ???
                outputStream = new FileOutputStream(file, true);
                return outputStream;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return outputStream;
    }

    /**
     * @see ecologylab.services.nio.ContextManager#performService(ecologylab.services.messages.RequestMessage)
     */
    @Override protected ResponseMessage performService(RequestMessage requestMessage)
    {
        if( requestMessage instanceof SendPrologue )
        {
            String name = logFilesPath + ((SendPrologue)requestMessage).getFileName();
            getFile(name);
//          servicesServer.getObjectRegistry().registerObject(LoggingDef.keyStringForFileObject, getFile(name) );
        }
        else if(outputStream == null )
        {
            debug("Prologue has not been received OR File has not been created!! " + requestMessage);
        }
        
        if((outputStream != null) && ( requestMessage instanceof LogRequestMessage) )
        {
            ((LogRequestMessage)requestMessage).setOutputStream(outputStream);
            
            if(requestMessage instanceof SendEpilogue)
                end = true;
        }
        ResponseMessage responseMessage = super.performService(requestMessage);
        return responseMessage;
    }

    /**
     * @see ecologylab.services.nio.ContextManager#shutdown()
     */
    @Override public void shutdown()
    {
        if(!end)
        {
            (new SendEpilogue()).performService(registry);
        }
        
        super.shutdown();
    }

    /**
     * @see ecologylab.services.nio.ContextManager#translateXMLStringToRequestMessage(java.lang.String)
     */
    @Override protected RequestMessage translateXMLStringToRequestMessage(String messageString) throws XmlTranslationException
    {
        // translate with recursive descent!
        RequestMessage requestMessage = null;
        
        try
        {
            requestMessage = super.translateXMLStringToRequestMessage(messageString);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        
        if (requestMessage instanceof LogRequestMessage)
        {
            LogRequestMessage lrm = (LogRequestMessage) requestMessage;
            lrm.setXmlString(messageString);
        }
        else
            throw new XmlTranslationException("LoggingServer received non logging message: " + requestMessage);
        return requestMessage;
    }
}
