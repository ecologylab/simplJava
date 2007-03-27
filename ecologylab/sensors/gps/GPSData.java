package ecologylab.sensors.gps;
public class GPSData {
  public Angle latitude;
  public Angle longitude;
  public float altitude;
  public String altitudeUnits;
  public float positionDOP;
  public float horizontalDOP;
  public float verticalDOP;
  public GPSDate date;
  public GPSTime time;
  public String speed = "";
  public String heading = "";
  public String magneticVariation = "";
  public String magneticVariationDirection = "";
  public boolean positionStatus = false;
  public boolean DGPSStatus = false;
  public boolean fix3D = false;
  public boolean fix2D = false;
  public int SVInUse = 0;
  public boolean force3D = false;
  public int[] SVInUseArray;
  public int SVInView = 0 ;
  public SVData[] SV;

  public GPSData() {
    SVInUseArray = new int[12];
    SV = new SVData[12];
  }
}