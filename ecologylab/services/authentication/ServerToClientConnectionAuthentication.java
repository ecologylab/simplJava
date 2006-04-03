/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.io.IOException;
import java.net.Socket;

import ecologylab.services.ServerToClientConnection;
import ecologylab.services.ServicesServer;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.XmlTranslationException;

public class ServerToClientConnectionAuthentication extends
        ServerToClientConnection implements AuthenticationMessages {

    private boolean loggedIn = false;

    private RequestMessage requestMessage;

    private ResponseMessage responseMessage;

    public ServerToClientConnectionAuthentication(Socket incomingSocket,
            ServicesServer servicesServer) throws IOException {
        super(incomingSocket, servicesServer);
    }

    /**
     * Service the client connection.
     */
    public void run() {
        while (running) {
            // debug("waiting for packet");
            // get the packet message
            String messageString = "";
            try {
                // TODO -- change to nio
                if (inputStreamReader.ready()) {
                    messageString = inputStreamReader.readLine();

                //    debugA("got raw message: " + messageString);

                    if (messageString != null) {
                        requestMessage = servicesServer
                                .translateXMLStringToRequestMessage(messageString);
                        System.out.println(requestMessage.translateToXML(false));
                    } else {
                        requestMessage = null;
                    }

                    if (requestMessage == null) {
                        debug("ERROR: translation failed: " + messageString);
                    } 
            
                    else {
                        // if not logged in yet, make sure they log in first
                        if (!loggedIn)
                        {
                            /* ZACH --  please use the isOK() method to test here
                             * also, your String test here will fail if response == null
                             * the correct way to write something like this is
                             * if (LOGIN_SUCCESSFUL.equals(responseMessage.getResponse()))
                             * 
                             * i dont understand, anyway, why this code is here instead of inside performService()
                             * 
                             * further, any subclass that duplicates the networking code is... wrong.
                             * its bad structure.
                             * if necessary, we can add hooks in the base class, with empty default implementations.
                             * so you can customize as necessary.
                             * 
                             * please follow our indentation conventions for open and close brace!
                             * (info on the insiders > software development > programming style page, 
                             * if you need it)

                            if (requestMessage instanceof ecologylab.services.messages.Login) 
                            {
                                // perform the service being requested, if
                                // they're logged in
                                responseMessage = servicesServer
                                        .performService(requestMessage);
                              if (responseMessage.getResponse().equals(LOGIN_SUCCESSFUL)) {
                                    loggedIn = true;
                                }
                            } else 
                            { // otherwise we consider it bad!
                                responseMessage.setResponse(REQUEST_FAILED_NOT_AUTHENTICATED);
                            }
                            */
                                
                        } else {
                            // perform the service being requested, if they're
                            // logged in
                            // we check to see if they want to log out; if they
                            // do, we terminate
                            if (requestMessage instanceof ecologylab.services.authentication.Logout) {
                                responseMessage = servicesServer
                                        .performService(requestMessage);

                                // shut down this thread, if the logout was
                                // successful
                                if (responseMessage.getResponse().equals(OK)) {
                                    loggedIn = false;
                                    running = false;
                                }
                            } else {
                                responseMessage = servicesServer
                                        .performService(requestMessage);

                            }
                        }

                        // send the response
                        outputStreamWriter.println(responseMessage
                                .translateToXML(false));
                        outputStreamWriter.flush();
                    }
                }
            } catch (java.net.SocketException e) {
                // this seems to mean the connection went away
                if (outputStreamWriter != null) // dont need the message if
                    // we're already shutting down
                    debug("STOPPING:  It seems we are no longer connected to the client.");
                break;
            } catch (IOException e) {
                // TODO count streak of errors and break;
                debug("IO ERROR: " + e.getMessage());
                e.printStackTrace();
            } catch (XmlTranslationException e) {
                // report error on XML passed through the socket
                debug("Bogus Message ERROR: " + messageString);
                e.printStackTrace();
            }
        }

        synchronized (this) {
            if (!running)
                stop();
        }
    }
}
