package ecologylab.xml.internaltranslators.cocoa;

public class TranslationConstants
{
   
   /*
    * Syntactical constants
    */
   public static final String HEADER_FILE_OPENING = "#import <objc/Object.h>";
   public static final String LINE_BREAK = "\n";
   public static final String INTERFACE = "@interface";
   public static final String OPENING_BRACE = "{";
   public static final String CLOSING_BRACE = "}";
   
   
   /*
    * Scalar types
    */   
   public static final String OBJC_INTEGER = "int";
   public static final String OBJC_FLOAT = "float";
   public static final String OBJC_DOUBLE = "double";
   public static final String OBJC_BYTE = "char";
   public static final String OBJC_CHAR = "char";
   public static final String OBJC_BOOLEAN = "bool";
   public static final String OBJC_LONG = "long";
   public static final String OBJC_SHORT = "short";
   public static final String OBJC_STRING = "NSString*";  
   
   /*
    * Reference types
    */    
   public static final String OBJC_DATE = "NSDate";
   public static final String OBJC_STRING_BUILDER = "NSMutableString";
   public static final String OBJC_URL = "NSURL";
   public static final String OBJC_PARSED_URL = "NSURL";
   
   /*
    * Collection types
    */
   public static final String OBJC_ARRAYLIST = "NSMutableArray*";
   public static final String OBJC_HASHMAP = "NSDictionary*";
   
   
}
