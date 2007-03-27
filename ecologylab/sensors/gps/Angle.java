package ecologylab.sensors.gps;

public class Angle {
  public float degrees;
  public int hours;
  public int minutes;
  public int seconds;
  public int milliseconds;
  private boolean northSouth;
  private boolean sign;

  public String directionString(int format) {
    if (northSouth) {
      if (sign) {
        switch (format) {
          case 0:  return "North";
          case 1:  return "N";
          default: return "";
        }
      } else {
        switch (format) {
          case 0:  return "South";
          case 1:  return "S";
          default: return "";
        }
      }
    } else {
      if (sign) {
        switch (format) {
          case 0:  return "East";
          case 1:  return "E";
          default: return "";
        }
      } else {
        switch (format) {
          case 0:  return "West";
          case 1:  return "W";
          default: return "";
        }
      }
    }
  }

  public Angle()
  {
  	
  }
  
  public Angle(float degrees, String direction) {
  	this.degrees = degrees;
  	this.sign = true;
    this.northSouth = true;
  	if (direction.equals("E")|direction.equals("W")) {
        this.northSouth = false;
      }
      if (direction.equals("S")|direction.equals("W")) {
        this.sign = false;
      }
      float da = java.lang.Math.abs(degrees);
      hours = (int) da;
      float mr = (float) da-hours;
      float mf = mr*60;
      minutes = (int) mf;
      float s = (mf - minutes)*60;
      seconds = (int) s;
      float ms = (float) s-seconds;
      milliseconds = (int) (ms*1000);    
  }
  
  public Angle(String angleString, String direction) {
    this.sign = true;
    this.northSouth = true;
    if (direction.equals("E")|direction.equals("W")) {
      this.northSouth = false;
    }
    if (direction.equals("S")|direction.equals("W")) {
      this.sign = false;
    }
    int index = angleString.indexOf(".");
    if (this.sign) {
      this.degrees = (Integer.parseInt(angleString.substring(0,index - 2)) +
        Float.valueOf(angleString.substring(index - 2)).floatValue()/60);
    } else {
      this.degrees = -1 * (Integer.parseInt(angleString.substring(0,index - 2)) +
        Float.valueOf(angleString.substring(index - 2)).floatValue()/60);
    }
    float da = java.lang.Math.abs(degrees);
    hours = (int) da;
    float mr = (float) da-hours;
    float mf = mr*60;
    minutes = (int) mf;
    float s = (mf - minutes)*60;
    seconds = (int) s;
    float ms = (float) s-seconds;
    milliseconds = (int) (ms*1000);
  }

  public String toString(int format) {
    switch (format) {
      case 1: // North 30.44493 deg
        return directionString(0) + " " + java.lang.Math.abs(degrees) + " deg ";
      case 2: // 93.3538 (GPS format without sign)
        return (String) Float.toString(java.lang.Math.abs(degrees));
      case 3: // 93.3538W (GPS format with sign)
        return (String) printPortion(Float.toString(java.lang.Math.abs(degrees)),6) + directionString(1);
      case 4: // West 93 degrees 21 minutes 13.93 seconds
        float da = java.lang.Math.abs(degrees);
        int d = (int) da;
        float mr = (float) da-d;
        float mf = mr*60;
        int m = (int) mf;
        float s = (mf -m)*60;
        return (String) directionString(0) + " " + Integer.toString(d) + " deg " +
          Integer.toString(m) + " min " + printPortion(Float.toString(s),2) + " sec";
      case 5: // 93 21'13"930ms W
      	return (String) Integer.toString(hours)+" "+Integer.toString(minutes) + "'" +
		  Integer.toString(seconds) + "\"" + Integer.toString(milliseconds) +"ms"+ directionString(1);
      case 6: // 93_21_13W
      	return (String) Integer.toString(hours)+"_"+Integer.toString(minutes) + "_" +
		  Integer.toString(seconds) + directionString(1);
      default:
        return "";
    }
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

}
