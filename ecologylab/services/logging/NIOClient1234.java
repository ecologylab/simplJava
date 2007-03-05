/*
 * Created on Mar 2, 2007
 */
package ecologylab.services.logging;

import java.io.IOException;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.nio.NIOClient;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;

/**
 * @author Zach Toups (toupsz@gmail.com)
 */
public class NIOClient1234 extends NIOClient
{

    /**
     * @param server
     * @param port
     * @param messageSpace
     * @param objectRegistry
     */
    public NIOClient1234(String server, int port,
            TranslationSpace messageSpace, ObjectRegistry objectRegistry)
    {
        super(server, port, messageSpace, objectRegistry);
    }

    /**
     * @see ecologylab.services.nio.NIOClient#nonBlockingSendMessage(ecologylab.services.messages.RequestMessage)
     */
    @Override public long nonBlockingSendMessage(RequestMessage request)
            throws IOException
    {
        debug("--------------------------sending.");
        try
        {
            debug("req: "+request.translateToXML(false));
        }
        catch (XmlTranslationException e)
        {
            e.printStackTrace();
        }

        long retVal = super.nonBlockingSendMessage(request);

        if (request instanceof LogRequestMessage)
        {
            if (Math.random() > .5)
            {
                debug("--------------------------------------------------------------randomly disconnecting now.");
                this.disconnect(false);

                debug("--------------------------------------------------------------reconnecting now.");
                this.reconnect();
            }
        }

        return retVal;
    }

}
