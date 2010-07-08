package translators.sql.java2sql;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;

import translators.sql.testing.ecologylabXmlTest.ChannelTest;
import translators.sql.testing.ecologylabXmlTest.ItemTest;
import translators.sql.testing.ecologylabXmlTest.PdfTest;
import translators.sql.testing.ecologylabXmlTest.RssStateTest;
import ecologylab.generic.HashMapArrayList;
import ecologylab.semantics.generated.library.GeneratedMetadataTranslationScope;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.ElementState.DbHint;
import ecologylab.serialization.ElementState.simpl_collection;
import ecologylab.serialization.ElementState.simpl_db;
import ecologylab.serialization.library.rss.Channel;
import ecologylab.serialization.library.rss.Item;
import ecologylab.serialization.library.rss.RssState;

public class SqlTranslatorMain extends SqlTranslator
{

	/*
	 * Default constructor
	 */
	public SqlTranslatorMain() throws SIMPLTranslationException
	{
		super();

		/* set outputFileName by date and time */
		SimpleDateFormat thisDateFormat = new SimpleDateFormat("MM-dd_'at'_HH_mm_ss");
		String thisOutputFileName = thisDateFormat.format(Calendar.getInstance().getTime()) + ".sql";

		super.setDEFAULT_SQL_FILE_NAME(thisOutputFileName);
	}

	/*
	 * Overloaded constructor
	 */
	public SqlTranslatorMain(String outputFileName) throws SIMPLTranslationException
	{
		super();
		super.setDEFAULT_SQL_FILE_NAME(outputFileName);

	}

	/*
	 * this class automatically select candidate classes for defining composite type
	 */
	public void createSQLTableCompositeTypeSchema(TranslationScope translationScope)
			throws IOException
	{
		/*
		 * routine for checking intersection between targetClassesName(Collection) and
		 * typeNames(Collection)
		 */
		Class<? extends ElementState>[] thisTargetCompositeTypeClasses = getCompositeTypeClasses(translationScope);

		/* new translationscope based on derived target composite class */
		TranslationScope thisNewTranslationScope = TranslationScope.get("newTranslationScope",
				thisTargetCompositeTypeClasses);

		/* call main function */
		this.createSQLTableSchema(thisNewTranslationScope, DEFAULT_COMPOSITE_TYPE_TABLE_MODE);

	}

	@Test
	public void testCreateSQLTableCompositeTypeSchema() throws IOException
	{
		/* Channel composite should be generated */
		TranslationScope thisTranslationScope = TranslationScope.get("thisTranslationScope",
				Channel.class, Item.class, RssState.class);
		createSQLTableCompositeTypeSchema(thisTranslationScope);

	}

	/*
	 * core function for deriving composite type definition
	 */
	private Class<? extends ElementState>[] getCompositeTypeClasses(TranslationScope translationScope)
	{
		HashSet<String> thisClassNameSet = new HashSet<String>();
		HashSet<String> thisTypeSet = new HashSet<String>();

		HashSet<Class<? extends ElementState>> thisResultClassesSet = new HashSet<Class<? extends ElementState>>();

		/*
		 * Step 1) collect class name
		 */
		ArrayList<Class<? extends ElementState>> thisAllClasses = translationScope.getAllClasses();
		for (Class<? extends ElementState> thisClass : thisAllClasses)
		{
			thisClassNameSet.add(thisClass.getSimpleName());
		}

		/*
		 * Step 2) collect type name
		 */
		Collection<ClassDescriptor> thisClassDescriptor = translationScope.getClassDescriptors();
		for (ClassDescriptor classDescriptor : thisClassDescriptor)
		{
			HashMapArrayList thisFieldDescriptors = classDescriptor.getFieldDescriptorsByFieldName();
			for (Object object : thisFieldDescriptors)
			{
				FieldDescriptor thisFieldDescriptor = (FieldDescriptor) object;

				String thisFieldType = thisFieldDescriptor.getFieldType().getSimpleName();
				if (thisFieldType.equalsIgnoreCase("ArrayList"))
					/* extract Collection type e.g. 'Item' of ArrayList[Item] */
					thisTypeSet.add(this.getFieldTypeFromGenericFieldType(thisFieldDescriptor));
				else
					thisTypeSet.add(thisFieldType);
			}
		}

		/*
		 * Step 3) get intersection of 1) and 2)
		 */
		if (thisClassNameSet.retainAll(thisTypeSet))
		{
			for (String name : thisClassNameSet)
			{
				// System.out.println("intersect - " + name);
				thisResultClassesSet.add(translationScope.getClassBySimpleName(name));

			}
		}

		/*
		 * Step 4) type conversion cf. hashSet -> Class<? extends ElementState>[]
		 */
		Class<? extends ElementState>[] thisResultClassesArray = new Class[thisResultClassesSet.size()];

		int i = 0;
		for (Class<? extends ElementState> thisClass : thisResultClassesSet)
		{
			thisResultClassesArray[i++] = thisClass;
		}

		return thisResultClassesArray;

	}

