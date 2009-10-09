package ecologylab.xml.internaltranslators.cocoa;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * TODO: add comments
 * 
 * @author Nabeel Shahzad
 */
public class TranslationUtilities
{
   
   /**
    * TODO: add comments
    * 
    * @param type
    * @return String
    * @throws CocoaTranslationException 
    * @throws Exception
    */
   public static String getObjectiveCType(Class<?> type) throws CocoaTranslationException
   {
      if (int.class == type)
      {
         return TranslationConstants.OBJC_INTEGER;
      }
      else if (float.class == type)
      {
         return TranslationConstants.OBJC_FLOAT;
      }
      else if (double.class == type)
      {
         return TranslationConstants.OBJC_DOUBLE;
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
      else if (StringBuilder.class == type)
      {
         return TranslationConstants.OBJC_STRING_BUILDER;
      }
      else if (URL.class == type)
      {
         return TranslationConstants.OBJC_URL;
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
         throw new CocoaTranslationException(CocaTranslationExceptionTypes.UNSUPPORTED_DATATYPE);
      }
   }
}
