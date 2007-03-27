package ecologylab.sensors.gps;
import java.util.StringTokenizer;

class GPSDecoder extends Thread {

  private Queue GPSMessageQueue;
  private GPSData GPSData;
  private int year;
  private int temp;
  volatile boolean keepRunning;
  StringTokenizer st;
  private String sTemp, dTemp;
  private int decoderPointer;
  private boolean doneDecoding, returnValue;
  private String PRNTemp, azimuthTemp, elevationTemp, SNRTemp;
  private String TIME, POS_UTC;
  private int starIndex, startPointer, endPointer, index;

  public GPSDecoder(Queue GPSMessageQueue, GPSData GPSData) {
    this.GPSMessageQueue = GPSMessageQueue;
    this.GPSData = GPSData;
    keepRunning = true;
  }

  private String extractData(String data, boolean firstTime) {
    if (firstTime) {
      decoderPointer = 0;
      doneDecoding = false;
    }
    endPointer = data.indexOf(",", decoderPointer);
    if (endPointer == -1) { // nothing found
      doneDecoding = true;
      return data.substring(decoderPointer);
    }
    startPointer = decoderPointer;
    decoderPointer = endPointer + 1;
    return data.substring(startPointer, endPointer);
  }

  public void run() {
    while (keepRunning) {
      try {
        decodeNMEAString(GPSMessageQueue.pop());
      } catch (java.lang.NullPointerException ioe) {
        ioe.printStackTrace();
      }
    }
    System.out.println("Decoder is stopped");
  }

  public void decodeNMEAString(String NMEAData) {
    returnValue = false;
    if (verifyChecksum(NMEAData)) {
      index = NMEAData.indexOf(",");
      String NMEAId = NMEAData.substring(1,index);
      String NMEAPayload = NMEAData.substring(index + 1,NMEAData.indexOf("*"));
      if (NMEAId.equals("PRWIBIT")) { // Rockwell proprietary built-in test results
        decodePWRIBIT(NMEAPayload);
      } else if (NMEAId.equals("GPGGA")) { // GPS fix data
        decodeGPGGA(NMEAPayload);
      } else if (NMEAId.equals("GPGSA")) { // GPS DOP and active SVs
        decodeGPGSA(NMEAPayload);
      } else if (NMEAId.equals("GPGSV")) { // GPS SVs in view
        decodeGPGSV(NMEAPayload);
      } else if (NMEAId.equals("GPRMC")) { // reccomended minimum specific GPS data
        decodeGPRMC(NMEAPayload);
      } else if (NMEAId.equals("PRWIRID")) { // Rockwell proprietary receiver ID
        decodePRWIRID(NMEAPayload);
      } else if (NMEAId.equals("PRWIZCH")) { // Rockwell proprietary zodiac channel status
        decodePRWIZCH(NMEAPayload);
      }
    }
  }

  public boolean verifyChecksum(String NMEAData) {
    returnValue = false;
    if (NMEAData.indexOf("$") == 0) {
      starIndex = NMEAData.indexOf("*");
      if (starIndex > 0) {
        temp = 0;
        for (index=1;index<starIndex;index++) {
          temp = temp ^ NMEAData.charAt(index);
        }
        returnValue = (Integer.parseInt( NMEAData.substring(starIndex+1, starIndex+3), 16) == temp);
      }
    }
    return returnValue;
  }

  private void decodePWRIBIT(String NMEAData) {
    System.out.println("Built In Test Results");
    st = new StringTokenizer(NMEAData,",");
    if (st.countTokens() != 11) { return; }  // not the proper number of fields!

  }

  private void decodeGPGGA(String NMEAData) {
    st = new StringTokenizer(NMEAData,",");
    if (st.countTokens() < 12) { return; }  // not the proper number of fields!
    GPSData.time = new GPSTime(st.nextToken());
    GPSData.latitude = new Angle(st.nextToken(),st.nextToken());
    GPSData.longitude = new Angle(st.nextToken(),st.nextToken());
    temp = Integer.parseInt(st.nextToken());
    GPSData.SVInUse = Integer.parseInt(st.nextToken());
    GPSData.horizontalDOP = Float.valueOf(st.nextToken()).floatValue();
    GPSData.altitude = Float.valueOf(st.nextToken()).floatValue();
    GPSData.altitudeUnits = st.nextToken().toLowerCase();
    sTemp = st.nextToken(); // GEOID_SEP
    sTemp = st.nextToken(); // GEOID_SEP_Units
    GPSData.positionStatus = false;
    GPSData.DGPSStatus = false;
    if (temp >= 1) { GPSData.positionStatus = true; }
    if (temp == 2) { GPSData.DGPSStatus = true; }
  }