	/**
	 * Create HashMapTable ArrayList, which contains table schema information (className, field, and
	 * annotation, etc.)
	 * 
	 * @param translationScope
	 * @param mode
	 * @throws IOException
	 */
	public void createSQLTableSchema(TranslationScope translationScope, int mode) throws IOException
	{
		/* set mode */
		super.setDB_SCHEMA_GENERATOR_MODE(mode);

		TranslationScope thisTranslationScope = null;
		if (mode == DEFAULT_CREATE_TABLE_MODE)
		{
			thisTranslationScope = translationScope;

		}
		else if (mode == DEFAULT_COMPOSITE_TYPE_TABLE_MODE)
		{
			/**
			 * core routine for extracting intersection between targetClassesName(Collection) and
			 * typeNames(Collection) e.g. 'Item' is derived if Item(table) ArrayList[Item]
			 */
			Class<? extends ElementState>[] thisTargetCompositeTypeClasses = this
					.getCompositeTypeClasses(translationScope);

			/* new translationscope based on derived target composite class */
			thisTranslationScope = TranslationScope.get("newTranslationScope",
					thisTargetCompositeTypeClasses);

		}

		Collection<ClassDescriptor> classDescriptors = thisTranslationScope.getClassDescriptors();
		for (ClassDescriptor thisClassDescriptor : classDescriptors)
		{
			/* 1) class descriptor - thisClassDescriptor(assuming className=tableName) */

			/* 2) fields */
			Field[] fields = thisClassDescriptor.getDescribedClass().getDeclaredFields();

			/* 3) call newly defined method */
			this.createTableArrayListForMultiAttributes(thisClassDescriptor, fields);

		}

		if (mode == DEFAULT_CREATE_TABLE_MODE)
		{
			assertNotNull(super.thisHashMapTableArrayList);
			super.createMMTableSQLFileFromHashMapArrayList(DBName.POSTGRESQL);
			System.out.println(super
					.createSQLStringFromHashMapArrayListForDBConstraint(super.thisHashMapTableArrayList));

		}
		else if (mode == DEFAULT_COMPOSITE_TYPE_TABLE_MODE)
		{
			assertNotNull(super.thisHashMapTableArrayListForCompositeType);
			super.createMMTableSQLFileFromHashMapArrayList(DBName.POSTGRESQL);
			System.out
					.println(super
							.createSQLStringFromHashMapArrayListForDBConstraint(super.thisHashMapTableArrayListForCompositeType));

		}

	}

	@Test
	public void testCreateSQLTableSchema() throws IOException
	{

		TranslationScope thisTranslationScope = TranslationScope.get("thisTranslationScope",
				RssStateTest.class, ItemTest.class, ChannelTest.class, PdfTest.class);
		
		/*test case for GeneratedMetadataTranslationScope*/ 
		thisTranslationScope = GeneratedMetadataTranslationScope.get(); 
		Collection<ClassDescriptor> thisClassDescriptor = thisTranslationScope.getClassDescriptors();
		for (ClassDescriptor classDescriptor : thisClassDescriptor)
		{
			System.out.println(classDescriptor.getDecribedClassSimpleName());  
			
		}
		
		createSQLTableSchema(thisTranslationScope, DEFAULT_CREATE_TABLE_MODE);
		createSQLTableSchema(thisTranslationScope, DEFAULT_COMPOSITE_TYPE_TABLE_MODE);

	}

