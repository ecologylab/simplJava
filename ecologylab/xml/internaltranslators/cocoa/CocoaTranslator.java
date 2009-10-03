package ecologylab.xml.internaltranslators.cocoa;

import java.io.IOException;
import java.util.ArrayList;
import ecologylab.generic.Debug;
import ecologylab.generic.HashMapArrayList;
import ecologylab.xml.ElementState;
import ecologylab.xml.FieldAccessor;
import ecologylab.xml.Optimizations;
import ecologylab.xml.XMLTools;
import ecologylab.xml.internaltranslators.cocoa.library.CocoaInheritTest;

/**
 * This class is the main class which provides the functionality of translation of Java 
 * classes into the objective class header files.
 * 
 * <p>It uses the same syntactical annotations used the by {@code ecologylab.xml} to translate
 * Java objects into xml files. Since it uses the same annotations the data types supported for translation
 * are also the same. The entry point functions into the class are.
 * <ul>
 *  <li>{@code translateToObjC(Class<? extends ElementState>, Appendable)}</li>
 * </ul>
 * </p>
 * @author Nabeel Shahzad  
 * @version 1.0
 */
public class CocoaTranslator
{
   /**
    * Using this internal class for the calling the hook method after translation of a Java class to C
    * This basically used for <code>@xml_nested</code> attribute from the <code>ecologylab.xml</code>
    * When ever we find an <code>@xml_nested</code> attribute we want to generate the Objective-C header 
    * file for the nested class as well 
    * 
    * @author Nabeel Shahzad       
    */
   private class NestedTranslationHook
   {
      /**
       * Class on which this class will fire a hook method to generate Objective-C class.        
       */
      private Class<?>   inputClass;

      /**
       * The appendable object on which the hook method will write the generated code.        
       */
      private Appendable appendable;

      /**
       * Constructor method. Takes the <code>Class</code> for which it will generate the Objective-C header file
       * and also the <code>Appendable</code> object on which it will append the generated code.
       * 
       * @param inputClass
       * @param appendable
       */
      public NestedTranslationHook(Class<?> inputClass, Appendable appendable)
      {
         this.inputClass = inputClass;
         this.appendable = appendable;
      }

      
      /**
       * The main hook method. It simple instantiates and object of the <code>CocoaTranslator</code>and calls
       * the entry point method of {@code translateToObjC(Class<?extends ElementState>, Appendable)} on
       * the already populated member fields.
       *  
       * @throws Exception
       */
      public void postGenerationOperation() throws Exception
      {
         CocoaTranslator ct = new CocoaTranslator();
         ct.translateToObjC((Class<? extends ElementState>) inputClass, appendable);
      }
   }

   private ArrayList<NestedTranslationHook> nestedTranslationHooks;

   /**
    * Constructor method <p> Initializes the {@code nestedTranslationHooks} member of the class </p>
    */
   public CocoaTranslator()
   {
      nestedTranslationHooks = new ArrayList<NestedTranslationHook>();
   }

   /**
    * TODO: add comments
    * 
    * @param inputClass
    * @param appendable
    * @throws Exception
    */
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

