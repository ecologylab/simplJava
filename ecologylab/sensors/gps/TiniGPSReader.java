package ecologylab.sensors.gps;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

class TiniGPSReader extends Thread implements SerialPortEventListener {
    private SerialPort sp;
    private InputStream sin;
    private OutputStream sout;

    //removed to common strings

//  private String inBuffer = "";
//  private String outBuffer;

    //added string buffer

    private LightBufBucket cache=new LightBufBucket();

    private Queue GPSMessageQueue;
    private boolean dataReady = false;
    volatile boolean keepRunning;
    public TiniGPSReader(String portName, int baudRate, Queue GPSMessageQueue) throws
        NoSuchPortException, PortInUseException, UnsupportedCommOperationException,
        IOException {
        try {
            // Create SerialPort object for specified port
            System.out.println("Initializing serial port");
            sp = (SerialPort)
                CommPortIdentifier.getPortIdentifier(portName).open("TiniGPSReader", 5000);
            // Configure port for 8 databits, 1 stop bit and no parity checks
            sp.setSerialPortParams(baudRate, SerialPort.DATABITS_8,
                                   SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            System.out.println("Initializing Queue");
            this.GPSMessageQueue = GPSMessageQueue;
            keepRunning = true;
            System.out.println("Turning Tripmate on");
            sp.setDTR(true);
        } catch (NoSuchPortException nsp) {
            System.out.println("Specified serial port ("+portName+") does not exist");
            throw nsp;
        } catch (PortInUseException piu) {
            System.out.println("Serial port "+portName+" is in use by another application");
            throw piu;
        } catch (UnsupportedCommOperationException usc) {
            System.out.println("Unable to configure port:"+portName);
            throw usc;
        }
        try {
            // Get input and output streams for serial data I/O
            sin = sp.getInputStream();
            sout = sp.getOutputStream();
        } catch (IOException ioe) {
            System.out.println("Unable to acquire I/O streams for port " + portName);
            throw ioe;
        }
    }

    public void run() {
    	    	
        try {
            // Add 'this' object as a Serial port event listener
            // and request notification when receive data is available
            sp.addEventListener(this);
            sp.notifyOnDataAvailable(true);
        } catch (TooManyListenersException tml) {
            tml.printStackTrace();
            System.exit(1);
        }

        // Return as soon as any bytes are available
        // (i.e. don't wait for line termination)
//        ((SystemInputStream) System.in).setRawMode(true);

        while (keepRunning) {
        	if(this == Thread.currentThread())
        	{
        		try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
        //Comment out next 2 lines when you want the GPS to keep it's fix, and stay running
        System.out.println("Turning Tripmate off");
        sp.setDTR(false);
        System.out.println("GPS Reader is shut down");
    }

    public void stopThread() {
        keepRunning = false;
    }

    public void writeToGPS(String outString) {
        try {
            sout.write(outString.getBytes());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void serialEvent(SerialPortEvent ev) {
        switch (ev.getEventType()) {
            case SerialPortEvent.DATA_AVAILABLE:
                try {

                    int count = sin.available();
                    if (count > 0) {
                        cache.read(sin,count);
                        //count = sin.read(buf, 0, count);
                        //append only the read bytes as defined bey the count
                        //cache.append(new String(buf,0,count));
                        /*
                         if (inBuffer.length() == 0) {
                         inBuffer = new String(buf);
                         }
                         else {
                         inBuffer = inBuffer + new String(buf);
                         int returnPosition = inBuffer.indexOf(13);
                         */
                        //using cacheTostring should not allocate any more memory unless
                        //it is assigned to a variable
                        if (cache.hasNewLine()) {
                            String line=cache.toString();
                            int returnPosition = line.indexOf(13);
                            //if (returnPosition >= 0) {
                            int startIndex = 0;

                            if (line.indexOf(10)==0)
                                startIndex=1;
                            //if (inBuffer.indexOf(10)==0) {
                            //  startIndex = 1;
                            //}

                            String outBuffer = line.substring(startIndex,returnPosition);
                            //String inBuffer = line.substring(returnPosition+1);
                            if (outBuffer.indexOf("ASTRAL") >= 0) {
                                writeToGPS(outBuffer);
                            } else if (outBuffer.indexOf("$") == 0) {
                                GPSMessageQueue.push(outBuffer);
                            }
                            //remove cache once one line has been read
                            cache.reset();
                        }
                    }
                    //}
                } catch (IOException ioe) {
                    System.out.println("I/O Exception!");
                    ioe.printStackTrace();
                }
                break;
            default:
                // Ignoring any unexpected events
                break;
        }
    }
}
