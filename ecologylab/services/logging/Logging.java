package ecologylab.services.logging;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.Vector;

import ecologylab.appframework.Memory;
import ecologylab.appframework.ObjectRegistry;
import ecologylab.appframework.PropertiesAndDirectories;
import ecologylab.appframework.types.Preference;
import ecologylab.generic.Generic;
import ecologylab.io.Files;
import ecologylab.services.ServicesHostsAndPorts;
import ecologylab.services.nio.NIOClient;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTools;
import ecologylab.xml.XmlTranslationException;
import ecologylab.xml.types.element.ArrayListState;

/**
 * Provides a framework for interaction logging.
 * Uses ecologylab.xml to serialize user and agent actions,
 * and write them either to a file on the user's local machine, or, across the
 * network, to the LoggingServer.
 * 
 * @author andruid
 */
public class Logging extends ElementState implements Runnable,
        ServicesHostsAndPorts
{
    /**
     * This field is used for reading a log in from a file, but not for writing
     * one, because we dont the write the log file all at once, and so can't
     * automatically translate the start tag and end tag for this element.
     */
    @xml_nested protected ArrayListState       opSequence;
    

    Thread                      thread;
    
    TranslationSpace                   nameSpace;

    /**
     * Does all the work of logging, if there is any work to be done.
     * If this is null, then there is no logging;
     * conversely, if there is no longging, this is null.
     */
    LogWriter                   logWriter;

    /**
     * Object for sending a batch of ops to the LoggingServer.
     */
    LogOps                      opSet                    = new LogOps();

    /**
     * Queue of action opperations that have been sent to us for logging. Our
     * Runnable Thread will actually to the file writes, at a convenient time,
     * at a low priority.
     */
    // Vector opsToWrite = new Vector();
    /**
     * This is the Vector for the operations that are being queued up before
     * they can go to outgoingOps.
     */
    Vector<String>                      incomingOpsQueue         = new Vector<String>();

    /**
     * This is the Vector for the operations that are in the process of being
     * written out.
     */
    Vector<String>                      outgoingOpsQueue         = new Vector<String>();

    /**
     * Stores the pointer to outgoingOpsQueue for swapQueues.
     */
    Vector<String>                      tempQueue                = null;

    /**
     * Iterator for writing out ops.
     */
    Iterator                    outgoingOpsQueueIterator = null;

    boolean                     finished;

    static final int            THREAD_PRIORITY          = 1;

    static final int            SLEEP_TIME               = 15000; // 15 seconds

    static final long           sessionStartTime = System.currentTimeMillis();

    long                        lastGcTime;

    static final long           KICK_GC_INTERVAL = 300000; // 5 minutes
    

    private static final String SESSION_LOG_START        = "\n<session_log>\n ";

    static final String         OP_SEQUENCE_START        = "\n\n<op_sequence>\n\n";

    static final String         OP_SEQUENCE_END          = "\n</op_sequence>\n";

    /**
     * Logging closing message string written to the logging file at the end
     */
    public static final String  LOG_CLOSING              = "\n</op_sequence></session_log>\n\n";

    /**
     * Logging Header message string written to the logging file in the begining
     */
    static final String         BEGIN_EMIT               = XmlTools.xmlHeader()
                                                                 + SESSION_LOG_START;

    public static final int      NO_LOGGING               	= 0;

    public static final int      LOG_TO_FILE              	= 1;

    public static final int      LOG_TO_SERVICES_SERVER  	= 2;
    
    public static final int      LOG_TO_MEMORY_MAPPED_FILE	= 3;

    static final int             MAX_OPS_BEFORE_WRITE     	= 10;
    
    public static final String	 LOGGING_HOST_PARAM			= "logging_host";
    
    public static final String	 LOGGING_PORT_PARAM  		= "logging_port";
    
    public static final String	 LOGGING_MODE_PARAM 		= "logging_mode";
    
    final int                    maxOpsBeforeWrite;

    public Logging(TranslationSpace nameSpace)
    {
        this(nameSpace, null);
    }
    public Logging(TranslationSpace nameSpace, String logFileName)
    {
        this(nameSpace, logFileName, MAX_OPS_BEFORE_WRITE);
    }
    public Logging(TranslationSpace nameSpace, String logFileName, int maxOpsBeforeWrite)
    {
        super();
        this.maxOpsBeforeWrite   = maxOpsBeforeWrite;
        finished            = false;
        this.nameSpace      = nameSpace;
        int logMode         = Preference.lookupInt(LOGGING_MODE_PARAM, NO_LOGGING);
        switch (logMode)
        {
            case NO_LOGGING:
                debug("logging disabled; NO_LOGGING specified");
                break;
            case LOG_TO_FILE:
                if (logFileName == null)
                {
                    debug("Logging disabled; no file name specified");
                }
                else
                {
                    File logDir = PropertiesAndDirectories.logDir();
                    if (logDir == null)
                    {
                        debug("Can't write to logDir=" + logDir);
                    } else
                    {
                        debug("Logging to file: " + logDir + logFileName);

                        File logFile = new File(logDir, logFileName);
                        BufferedWriter bufferedWriter  = Files.openWriter(logFile);
                        
                        if (bufferedWriter != null)
                        {
                            try
                            {
                                logWriter   = new FileLogWriter(logFile, bufferedWriter);
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            debug("ERROR: cant open writer to " + logFile);
                        }
                    }
                }
                break;
            case LOG_TO_MEMORY_MAPPED_FILE:
                if (logFileName == null)
                {
                    debug("Logging disabled; no file name specified");
                }
                else
                {
                    File logDir = PropertiesAndDirectories.logDir();
                    
                    if (logDir == null)
                    {
                        debug("Can't write to logDir=" + logDir);
                    } else
                    {
                        debug("Logging to file: " + logDir + logFileName);

                        File logFile = new File(logDir, logFileName);

                            try
                            {
                                logWriter   = new MemoryMappedFileLogWriter(logFile);
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                    }
                }
                break;
            case LOG_TO_SERVICES_SERVER:
                /**
                 * Create the logging client which communicates with the logging
                 * server
                 */
            	String loggingHost = Preference.lookupString(LOGGING_HOST_PARAM);
            	if (loggingHost == null)
            		loggingHost = LOGGING_HOST;
            	int loggingPort	= Preference.lookupInt(LOGGING_PORT_PARAM, ServicesHostsAndPorts.LOGGING_PORT);

                NIOClient loggingClient = new NIOClient(loggingHost, loggingPort, nameSpace, new ObjectRegistry());
                
                // CONNECT TO SERVER
                if (loggingClient.connect())
                {
                    try
                    {
                        logWriter       = new NetworkLogWriter(loggingClient);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    
                    debug("logging to server: "+loggingHost+":"+loggingPort);
                }
                else
                {
                    loggingClient   = null;
                    debug("Logging disabled: cannot reach server");
                }
                
                break;

            default:
                break;
        }
    }
    
    /**
     * Constructor for automatic translation from XML
     * 
     */
    public Logging()
    {
        this.maxOpsBeforeWrite  = MAX_OPS_BEFORE_WRITE;
    }

    /**
     * If
     * 
     */
    private void swapQueues()
    {
        synchronized (this.outgoingOpsQueue)
        {
            synchronized (this.incomingOpsQueue)
            {
                if (outgoingOpsQueue.isEmpty())
                {
                    tempQueue = outgoingOpsQueue;
                    outgoingOpsQueue = incomingOpsQueue;
                    incomingOpsQueue = tempQueue;
                }
            }
        }
    }

    /**
     * Translates op to XML then logs it.
     * @param op - the operation to be logged.
     */
    public void logAction(MixedInitiativeOp op)
    {
        if (logWriter != null)
        {
            try
            {
                logAction(op.translateToXML(false));
            }
            catch (XmlTranslationException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void logAction(String translatedXML)
    {
        if (logWriter != null)
        {
            synchronized (incomingOpsQueue)
            {
                incomingOpsQueue.add(translatedXML);
            }

            if ((thread != null) && (incomingOpsQueue.size() > maxOpsBeforeWrite))
            {
                debugA("interrupting thread to do i/o now");
                thread.interrupt(); // end sleep in that thread prematurely to
                                    // do i/o
            }
        }
    }

    /**
     * Write the start of the log header out to the log file OR, send the
     * begining logging file message so that logging server write the start of
     * the log header. <p/> Then start the looping thread that periodically
     * wakes up and performs log i/o.
     */
    public void start()
    {
        if ((logWriter != null) && (thread == null))
        {
        	SendPrologue sendPrologue = null;
        	sendPrologue = new SendPrologue(this, getPrologue());
        	
            logWriter.writePrologue(sendPrologue);

            thread = new Thread(this);
            thread.setPriority(THREAD_PRIORITY);
            thread.start();
        }
    }

    public synchronized void stop()
    {
        if (thread != null)
        {
            finished = true;
            thread = null;
            if (logWriter != null)
            {
                writeQueuedActions();
                
                logWriter.writeEpilogueAndClose(new SendEpilogue(this, getEpilogue()));
                logWriter	= null;
            }
        }
    }

    /**
     * Logging to a file is delayed to the actions of this thread, because
     * otherwise, it can mess up priorities in the system, because events get
     * logged in the highest priority thread.
     * <p/>
     * This MUST be the only thread that ever calls writeQueuedActions().
     */
    public void run()
    {
        lastGcTime = System.currentTimeMillis();
        while (!finished)
        {
            Thread.interrupted();
            Generic.sleep(SLEEP_TIME, false);
            writeQueuedActions();

            long now = System.currentTimeMillis();
            long deltaT = now - lastGcTime;

            if (deltaT >= KICK_GC_INTERVAL)
            {
                debug("kick GC");
                lastGcTime = now;
                Memory.reclaim();
            }
        }
    }

    /**
     * Use the LogWriter, if there is one, to output queued actions to the log.
     * <p/>
     * NB: This method is SINGLE Threaded! It is not thread safe.
     * It must only be called from the run() method.
     */
    protected void writeQueuedActions()
    {
        if (logWriter == null)
            return;
        
        Vector<String> ourQueueToWrite = incomingOpsQueue;
        synchronized (ourQueueToWrite)
        {
            int size = ourQueueToWrite.size();
            if (size == 0)
                return;
            swapQueues();
            // what was incomingOpsQueue is now outgoing!
            String firstEntry = (String) ourQueueToWrite.get(0);
            //println("Logging: writeQueuedActions() start of output loop.");
            if (size == 1)
            {
               logWriter.consumeOp(firstEntry);
            }
            else
            {
                // allocate storage with a reasonable size estimate
            	logWriter.consumeOp(firstEntry);
                for (int i = 1; i < size; i++)
                {
                    String thatEntry = (String) ourQueueToWrite.get(i);
                    logWriter.consumeOp(thatEntry);
                }
            }
            logWriter.finishConsumingQueue();
            //println("Logging: writeQueuedActions() after output loop.");
            ourQueueToWrite.clear();
        }
       
    }
    /**
     * A message at the beginnging of the log. This method may be overridden to
     * return a subclass of Prologue, by subclasses of this, that wish to emit
     * application specific information at the start of a log.
     * 
     * @return
     */
    protected Prologue getPrologue()
    {
        return new Prologue();
    }

    /**
     * A message at the end of the log. This method may be overridden to return
     * a subclass of Epilogue, by subclasses of this, that wish to emit
     * application specific information at the end of a log.
     * 
     * @return
     */
    protected Epilogue getEpilogue()
    {
        Epilogue epilogue = new Epilogue();
        return epilogue;
    }
    
    public TranslationSpace getNameSpace()
    {
    	return nameSpace;
    }

    // TODO this looks like dead code.
    public static ElementState loadLogXML(String fileName, TranslationSpace nameSpace)
    {

        //fixBrokenLog(fileName);

        // build the state object from the XML
        ElementState stateObject = null;
        try
        {
            stateObject = (ElementState) translateFromXML(fileName, nameSpace);
        } catch (XmlTranslationException e)
        {
            e.printStackTrace();
        }
        return stateObject;
    }

    public static void fixBrokenLog(String fileName)
    {
        String lastLine = null;
        String lastSecondLine = null;
        String currentLine = null;
        BufferedReader reader = Files.openReader(fileName);
        if (reader == null)
        {
            println("CANT OPEN LOGGING FILE: " + fileName);
            return;
        }
        currentLine = Files.readLine(reader);
        while (currentLine != null)
        {
            lastLine = currentLine;
            currentLine = Files.readLine(reader);
            if (currentLine != null)
            {
                lastSecondLine = lastLine;
                lastLine = currentLine;
            }
        }
        Files.closeReader(reader);

        // println("LatSL is " + lastSecondLine);
        // println("LatL is " + lastLine);

        if ((lastLine == null) || (!lastLine.equals("</collage_log>")))
        {
            BufferedWriter writer = Files.openWriter(fileName, true);
            if ((lastSecondLine == null)
                    || (!lastSecondLine.equals("</collage_op_sequence>")))
            {
                // debug(3,"write last second line");
                Files.writeLine(writer, "/n</collage_op_sequence>");
            }

            // debug(0,"write last line");
            Files.writeLine(writer, "/n</collage_log>");

            Files.closeWriter(writer);
        }
    }

    /**
     * Return our session start timestamp.
     * 
     * @return
     */
    public static final long sessionStartTime()
    {
        return sessionStartTime;
    }
    
    /**
     * Objects that process queuedActions for writing, either to a local file,
     * or, using the network, to the LoggingServer.
     * 
     * @author andruid
     * @author toupsz
     */
    protected abstract class LogWriter
    {
        LogWriter()
        throws IOException
        {
            
        }
        /**
         * Write the prologue -- special stuff at the beginning of a session.
         * @param prologue
         */
        abstract void writePrologue(SendPrologue sendPrologue);
        /**
         * Process a single op -- take steps to send to destination, or actually send it.
         * @param op
         */
        abstract void consumeOp(String op);
        /**
         * Optional: commit changes; complete processing of the queue.
         *
         */
        abstract void finishConsumingQueue();
        /**
         * Close resources associated with this at the end of a session.
         * @param sendEpilogue TODO
         */
        abstract void writeEpilogueAndClose(SendEpilogue sendEpilogue);
        
    }
    
    /**
     * LogWriter that uses a memory-mapped local file for logging.
     * 
     * @author andruid
     * @author toupsz
     */
    protected class MemoryMappedFileLogWriter extends LogWriter
    {
        // BufferedWriter bufferedWriter;
        MappedByteBuffer       buffy   = null;

        FileChannel            channel = null;

        private CharsetEncoder encoder = Charset.forName("ASCII").newEncoder();
        
        private File logFile;
        
        /**
         * The base size for the log file, and the amount it will be incremented
         * whenever its buffer overflows.
         */
        static final int            LOG_FILE_INCREMENT       = 1024*512;

        private int                         endOfMappedBytes                  = LOG_FILE_INCREMENT;

        MemoryMappedFileLogWriter(File logFile/* , BufferedWriter bufferedWriter */)
                throws IOException
        {
            this.logFile = logFile;
            
            channel = new RandomAccessFile(logFile, "rw").getChannel();

            // allocate LOG_FILE_INCREMENT for the file size; this will be
            // incremented as necessary
            buffy = channel.map(MapMode.READ_WRITE, 0, LOG_FILE_INCREMENT);

            // if (bufferedWriter == null)
            // throw new IOException("Can't log to File with null
            // buffereredWriter.");
            // this.bufferedWriter = bufferedWriter;
            Logging.this.debugA("Logging to " + logFile + " " + buffy);
        }

        /**
         * Write the prologue -- special stuff at the beginning of a session.
         * 
         * @param prologue
         */
        void writePrologue(SendPrologue sendPrologue)
        {
            try
            {
                putInBuffer(encoder.encode(CharBuffer.wrap(sendPrologue
                        .getMessageString())));
            }
            catch (CharacterCodingException e)
            {
                e.printStackTrace();
            }
            // Files.writeLine(bufferedWriter, sendPrologue.getMessageString());
        }

        void consumeOp(String op)
        {
            // Files.writeLine(bufferedWriter, op);
            try
            {
                putInBuffer(encoder.encode(CharBuffer.wrap(op + "\n")));
            }
            catch (CharacterCodingException e)
            {
                e.printStackTrace();
            }
        }

        void finishConsumingQueue()
        {

        }

        /**
         * Close the local file.
         */
        void writeEpilogueAndClose(SendEpilogue sendEpilogue)
        {
            System.out.println("writing epilogue and closing");

            try
            {
                putInBuffer(encoder.encode(CharBuffer.wrap(sendEpilogue
                        .getMessageString())));

                buffy.force();

                // shrink the file to the appropriate size
                int fileSize = 0;
                
                fileSize = endOfMappedBytes - LOG_FILE_INCREMENT + buffy.position();
                
                buffy = null;
                
                channel.close();
                
                channel = null;
                
                // do garbage collection to ensure that the file is no longer mapped
                System.runFinalization();
                System.gc();
                
                debug("final log file size is "+fileSize+" bytes.");
                
                debug("truncating log.");
                
                boolean truncated = false;
                int numTries = 0;
                
                while (!truncated)
                {
                    try
                    {
                        new RandomAccessFile(logFile, "rw").getChannel().truncate(fileSize);
                        
                        truncated = true;
                    }
                    catch (IOException e)
                    {
                        debug("truncation failed because the file is still mapped, attempting to garbage collect...AGAIN.");

                        //do garbage collection to ensure that the file is no longer mapped
                        System.runFinalization();
                        System.gc();
                        
                        numTries ++;
                    }
                    
                    if (numTries == 10)
                    {
                        debug("Tried to unmap file 10 times; failing now.");
                        
                        truncated = true;
                    }
                }
            }
            catch (CharacterCodingException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            debug("log file closed.");
        }

        private void putInBuffer(ByteBuffer incoming)
        {

            try
            {
                int remaining = buffy.remaining();

                if (remaining < incoming.remaining())
                { // we want to write more than will fit; so we just write
                    // what we can first...
                    System.out.println("not enough space in the buffer: "
                            + remaining + " remaining, " + incoming.remaining()+" needed.");
                    System.out.println("last range file range: "
                            + (endOfMappedBytes - LOG_FILE_INCREMENT) + "-" + endOfMappedBytes);
                    System.out.println("new range will be: " + (endOfMappedBytes) + "-"
                            + (endOfMappedBytes + LOG_FILE_INCREMENT));
                    debug("creating temp byte array of size: " + remaining);

                    byte[] temp = new byte[remaining];
                    incoming.get(temp, incoming.position(), remaining);

                    buffy.put(temp);

                    // ensure that the buffer has been written out
                    buffy.force();

                    // then shift buffy to map to the next segment of the file
                    buffy = channel.map(MapMode.READ_WRITE, endOfMappedBytes,
                            LOG_FILE_INCREMENT);
                    endOfMappedBytes += LOG_FILE_INCREMENT;

                    // recursively call on the remainder of incoming
                    putInBuffer(incoming);
                }
                else
                {
                    if (show(5))
                        debug("writing to buffer: " + remaining
                                + "bytes remaining before resize");

                    buffy.put(incoming);
                }
            }
            catch (NullPointerException e)
            {
                debug("null pointer; data to be written:");
                try
                {
                    debug(Charset.forName("ASCII").newDecoder()
                            .decode(incoming).toString());
                }
                catch (CharacterCodingException e1)
                {
                    e1.printStackTrace();
                }

                e.printStackTrace();
            }
            catch (CharacterCodingException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * LogWriter that uses a local file for logging.
     * 
     * @author andruid
     * @author toupsz
     */
    protected class FileLogWriter
    extends LogWriter
    {
        BufferedWriter  bufferedWriter;
        
        FileLogWriter(File logFile, BufferedWriter bufferedWriter)
        throws IOException
        {
            if (bufferedWriter == null)
                throw new IOException("Can't log to File with null buffereredWriter.");
            this.bufferedWriter = bufferedWriter;
            Logging.this.debugA("Logging to " + logFile + " " + bufferedWriter);
        }

        /**
         * Write the prologue -- special stuff at the beginning of a session.
         * @param prologue
         */
        void writePrologue(SendPrologue sendPrologue)
        {
            Files.writeLine(bufferedWriter, sendPrologue.getMessageString());
        }
        void consumeOp(String op)
        {
        	Files.writeLine(bufferedWriter, op);
        }
        void finishConsumingQueue()
        {
            
        }
        /**
         * Close the local file.
         */
        void writeEpilogueAndClose(SendEpilogue sendEpilogue)
        {
            Files.writeLine(bufferedWriter, sendEpilogue.getMessageString());
            try
            {
                bufferedWriter.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            bufferedWriter  = null;
        }
    }
    /**
     * LogWriter that connects to the ServicesServer over the network for logging.
     * 
     * @author andruid
     * @author toupsz
     */
    protected class NetworkLogWriter
    extends LogWriter
    {
        NIOClient  loggingClient;
        
        NetworkLogWriter(NIOClient loggingClient)
        throws IOException
        {
            if (loggingClient == null)
                throw new IOException("Can't log to Network with null loggingClient.");
            this.loggingClient = loggingClient;
            Logging.this.debug("Logging to service via connection: " + loggingClient);
        }

        /**
         * Write the prologue -- special stuff at the beginning of a session.
         * @param prologue
         */
        void writePrologue(SendPrologue sendPrologue)
        {
            debug("logging client writing prologue");
            
            int uid = Preference.lookupInt("uid", 0);
            Logging.this.debug("Logging: Sending Prologue userID:" + uid);
            sendPrologue.prologue.setUserID(uid);
            try
            {
                loggingClient.nonBlockingSendMessage(sendPrologue);
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }            
        }
        void consumeOp(String op)
        {
            opSet.recordStringOp(op);
        }
        void finishConsumingQueue()
        {
            try
            {
                loggingClient.nonBlockingSendMessage(opSet);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            
            opSet.clearSet();
        }
        /**
         * Close the connection to the loggingServer.
         */
        void writeEpilogueAndClose(SendEpilogue sendEpilogue)
        {
            debug("write epilogue and close.");
            
            Logging.this.debug("Logging: Sending Epilogue " + LOG_CLOSING);
            try
            {
                loggingClient.nonBlockingSendMessage(sendEpilogue);
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            loggingClient.disconnect();
            loggingClient   = null;
        }
    }
    /**
     * @return the opSequence
     */
    public ArrayListState getOpSequence()
    {
        return opSequence;
    }
}
