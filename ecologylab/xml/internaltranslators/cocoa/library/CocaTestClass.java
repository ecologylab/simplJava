package ecologylab.xml.internaltranslators.cocoa.library;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import ecologylab.xml.ElementState;
import ecologylab.xml.XMLTranslationException;

import ecologylab.xml.xml_inherit;

public @xml_inherit
class CocaTestClass extends ElementState
{
   @xml_attribute
   private int                     intAttribute;

   @xml_attribute
   private float                   floatAttribute;

   @xml_attribute
   private double                  doubleAttribute;

   @xml_attribute
   private byte                    byteAttribute;

   @xml_attribute
   private char                    charAttribute;

   @xml_attribute
   private boolean                 booleanAttribute;

   @xml_attribute
   private long                    longAttribute;

   @xml_attribute
   private short                   shortAttribute;

   @xml_attribute
   private String                  stringAttribute;

   @xml_attribute
   private Date                    dateAttribute;

   @xml_attribute
   private StringBuilder           stringBuilderAttribute;

   @xml_attribute
   private URL                     urlAttribute;

   @xml_collection
   private ArrayList<String>       arrayListAttribute;

   @xml_map
   private HashMap<String, String> hashMapAttribute;

   public CocaTestClass() throws MalformedURLException
   {
      this.intAttribute = 1;
      this.floatAttribute = 1.0f;
      this.doubleAttribute = 2.0;
      this.byteAttribute = 1;
      this.charAttribute = 'c';
      this.booleanAttribute = false;
      this.longAttribute = 1;
      this.shortAttribute = 3;
      this.stringAttribute = "234";
      this.dateAttribute = new Date();
      this.stringBuilderAttribute = new StringBuilder();
      this.urlAttribute = new URL("asdf");
      this.arrayListAttribute = new ArrayList<String>();
      this.hashMapAttribute = new HashMap<String, String>();

      this.arrayListAttribute.add("234");
      this.hashMapAttribute.put("1", "3");
   }

   public static void main(String args[]) throws XMLTranslationException, MalformedURLException
   {
      CocaTestClass test = new CocaTestClass();
      test.translateToXML(System.out);
   }

   public void setIntAttribute(int intAttribute)
   {
      this.intAttribute = intAttribute;
   }

   public int getIntAttribute()
   {
      return intAttribute;
   }

   public void setFloatAttribute(float floatAttribute)
   {
      this.floatAttribute = floatAttribute;
   }

   public float getFloatAttribute()
   {
      return floatAttribute;
   }

   public void setDoubleAttribute(double doubleAttribute)
   {
      this.doubleAttribute = doubleAttribute;
   }

   public double getDoubleAttribute()
   {
      return doubleAttribute;
   }

   public void setByteAttribute(byte byteAttribute)
   {
      this.byteAttribute = byteAttribute;
   }

   public byte getByteAttribute()
   {
      return byteAttribute;
   }

   public void setCharAttribute(char charAttribute)
   {
      this.charAttribute = charAttribute;
   }

   public char getCharAttribute()
   {
      return charAttribute;
   }

   public void setBooleanAttribute(boolean booleanAttribute)
   {
      this.booleanAttribute = booleanAttribute;
   }

   public boolean isBooleanAttribute()
   {
      return booleanAttribute;
   }

   public void setLongAttribute(long longAttribute)
   {
      this.longAttribute = longAttribute;
   }

   public long getLongAttribute()
   {
      return longAttribute;
   }

   public void setShortAttribute(short shortAttribute)
   {
      this.shortAttribute = shortAttribute;
   }

   public short getShortAttribute()
   {
      return shortAttribute;
   }

   public void setStringAttribute(String stringAttribute)
   {
      this.stringAttribute = stringAttribute;
   }

   public String getStringAttribute()
   {
      return stringAttribute;
   }

   public void setDateAttribute(Date dateAttribute)
   {
      this.dateAttribute = dateAttribute;
   }

   public Date getDateAttribute()
   {
      return dateAttribute;
   }

   public void setStringBuilderAttribute(StringBuilder stringBuilderAttribute)
   {
      this.stringBuilderAttribute = stringBuilderAttribute;
   }

   public StringBuilder getStringBuilderAttribute()
   {
      return stringBuilderAttribute;
   }

   public void setUrlAttribute(URL urlAttribute)
   {
      this.urlAttribute = urlAttribute;
   }

   public URL getUrlAttribute()
   {
      return urlAttribute;
   }

   public void setArrayListAttribute(ArrayList<String> arrayListAttribute)
   {
      this.arrayListAttribute = arrayListAttribute;
   }

   public ArrayList<String> getArrayListAttribute()
   {
      return arrayListAttribute;
   }

   public void setHashMapAttribute(HashMap<String, String> hashMapAttribute)
   {
      this.hashMapAttribute = hashMapAttribute;
   }

   public HashMap<String, String> getHashMapAttribute()
   {
      return hashMapAttribute;
   }
}
