/**
 * 
 */
package ecologylab.services.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.SocketChannel;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.services.nio.ContextManager;
import ecologylab.services.nio.NIOServerBackend;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;

/**
 * Special version of ServerToClientConnection for the LoggingServer. Provides a
 * special impleentation of performService(), that open's an OutputStream as
 * necessary to the appropriate directory for logging, based on the headers in
 * the message, then logs the message to there with a minimum of translation.
 * 
 * @author andruid
 * @author eunyee
 * @author toupsz
 */
public class LoggingContextManager extends ContextManager
{

    FileOutputStream outputStream;

    NIOLoggingServer loggingServer;

    boolean          end = false;

    /**
     * @param token
     * @param loggingServer
     * @param server
     * @param socket
     * @param translationSpace
     * @param registry
     */
    public LoggingContextManager(Object token, NIOLoggingServer loggingServer,
            NIOServerBackend server, SocketChannel socket,
            TranslationSpace translationSpace, ObjectRegistry registry)
    {
        super(token, server, socket, translationSpace, registry);

        this.loggingServer = loggingServer;
    }

    @Override
    protected ResponseMessage performService(RequestMessage requestMessage)
    {
        if (requestMessage instanceof SendPrologue)
        {
            String name = loggingServer.getLogFilesPath()
                    + ((SendPrologue) requestMessage).getFileName();
            getFile(name);
            // servicesServer.getObjectRegistry().registerObject(LoggingDef.keyStringForFileObject,
            // getFile(name) );
        }
        else if (outputStream == null)
        {
            debug("Prologue has not been received OR File has not been created!! "
                    + requestMessage);
        }

        if ((outputStream != null)
                && (requestMessage instanceof LogRequestMessage))
        {
            ((LogRequestMessage) requestMessage).setOutputStream(outputStream);
        }

        ResponseMessage responseMessage = super.performService(requestMessage);

        if (requestMessage instanceof SendEpilogue)
        {
            end = true;
        }

        return responseMessage;
    }

    protected FileOutputStream getFile(String fileName)
    {
        if (outputStream == null)
        {
            try
            {
                File file = new File(fileName);
                String dirPath = file.getParent();
                File dir = new File(dirPath);
                if (!dir.exists())
                    dir.mkdirs();
                // TODO what if (file.exists()) ???
                outputStream = new FileOutputStream(file, true);
                return outputStream;
            }
            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return outputStream;
    }

    /**
     * Use the ServicesServer and its ObjectRegistry to do the translation. Put
     * a copy of the xmlString into the LoggingRequestMessage. Do not
     * doRecursiveDescent when translating the message, since we're logging, so
     * all we need is to copy the String to the appropriate log file.
     * 
     * @param messageString
     * @return
     * @throws XmlTranslationException
     */
    protected RequestMessage translateXMLStringToRequestMessage(
            String messageString) throws XmlTranslationException
    {
        RequestMessage requestMessage = (RequestMessage) ElementState
                .translateFromXMLString(messageString, translationSpace,
                        false);

        if (requestMessage instanceof LogRequestMessage)
        {
            LogRequestMessage lrm = (LogRequestMessage) requestMessage;
            lrm.setXmlString(messageString);
        }
        else
            throw new XmlTranslationException(
                    "LoggingServer received non logging message: "
                            + requestMessage);
        return requestMessage;
    }

    @Override
    public void shutdown()
    {
        if (!end)
        {
            (new SendEpilogue()).performService(registry);
        }
        
        super.shutdown();
    }
}
