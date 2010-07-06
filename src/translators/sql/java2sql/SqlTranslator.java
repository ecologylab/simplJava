package translators.sql.java2sql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ecologylab.generic.Debug;
import ecologylab.xml.XMLTranslationException;

public class SqlTranslator extends Debug implements DBName
{
	String																																thisStringTargetDirectory									= null;

	protected static String																								DEFAULT_SQL_FILE_NAME											= "postgreSQL.sql";

	protected static String																								DEFAULT_SQL_OUTPUT_DIRECTORY							= "src//translators//sql//java2sql//output";

	public static final int																								DEFAULT_CREATE_TABLE_MODE									= 0;

	public static final int																								DEFAULT_COMPOSITE_TYPE_TABLE_MODE					= 1;

	/* DB schema generator mode */
	public static int																											DB_SCHEMA_GENERATOR_MODE									= DEFAULT_CREATE_TABLE_MODE;

	/*
	 * MAIN data structure for containing sets of table extracted from MetaMetaData definition
	 * consisting of unique tableName i.e. String(name), and subHashMap(fieldName, fieldType)
	 */
	protected static ArrayList<HashMap<String, HashMap<String, String>>>	thisHashMapTableArrayList									= new ArrayList<HashMap<String, HashMap<String, String>>>();

	/*
	 * Define another HashMapTableArrayListForCompositeType
	 */
	protected static ArrayList<HashMap<String, HashMap<String, String>>>	thisHashMapTableArrayListForCompositeType	= new ArrayList<HashMap<String, HashMap<String, String>>>();

	public SqlTranslator() throws XMLTranslationException
	{

	}

	public SqlTranslator(String targetDirectory)
	{
		this.thisStringTargetDirectory = targetDirectory;
		new File(thisStringTargetDirectory);

	}

	/*
	 * MAIN function for creating memory-based table schema
	 */
	public void createMMTableArrayList(String tableName, String fieldName, String fieldType)
	{
		if (!isTableExist(tableName))
		{
			if (!addHashMapTableArrayList(createNestedHashMapTable(tableName, fieldName, fieldType)))
				Debug.println("Error in addHashMapTableArrayList() : " + this.getClass().getName());

		}
		else
		{
			updateHashMapTableArrayList(tableName, fieldName, fieldType);

		}
	}

	/*
	 * subfunction for createMMTableSchema() check if table is already exist in the hashMapTable
	 * ArrayList
	 */
	public boolean isTableExist(String tableName)
	{
		if (getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_CREATE_TABLE_MODE)
		{
			for (Iterator iterator = thisHashMapTableArrayList.iterator(); iterator.hasNext();)
			{
				HashMap<String, HashMap<String, String>> thisHashMapTable = (HashMap<String, HashMap<String, String>>) iterator
						.next();

				if (thisHashMapTable.keySet().contains(tableName))
					return true;

			}
		}
		else if (getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_COMPOSITE_TYPE_TABLE_MODE)
		{
			for (Iterator iterator = thisHashMapTableArrayListForCompositeType.iterator(); iterator
					.hasNext();)
			{
				HashMap<String, HashMap<String, String>> thisHashMapTable = (HashMap<String, HashMap<String, String>>) iterator
						.next();

				if (thisHashMapTable.keySet().contains(tableName))
					return true;

			}
		}
		return false;

	}

	/*
	 * subfunction for createMMTableSchema() add new hashMapTable to hashMapTable ArrayList
	 */
	private boolean addHashMapTableArrayList(HashMap<String, HashMap<String, String>> newHashMapTable)
	{
		if (getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_CREATE_TABLE_MODE)
			return thisHashMapTableArrayList.add(newHashMapTable);

		else if (getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_COMPOSITE_TYPE_TABLE_MODE)
			return thisHashMapTableArrayListForCompositeType.add(newHashMapTable);

		return false;

	}

