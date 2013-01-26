package simpl.core;

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import ecologylab.generic.Debug;

/**
 * JSON related tools.
 * 
 * @author quyin
 * 
 */
public class JSONTools
{

  static JsonFactory factory = new JsonFactory();
  
  /**
   * Validate if an input JSON strong is valid. Empty strings or null values will be regarded as
   * INVALID.
   * 
   * @param jsonString
   * @return true if and only if the input string is an non-empty valid JSON sequence.
   */
  public static boolean validate(String jsonString)
  {
    if (jsonString == null)
      return false;

    try
    {
      JsonParser parser = factory.createJsonParser(jsonString);
      while (parser.nextToken() != null)
      {
        // no-op
      }
      return true;
    }
    catch (JsonParseException e)
    {
      Debug.warning(JSONTools.class, "json validation failed: " + e.getMessage());
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    return false;
  }

}
