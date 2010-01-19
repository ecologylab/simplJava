package ecologylab.xml.internaltranslators.cocoa;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ecologylab.collections.Scope;
import ecologylab.generic.HashMapArrayList;
import ecologylab.net.ParsedURL;
import ecologylab.xml.types.scalar.ScalarType;

/**
 * Utilities class to provide static helper methods and code logic used again
 * and again by the system
 * 
 * @author Nabeel Shahzad
 */
public class TranslationUtilities
{

   /**
    * Methods that does the job of mapping the Java datatypes to corresponding
    * Objective-C datatypes.
    * 
    * @param type
    * @return String
    * @throws CocoaTranslationException
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
      else if (ParsedURL.class == type)
      {
         return TranslationConstants.OBJC_PARSED_URL;
      }
      else if (ScalarType.class == type)
      {
         return TranslationConstants.OBJC_SCALAR_TYPE;
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
      else if (HashMapArrayList.class == type)
      {
         return TranslationConstants.OBJC_HASHMAPARRAYLIST;
      }
      else if (Scope.class == type)
      {
         return TranslationConstants.OBJC_SCOPE;
      }
      else if (Class.class == type)
      {
         return TranslationConstants.OBJC_CLASS;
      }
      else if (Field.class == type)
      {
         return TranslationConstants.OBJC_FIELD;
      }
      else
      {
         throw new CocoaTranslationException(CocaTranslationExceptionTypes.UNSUPPORTED_DATATYPE);
      }
   }
}
