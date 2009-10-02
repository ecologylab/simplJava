package ecologylab.xml.internaltranslators.cocoa;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TranslationUtilities
{
   
   public static String getObjectiveCType(Class<?> type) throws Exception
   {
      if (int.class == type)
      {
         return TranslationConstants.OBJC_INTEGER;
      }
      else if (float.class == type)
      {
         return TranslationConstants.OBJC_FLOAT;
      }
      else if (byte.class == type)
      {
         return TranslationConstants.OBJC_BYTE;
      }
      else if (char.class == type)
      {
         return TranslationConstants.OBJC_CHAR;
      }
      else if (boolean.class == type)
      {
         return TranslationConstants.OBJC_BOOLEAN;
      }
      else if (long.class == type)
      {
         return TranslationConstants.OBJC_LONG;
      }
      else if (short.class == type)
      {
         return TranslationConstants.OBJC_SHORT;
      }
      else if (String.class == type)
      {
         return TranslationConstants.OBJC_STRING;
      }
      else if (Date.class == type)
      {
         return TranslationConstants.OBJC_DATE;
      }
      else if (ArrayList.class == type)
      {
         return TranslationConstants.OBJC_ARRAYLIST;
      }
      else if (HashMap.class == type)
      {
         return TranslationConstants.OBJC_HASHMAP;
      }
      else
      {
         throw new Exception("Unsupported type");
      }
   }
}
