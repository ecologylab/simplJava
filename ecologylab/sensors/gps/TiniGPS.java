package ecologylab.sensors.gps;
import java.io.IOException;

class TiniGPS {
  public static void main(String[] args) {
    if (args.length != 0) {
      System.out.println("Usage: java TiniGPS.tini");
      System.exit(1);
    }
    GPSData GPSData = new GPSData();
    GPS tripmate = new GPS(GPSData);
//    httpDaemon GPSWeb = new httpDaemon(GPSData);
//    GPSWeb.start();
//    Clock rtc = new Clock();
//    System.out.println("Year: " + rtc.getYear());
//    int a = rtc.getYear();
//    String ys,ms,ds,hs,Ms;
//    if (a < 10) {
//      ys = "0" + a;
//    } else {
//      ys = Integer.toString(a) ;
//
//    }
//    a = rtc.getMonth();
//    if (a < 10) {
//      ms = "0" + a;
//    } else {
//      ms = Integer.toString(a);
//    }
//    a = rtc.getDate();
//    if (a < 10) {
//      ds = "0" + a;
//    } else {
//      ds = Integer.toString(a);
//    }
//    a = rtc.getHour();
//    if (a < 10) {
//      hs = "0" + a;
//    } else {
//      hs = Integer.toString(a);
//    }
//    a = rtc.getMinute();
//    if (a < 10) {
//      Ms = "0" + a;
//    } else {
//      Ms = Integer.toString(a);
//    }
//    String dateString = ds + ms + ys;
//    String timeString = hs + Ms + "00";
//    System.out.println(dateString+ " " + timeString);
//    System.out.println("Month: " + rtc.getMonth());
//    System.out.println("Day: " + rtc.getDate());
//    System.out.println("Hour: " + rtc.getHour());
//    System.out.println("Minute: " + rtc.getMinute());
//    System.out.println("Second: " + rtc.getSecond());
//    String initMessage = "$PRWIINIT,V,,,,,,,,,,,,"+timeString+","+dateString;
//    for (int index = 1; index < 100000; index ++) {
//      int abc = 1;
//    }
//    System.out.println("trying to set GPS Clock");
//    tripmate.term.writeToGPS(initMessage);
//    try {
      while (true) {
        byte b = (byte) ' ';
        String timeString = null;
//        if(GPSWeb != null)
         {
        	if(GPSData != null)
        	{
             if(GPSData.time != null)
             {
	           timeString = /* GPSWeb. */ GPSData.time.toString(1);
	           System.out.println("GPS time:" + timeString);
             }
             if(GPSData.latitude != null)
             	System.out.println("Lat: " + GPSData.latitude.degrees);
             if(GPSData.longitude != null)
             	System.out.println("Longv: " + GPSData.longitude.degrees);
        	}
         }  
        if (b == (byte) '~') {
          System.out.println("Shutting down threads");
          tripmate.stop();
//          GPSWeb.stopThread();
//          while (GPSWeb.isAlive()) {
//          }
          System.out.println("Threads shut down");
          System.out.println("Exiting TiniGPS");
          break;
        }
      }
    /*} catch (IOException ioe) {
      ioe.printStackTrace();
    } */
  }
}