	/*
	 * subfunction for createMMTableSchema()
	 */
	private HashMap<String, HashMap<String, String>> createNestedHashMapTable(String tableName,
			String fieldName, String fieldType)
	{
		HashMap<String, String> newHashMapFieldNameType = new HashMap<String, String>();
		newHashMapFieldNameType.put(fieldName, fieldType);

		HashMap<String, HashMap<String, String>> newHashMap = new HashMap<String, HashMap<String, String>>();
		newHashMap.put(tableName, newHashMapFieldNameType);

		return newHashMap;
	}

	/*
	 * update hashMapTable ArrayList by adding new field name and type to corresponding table
	 */
	protected void updateHashMapTableArrayList(String tableName, String fieldName, String fieldType)
	{
		if (getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_CREATE_TABLE_MODE)
		{
			for (Iterator iterator = thisHashMapTableArrayList.iterator(); iterator.hasNext();)
			{
				HashMap<String, HashMap<String, String>> thisUpperHashMapTable = (HashMap<String, HashMap<String, String>>) iterator
						.next();
				if (thisUpperHashMapTable.containsKey(tableName))
				{
					/* ADD new fieldName and fieldType (subsequently update UpperHashMapTable) */
					thisUpperHashMapTable.get(tableName).put(fieldName, fieldType);
					break;

				}
			}
		}
		else if (getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_COMPOSITE_TYPE_TABLE_MODE)
		{
			for (Iterator iterator = thisHashMapTableArrayListForCompositeType.iterator(); iterator
					.hasNext();)
			{
				HashMap<String, HashMap<String, String>> thisUpperHashMapTable = (HashMap<String, HashMap<String, String>>) iterator
						.next();
				if (thisUpperHashMapTable.containsKey(tableName))
				{
					/* ADD new fieldName and fieldType (subsequently update UpperHashMapTable) */
					thisUpperHashMapTable.get(tableName).put(fieldName, fieldType);
					break;

				}
			}
		}

	}

	/*
	 * core function for creating DB schema generation file(SQL) corresponding to metametadata
	 * definition
	 */
	// TODO make the generated file name(e.g. "primitives.sql") be same as the target metadata file
	// name(e.g. "primitives.xml")
	public boolean createMMTableSQLFileFromHashMapArrayList(String dbCategory) throws IOException
	{
		if (dbCategory.equals(DBName.POSTGRESQL)
				&& getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_CREATE_TABLE_MODE)
		{
			if (SqlTranslator.thisHashMapTableArrayList.size() >= 1)
			{
				/* MAIN function */
				if (createSQLFileWriter(getDEFAULT_SQL_FILE_NAME(), DEFAULT_SQL_OUTPUT_DIRECTORY,
						thisHashMapTableArrayList))
				{
					Debug.println("postgreSQL File is generated successfully : "
							+ getDEFAULT_SQL_OUTPUT_DIRECTORY());
					return true;
				}
				else
				{
					Debug.println("[Error] postgreSQL File generation failed : " + this.getClass().getName());
					return false;
				}
			}
			else
			{
				Debug
						.println("[Warning] run MetadataSchemaGenerator.createMMTableSchmea() first - thisHashMapTableArrayList is null:"
								+ this.getClass().getName());
				return false;

			}

		}
		else if (dbCategory.equals(DBName.POSTGRESQL)
				&& getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_COMPOSITE_TYPE_TABLE_MODE)
		{
			if (SqlTranslator.thisHashMapTableArrayListForCompositeType.size() >= 1)
			{
				/* MAIN function */
				if (createSQLFileWriter(getDEFAULT_SQL_FILE_NAME(), DEFAULT_SQL_OUTPUT_DIRECTORY,
						thisHashMapTableArrayListForCompositeType))
				{
					Debug.println("postgreSQL File is generated successfully : "
							+ getDEFAULT_SQL_OUTPUT_DIRECTORY());
					return true;
				}
				else
				{
					Debug.println("[Error] postgreSQL File generation failed : " + this.getClass().getName());
					return false;
				}
			}
			else
			{
				Debug
						.println("[Warning] run MetadataSchemaGenerator.createMMTableSchmea() first - thisHashMapTableArrayList is null:"
								+ this.getClass().getName());
				return false;

			}

		}
		return false;

	}

