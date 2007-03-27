package ecologylab.sensors.gps;
public class GPSDate {
  public int year = 0;
  public int month = 1;
  public int day = 1;

  public GPSDate (String date) {
    year = Integer.parseInt(date.substring(4));
    if (year >= 95) {
      year = (int) year + 1900;
    } else {
      year = (int) 2000 + year;
    }
    month = Integer.parseInt(date.substring(2,4));
    day = Integer.parseInt(date.substring(0,2));
  }

  public String toString(int format) {
    switch (format) {
      case 1:
        return (String) Integer.toString(month) + "/" + Integer.toString(day) + "/" + Integer.toString(year);
      default:
        return "";
    }
  }
}