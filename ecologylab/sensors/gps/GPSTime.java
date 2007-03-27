package ecologylab.sensors.gps;
public class GPSTime {
  public double hour = 1;
  public double minute = 0;
  public double second = 0;

  public GPSTime (String time) {
    hour = Double.parseDouble(time.substring(0,2));
    minute = Double.parseDouble(time.substring(2,4));
    second = Double.parseDouble(time.substring(4));
  }

  public String toString(int format) {
    switch (format) {
      case 1:
          return (String) Double.toString(hour) + ":" + Double.toString(minute) + ":" + Double.toString(second);
      default:
        return "";
    }
  }
}