	/*
	 * sub function of createMMTableSQLFileFromHashMapArrayList()
	 */
	private boolean createSQLFileWriter(String fileName, String fileDirectory,
			ArrayList<HashMap<String, HashMap<String, String>>> thisHashMapTableArrayList)
			throws IOException
	{

		File thisResultFile = new File(fileDirectory, fileName);

		// thisResultFile.delete();
		// thisResultFile.createNewFile();
		// thisResultFile.setWritable(true);

		FileWriter thisFileWriter = new FileWriter(thisResultFile, true);
		BufferedWriter thisBufferedWriter = new BufferedWriter(thisFileWriter);
		thisBufferedWriter.write(createSQLStringFromHashMapArrayList(thisHashMapTableArrayList));

		thisBufferedWriter.close();
		thisFileWriter.close();

		return true;

	}

	/*
	 * create SQL schema(CREATE TABLE) in memory ref. printMMTableSchema() body - iterative data
	 * search
	 */
	public String createSQLStringFromHashMapArrayList(
			ArrayList<HashMap<String, HashMap<String, String>>> thisHashMapTableArrayList)
	{
		String thisSQLStatement = "";

		for (Iterator iterator = thisHashMapTableArrayList.iterator(); iterator.hasNext();)
		{
			HashMap<String, HashMap<String, String>> thisUpperHashMap = (HashMap<String, HashMap<String, String>>) iterator
					.next();

			/*
			 * table name tokenize(tableName$tableExtend$tableComment)
			 */
			Object[] thisUpperHashTableName = thisUpperHashMap.keySet().toArray();
			/* there is only one table name for each upperHashMap */
			String thisUpperHashMapTableAttributes = (String) thisUpperHashTableName[0];

			/**
			 * Extract table values from UpperHashMapTable
			 */
			String tableName = this.extractToken("tableName", thisUpperHashMapTableAttributes);
			String tableExtend = this.extractToken("tableExtend", thisUpperHashMapTableAttributes);
			String tableComment = this.extractToken("tableComment", thisUpperHashMapTableAttributes);

			String tableCommentExtracted = tableComment.equals("null") ? "" : "--" + tableComment;

			StringBuilder thisEachTableSQLStatement = new StringBuilder();
			thisEachTableSQLStatement.append(tableCommentExtracted + "\n");

			if (getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_CREATE_TABLE_MODE)
				thisEachTableSQLStatement.append("CREATE TABLE " + tableName + " (\n");
			else if (getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_COMPOSITE_TYPE_TABLE_MODE)
				thisEachTableSQLStatement.append("CREATE TYPE " + tableName + " AS (\n");

			/*
			 * TODO update to set each field constraints based on @simpl_db annotation (DbHint.PRIMARY_KEY, Db_Hint.ALLOW_NOT_NULL, DbHint.UNIQUE)
			 */
			/* first element referred to create last PRIMARY KEY constraint */
			String thisFirstTableElement = "";
			/* deriving each table - HashMap */
			HashMap<String, String> thisSubHashMapTable = thisUpperHashMap
					.get(thisUpperHashMapTableAttributes);
			/* transient counter for checking the order of fields */
			int thisTmpCount = 1;
			/* inner iteration for retrieving fieldName and fieldType of individual table */
			for (Iterator iterator2 = thisSubHashMapTable.keySet().iterator(); iterator2.hasNext();)
			{
				String thisTmpFieldName = (String) iterator2.next();

				/** 
				 * Extract field values from SubHashMapTable
				 */
				/* field attributes tokenize(fieldType#fieldComment#fieldCollectionType) */
				String thisTmpFieldAttributes = thisSubHashMapTable.get(thisTmpFieldName);
				/* added for fieldCollectionType e.g. 'Item' of ArrayList[Item] default 'null' */
				String thisTmpFieldCollectionType = this.extractToken("fieldCollectionType",
						thisTmpFieldAttributes);
				String thisTmpFieldType = convertToValidFieldType(DBName.POSTGRESQL, this.extractToken(
						"fieldType", thisTmpFieldAttributes), thisTmpFieldCollectionType);
				String thisTmpFieldComment = this.extractToken("fieldComment", thisTmpFieldAttributes);

				String thisTmpFieldCommentExtracted = thisTmpFieldComment.equals("null") ? "" : "	/*"
						+ thisTmpFieldComment + "*/";

				/* in case of only one field exists, adding 'Primary Key' constraint */
				if (thisSubHashMapTable.size() == 1)
				{
					thisEachTableSQLStatement.append(thisTmpFieldName + " " + thisTmpFieldType + ","
							+ thisTmpFieldCommentExtracted + "\n");
					if (!(getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_COMPOSITE_TYPE_TABLE_MODE))
						thisEachTableSQLStatement.append("CONSTRAINT " + tableName + "_pkey PRIMARY KEY("
								+ thisTmpFieldName + "))");
					else
						thisEachTableSQLStatement.append(")");

					if (!tableExtend.equals("null"))
						thisEachTableSQLStatement.append("\nINHERITS (" + tableExtend + ")");

					thisEachTableSQLStatement.append(";");
				}
				/* in case of first element, adding 'UNIQUE' constraint */
				else if (thisTmpCount == 1)
				{
					thisEachTableSQLStatement.append(thisTmpFieldName + " " + thisTmpFieldType + " UNIQUE,"
							+ thisTmpFieldCommentExtracted + "\n");
					thisFirstTableElement = thisTmpFieldName;
				}
				/* in case of last element, adding 'Primary Key' constraint at the end */
				else if (thisTmpCount == thisSubHashMapTable.size())
				{
					if (!(getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_COMPOSITE_TYPE_TABLE_MODE)){
						thisEachTableSQLStatement.append(thisTmpFieldName + " " + thisTmpFieldType + ","
								+ thisTmpFieldCommentExtracted + "\n");

						thisEachTableSQLStatement.append("CONSTRAINT " + tableName + "_pkey PRIMARY KEY("
								+ thisFirstTableElement + "))");
					}	
					else{
						/* 
						 * remove trailing comma for COMPOSITE TYPE last element 
						 */ 
						thisEachTableSQLStatement.append(thisTmpFieldName + " " + thisTmpFieldType + " "
								+ thisTmpFieldCommentExtracted + "\n");
						thisEachTableSQLStatement.append(")");
					}
					if (!tableExtend.equals("null"))
						thisEachTableSQLStatement.append("\nINHERITS (" + tableExtend + ")");

					thisEachTableSQLStatement.append(";");
				}
				/* in case of other intermediate fields */
				else
					thisEachTableSQLStatement.append(thisTmpFieldName + " " + thisTmpFieldType + ","
							+ thisTmpFieldCommentExtracted + "\n");

				thisTmpCount += 1;

			}
			thisSQLStatement += thisEachTableSQLStatement.append("\n\n").toString();

		}
		return thisSQLStatement;

	}

