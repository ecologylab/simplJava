package ecologylab.xml.internaltranslators.cocoa;

import java.util.ArrayList;
import java.util.HashMap;

public class TranslationUtilities
{
   public static String getObjectiveCType(Class<?> type) throws Exception
   {
      if (int.class == type)
      {
         return TranslationConstants.OBJC_INTEGER;
      }
      if (float.class == type)
      {
         return TranslationConstants.OBJC_FLOAT;
      }
      if (byte.class == type)
      {
         return TranslationConstants.OBJC_BYTE;
      }
      if (char.class == type)
      {
         return TranslationConstants.OBJC_CHAR;
      }
      if (boolean.class == type)
      {
         return TranslationConstants.OBJC_BOOLEAN;
      }
      if (long.class == type)
      {
         return TranslationConstants.OBJC_LONG;
      }
      if (short.class == type)
      {
         return TranslationConstants.OBJC_SHORT;
      }
      if (String.class == type)
      {
         return TranslationConstants.OBJC_STRING;
      }      
      if (ArrayList.class == type)
      {
         return TranslationConstants.OBJC_STRING;
      }
      
      if (HashMap.class == type)
      {
         return TranslationConstants.OBJC_HASHMAP;
      }
      else
      {
         throw new Exception("Unsupported type");
      }
   }
}