	/*
	 * for composite type
	 */
	@Test
	public void testTranslationScope()
	{
		TranslationScope thisTranslationScope = TranslationScope.get("thisTranslationScope",
				ChannelTest.class, ItemTest.class, RssStateTest.class);
		ArrayList<Class<? extends ElementState>> thisAllClasses = thisTranslationScope.getAllClasses();
		for (Class<? extends ElementState> thisClass : thisAllClasses)
		{
			// System.out.println(thisClass.getSimpleName());
		}

		String thisString = "Channel_Test";
		Class<? extends ElementState> thisClass = thisTranslationScope.getClassBySimpleName(thisString);
		assertNotNull(thisClass);
		// System.out.println("tag: " + thisTranslationScope.getTagBySimpleName(thisString));

		ClassDescriptor thisClassDescriptor = ClassDescriptor.getClassDescriptor(thisClass);

		/**
		 * added for deriving values from new annotation
		 */
		Field[] fields = thisClassDescriptor.getDescribedClass().getDeclaredFields();

		for (Field field : fields)
		{
			System.out.println(field.getName() + " " + field.getType().getSimpleName());
			simpl_db simpdbAnnotation = field.getAnnotation(ElementState.simpl_db.class);
			simpl_collection thisXmlCollection = field.getAnnotation(ElementState.simpl_collection.class);

			if (simpdbAnnotation != null)
			{
				DbHint enumArray[] = simpdbAnnotation.value();
				for (DbHint enumValue : enumArray)
				{
					System.out.println("ElementState.simpl_db value : " + field.getName() + " " + enumValue);
				}
			}

			if (thisXmlCollection != null)
			{
				System.out.println("ElementState.xml_Collection value : " + thisXmlCollection.value());

			}
			System.out.println();
		}

		HashMapArrayList thisFieldDescriptors = thisClassDescriptor.getFieldDescriptorsByFieldName();
		for (Object object : thisFieldDescriptors)
		{
			FieldDescriptor thisFieldDescriptor = (FieldDescriptor) object;
			Class<?> thisFieldType = thisFieldDescriptor.getFieldType();
			String thisFieldName = thisFieldDescriptor.getFieldName();

			// System.out.println("className : " + thisClassDescriptor.getDecribedClassSimpleName() +
			// " fieldType - " + thisFieldType.getSimpleName());
			System.out.println("\n(name) - " + thisFieldName);
			System.out.println("(fieldType) : " + thisFieldType.getCanonicalName()
					+ "\n (simpleTypeName) : " + thisFieldType.getSimpleName()
					/* value of @xml_collection("item") */
					/*
					 * TODO inspect code below deriving value from @xml_collection
					 */
					+ "\n ****(ColectionOrMapTagName) : " + thisFieldDescriptor.getCollectionOrMapTagName());

			/****************************************
			 * thisFieldDescriptor.getScalarType() - return null if not primitive type e.g.
			 * ArrayList<Item> - null check and return if not primitive TODO get container field type e.g.
			 * ArrayList<Item> items;
			 ****************************************/
			/* in case of not defined scalarType, just return null e.g. ArrayList<Item> */
			System.out.println("(scalarType) : " + thisFieldDescriptor.getScalarType());
			System.out.println("(GenericString) - " + thisFieldDescriptor.getField().toGenericString());
			System.out.println("(Field().toString() - " + thisFieldDescriptor.getField().toString());
			/* java.util.ArrayList<ecologylab.xml.tools.sqlTranslator.input.Item> */
			System.out.println("(GenericType) - " + thisFieldDescriptor.getField().getGenericType());

			/*
			 * TODO get collection value
			 */
			simpl_collection thisCollectionValue = thisFieldDescriptor.getField().getAnnotation(
					ElementState.simpl_collection.class);

			if (thisCollectionValue != null)
				System.out.println("**** (@xml_collection value) " + thisCollectionValue.value());
			else
				System.out.println("**** (@xml_collection value) : null");

			/*
			 * Get newly defined db annotation value
			 */
			/*
			 * simpl_db thisSimplDbValue =
			 * thisFieldDescriptor.getField().getAnnotation(ElementState.simpl_db.class); if
			 * (thisSimplDbValue != null){ DbHint[] thisDBValue = thisSimplDbValue.value(); for (DbHint
			 * dbHint : thisDBValue) { System.out.println("<<<<< (@simpl_db value)" + dbHint); } } else
			 * System.out.println("<<<<< (@simpl_db value) : null");
			 */

			/*
			 * Get annotations
			 */
			Annotation[] thisAnnotation = thisFieldDescriptor.getField().getAnnotations();
			for (Annotation annotation : thisAnnotation)
			{
				System.out.println("(annotation) - " + annotation.annotationType().getSimpleName());

			}

		}
	}