	private String extractToken(String targetField, String attributes)
	{
		String thisTmpSplittedToken[] = attributes.split("#");

		if (targetField.equals("tableName") || targetField.equals("fieldType"))
			return thisTmpSplittedToken[0];

		else if (targetField.equals("tableExtend") || targetField.equals("fieldComment"))
			return thisTmpSplittedToken[1];

		else if (targetField.equals("tableComment") || targetField.equals("fieldCollectionType"))
			return thisTmpSplittedToken[2];

		else
			return null;
	}

	/*
	 * Auxiliary function - pretty print MM table schema
	 */
	// @Ignore("completed testing printMMTableSchema()")
	// @Test
	public void printMMTableSchema()
	{

		/* uncomment below to test this method */
		// this.testCreateMMTableSchemaGenerationFile();
		if (SqlTranslator.thisHashMapTableArrayList.size() >= 1)
		{
			String thisBar = "# of tables to be created : " + thisHashMapTableArrayList.size() + "\n";
			thisBar += "-------------------------------------------------------------\n";
			thisBar += "name\t\t field\t\t type\t\t constraint\n";
			thisBar += "-------------------------------------------------------------\n";
			System.out.println(thisBar);

			for (Iterator iterator = thisHashMapTableArrayList.iterator(); iterator.hasNext();)
			{
				HashMap<String, HashMap<String, String>> thisUpperHashMap = (HashMap<String, HashMap<String, String>>) iterator
						.next();
				Object[] thisUpperHashTableName = thisUpperHashMap.keySet().toArray();

				String thisTableContents = "";

				/* only one table name for each upperHashMap containing every table */
				String thisUpperHashMapTableName = (String) thisUpperHashTableName[0];
				thisTableContents += thisUpperHashMapTableName + "\n\t\t";

				/* each table HashMap */
				HashMap<String, String> thisSubHashMapTable = thisUpperHashMap
						.get(thisUpperHashMapTableName);

				int thisTmpCount = 0;
				for (Iterator iterator2 = thisSubHashMapTable.keySet().iterator(); iterator2.hasNext();)
				{
					String thisTmpFieldName = (String) iterator2.next();
					String thisTmpFieldType = convertToValidFieldType(DBName.POSTGRESQL, thisSubHashMapTable
							.get(thisTmpFieldName), "null");

					/* in case of first element, adding 'Primary Key' constraint */
					if (thisTmpCount == 0)
						thisTableContents += thisTmpFieldName + "\t\t|" + thisTmpFieldType + "\t\t|"
								+ "Primary Key\n\t\t";
					else
						thisTableContents += thisTmpFieldName + "\t\t|" + thisTmpFieldType + "\n\t\t";

					thisTmpCount += 1;

				}
				/* print out tableContents */
				System.out.println(thisTableContents);

			}

		}
		else
		{
			Debug
					.println("[Warning] run MetadataSchemaGenerator.createMMTableSchmea() first - thisHashMapTableArrayList is null:"
							+ this.getClass().getName());

		}
	}

