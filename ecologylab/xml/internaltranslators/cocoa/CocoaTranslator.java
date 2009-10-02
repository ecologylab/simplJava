package ecologylab.xml.internaltranslators.cocoa;

import java.io.IOException;

import ecologylab.generic.HashMapArrayList;
import ecologylab.xml.ElementState;
import ecologylab.xml.FieldAccessor;
import ecologylab.xml.Optimizations;
import ecologylab.xml.XMLTools;
import ecologylab.xml.internaltranslators.cocoa.library.CocaTestClass;

public class CocoaTranslator
{
   public CocoaTranslator()
   {

   }

   public void translateToObjC(Class<? extends ElementState> inputClass, Appendable appendable) throws Exception
   {
      HashMapArrayList<String, FieldAccessor> attributes = Optimizations.getFieldAccessors(inputClass);

      openHeaderFile(inputClass, appendable);

      if (attributes.size() > 0)
      {
         openFieldDeclartion(appendable);

         for (FieldAccessor fieldAccessor : attributes)
         {
            appendFieldAsObjectiveCAttribute(fieldAccessor, appendable);
         }

         closeFieldDeclartion(appendable);

         for (FieldAccessor fieldAccessor : attributes)
         {
            appendPropertyOfField(fieldAccessor, appendable);
         }
      }

      closeHeaderFile(appendable);
   }

   private void openHeaderFile(Class<? extends ElementState> inputClass, Appendable appendable) throws IOException
   {
      appendable.append(TranslationConstants.HEADER_FILE_OPENING);
      appendable.append(TranslationConstants.DOUBLE_LINE_BREAK);
      appendable.append(TranslationConstants.INTERFACE);
      appendable.append(TranslationConstants.SPACE);
      appendable.append(XMLTools.getClassName(inputClass));
      appendable.append(TranslationConstants.SPACE);
      appendable.append(TranslationConstants.INHERITENCE_OPERATOR);
      appendable.append(TranslationConstants.SPACE);
      appendable.append(TranslationConstants.INHERITENCE_OBJECT);
      appendable.append(TranslationConstants.SINGLE_LINE_BREAK);

   }

   private void closeHeaderFile(Appendable appendable) throws IOException
   {
      appendable.append(TranslationConstants.SINGLE_LINE_BREAK);
      appendable.append(TranslationConstants.END);
      appendable.append(TranslationConstants.SPACE);
   }

   private void openFieldDeclartion(Appendable appendable) throws IOException
   {
      appendable.append(TranslationConstants.OPENING_BRACE);
      appendable.append(TranslationConstants.SINGLE_LINE_BREAK);
   }

   private void closeFieldDeclartion(Appendable appendable) throws IOException
   {
      appendable.append(TranslationConstants.CLOSING_BRACE);
      appendable.append(TranslationConstants.DOUBLE_LINE_BREAK);
   }

   private void appendFieldAsObjectiveCAttribute(FieldAccessor fieldAccessor, Appendable appendable) throws Exception
   {
      if (fieldAccessor.getScalarType().isPrimitive() && fieldAccessor.getField().getType() != String.class)
      {
         appendFieldAsPrimitive(fieldAccessor, appendable);
      }
      else
      {
         appendFieldAsReference(fieldAccessor, appendable);
      }
   }

   private void appendPropertyOfField(FieldAccessor fieldAccessor, Appendable appendable) throws Exception
   {
      if (fieldAccessor.getScalarType().isPrimitive() && fieldAccessor.getField().getType() != String.class)
      {
         appendPropoertyAsPrimitive(fieldAccessor, appendable);
      }
      else
      {
         appendPropertyAsReference(fieldAccessor, appendable);
      }
   }

   private void appendFieldAsReference(FieldAccessor fieldAccessor, Appendable appendable) throws Exception
   {
      StringBuilder fieldDeclaration = new StringBuilder();

      fieldDeclaration.append(TranslationConstants.TAB);
      fieldDeclaration.append(TranslationUtilities.getObjectiveCType(fieldAccessor.getField().getType()));
      fieldDeclaration.append(TranslationConstants.SPACE);
      fieldDeclaration.append(TranslationConstants.REFERENCE);
      fieldDeclaration.append(fieldAccessor.getFieldName());
      fieldDeclaration.append(TranslationConstants.TERMINATOR);
      fieldDeclaration.append(TranslationConstants.SINGLE_LINE_BREAK);

      appendable.append(fieldDeclaration);
   }

   private void appendFieldAsPrimitive(FieldAccessor fieldAccessor, Appendable appendable) throws Exception
   {
      StringBuilder fieldDeclaration = new StringBuilder();

      fieldDeclaration.append(TranslationConstants.TAB);
      fieldDeclaration.append(TranslationUtilities.getObjectiveCType(fieldAccessor.getField().getType()));
      fieldDeclaration.append(TranslationConstants.SPACE);
      fieldDeclaration.append(fieldAccessor.getFieldName());
      fieldDeclaration.append(TranslationConstants.TERMINATOR);
      fieldDeclaration.append(TranslationConstants.SINGLE_LINE_BREAK);

      appendable.append(fieldDeclaration);
   }

   private void appendPropertyAsReference(FieldAccessor fieldAccessor, Appendable appendable) throws Exception
   {
      StringBuilder propertyDeclaration = new StringBuilder();

      propertyDeclaration.append(TranslationConstants.PROPERTY);
      propertyDeclaration.append(TranslationConstants.SPACE);
      propertyDeclaration.append(TranslationUtilities.getObjectiveCType(fieldAccessor.getField().getType()));
      propertyDeclaration.append(TranslationConstants.SPACE);
      propertyDeclaration.append(fieldAccessor.getFieldName());
      propertyDeclaration.append(TranslationConstants.TERMINATOR);
      propertyDeclaration.append(TranslationConstants.SINGLE_LINE_BREAK);

      appendable.append(propertyDeclaration);
   }

   private void appendPropoertyAsPrimitive(FieldAccessor fieldAccessor, Appendable appendable) throws Exception
   {
      StringBuilder propertyDeclaration = new StringBuilder();

      propertyDeclaration.append(TranslationConstants.PROPERTY);
      propertyDeclaration.append(TranslationConstants.SPACE);
      propertyDeclaration.append(TranslationUtilities.getObjectiveCType(fieldAccessor.getField().getType()));
      propertyDeclaration.append(TranslationConstants.SPACE);
      propertyDeclaration.append(fieldAccessor.getFieldName());
      propertyDeclaration.append(TranslationConstants.TERMINATOR);
      propertyDeclaration.append(TranslationConstants.SINGLE_LINE_BREAK);

      appendable.append(propertyDeclaration);
   }

   public static void main(String args[]) throws Exception
   {
      CocoaTranslator c = new CocoaTranslator();
      c.translateToObjC(CocaTestClass.class, System.out);
   }
}