	/**
	 * method to extract FieldType from Generic Type expression e.g. 'ParsedURL' of class
	 * ecologylab.net.ParsedURL, or 'Item' of
	 * java.util.ArrayList<ecologylab.xml.tools.sqlTranslator.input.Item>
	 * 
	 * @param thisFieldDescriptor
	 * @return
	 */
	public String getFieldTypeFromGenericFieldType(FieldDescriptor thisFieldDescriptor)
	{
		Type thisGenericTypeExpression = thisFieldDescriptor.getField().getGenericType();
		String thisReplacedString = thisGenericTypeExpression.toString()
				.replaceAll("[^A-Za-z0-9]", " ");
		String[] thisSplittedString = thisReplacedString.split(" ");

		return thisSplittedString[thisSplittedString.length - 1];

	}

	/**
	 * method to extract FieldType from Generic Type expression e.g. 'ParsedURL' of class
	 * ecologylab.net.ParsedURL, or 'Item' of
	 * java.util.ArrayList<ecologylab.xml.tools.sqlTranslator.input.Item>
	 * 
	 * @param thisField
	 * @return
	 */
	private String getFieldTypeFromGenericFieldType(Field thisField)
	{
		Type thisGenericTypeExpression = thisField.getGenericType();

		String thisReplacedString = thisGenericTypeExpression.toString()
				.replaceAll("[^A-Za-z0-9]", " ");
		String[] thisSplittedString = thisReplacedString.split(" ");

		return thisSplittedString[thisSplittedString.length - 1];

	}

	@Test
	public void testGetFieldTypeFromGenericType()
	{
		/* java.util.ArrayList<ecologylab.xml.tools.sqlTranslator.input.Item> */
		String thisString = "java.util.ArrayList<ecologylab.xml.tools.sqlTranslator.input.Item>";
		String thisString2 = "class ecologylab.net.ParsedURL";
		String thisString3 = "java.util.ArrayList<translators.sql.testing.ecologylabXmlTest.ItemTest>";

		String thisReplacedString = thisString3.replaceAll("[^A-Za-z0-9]", " ");
		String[] thisSplittedString = thisReplacedString.split(" ");

		String thisFieldTypeExtracted = thisSplittedString[thisSplittedString.length - 1];
		System.out.println(thisFieldTypeExtracted);

	}

	/**
	 * TODO considering @simpl_db metalanguage values
	 * 
	 * @param thisClassDescriptor
	 * @param fields
	 */
	private void createTableArrayListForMultiAttributes(ClassDescriptor thisClassDescriptor,
			Field[] fields)
	{
		String tableName = thisClassDescriptor.getDescribedClass().getSimpleName();
		String tableExtend = new String("null");
		String tableComment = thisClassDescriptor.toString();

		/**
		 * table attributes consisting of table name, extend, and comment
		 */
		String tableNameForMultiAttributes = tableName + "#" + tableExtend + "#" + tableComment;

		/* for test */
		// System.out.println("tableNameForMultiAttributes : " + tableNameForMultiAttributes);
		for (Field thisField : fields)
		{
			String fieldName = thisField.getName();
			String fieldType = thisField.getType().getSimpleName();

			/* added for handling collection field such as 'Item' of Arrayist[Item], default 'null' */
			String fieldCollectionType = new String("null");
			/* check if fieldType == ArrayList */
			if (fieldType.equalsIgnoreCase("ArrayList"))
				fieldCollectionType = this.getFieldTypeFromGenericFieldType(thisField);

			/**
			 * extract annotations (extend field attributes to include fieldDBConstraint)
			 */
			/* default value */
			String fieldComment = "";
			String fieldDBConstraint = "";

			Annotation[] fieldAnnotations = thisField.getAnnotations();
			for (Annotation thisAnnotation : fieldAnnotations)
			{
				/* separating DB constraints */
				if (thisAnnotation.annotationType().equals(ElementState.simpl_db.class))
				{
					DbHint[] thisDBConstraintValue = thisField.getAnnotation(ElementState.simpl_db.class)
							.value();
					for (DbHint dbHint : thisDBConstraintValue)
					{
						fieldDBConstraint += dbHint + " ";
					}
				}
				else
				{
					if (thisAnnotation.annotationType().getSimpleName() != null)
						fieldComment += thisAnnotation.annotationType().getSimpleName() + " ";
				}
			}

			/* set null if no value exists */
			if (fieldComment.equals(""))
				fieldComment = "null";
			if (fieldDBConstraint.equals(""))
				fieldDBConstraint = "null";

			/* extended field attributes */
			String fieldForMultiAttributes = fieldType + "#" + fieldComment + "#" + fieldCollectionType
					+ "#" + fieldDBConstraint;

			/* for test */
			// System.out.println("fieldForMultiAttributes : " + fieldForMultiAttributes);
			super.createMMTableArrayList(tableNameForMultiAttributes, fieldName, fieldForMultiAttributes);

		}

	}