  private void decodeGPGSA(String NMEAData) {
    StringTokenizer st = new StringTokenizer(NMEAData,",");
    sTemp = st.nextToken();
    if (sTemp.equals("M")) {
      GPSData.force3D = false;
    } else {
      GPSData.force3D = true;
    }
    sTemp = st.nextToken();
    GPSData.fix2D = false;
    GPSData.fix3D = false;
    if (sTemp.equals("2")) {
      GPSData.fix2D = true;
    } else if (sTemp.equals("3")) {
      GPSData.fix3D = true;
    }
    index = 0;
    while (st.countTokens() > 3) {
      GPSData.SVInUseArray[index++] = Integer.parseInt(st.nextToken());
    }
    while (index < 12) {
      GPSData.SVInUseArray[index++] = 0;
    }
    GPSData.positionDOP = Float.valueOf(st.nextToken()).floatValue();
    GPSData.horizontalDOP  = Float.valueOf(st.nextToken()).floatValue();
    GPSData.verticalDOP  = Float.valueOf(st.nextToken()).floatValue();
  }

  private void decodeGPGSV(String NMEAData) {
    sTemp = extractData(NMEAData,true); // read total number of messages
    if (doneDecoding) { return; }
    sTemp = extractData(NMEAData,false); // read messages number
    if (doneDecoding) { return; }
    index = 4 * (Integer.parseInt(sTemp) - 1);
    sTemp = extractData(NMEAData,false); // read number of SVs in view
    GPSData.SVInView = Integer.parseInt(sTemp);
    if (doneDecoding) { return; }
    while (!doneDecoding) {
      PRNTemp = extractData(NMEAData,false);
      if (doneDecoding) { return; }
      if (PRNTemp.equals("")) { PRNTemp = "0"; }
      elevationTemp = extractData(NMEAData,false);
      if (doneDecoding) { return; }
      if (elevationTemp.equals("")) { elevationTemp = "0"; }
      azimuthTemp = extractData(NMEAData,false);
      if (doneDecoding) { return; }
      if (azimuthTemp.equals("")) { azimuthTemp = "0"; }
      SNRTemp = extractData(NMEAData,false);
      if (SNRTemp.equals("")) { SNRTemp = "-99"; }
      GPSData.SV[index++] = new SVData(PRNTemp,elevationTemp,azimuthTemp,SNRTemp);
      if (doneDecoding) { return; }
    }
  }

  private void decodeGPRMC(String NMEAData) {
    StringTokenizer st = new StringTokenizer(NMEAData,",");
    if (st.countTokens() != 11) { return; }  // not the proper number of fields!
    GPSData.time = new GPSTime(st.nextToken());
    GPSData.positionStatus = false;
    if (st.nextToken().equals("A")) {
      GPSData.positionStatus = true;
    }
    GPSData.latitude = new Angle(st.nextToken(),st.nextToken());
    GPSData.longitude = new Angle(st.nextToken(),st.nextToken());
    GPSData.speed = st.nextToken();
    GPSData.heading = st.nextToken();
    GPSData.date = new GPSDate(st.nextToken());
    GPSData.magneticVariation = st.nextToken();
    GPSData.magneticVariationDirection = st.nextToken();
  }

  private void decodePRWIRID(String NMEAData) {
    StringTokenizer st = new StringTokenizer(NMEAData,",");
    if (st.countTokens() < 3 ) { return; }  // not the proper number of fields!
    System.out.println(st.nextToken() + " channel GPS reciever, software version " + st.nextToken() + ", " + st.nextToken());
  }

  private void decodePRWIZCH(String NMEAData) {
//    StringTokenizer st = new StringTokenizer(NMEAData,",");
//    if (st.countTokens() != 24) { return; }  // not the proper number of fields!
//    int numberFields = st.countTokens();
  }

  public void stopThread() {
    keepRunning = false;
  }
}
