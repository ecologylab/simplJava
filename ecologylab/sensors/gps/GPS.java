package ecologylab.sensors.gps;
import java.util.Enumeration;
import java.util.Vector;

import javax.comm.CommPortIdentifier;

public class GPS {
  public TiniGPSReader term;
  public GPSDecoder decoder;
  private GPSData GPSData;
  
  final static int[] baudRates = {2400, 4800, 9600, 19200};
  
  public GPS(GPSData GPSData, String portName, int baudRate) {
    this.GPSData = GPSData;
    Queue GPSMessageQueue = new Queue();
    
    
    try {
      term = new TiniGPSReader(portName, baudRate, GPSMessageQueue);
      decoder = new GPSDecoder(GPSMessageQueue,GPSData);
      term.start();
      decoder.start();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }

  public GPS(GPSData GPSData, String portName) {
    this.GPSData = GPSData;
    Queue GPSMessageQueue = new Queue();
    
    try {
      term = new TiniGPSReader(portName, 4800, GPSMessageQueue);
      decoder = new GPSDecoder(GPSMessageQueue,GPSData);
      term.start();
      decoder.start();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }

  public GPS(GPSData GPSData) {
	String[] ports = getPortList();
	boolean foundGPS = false;
	
    this.GPSData = GPSData;
    Queue GPSMessageQueue = new Queue();
	for ( int i = 0; i < ports.length; i++ )
	{
		for ( int k = 0; k < baudRates.length; k++ )
		{

			try {
				System.out.println("Making connection to: "+ ports[i] +"/" + baudRates[k]);
				term = new TiniGPSReader(ports[i], baudRates[k], GPSMessageQueue);
				decoder = new GPSDecoder(GPSMessageQueue,GPSData);
				term.start();
				decoder.start();
				foundGPS = true;
			} catch (Exception e) {
				System.err.println("Failed connection to: "+ ports[i] +"/" + baudRates[k]);
				System.out.println(e.getMessage());
				e.printStackTrace();
				foundGPS = false;
				term.stop();
				decoder.stop();
				continue;
			}
			if(foundGPS) break;
		}
		if(foundGPS) break;
	}
  }

  public void stop() {
    if (decoder.isAlive()) {
      decoder.stopThread();
      while (decoder.isAlive()) {
      }
    }
    if (term.isAlive()) {
      term.stopThread();
      while (term.isAlive()) {
      }
    }
  }

	/**
	 *  Returns the list of all known serial ports.
	 *
	 *@return    List of ports.
	 */
	public static String[] getPortList()
	{
		Vector v = new Vector();
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();

		while ( portList.hasMoreElements() )
		{
			CommPortIdentifier portId = ( CommPortIdentifier ) portList.nextElement();
			//System.out.println(portId.getName());
			if ( portId.getPortType() == CommPortIdentifier.PORT_SERIAL )
			{
				v.add( portId.getName() );
			}
		}

		return ( String[] ) v.toArray( new String[v.size()] );
	}


	/**
	 *  Returns the list of all known baud rates.
	 *
	 *@return    List of Baud rates.
	 */
	public static int[] getBaudrateList()
	{
		return baudRates;
	}
	
}