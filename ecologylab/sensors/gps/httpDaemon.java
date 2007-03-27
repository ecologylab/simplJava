package ecologylab.sensors.gps;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class httpDaemon extends Thread {
  private boolean keepRunning;
  private int countValue;
  private int index; // temporary generic index value
  GPSData GPSData;
  private String inputString;
  private boolean refresh;
  public void run() {//throws IOException {
    try {
      ServerSocket server = new ServerSocket(80);
      while (keepRunning) {
        Socket socket = server.accept();
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // Read in the HTTP headers
        String s;
        index = 0;
        while ((s = in.readLine()) != null) {
          index = s.indexOf("GET");
          if (index >= 0) {
            if (s.length() <= 5) {
              inputString = "/";
            } else {
              inputString = s.substring(4,s.indexOf(" ",4));
            }
          }
          //
          if (s.equals("")) {break;}
        }
        // HTTP headers done
        refresh = false;
        index = 0;
        inputString = inputString.toLowerCase();
        if (inputString.equals("/")) { index = 0; refresh = false; } // default output
        else if (inputString.equals("/refresh")) { index = 0; refresh = true; } // just put out the basic data
        else if (inputString.equals("/cmap")) { index = 1; refresh = false; } // output a mapblast map
        else if (inputString.equals("/mbmap")) { index = 2; refresh = false; } // output a mapblast map
        else if (inputString.equals("/full")) { index = 3; refresh = true; } // print everything
        else if (inputString.equals("/sv")) { index = 4; refresh = true; } // print SV summary
        else if (inputString.equals("/datamap")) { index = 5; refresh = true; } // print SV summary
        else if (inputString.equals("/basic")) { index = 6; refresh = false; } // just put out the basic data
        else if (inputString.equals("/help")) { index = 99; refresh = false; } // print a helper file
        else { inputString = "/"; }
        // send reply, start with the header
        out.print("HTTP/1.0 200 OK\r\n\r\n");
        if (index == 6) {
          if (GPSData.fix2D) {
            out.print("2;");
          } else if (GPSData.fix3D) {
            out.print("3;");
          } else {
            out.print("-;");
          }
          out.print(GPSData.date.toString(1));
          out.print(";");
          out.print(GPSData.time.toString(1));
          out.print(";");
          out.print(GPSData.latitude.toString(3));
          out.print(";");
          out.print(GPSData.longitude.toString(3));
          out.print(";");
          if (GPSData.fix3D) {
            out.print(GPSData.altitude);
            out.print(GPSData.altitudeUnits);
            out.print(";");
          } else {
            out.print("-;");
          }
          out.print(GPSData.heading);
          out.print(";");
          out.print(GPSData.speed);
          out.print("\r\n");
        } else {
          out.print("<HTML><HEAD><TITLE>TiniGPS: ");
          if (GPSData.positionStatus) {
            out.print("Valid GPS Solution");
          } else {
            out.print("Invalid GPS Solution");
          }
          out.print("</TITLE></HEAD><BODY>\r\n");
          if (refresh) {
            out.print("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"60; URL=" + inputString + "\">\r\n");
          }
          switch (index) {
            case 1: mapPage(out,1);       break;
            case 2: mapPage(out,2);       break;
            case 3: printFull(out);       break;
            case 4: printSVSummary(out);  break;
            case 5: dataMap(out);         break;
            case 99: printHelp(out);      break;
            default: printBasic(out);
          }
          out.print("</BODY></HTML>\r\n");
        }
        out.flush();
        socket.close();
        System.gc();
      }
    } catch (java.io.IOException ioe) {
        ioe.printStackTrace();
    }
  }

   public void mapPage(PrintWriter out, int format) {
    if (GPSData.positionStatus) {
      GPSMapOut(out,format);
    } else {
      out.print("GPS Data not available, I can't make an accurate map.<BR>");
    }
  }

   public void dataMap(PrintWriter out) {
    out.print("<TABLE BORDER>");
    GPSDateOut(out,1);
    GPSValidOut(out,1);
    if (GPSData.positionStatus) {
      GPSPositionDataOut(out,1);
      GPSMapOut(out,2);
    }
    out.print("</TABLE>\r\n");
  }

  public void printFull(PrintWriter out) {
    out.print("<TABLE BORDER>");
    GPSDateOut(out,1);
    GPSValidOut(out,1);
    if (GPSData.positionStatus) {
      GPSPositionDataOut(out,1);
      GPSMapOut(out,2);
      SVSummary(out,2);
    }
    out.print("</TABLE>\r\n");
  }

  public void printBasic(PrintWriter out) {
    out.print("<TABLE BORDER>");
    GPSDateOut(out,1);
    GPSValidOut(out,1);
    if (GPSData.positionStatus) {
      GPSPositionDataOut(out,1);
    }
    out.print("</TABLE>\r\n");
  }

  public void printSVSummary(PrintWriter out) {
    out.print("<TABLE BORDER>");
    SVSummary(out,2);
    out.print("</TABLE>\r\n");
  }

  public void printHelp(PrintWriter out) {
    out.print("<H1>TiniGPS Help</H1><P>\r\n");
    out.print("<a href=\"/\">Basic GPS data</a><BR>\r\n");
    out.print("<a href=\"/refresh\">Basic GPS data with refresh</a><BR>\r\n");
    out.print("<a href=\"/datamap\">Basic GPS data with Map</a><BR>\r\n");
    out.print("<a href=\"/full\">Data, map, and SV data</a><BR>\r\n");
    out.print("<a href=\"/cmap\">Map of the current position from the Census</a><BR>\r\n");
    out.print("<a href=\"/mbmap\">Map of the current position from MapBlast</a><BR>\r\n");
    out.print("<a href=\"/sv\">SV data</a><BR>\r\n");
    out.print("<a href=\"/basic\">Simple data</a><BR>\r\n");
    out.print("<a href=\"/help\">This file</a><BR>\r\n");
  }

  public void SVSummary(PrintWriter out, int format) {
    switch (format) {
      case 1:
        out.print("<TR><TD>SVs in view</TD><TD COLSPAN=3>" + GPSData.SVInView + "</TD></TR>\r\n");
        for (index = 0; index < GPSData.SVInView; index++) {
          out.print("<TR><TD COLSPAN=4>" + GPSData.SV[index].toString(1) + "</TD></TR>\r\n");
        }
        break;
      case 2:
        out.print("<TR><TD>SV PRN</TD><TD>SNR</TD><TD>Elevation</TD><TD>Azimuth</TD></TR>\r\n");
        for (index = 0; index < GPSData.SVInView; index++) {
          boolean found = false;
          for (int a = 0; a < 12 ; a++) {
            if (GPSData.SV[index].PRN == GPSData.SVInUseArray[a]) {
              found = true;
              break;
            }
          }
          if (found) {
            out.print(GPSData.SV[index].toString(3));
          } else {
            out.print(GPSData.SV[index].toString(2));
          }
        }
        break;
      default:
    }
  }

  public void GPSDateOut(PrintWriter out, int format) {
    switch (format) {
      case 1:
        out.print("<TR><TD>GPS Date</TD><TD>" + GPSData.date.toString(1) + "</TD>");
        out.print("<TD>GPS time</TD><TD>" + GPSData.time.toString(1) + "</TD></TR>\r\n");
        break;
      default:
    }
  }

  public void GPSValidOut(PrintWriter out, int format) {
    switch (format) {
      case 1:
        out.print("<TR><TD>GPS Solution</TD><TD COLSPAN=3><B>");
        if (GPSData.positionStatus) {
          out.print("Valid</B>");
        } else {
          out.print("Invalid</B>");
        }
        if (GPSData.fix2D) {
          out.print(", 2D</TD></TR>\r\n");
        } else if (GPSData.fix3D) {
          out.print(", 3D</TD></TR>\r\n");
        } else {
          out.print("</TD></TR>\r\n");
        }
        break;
      default:
    }
  }

  public void GPSMapOut(PrintWriter out, int format) {
    switch (format) {
      case 1:
        out.print("<TR><TD COLSPAN=4>" + mapURL(1) + "</TD></TR>\r\n");
        break;
      case 2:
        out.print("<TR><TD COLSPAN=4>" + mapURL(2) + "</TD></TR>\r\n");
        break;
      default:
    }
  }

  public void GPSPositionDataOut(PrintWriter out, int format) {
    switch (format) {
      case 1:
        out.print("<TR><TD>Position</TD>");
        if (GPSData.fix3D) {
          out.print("<TD>" + GPSData.latitude.toString(3) + " " + GPSData.longitude.toString(3) +
              "</TD><TD>Altitude</TD><TD>" + java.lang.Math.round(GPSData.altitude) +
              GPSData.altitudeUnits + "</TD></TR>\r\n");
          } else {
          out.print("<TD>" + GPSData.latitude.toString(3) + " " + GPSData.longitude.toString(3) +
          "</TD><TD>Altitude</TD><TD>----</TD></TR>\r\n");
          }
        out.print("<TR><TD>Heading</TD><TD>" + GPSData.heading + "</TD><TD>Speed</TD><TD>" +
                   GPSData.speed + "</TD></TR>\r\n" );
        out.print("<TR><TD></TD><TD>Position</TD><TD>Vertical</TD><TD>Horizontal</TD></TR>\r\n");
        out.print("<TR><TD>DOP</TD><TD>" + GPSData.positionDOP +
                             "</TD><TD>" + GPSData.verticalDOP +
                             "</TD><TD>" + GPSData.horizontalDOP + "</TD></TR>\r\n");
        break;
      default:
    }
  }

  public String mapURL(int whichEngine) {
    String la = printPortion(Float.toString(GPSData.latitude.degrees),6);
    String lo = printPortion(Float.toString(GPSData.longitude.degrees),6);
    int width = 1;
    int height = 1;
    String URL = "";
    switch (whichEngine) {
      case 1: // Census Map
        width = 422;
        height = 359;
        URL = "http://grizzly.ssd.census.gov/cgi-bin/mapper/map.gif?&lat=" +
          la + "&lon=" + lo + "&ht=0.006&wid=0.006&&tlevel=-&tvar=-&tmeth=i&mlat=" +
          la + "&mlon=" + lo + "&msym=cross&mlabel=&murl=&conf=mapnew.con&iht="+ height +
          "&iwd=" + width;
        break;
      case 2: // mapblast map
        width = 456;
        height = 259;
        URL = "http://www.mapblast.com/gif?&CT=" + la + ":" + lo + ":5000&IC=" +
          la + ":" + lo + ":8:&W=" + width + "&H=" + height + "&FAM=myblast&LB=";
        break;
      default:
        break;
    }
    return "<img src=\"" + URL + "\" width=" + width + " height=" + height + ">";
  }

  public httpDaemon(GPSData GPSData) {
    this.GPSData = GPSData;
    keepRunning = true;

  }

  private String printPortion(String numberString, int decimalPlaces) {
    int decimalPosition = numberString.indexOf(".");
    if (decimalPosition < 0) {
      return numberString;
    } else {
      int endString = decimalPosition+decimalPlaces+1;
      if (endString > numberString.length()) { endString = numberString.length();}
      return numberString.substring(0,endString);
    }
  }


  public void stopThread() {
    System.out.println("Sending the web daemon the shutdown message");
    keepRunning = false;
  }
}