      for (NestedTranslationHook nestedTranslationHook : nestedTranslationHooks)
      {
         nestedTranslationHook.postGenerationOperation();
      }

   }

   /**
    * TODO: add comments
    * 
    * @param inputClass
    * @param appendable
    * @throws IOException
    */
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

   /**
    * TODO: add comments
    * 
    * @param appendable
    * @throws IOException
    */
   private void closeHeaderFile(Appendable appendable) throws IOException
   {
      appendable.append(TranslationConstants.SINGLE_LINE_BREAK);
      appendable.append(TranslationConstants.END);
      appendable.append(TranslationConstants.DOUBLE_LINE_BREAK);
   }

   /**
    * TODO: add comments
    * 
    * @param appendable
    * @throws IOException
    */
   private void openFieldDeclartion(Appendable appendable) throws IOException
   {
      appendable.append(TranslationConstants.OPENING_BRACE);
      appendable.append(TranslationConstants.SINGLE_LINE_BREAK);
   }

   /**
    * TODO: add comments
    * 
    * @param appendable
    * @throws IOException
    */
   private void closeFieldDeclartion(Appendable appendable) throws IOException
   {
      appendable.append(TranslationConstants.CLOSING_BRACE);
      appendable.append(TranslationConstants.DOUBLE_LINE_BREAK);
   }

   /**
    * TODO: add comments
    * 
    * @param fieldAccessor
    * @param appendable
    * @throws Exception
    */
   private void appendFieldAsObjectiveCAttribute(FieldAccessor fieldAccessor, Appendable appendable) throws Exception
   {
      if (fieldAccessor.isCollection())
      {
         appendFieldAsReference(fieldAccessor, appendable);
      }
      else if (fieldAccessor.isScalar())
      {
         if (fieldAccessor.getScalarType().isPrimitive() && fieldAccessor.getField().getType() != String.class)
         {
            appendFieldAsPrimitive(fieldAccessor, appendable);
         }
         else if (fieldAccessor.getScalarType().isReference() || fieldAccessor.getField().getType() == String.class)
         {
            appendFieldAsReference(fieldAccessor, appendable);
         }
      }
      else if (fieldAccessor.isNested())
      {
         appendFieldAsNestedAttribute(fieldAccessor, appendable);

         NestedTranslationHook nestedTranslationHook = new NestedTranslationHook(fieldAccessor.getFieldType(), appendable);
         nestedTranslationHooks.add(nestedTranslationHook);
      }
   }

   /**
    * TODO: add comments
    * 
    * @param fieldAccessor
    * @param appendable
    * @throws Exception
    */
   private void appendPropertyOfField(FieldAccessor fieldAccessor, Appendable appendable) throws Exception
   {
      if (fieldAccessor.isCollection())
      {
         appendPropertyAsReference(fieldAccessor, appendable);
      }
      else if (fieldAccessor.isScalar())
      {
         if (fieldAccessor.getScalarType().isPrimitive() && fieldAccessor.getField().getType() != String.class)
         {
            appendPropoertyAsPrimitive(fieldAccessor, appendable);
         }
         else if (fieldAccessor.getScalarType().isReference() || fieldAccessor.getField().getType() == String.class)
         {
            appendPropertyAsReference(fieldAccessor, appendable);
         }
      }
      else if (fieldAccessor.isNested())
      {
         // nothing needs to be done here since hooks are already added.
      }
   }

   /**
    * TODO: add comments
    * 
    * @param fieldAccessor
    * @param appendable
    * @throws Exception
    */
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

   /**
    * TODO: add comments
    * 
    * @param fieldAccessor
    * @param appendable
    * @throws Exception
    */
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

   /**
    * TODO: add comments
    * 
    * @param fieldAccessor
    * @param appendable
    * @throws Exception
    */
   private void appendFieldAsNestedAttribute(FieldAccessor fieldAccessor, Appendable appendable) throws Exception
   {
      StringBuilder fieldDeclaration = new StringBuilder();

      fieldDeclaration.append(TranslationConstants.TAB);
      fieldDeclaration.append(Debug.classSimpleName(fieldAccessor.getFieldType()));
      fieldDeclaration.append(TranslationConstants.SPACE);
      fieldDeclaration.append(TranslationConstants.REFERENCE);
      fieldDeclaration.append(fieldAccessor.getFieldName());
      fieldDeclaration.append(TranslationConstants.TERMINATOR);
      fieldDeclaration.append(TranslationConstants.SINGLE_LINE_BREAK);

      appendable.append(fieldDeclaration);
   }

   /**
    * TODO: add comments
    * 
    * @param fieldAccessor
    * @param appendable
    * @throws Exception
    */
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

   /**
    * TODO: add comments
    * 
    * @param fieldAccessor
    * @param appendable
    * @throws Exception
    */
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
      c.translateToObjC(CocoaInheritTest.class, System.out);
   }
}
