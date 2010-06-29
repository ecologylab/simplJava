package ecologylab.standalone.sqltranslator;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.Test;
import static org.junit.Assert.*;

import ecologylab.generic.HashMapArrayList;
import ecologylab.standalone.sqltranslator.input.Channel;
import ecologylab.standalone.sqltranslator.input.Item;
import ecologylab.standalone.sqltranslator.input.RssState;
import ecologylab.xml.ClassDescriptor;
import ecologylab.xml.ElementState;
import ecologylab.xml.FieldDescriptor;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.ElementState.xml_collection;

public class SqlTranslatorMain extends SqlTranslator{ 
	
	public SqlTranslatorMain() throws XMLTranslationException{
		super();
	}
	
	/*
	 * this class automatically select candidate classes for defining composite type
	 */
	public void createSQLTableCompositeTypeSchema(TranslationScope translationScope) throws IOException{
		/*routine for checking intersection between targetClassesName(Collection) and typeNames(Collection)*/
		Class<? extends ElementState>[] thisTargetCompositeTypeClasses = getCompositeTypeClasses(translationScope); 
				
		/*new translationscope based on derived target composite class*/ 
		TranslationScope thisNewTranslationScope = TranslationScope.get("newTranslationScope", thisTargetCompositeTypeClasses);
		
		/*call main function*/ 
		this.createSQLTableSchema(thisNewTranslationScope, DEFAULT_COMPOSITE_TYPE_TABLE_MODE);
		
		
	}
	
	@Test
	public void testCreateSQLTableCompositeTypeSchema() throws IOException{
		/*Channel composite should be generated*/
		TranslationScope thisTranslationScope = TranslationScope.get("thisTranslationScope", Channel.class, Item.class, RssState.class);
		createSQLTableCompositeTypeSchema(thisTranslationScope);
		
	}
	
	/*
	 * main method for deriving composite type definition  
	 */
	private Class<? extends ElementState>[] getCompositeTypeClasses(TranslationScope translationScope) {
		HashSet<String> thisClassNameSet = new HashSet<String>();
		HashSet<String> thisTypeSet = new HashSet<String>();
		
		HashSet<Class<? extends ElementState>> thisResultClassesSet = new HashSet<Class<? extends ElementState>>();
		
		/*1) collect class name*/ 
		ArrayList<Class<? extends ElementState>> thisAllClasses = translationScope.getAllClasses();
		for (Class<? extends ElementState> thisClass : thisAllClasses) {
			thisClassNameSet.add(thisClass.getSimpleName());  
		}
		
		/*2) collect type name*/
		Collection<ClassDescriptor> thisClassDescriptor = translationScope.getClassDescriptors();
		for (ClassDescriptor classDescriptor : thisClassDescriptor) {
			HashMapArrayList thisFieldDescriptors = classDescriptor.getFieldDescriptorsByFieldName();
			for (Object object : thisFieldDescriptors) {
				FieldDescriptor thisFieldDescriptor = (FieldDescriptor)object;
				
				String thisFieldType = thisFieldDescriptor.getFieldType().getSimpleName(); 
				if(thisFieldType.equalsIgnoreCase("ArrayList"))
					/*extract Collection type e.g. 'Item' of ArrayList[Item]*/
					thisTypeSet.add(this.getFieldTypeFromGenericFieldType(thisFieldDescriptor)); 
				else
					thisTypeSet.add(thisFieldType); 
			}
		}
		
		/*get intersection of 1) and 2)*/ 
		if (thisClassNameSet.retainAll(thisTypeSet)){
			for (String	name : thisClassNameSet) {
//				System.out.println("intersect - " + name);
				thisResultClassesSet.add(translationScope.getClassBySimpleName(name));
				
			}
		}
		
		/*Return type conversion cf. hashSet -> Class<? extends ElementState>[]*/
		Class<? extends ElementState>[] thisResultClassesArray = new Class[thisResultClassesSet.size()];
		
		int i = 0 ; 
		for (Class<? extends ElementState> thisClass : thisResultClassesSet) {
			thisResultClassesArray[i++] = thisClass; 
		}
		
		
		return thisResultClassesArray; 
		
	}
		
	
	public void createSQLTableSchema(TranslationScope translationScope, int mode) throws IOException{
		/*set mode*/ 
		super.setDB_SCHEMA_GENERATOR_MODE(mode); 
		
		TranslationScope thisTranslationScope = null;
		if (mode == DEFAULT_CREATE_TABLE_MODE){
			thisTranslationScope = translationScope;
			
		}else if(mode == DEFAULT_COMPOSITE_TYPE_TABLE_MODE){
			/*routine for checking intersection between targetClassesName(Collection) and typeNames(Collection)*/
			Class<? extends ElementState>[] thisTargetCompositeTypeClasses = this.getCompositeTypeClasses(translationScope); 
					
			/*new translationscope based on derived target composite class*/ 
			thisTranslationScope = TranslationScope.get("newTranslationScope", thisTargetCompositeTypeClasses);
			
		}	
			
		Collection<ClassDescriptor> thisClassDescriptors = thisTranslationScope.getClassDescriptors();
		for (ClassDescriptor classDescriptor : thisClassDescriptors) {
			HashMapArrayList thisFieldDescriptors = classDescriptor.getFieldDescriptorsByFieldName();
			
			for (Iterator iterator = thisFieldDescriptors.iterator(); iterator.hasNext();) {
				FieldDescriptor fieldDescriptor = (FieldDescriptor) iterator.next(); 
				Annotation[] thisAnnotations = fieldDescriptor.getField().getAnnotations();
				/*extract only first annotation*/
				/*
				 * TODO elaborate annotation information 
				 */
				Annotation annotation = thisAnnotations[0];
				
				this.createTableArrayListForMultiAttributes(classDescriptor, fieldDescriptor, annotation);
				/*in case of considering multiple annotations*/ 
//				for (Annotation annotation : thisAnnotations) {
//					createTableArrayListForMultiAttributes(classDescriptor, fieldDescriptor, annotation);
//				}

			}
		}
		
		if(mode == DEFAULT_CREATE_TABLE_MODE){
			assertNotNull(super.thisHashMapTableArrayList);
			super.createMMTableSQLFileFromHashMapArrayList(DBName.POSTGRESQL);
			System.out.println(super.createSQLStringFromHashMapArrayList(super.thisHashMapTableArrayList));
			
		}else if(mode == DEFAULT_COMPOSITE_TYPE_TABLE_MODE){
			assertNotNull(super.thisHashMapTableArrayListForCompositeType);
			super.createMMTableSQLFileFromHashMapArrayList(DBName.POSTGRESQL);
			System.out.println(super.createSQLStringFromHashMapArrayList(super.thisHashMapTableArrayListForCompositeType));
			
		}
		
	}
	
