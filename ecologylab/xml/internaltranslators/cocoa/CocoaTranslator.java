package ecologylab.xml.internaltranslators.cocoa;

import java.io.IOException;
import java.lang.reflect.Field;

import ecologylab.xml.*;
import ecologylab.generic.HashMapArrayList;
import ecologylab.xml.internaltranslators.cocoa.library.*;

public class CocoaTranslator
{
   public CocoaTranslator()
   {

   }

   public void translateToObjC(Class<? extends ElementState> inputClass, Appendable appendable) throws Exception
   {
      openObjectiveHeaderFile(inputClass, appendable);

      HashMapArrayList<String, FieldAccessor> attributes = Optimizations.getFieldAccessors(inputClass);

      for (FieldAccessor fieldAccessor : attributes)
      {
         if (fieldAccessor.isScalar())
         {
            appendFieldAsObjectiveCAttribute(fieldAccessor.getField(), appendable);
         }
         
         if (fieldAccessor.isCollection())
         {
            System.out.println("This is the xml collection " + fieldAccessor.getFieldName() );
         }       
      }

      closeFieldDeclartion(appendable);

      for (FieldAccessor fieldAccessor : attributes)
      {
         if (fieldAccessor.isScalar())
         {
            appendPropertyOfField(fieldAccessor.getField(), appendable);
         }
      }

      closeObjectiveHeaderFile(appendable);

   }

   private void openObjectiveHeaderFile(Class<? extends ElementState> inputClass, Appendable appendable) throws IOException
   {
      appendable.append(TranslationConstants.HEADER_FILE_OPENING);
      appendable.append(TranslationConstants.LINE_BREAK);

      String implementationDeclaration = "@interface " + XMLTools.getClassName(inputClass) + " :" + " Object" + "\n{\n";
      appendable.append(implementationDeclaration);

   }

   private void closeObjectiveHeaderFile(Appendable appendable) throws IOException
   {
      appendable.append("\n@end");
   }

   private void closeFieldDeclartion(Appendable appendable) throws IOException
   {
      appendable.append("}\n\n");
   }

   private void appendFieldAsObjectiveCAttribute(Field field, Appendable appendable) throws Exception
   {
      String fieldDeclaration = "\t" + TranslationUtilities.getObjectiveCType(field.getType()) + " " + field.getName() + ";\n";
      appendable.append(fieldDeclaration);
   }

   private void appendPropertyOfField(Field field, Appendable appendable) throws Exception
   {
      String propertyDeclaration = "@property " + TranslationUtilities.getObjectiveCType(field.getType()) + " " + field.getName() + ";\n";
      appendable.append(propertyDeclaration);
   }

  

   public static void main(String args[]) throws Exception
   {
      CocoaTranslator c = new CocoaTranslator();
      c.translateToObjC(CocaTestClass.class, System.out);
   }
}