	/*
	 * sub function of printMMTableSchema() for checking valid type TODO 1) replace with HashMap
	 */
	protected String convertToValidFieldType(String dbCategory, String fieldType,
			String thisTmpFieldCollectionType)
	{
		/* in case of POSTGRESQL */
		if (dbCategory.equals(DBName.POSTGRESQL))
		{
			if (fieldType.equalsIgnoreCase("StringBuilder") || fieldType.equalsIgnoreCase("String"))
				return "text";

			else if (fieldType.equalsIgnoreCase("ParsedURL"))
				return "varchar(30)";

			else if (fieldType.equalsIgnoreCase("int"))
				return "float";
			/* supporting array */
			else if (fieldType.equalsIgnoreCase("ArrayList")
					&& thisTmpFieldCollectionType.equalsIgnoreCase("String"))
				return "text[]";
			/* array of composite type e.g. Item[] */
			else if (fieldType.equalsIgnoreCase("ArrayList"))
				return thisTmpFieldCollectionType + "[]";
			else
				return fieldType;
		}
		return null;

	}

	public static void setDEFAULT_SQL_OUTPUT_DIRECTORY(String sql_output_directory)
	{
		DEFAULT_SQL_OUTPUT_DIRECTORY = sql_output_directory;
	}

	public static String getDEFAULT_SQL_OUTPUT_DIRECTORY()
	{
		return DEFAULT_SQL_OUTPUT_DIRECTORY;
	}

	public static int getDB_SCHEMA_GENERATOR_MODE()
	{
		return DB_SCHEMA_GENERATOR_MODE;
	}

	public static void setDB_SCHEMA_GENERATOR_MODE(int db_schema_generator_mode)
	{
		DB_SCHEMA_GENERATOR_MODE = db_schema_generator_mode;
	}

	public static String getDEFAULT_SQL_FILE_NAME()
	{
		return DEFAULT_SQL_FILE_NAME;
	}

	public static void setDEFAULT_SQL_FILE_NAME(String sql_file_name)
	{
		DEFAULT_SQL_FILE_NAME = sql_file_name;
	}

	public static void main(String args[]) throws XMLTranslationException, IOException
	{
		SqlTranslator thisDBSchemaGenerator = new SqlTranslator();

	}
}