	@Test
	public void testCreateTableArrayListForMultiAttributes()
	{
		TranslationScope thisTranslationScope = TranslationScope.get("thisTranslationScope2",
				ChannelTest.class);
		Collection<ClassDescriptor> thisClassDescriptors = thisTranslationScope.getClassDescriptors();

		for (ClassDescriptor classDescriptor : thisClassDescriptors)
		{
			Field[] thisDeclaredFields = classDescriptor.getDescribedClass().getDeclaredFields();

			this.createTableArrayListForMultiAttributes(classDescriptor, thisDeclaredFields);

		}

	}

	/**
	 * 
	 * @param classDescriptor
	 * @param fieldDescriptor
	 * @param annotation
	 */
	private void createTableArrayListForMultiAttributes(ClassDescriptor classDescriptor,
			FieldDescriptor fieldDescriptor, Annotation annotation)
	{
		String tableName = classDescriptor.getDecribedClassSimpleName();
		String tableExtend = new String("null");
		String tableComment = classDescriptor.toString();

		/**
		 * table attributes consisting of table name, extend, and comment
		 */
		String tableNameForMultiAttributes = tableName + "#" + tableExtend + "#" + tableComment;

		/* FieldName(key of hashMap) */
		String fieldName = fieldDescriptor.getFieldName();
		String fieldType = fieldDescriptor.getFieldType().getSimpleName();

		/* added for handling collection field, default 'null' e.g. 'Item' of ArrayList[Item] */
		String fieldCollectionType = "null";
		/* check if fieldType == ArrayList */
		if (fieldType.equalsIgnoreCase("ArrayList"))
			fieldCollectionType = this.getFieldTypeFromGenericFieldType(fieldDescriptor);

		String fieldComment = annotation.annotationType().getName();

		/**
		 * field attributes consisting of field name(key of subHashpMap), type, comment, collectionType,
		 * and DB constraints (PRIMARY KEY, UNIQUE, NOT NULL, etc.)
		 */
		String fieldForMultiAttributes = fieldType + "#" + fieldComment + "#" + fieldCollectionType;

		super.createMMTableArrayList(tableNameForMultiAttributes, fieldName, fieldForMultiAttributes);

	}

	public static void main(String[] args) throws IOException
	{
		// SqlTranslatorMain thisSqlTranslator = new SqlTranslatorMain("postgreSQLOutput.sql");
		
		SqlTranslatorMain thisSqlTranslator = null;
		try
		{
			thisSqlTranslator = new SqlTranslatorMain();
		}
		catch (SIMPLTranslationException e)
		{
			e.printStackTrace();
		}

		TranslationScope thisTranslationScope = null;  
			
//		thisTranslationScope = TranslationScope.get("thisTranslationScope", RssStateTest.class, ItemTest.class, ChannelTest.class);
		
		thisTranslationScope = GeneratedMetadataTranslationScope.get(); 
		 
		thisSqlTranslator.createSQLTableSchema(thisTranslationScope, DEFAULT_CREATE_TABLE_MODE);
		thisSqlTranslator.createSQLTableSchema(thisTranslationScope, DEFAULT_COMPOSITE_TYPE_TABLE_MODE);
		
		

	}

}
