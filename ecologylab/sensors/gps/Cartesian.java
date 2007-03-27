package ecologylab.sensors.gps;
public class Cartesian {
  public Angle latitude;
  public Angle longitude;
  public Cartesian(String lat, String latDir, String lon, String lonDir) {
    latitude = new Angle(lat,latDir);
    longitude = new Angle(lon, lonDir);
  }

  public String toString(int format) {
    switch (format) {
      case 1: return latitude.toString(4) + " " + longitude.toString(4);
      // North 93 deg 21 min 23.23 sec West 93 deg 21 min 23.23 sec
      case 2: return latitude.toString(3) + " " + longitude.toString(3);
      // 93.212323W 30.234598N
      case 3: return longitude.toString(4) + " " + latitude.toString(4);
      // West 93 deg 21 min 23.23 sec North 93 deg 21 min 23.23 sec
      case 4: return longitude.toString(3) + " " + latitude.toString(3);
      //  30.234598N 93.212323W
      case 5: return latitude.toString(4) + ";" + longitude.toString(4);
      // North 93 deg 21 min 23.23 sec West 93 deg 21 min 23.23 sec
      case 6: return latitude.toString(3) + ";" + longitude.toString(3);
      // 93.212323W 30.234598N
      default: return "";
    }
  }
}