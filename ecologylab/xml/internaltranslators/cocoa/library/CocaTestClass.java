package ecologylab.xml.internaltranslators.cocoa.library;

import java.util.ArrayList;
import java.util.Date;

import ecologylab.xml.ElementState;
import ecologylab.xml.XMLTranslationException;

import ecologylab.xml.xml_inherit;

public @xml_inherit class CocaTestClass extends ElementState
{
   @xml_attribute
   protected int    myAttribute1;

   @xml_attribute
   protected String myAttribute2;
   
   @xml_attribute
   protected Date myAttribute3;
   
   @xml_collection 
   protected ArrayList<String> integerSet;

   public CocaTestClass()
   {
      myAttribute2 = "";
      myAttribute1 = 123;      
      integerSet = new ArrayList<String>();
      
      integerSet.add("!");
      integerSet.add("!");
      integerSet.add("!");
      integerSet.add("!");
      
      myAttribute3 = new Date();
      
   }

   public void setMyAttribute(int myAttribute)
   {
      this.myAttribute1 = myAttribute;
   }

   public int getMyAttribute()
   {
      return myAttribute1;
   }

   public void setMyAttribute2(String myAttribute2)
   {
      this.myAttribute2 = myAttribute2;
   }

   public String getMyAttribute2()
   {
      return myAttribute2;
   }
   
   public static void main(String args[]) throws XMLTranslationException{
      CocaTestClass test = new CocaTestClass();
      test.translateToXML(System.out);
   }
}
