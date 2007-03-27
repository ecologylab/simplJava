package ecologylab.sensors.gps;
public class SVData {
  public int PRN = 0;
  public int elevation = 0;
  public int azimuth = 0;
  public int SNR = 0;
  private String SNRMessage;
//  private static final String TABLE_BEGIN = "<TR><TD>";
//  private static final String TABLE_NEXT = "</TD><TD>";
//  private static final String TABLE_END = "</TD></TR>";
//  private static final String TABLE_BEGIN_B = "<TR><TD><B>";
//  private static final String TABLE_NEXT_B = "</B></TD><TD><B>";
//  private static final String TABLE_END_B = "</B></TD></TR>";

  public SVData(String PRN, String elevation, String azimuth, String SNR) {
    try {
      this.PRN = Integer.parseInt(PRN);
      this.elevation = Integer.parseInt(elevation);
      this.azimuth = Integer.parseInt(azimuth);
      this.SNR = Integer.parseInt(SNR);
    } catch (java.lang.NumberFormatException nfe) {
      if (PRN.length() == 0) { PRN = "0";}
      if (elevation.length() == 0) { elevation = "0"; }
      if (azimuth.length() == 0) { azimuth = "0"; }
      if (SNR.length() == 0) { SNR = "-99"; }
      this.PRN = Integer.parseInt(PRN);
      this.elevation = Integer.parseInt(elevation);
      this.azimuth = Integer.parseInt(azimuth);
      this.SNR = Integer.parseInt(SNR);
    }
  }

  public String toString(int format) {
    SNRMessage = Integer.toString(SNR) + " dB";
    if (SNR == -99) {
      SNRMessage = "Not Tracking";
    }
//    StringBuffer sb = new StringBuffer(60);
    switch (format) {
      case 1:
        return Integer.toString(PRN) + "--> elevation: " + Integer.toString(elevation) +
          " azimuth: " + Integer.toString(azimuth) + " SNR: " + SNRMessage;
      case 2:
//                sb.append(TABLE_BEGIN);
//                sb.append(Integer.toString(PRN));
//                sb.append(TABLE_NEXT);
//                sb.append(SNRMessage);
//                sb.append(TABLE_NEXT);
//                sb.append(elevation);
//                sb.append(TABLE_NEXT);
//                sb.append(azimuth);
//                sb.append(TABLE_END);
//                return sb.toString();
        return "<TR><TD>" + Integer.toString(PRN) +
              "</TD><TD>" + SNRMessage +
              "</TD><TD>" + Integer.toString(elevation) +
              "</TD><TD>"+ Integer.toString(azimuth) +"</TD></TR>\r\n";
      case 3:
//                sb.append(TABLE_BEGIN_B);
//                sb.append(Integer.toString(PRN));
//                sb.append(TABLE_NEXT_B);
//                sb.append(SNRMessage);
//                sb.append(TABLE_NEXT_B);
//                sb.append(elevation);
//                sb.append(TABLE_NEXT_B);
//                sb.append(azimuth);
//                sb.append(TABLE_END_B);
//                return sb.toString();
        return "<TR><TD><B>" + Integer.toString(PRN) +
              "</B></TD><TD><B>" + SNRMessage +
              "</B></TD><TD><B>" + Integer.toString(elevation) +
              "</B></TD><TD><B>" + Integer.toString(azimuth) + "</B></TD></TR>\r\n";
      default:
        return "Unknown format in SVData.toString";
    }
  }

}