	@Test
	public void testCreateSQLTableSchema() throws IOException{
		
		TranslationScope thisTranslationScope = TranslationScope.get("thisTranslationScope", RssState.class, Item.class, Channel.class);
		createSQLTableSchema(thisTranslationScope, DEFAULT_CREATE_TABLE_MODE);
		createSQLTableSchema(thisTranslationScope, DEFAULT_COMPOSITE_TYPE_TABLE_MODE);
		
	}
	
	
	/*
	 * for composite type 
	 */
	@Test
	public void testTranslationScope(){
		TranslationScope thisTranslationScope = TranslationScope.get("thisTranslationScope",Channel.class, Item.class, RssState.class );
		ArrayList<Class<? extends ElementState>> thisAllClasses = thisTranslationScope.getAllClasses();
		for (Class<? extends ElementState> thisClass : thisAllClasses) {
//			System.out.println(thisClass.getSimpleName()); 
		}		
		
		String thisString = "Channel";
		Class<? extends ElementState> thisClass = thisTranslationScope.getClassBySimpleName(thisString);
		assertNotNull(thisClass);
//		System.out.println("tag: " + thisTranslationScope.getTagBySimpleName(thisString));
		
		ClassDescriptor thisClassDescriptor = ClassDescriptor.getClassDescriptor(thisClass);
		HashMapArrayList thisFieldDescriptors = thisClassDescriptor.getFieldDescriptorsByFieldName();
		for (Object object : thisFieldDescriptors) {
			FieldDescriptor thisFieldDescriptor = (FieldDescriptor)object;
			Class<?> thisFieldType = thisFieldDescriptor.getFieldType();
			String thisFieldName = thisFieldDescriptor.getFieldName();
			
//			System.out.println("className : " + thisClassDescriptor.getDecribedClassSimpleName() + " fieldType - " + thisFieldType.getSimpleName()); 
			System.out.println("\n(name) - " + thisFieldName);
			System.out.println("(fieldType) : " + thisFieldType.getCanonicalName() 
					+ "\n (simpleTypeName) : " + thisFieldType.getSimpleName()  
					/*value of @xml_collection("item")*/ 
					/* 
					 * TODO inspect code below deriving value from @xml_collection
					 */
					+ "\n (ColectionOrMapTagName) : " + thisFieldDescriptor.getCollectionOrMapTagName());
			
			/****************************************
			 * thisFieldDescriptor.getScalarType()
			 * - return null if not primitive type e.g. ArrayList<Item>
			 * - null check and return if not primitive 
			 * TODO get container field type e.g. ArrayList<Item> items; 
			 ****************************************/ 
			/*in case of not defined scalarType, just return null e.g. ArrayList<Item>*/ 
			System.out.println("(scalarType) : " + thisFieldDescriptor.getScalarType());  
			System.out.println("(GenericString) - " + thisFieldDescriptor.getField().toGenericString()); 
			System.out.println("(Field().toString() - " + thisFieldDescriptor.getField().toString());
			/*java.util.ArrayList<ecologylab.xml.tools.sqlTranslator.input.Item>*/ 
			System.out.println("(GenericType) - " + thisFieldDescriptor.getField().getGenericType());
			
			/*get collection value*/ 
			xml_collection thisCollectionValue = thisFieldDescriptor.getField().getAnnotation(ElementState.xml_collection.class);
			if (thisCollectionValue != null)
				System.out.println("(@xml_collection value) " + thisCollectionValue.value());
			
			Annotation[] thisAnnotation = thisFieldDescriptor.getField().getAnnotations();
			for (Annotation annotation : thisAnnotation) {
				System.out.println("(annotation) - "  + annotation.annotationType().getSimpleName());
				
			}
			
		}
	}
	
	/*
	 * method to extract FieldType from Generic Type expression
	 * e.g. 'ParsedURL' of class ecologylab.net.ParsedURL, or 'Item' of java.util.ArrayList<ecologylab.xml.tools.sqlTranslator.input.Item>
	 */
	public String getFieldTypeFromGenericFieldType(FieldDescriptor thisFieldDescriptor){
		Type thisGenericTypeExpression = thisFieldDescriptor.getField().getGenericType();
		String thisReplacedString = thisGenericTypeExpression.toString().replaceAll("[^A-Za-z0-9]", " ");
		String[] thisSplittedString = thisReplacedString.split(" ");
		
		return thisSplittedString[thisSplittedString.length - 1];
		
	}
	
	@Test
	public void testGetFieldTypeFromGenericType(){
		/*java.util.ArrayList<ecologylab.xml.tools.sqlTranslator.input.Item>*/ 
		String thisString = "java.util.ArrayList<ecologylab.xml.tools.sqlTranslator.input.Item>";
		String thisString2 = "class ecologylab.net.ParsedURL"; 
		
		String thisReplacedString = thisString2.replaceAll("[^A-Za-z0-9]", " ");
		String[] thisSplittedString = thisReplacedString.split(" ");
	
		String thisFieldTypeExtracted = thisSplittedString[thisSplittedString.length -1];
		System.out.println(thisFieldTypeExtracted);
		
	}
	
		
	
	private void createTableArrayListForMultiAttributes(ClassDescriptor classDescriptor, FieldDescriptor fieldDescriptor, Annotation annotation) {
		String tableName = classDescriptor.getDecribedClassSimpleName(); 
		String tableExtend = new String("null"); 
		String tableComment = classDescriptor.toString();
		
		String tableNameForMultiAttributes = tableName + "#" + tableExtend + "#" + tableComment;
		
		/*FieldName(key of hashMap)*/
		String fieldName = fieldDescriptor.getFieldName();
		String fieldType = fieldDescriptor.getFieldType().getSimpleName();
		
		/*added for handling collection field, default 'null' e.g. 'Item' of ArrayList[Item]*/
		String fieldCollectionType = "null";
		/*check if fieldType == ArrayList*/
		if(fieldType.equalsIgnoreCase("ArrayList"))	
			fieldCollectionType = this.getFieldTypeFromGenericFieldType(fieldDescriptor);

		String fieldComment = annotation.annotationType().getName();
		
		String fieldForMultiAttributes = fieldType + "#" + fieldComment + "#" + fieldCollectionType;
		
		super.createMMTableArrayList(tableNameForMultiAttributes, fieldName, fieldForMultiAttributes);
		
	}
	
	
	
	public static void main(String[] args) throws XMLTranslationException, IOException {
		SqlTranslatorMain thisSqlTranslator = new SqlTranslatorMain();   
		
		TranslationScope thisTranslationScope = TranslationScope.get("thisTranslationScope", RssState.class, Item.class, Channel.class);
		thisSqlTranslator.createSQLTableSchema(thisTranslationScope, DEFAULT_CREATE_TABLE_MODE);
		thisSqlTranslator.createSQLTableSchema(thisTranslationScope, DEFAULT_COMPOSITE_TYPE_TABLE_MODE);
		
	}
	
}
