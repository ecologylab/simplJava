package translators.sql.metadata2sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.junit.Test;

import translators.sql.java2sql.DBName;
import ecologylab.generic.Debug;
import ecologylab.xml.SIMPLTranslationException;

public class sqlTranslator implements DBName{
	String thisStringTargetDirectory = null; 

	private File thisMMDTargetDirectory;

//	private MetaMetadataRepository thisMetadataRepository;
	
	protected static final String DEFAULT_SQL_FILE_NAME = "postgreSQL.sql";
	
	protected static final String DEFAULT_MMD_SRC_DIRECTORY = "src\\config\\semantics\\metametadata\\metaMetadataRepository\\repositorySources";

	public static final int DEFAULT_CREATE_TABLE_MODE = 0;
	
	public static final int DEFAULT_COMPOSITE_TYPE_TABLE_MODE = 1;
	/*DB schema generator mode*/ 
	public static int DB_SCHEMA_GENERATOR_MODE = DEFAULT_CREATE_TABLE_MODE;  
	

	/*
	 * MAIN data structure for containing sets of table extracted from MetaMetaData definition
	 * consisting of unique tableName i.e. String(name), and subHashMap(fieldName, fieldType)
	 */
	protected static ArrayList<HashMap<String, HashMap<String, String>>> thisHashMapTableArrayList
		= new ArrayList<HashMap<String,HashMap<String,String>>>();
	
	/*
	 * Define another HashMapTableArrayListForCompositeType 
	 */
	protected static ArrayList<HashMap<String, HashMap<String, String>>> thisHashMapTableArrayListForCompositeType
	= new ArrayList<HashMap<String,HashMap<String,String>>>();
	
	/*
	 * default constructor
	 */
	public sqlTranslator() throws SIMPLTranslationException{
//		thisParser = new MetaMetadataRepositoryParser2(); 
		
		
	}
	
	/*
	 * constructor 
	 */
	public sqlTranslator(String targetDirectory) {
		
		this.thisStringTargetDirectory = targetDirectory; 
		thisMMDTargetDirectory = new File(thisStringTargetDirectory);
		
	}
	
	/*
	 * MAIN Function (resulting in creating MMTableArrayList)
	 */
	public void createMMDTableSchema() throws IOException{
		thisMMDTargetDirectory = new File(DEFAULT_MMD_SRC_DIRECTORY);
//		this.thisMetadataRepository = MetaMetadataRepository.load(this.thisMMDTargetDirectory);
//		Collection<MetaMetadata> thisCollection = this.thisMetadataRepository.values(); 
//		for (MetaMetadata metaMetadata : thisCollection) {
//			HashMapArrayList<String, MetaMetadataField> thisChildMetadata = metaMetadata.getChildMetaMetadata();
//			
//			for (MetaMetadataField metaMetadataField : thisChildMetadata) {
				/*MAIN Function (result in creating 'thisHashMapTableArrayList')*/
//				createMMTableArrayList(metaMetadata.getChildTag(), metaMetadataField.getChildTag(), metaMetadataField.getScalarType().fieldTypeName());
//				createMMTableArrayListForMultiAttributes(metaMetadata, metaMetadataField);
				
//			}	
//		}
//		createMMTableSQLFileFromHashMapArrayList(DBName.POSTGRESQL);
		
	}
	
	@Test
	public void testMetadataTranslationScope(){
		thisMMDTargetDirectory = new File(DEFAULT_MMD_SRC_DIRECTORY);
//		this.thisMetadataRepository = MetaMetadataRepository.load(this.thisMMDTargetDirectory);
//		
//		TranslationScope thisTranslationScope = thisMetadataRepository.metadataTranslationScope();
//		Collection<ClassDescriptor> thisClassDescriptor = thisTranslationScope.getClassDescriptors();
		
//		for (ClassDescriptor classDescriptor : thisClassDescriptor) {
//			HashMapArrayList thisFieldDescriptor = classDescriptor.getFieldDescriptorsByFieldName();
//			Class<? extends HashMapArrayList> thisClass = thisFieldDescriptor.getClass();
//			
//		}
		
	}
	
	@Test
	/*
	 * MAIN function test
	 */
	public void testCreateMMDTableSchema() throws IOException{
		thisMMDTargetDirectory = new File(DEFAULT_MMD_SRC_DIRECTORY);
		
//		this.thisMetadataRepository = MetaMetadataRepository.load(this.thisMMDTargetDirectory);
//		Collection<MetaMetadata> thisCollection = this.thisMetadataRepository.values();
//			
//		for (MetaMetadata metaMetadata : thisCollection) {
//			HashMapArrayList<String, MetaMetadataField> thisChildMetadata = metaMetadata.getChildMetaMetadata();
//			
//			System.out.println("\n*(table name) " + metaMetadata.getName() +
//								" (extends)" + metaMetadata.getExtendsAttribute() +
//								" (comment)" + metaMetadata.getComment());
//			
//			for (MetaMetadataField metaMetadataField : thisChildMetadata) { 
//				System.out.println("(fieldName)" + metaMetadataField.getName() +
////								" | (fieldType)" + metaMetadataField.getScalarType().fieldTypeName() + 
//								" | (comment)" + metaMetadataField.getComment());
//				
				/*MAIN FUNCTION*/
//				createMMTableArrayList(metaMetadata.getChildTag(), metaMetadataField.getChildTag(), metaMetadataField.getScalarType().fieldTypeName());
//				createMMTableArrayListForMultiAttributes(metaMetadata, metaMetadataField); 
						
//			}
		
//		}
		/* Result Data Structure */
//		assertNotNull(thisHashMapTableArrayList);
//		printMMTableSchema();
		assertTrue("MMTableSchemaFile failed to be generated", createMMTableSQLFileFromHashMapArrayList(DBName.POSTGRESQL));
		System.out.println("\n" + createSQLStringFromHashMapArrayList(thisHashMapTableArrayList));
		
	}
	
	
	/*	
	 * MAIN function for handling multiple attributes
	 * HashMap<String, HashMap<String, String>> thisHashMap = new HashMap<String, HashMap<String, String>>(); 
	 *	e.g. HashMap<"tableName$extends$comment", HashMap<"fieldName","type$comment">>
	 */
	
//	public void createMMTableArrayListForMultiAttributes(MetaMetadata metaMetadata, MetaMetadataField metaMetadataField) {
//        /*table attributes*/
//        String tableName = metaMetadata.getName(); 
//        String tableExtend = metaMetadata.getExtendsAttribute(); 
//        String tableComment = metaMetadata.getComment(); 
//        
//        String tableNameForMultiAttributes = tableName + "#" + tableExtend + "#" + tableComment;
//        
//        /*field attributes for above table*/
//        String fieldName = metaMetadataField.getName(); 
////        String fieldType = metaMetadataField.getScalarType().fieldTypeName();
//        String fieldType = metaMetadataField.getMetadataFieldDescriptor().getFieldType().getName();
//        String fieldComment = metaMetadataField.getComment();
//                               
//        String fieldForMultiAttributes = fieldType + "#" + fieldComment; 
//        
//        /*call function*/
//        this.createMMTableArrayList(tableNameForMultiAttributes, fieldName, fieldForMultiAttributes);
//        
//        
//	}
			
	
	/*
	 * Multiple attributes
	 * HashMap<String, HashMap<String, String>> thisHashMap = new HashMap<String, HashMap<String, String>>(); 
	 *	e.g. HashMap<"tableName$extends$comment", HashMap<"fieldName","type$comment">>
	 */
	@Test
	public void testNestedHashMapForMultipleAttributes(){
		HashMap<String, String> thisSubHashMap = new HashMap<String, String>();
		thisSubHashMap.put("title","String#this is a title of book.");
		thisSubHashMap.put("author","String#this is author of the book");
		
		HashMap<String, HashMap<String, String>> thisNestedHashMapForMultiAttributes = new HashMap<String, HashMap<String, String>>();
		thisNestedHashMapForMultiAttributes.put("bookTable#document#This is a book table", thisSubHashMap);
		
		thisNestedHashMapForMultiAttributes.keySet();
		for (Iterator iterator = thisNestedHashMapForMultiAttributes.keySet().iterator(); iterator.hasNext();) {
			String thisKey = (String) iterator.next();
			System.out.println(thisKey);
			
			for (Iterator iterator2 = thisNestedHashMapForMultiAttributes.get(thisKey).keySet().iterator(); iterator2.hasNext();) {
				String	thisKey2 = (String) iterator2.next();
				System.out.println(thisKey2);
				
				System.out.println(thisNestedHashMapForMultiAttributes.get(thisKey).get(thisKey2));
				
			}
		}
		
	}
	
	
	
	@Test
	public void testNestedArrayListForMultipleAttributes(){
		//common variable declaration
		HashMap<String, String> thisTableSubHashMap = new HashMap<String, String>();
		HashMap<String, HashMap<String, String>> thisTableSuperHashMap = new HashMap<String, HashMap<String, String>>();
		
		HashMap<String, String> thisFieldSubHashMap = new HashMap<String, String>();
		HashMap<String, HashMap<String, String>> thisFieldSuperHashMap = new HashMap<String, HashMap<String, String>>();
		
		ArrayList<HashMap<String, HashMap<String, String>>> thisSubArrayList = new ArrayList<HashMap<String, HashMap<String, String>>>();
		ArrayList<ArrayList<HashMap<String, HashMap<String, String>>>> thisSuperArrayList = 
			new ArrayList<ArrayList<HashMap<String, HashMap<String, String>>>>();
		
		/*
		 * First SubArrayList (Containing table and field with each attributues)
		 */
		// First Table
		/*
		 * Formulating function (information insertion)
		 */
		// setting table attributes and their value 
		thisTableSubHashMap.put("extends","document"); 
		thisTableSubHashMap.put("package", "cf.ecologylab.net");
		
		thisTableSuperHashMap.put("thisFirstTableName", thisTableSubHashMap);
		
		// field attributes and their value 
		thisFieldSubHashMap.put("comment", "this is field comment");
		thisFieldSubHashMap.put("navigate_to","navigate to a certain url");
	
		thisFieldSuperHashMap.put("thisFirstFieldName", thisFieldSubHashMap);
		
		// First ArrayList grouping data above  
		thisSubArrayList.add(thisTableSuperHashMap);
		thisSubArrayList.add(thisFieldSuperHashMap);
		 
		assertEquals("document", thisTableSuperHashMap.get("thisFirstTableName").get("extends"));
		assertEquals("this is field comment", thisFieldSuperHashMap.get("thisFirstFieldName").get("comment"));
		
		// add to super array list 
		thisSuperArrayList.add(thisSubArrayList);
		
		// initializing HashMap before
//		thisTableSubHashMap.clear(); 
//		thisTableSuperHashMap.clear(); 
//		
//		thisFieldSubHashMap.clear(); 
//		thisFieldSuperHashMap.clear();
		
		// initializing SubArrayList
		thisSubArrayList.clear(); 
		
		
		/*
		 * Second SubArrayList
		 */
		// Second Table
		// setting table attributes
		thisTableSubHashMap.put("extends","document2"); 
		thisTableSubHashMap.put("package", "cf.ecologylab.net2");
		
		thisTableSuperHashMap.put("thisSecondTableName", thisTableSubHashMap);
		
		// field attributes and their value 
		thisFieldSubHashMap.put("comment", "this is field comment2");
		thisFieldSubHashMap.put("navigate_to","navigate to a certain url2");
	
		thisFieldSuperHashMap.put("thisSecondFieldName", thisFieldSubHashMap);
		
		// First ArrayList grouping data above  
		thisSubArrayList.add(thisTableSuperHashMap);
		thisSubArrayList.add(thisFieldSuperHashMap);
		
		// added to super array list 
		thisSuperArrayList.add(thisSubArrayList);
		assertEquals(2,thisSuperArrayList.size());
		
		/*
		 * Formulating Function (information extraction) 
		 */
		// extracting value from super array list 
		for (ArrayList<HashMap<String, HashMap<String, String>>> arrayList : thisSuperArrayList) {
			for (HashMap<String, HashMap<String, String>> hashMap : arrayList) {
				if (hashMap.get("thisFirstTableName") != null)
					System.out.println(hashMap.get("thisFirstTableName").get("extends"));
				
			}
		}
		
	}

	@Test
	public void testMultiDimensionalArray(){
		String thisArray[][] = {{"hi", "texas"},
								{"hello", "A&M"}};
		for (String[] strings : thisArray) {
			for (String string : strings) {
//				System.out.println(string);
				
			}
		}
		
		Vector<String> thisVector = new Vector<String>();
		
		HashMap<String, String> thisSubHashMap = new HashMap<String, String>();
		thisSubHashMap.put("hi", "texas");
		thisSubHashMap.put("hello", "A&M");
		
		HashMap<Vector<String>, HashMap<String, String>> thisHashMap = new HashMap<Vector<String>, HashMap<String, String>>();
		thisHashMap.put(thisVector, thisSubHashMap);
		
		System.out.println(thisHashMap.get(thisVector).get("hi"));

		
	}

	/*
	 * MAIN function for creating memory-based table schema 
	 */
	public void createMMTableArrayList(String tableName, String fieldName, String fieldType) {
		if (!isTableExist(tableName)){
			if(!addHashMapTableArrayList(createNestedHashMapTable(tableName, fieldName, fieldType))) 
				Debug.println("Error in addHashMapTableArrayList() : " + this.getClass().getName());  
			
		}else{
			updateHashMapTableArrayList(tableName, fieldName, fieldType); 
			
		}
	}
	
	
	/*
	 * subfunction for createMMTableSchema()
	 * check if table is already exist in the hashMapTable ArrayList
	 */
	public boolean isTableExist(String tableName) {
		if(getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_CREATE_TABLE_MODE){
			for (Iterator iterator = thisHashMapTableArrayList.iterator(); iterator.hasNext();) {
				HashMap<String, HashMap<String, String>> thisHashMapTable = (HashMap<String, HashMap<String, String>>) iterator.next();
				
				if (thisHashMapTable.keySet().contains(tableName))
					return true; 
				
			}
		}else if (getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_COMPOSITE_TYPE_TABLE_MODE){
			for (Iterator iterator = thisHashMapTableArrayListForCompositeType.iterator(); iterator.hasNext();) {
				HashMap<String, HashMap<String, String>> thisHashMapTable = (HashMap<String, HashMap<String, String>>) iterator.next();
				
				if (thisHashMapTable.keySet().contains(tableName))
					return true; 
				
			}
		}
		return false;
		
	}
	
	
	/*
	 * subfunction for createMMTableSchema()
	 * add new hashMapTable to hashMapTable ArrayList
	 */
	private boolean addHashMapTableArrayList(HashMap<String, HashMap<String, String>> newHashMapTable) {
		if(getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_CREATE_TABLE_MODE)
			return thisHashMapTableArrayList.add(newHashMapTable);
		
		else if(getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_COMPOSITE_TYPE_TABLE_MODE)
			return thisHashMapTableArrayListForCompositeType.add(newHashMapTable);
		
		return false;
		
	}
	
	/*
	 * subfunction for createMMTableSchema() 
	 */
	private HashMap<String, HashMap<String, String>> createNestedHashMapTable(String tableName, String fieldName, String fieldType) {
		HashMap<String, String> newHashMapFieldNameType = new HashMap<String, String>();
		newHashMapFieldNameType.put(fieldName, fieldType);
		
		HashMap<String, HashMap<String, String>> newHashMap = new HashMap<String, HashMap<String, String>>();
		newHashMap.put(tableName, newHashMapFieldNameType);
		
		return newHashMap;
	}
	
	/*
	 * update hashMapTable ArrayList by adding new field name and type to corresponding table
	 */
	protected void updateHashMapTableArrayList(String tableName, String fieldName, String fieldType) {
		if(getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_CREATE_TABLE_MODE){
			for (Iterator iterator = thisHashMapTableArrayList.iterator(); iterator.hasNext();) {
				HashMap<String, HashMap<String, String>> thisUpperHashMapTable = (HashMap<String, HashMap<String, String>>) iterator.next();
				if (thisUpperHashMapTable.containsKey(tableName)){
					/* ADD new fieldName and fieldType (subsequently update UpperHashMapTable) */
					thisUpperHashMapTable.get(tableName).put(fieldName, fieldType);
					break; 
				
				}
			}
		}else if(getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_COMPOSITE_TYPE_TABLE_MODE){
			for (Iterator iterator = thisHashMapTableArrayListForCompositeType.iterator(); iterator.hasNext();) {
				HashMap<String, HashMap<String, String>> thisUpperHashMapTable = (HashMap<String, HashMap<String, String>>) iterator.next();
				if (thisUpperHashMapTable.containsKey(tableName)){
					/* ADD new fieldName and fieldType (subsequently update UpperHashMapTable) */
					thisUpperHashMapTable.get(tableName).put(fieldName, fieldType);
					break; 
				
				}
			}
		}
		
	}
	
	
	/*
	 * core function for creating DB schema generation file(SQL) corresponding to metametadata definition 
	 */
	//TODO make the generated file name(e.g. "primitives.sql") be same as the target metadata file name(e.g. "primitives.xml")
	public boolean createMMTableSQLFileFromHashMapArrayList(String dbCategory) throws IOException {
		if(dbCategory.equals(DBName.POSTGRESQL) && getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_CREATE_TABLE_MODE){
			if(sqlTranslator.thisHashMapTableArrayList.size() >= 1){
				/*MAIN function*/
				if(createSQLFileWriter(DEFAULT_SQL_FILE_NAME, DEFAULT_MMD_SRC_DIRECTORY, thisHashMapTableArrayList)){
					Debug.println("postgreSQL File is generated successfully : " + this.getClass().getName());
					return true; 
				}
				else{
					Debug.println("[Error] postgreSQL File generation failed : " + this.getClass().getName());
					return false; 
				} 
			}else{
				Debug.println("[Warning] run MetadataSchemaGenerator.createMMTableSchmea() first - thisHashMapTableArrayList is null:" + this.getClass().getName());
				return false; 
				
			}
			
		}else if(dbCategory.equals(DBName.POSTGRESQL) && getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_COMPOSITE_TYPE_TABLE_MODE){
			if(sqlTranslator.thisHashMapTableArrayListForCompositeType.size() >= 1){
				/*MAIN function*/
				if(createSQLFileWriter(DEFAULT_SQL_FILE_NAME, DEFAULT_MMD_SRC_DIRECTORY, thisHashMapTableArrayListForCompositeType)){
					Debug.println("postgreSQL File is generated successfully : " + this.getClass().getName());
					return true; 
				}
				else{
					Debug.println("[Error] postgreSQL File generation failed : " + this.getClass().getName());
					return false; 
				} 
			}else{
				Debug.println("[Warning] run MetadataSchemaGenerator.createMMTableSchmea() first - thisHashMapTableArrayList is null:" + this.getClass().getName());
				return false; 
				
			}
			
			
		}
		return false;
		
	}
	
	/*
	 * sub function of createMMTableSQLFileFromHashMapArrayList()
	 */
	private boolean createSQLFileWriter(String fileName, String fileDirectory,
									ArrayList<HashMap<String, HashMap<String, String>>> thisHashMapTableArrayList) throws IOException{
			
			File thisResultFile = new File(fileDirectory, fileName);
			
//			thisResultFile.delete();
//			thisResultFile.createNewFile();
//			thisResultFile.setWritable(true);
			
			FileWriter thisFileWriter = new FileWriter(thisResultFile, true);
			BufferedWriter thisBufferedWriter = new BufferedWriter(thisFileWriter);
			thisBufferedWriter.write(createSQLStringFromHashMapArrayList(thisHashMapTableArrayList)); 
			
			thisBufferedWriter.close();
			thisFileWriter.close(); 
			
			return true; 
			
	}
		
	
	@Test(timeout = 3)
	public void testFileWriter() throws IOException{
		String fileName = "thisFile.out";
		String fileLocation = "src//ecologylab//semantics//tools//metadataDBSchemaGenerator";
		String fileContents = "this is file contents\n this is second line.\n this is third line."; 
		String fileContentsAdded = "these are contents added\n";
		
		String fileContentsInitialized = "these are added after removing previous contents";
		
//		File thisFile = new File(fileLocation);
//		assertTrue(thisFile.exists());
//		
//		String[] thisFileList = thisFile.list();
//		for (String string : thisFileList) {
//			System.out.println(string);
//		}
		
		File thisFile2 = new File(fileLocation, fileName);

		/*delete existing file and create new one*/ 
		thisFile2.delete(); 
		thisFile2.createNewFile();
		/*set file properties*/
		thisFile2.setWritable(true);
		
		assertTrue(thisFile2.exists()); 
		assertTrue(thisFile2.canWrite());
		
		BufferedWriter thisBufferedWriter = new BufferedWriter(new FileWriter(thisFile2, true));
		
		thisBufferedWriter.write(fileContents);
		thisBufferedWriter.write(fileContentsAdded);
		
		thisBufferedWriter.close(); 
		
	}
	
	/*
	 * create SQL schema(CREATE TABLE) in memory
	 * ref. printMMTableSchema() body - iterative data search  
	 */
	public String createSQLStringFromHashMapArrayList(ArrayList<HashMap<String, HashMap<String, String>>> thisHashMapTableArrayList) {
		String thisSQLStatement = ""; 
		
		for (Iterator iterator = thisHashMapTableArrayList.iterator(); iterator.hasNext();) {
			HashMap<String, HashMap<String, String>> thisUpperHashMap =	(HashMap<String, HashMap<String, String>>) iterator.next();	
			
			/*
			 * table name tokenize(tableName$tableExtend$tableComment)
			 */			
			Object[] thisUpperHashTableName = thisUpperHashMap.keySet().toArray();
			/*there is only one table name for each upperHashMap*/
			String thisUpperHashMapTableAttributes = (String)thisUpperHashTableName[0]; 
		
			String tableName = this.extractToken("tableName", thisUpperHashMapTableAttributes);
			String tableExtend = this.extractToken("tableExtend", thisUpperHashMapTableAttributes);
			String tableComment = this.extractToken("tableComment", thisUpperHashMapTableAttributes);

			String tableCommentExtracted = tableComment.equals("null") ? "" : "--" + tableComment; 
			/*
			 * TODO replace with StringBuilder 
			 */
			String thisEachTableSQLStatement = "";  			
			thisEachTableSQLStatement = tableCommentExtracted + "\n"; 	
			
			if (getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_CREATE_TABLE_MODE)
				thisEachTableSQLStatement += "CREATE TABLE " + tableName + " (\n";
			else if (getDB_SCHEMA_GENERATOR_MODE() == DEFAULT_COMPOSITE_TYPE_TABLE_MODE)
				thisEachTableSQLStatement += "CREATE TYPE " + tableName + " AS (\n";
			
			/*first element referred to create last PRIMARY KEY constraint*/
			String thisFirstTableElement = ""; 
			/*deriving each table - HashMap*/
			HashMap<String, String> thisSubHashMapTable = thisUpperHashMap.get(thisUpperHashMapTableAttributes);
			/*transient counter for checking the order of fields*/
			int thisTmpCount = 1 ; 
			/*inner iteration for retrieving fieldName and fieldType of individual table*/
			for (Iterator iterator2 = thisSubHashMapTable.keySet().iterator(); iterator2.hasNext();) {
				String thisTmpFieldName = (String) iterator2.next();
				
				/*field attributes tokenize(fieldType#fieldComment#fieldCollectionType)*/
				String thisTmpFieldAttributes = thisSubHashMapTable.get(thisTmpFieldName);
				/*added for fieldCollectionType e.g. 'Item' of ArrayList[Item] default 'null'*/
				String thisTmpFieldCollectionType = this.extractToken("fieldCollectionType", thisTmpFieldAttributes);
				String thisTmpFieldType = convertToValidFieldType(DBName.POSTGRESQL, this.extractToken("fieldType", thisTmpFieldAttributes), thisTmpFieldCollectionType);
				String thisTmpFieldComment = this.extractToken("fieldComment", thisTmpFieldAttributes);
				
				String thisTmpFieldCommentExtracted = thisTmpFieldComment.equals("null") ? "" : "	/*" + thisTmpFieldComment + "*/"; 
				
				/*in case of only one field exists, adding 'Primary Key' constraint*/
				if (thisSubHashMapTable.size() == 1){
					thisEachTableSQLStatement += thisTmpFieldName + " " + thisTmpFieldType + "," + thisTmpFieldCommentExtracted  + "\n";
					if(!(getDB_SCHEMA_GENERATOR_MODE()==DEFAULT_COMPOSITE_TYPE_TABLE_MODE))
						thisEachTableSQLStatement += "CONSTRAINT " + tableName + "_pkey PRIMARY KEY(" + thisTmpFieldName + "))";
					else	thisEachTableSQLStatement += ")";
					
					if(!tableExtend.equals("null"))
						thisEachTableSQLStatement += "\nINHERITS (" + tableExtend + ")"; 
					
					thisEachTableSQLStatement += ";";
				}
				/*in case of first element, adding 'UNIQUE' constraint*/
				else if(thisTmpCount ==1){
					thisEachTableSQLStatement += thisTmpFieldName + " " + thisTmpFieldType + " UNIQUE," + thisTmpFieldCommentExtracted + "\n";
					thisFirstTableElement = thisTmpFieldName; 					
				}
				/*in case of last element, adding 'Primary Key' constraint at the end*/
				else if(thisTmpCount == thisSubHashMapTable.size()){
					thisEachTableSQLStatement += thisTmpFieldName + " " + thisTmpFieldType + "," + thisTmpFieldCommentExtracted + "\n";
					if(!(getDB_SCHEMA_GENERATOR_MODE()==DEFAULT_COMPOSITE_TYPE_TABLE_MODE))
						thisEachTableSQLStatement += "CONSTRAINT " + tableName + "_pkey PRIMARY KEY(" + thisFirstTableElement + "))";
					else
						thisEachTableSQLStatement += ")";
				
					if(!tableExtend.equals("null"))
						thisEachTableSQLStatement += "\nINHERITS (" + tableExtend + ")"; 
					
					thisEachTableSQLStatement += ";";
				}
				/*in case of other intermediate fields*/
				else
					thisEachTableSQLStatement += thisTmpFieldName + " " + thisTmpFieldType + "," + thisTmpFieldCommentExtracted + "\n";
				
				thisTmpCount += 1;
				
			}
			thisSQLStatement += thisEachTableSQLStatement +"\n\n"; 
			
		}
		return thisSQLStatement; 
		 
	}	

	private String extractToken(String targetField, String attributes) {
		String thisTmpSplittedToken[] = attributes.split("#");
		
		if(targetField.equals("tableName") || targetField.equals("fieldType"))
			return thisTmpSplittedToken[0]; 
		
		else if(targetField.equals("tableExtend") || targetField.equals("fieldComment"))
			return thisTmpSplittedToken[1]; 
		
		else if(targetField.equals("tableComment") || targetField.equals("fieldCollectionType"))
			return thisTmpSplittedToken[2];  
		
		else
			return null;
	}
	
	@Test
	public void testExtractToken(){
		String thisTmpTableAttributes = "image#document#This is a image table";
		String thisTmpFieldAttributes = "String#This is a type of a book title";
		
		String thisString[] = thisTmpTableAttributes.split("#");
		for (String string : thisString) {
			System.out.println(string);
			
		}
		
		assertEquals("image", thisString[0]);
		assertEquals("document", thisString[1]);
		assertEquals("This is a image table", thisString[2]);
		
	}

	/*
	 * Auxiliary function - pretty print MM table schema
	 */
//	@Ignore("completed testing printMMTableSchema()")
//	@Test
	public void printMMTableSchema() {
		
		/*uncomment below to test this method*/
//		this.testCreateMMTableSchemaGenerationFile(); 
		
		if(sqlTranslator.thisHashMapTableArrayList.size() >= 1){
			String thisBar = "# of tables to be created : " + thisHashMapTableArrayList.size() + "\n";  
			thisBar +=       "-------------------------------------------------------------\n";
			thisBar +=       "name\t\t field\t\t type\t\t constraint\n"; 
			thisBar +=       "-------------------------------------------------------------\n";
			System.out.println(thisBar);
			 
			for (Iterator iterator = thisHashMapTableArrayList.iterator(); iterator.hasNext();) {
				HashMap<String, HashMap<String, String>> thisUpperHashMap =	(HashMap<String, HashMap<String, String>>) iterator.next();	
				Object[] thisUpperHashTableName = thisUpperHashMap.keySet().toArray();
				
				String thisTableContents = "";
				
				/*only one table name for each upperHashMap containing every table*/
				String thisUpperHashMapTableName = (String)thisUpperHashTableName[0];
				thisTableContents += thisUpperHashMapTableName + "\n\t\t"; 
				
				/*each table HashMap*/
				HashMap<String, String> thisSubHashMapTable = thisUpperHashMap.get(thisUpperHashMapTableName); 
				
				int thisTmpCount = 0 ; 
				for (Iterator iterator2 = thisSubHashMapTable.keySet().iterator(); iterator2.hasNext();) {
					String thisTmpFieldName = (String) iterator2.next(); 
					String thisTmpFieldType = convertToValidFieldType(DBName.POSTGRESQL, thisSubHashMapTable.get(thisTmpFieldName), "null"); 
					
					/*in case of first element, adding 'Primary Key' constraint*/
					if (thisTmpCount == 0)
						thisTableContents += thisTmpFieldName +"\t\t|" + thisTmpFieldType + "\t\t|" + "Primary Key\n\t\t";
					else
						thisTableContents += thisTmpFieldName +"\t\t|" + thisTmpFieldType + "\n\t\t";
					
					thisTmpCount += 1;
					
				}	
				/*print out tableContents*/
				System.out.println(thisTableContents);
				
			}
			
		}else{
			Debug.println("[Warning] run MetadataSchemaGenerator.createMMTableSchmea() first - thisHashMapTableArrayList is null:" + this.getClass().getName());
			
		}
	}

	/*
	 * sub function of printMMTableSchema() for checking valid type
	 * TODO 1) replace with HashMap  
	 * 
	 */
	protected String convertToValidFieldType(String dbCategory, String fieldType, String thisTmpFieldCollectionType) {
		/*in case of POSTGRESQL*/
		if (dbCategory.equals(DBName.POSTGRESQL)){
			if(fieldType.equalsIgnoreCase("StringBuilder") || fieldType.equalsIgnoreCase("String"))
				return "text";
			
			else if(fieldType.equalsIgnoreCase("ParsedURL"))
				return "varchar(30)"; 
			
			else if(fieldType.equalsIgnoreCase("int"))
				return "float";  
			/*supporting array*/ 
			else if(fieldType.equalsIgnoreCase("ArrayList") && thisTmpFieldCollectionType.equalsIgnoreCase("String"))
				return "text[]"; 
			/*array of composite type e.g. Item[]*/ 
			else if(fieldType.equalsIgnoreCase("ArrayList"))
				return thisTmpFieldCollectionType + "[]";
			else
				return fieldType;
		}	
		return null;
		
	}	


	/*
	 * list target metadata files in designated directory 
	 */
	public String[] listOfTargetMetadataFiles() {
		if (thisMMDTargetDirectory.isDirectory())
			return thisMMDTargetDirectory.list(); 
		else
			return null; 

	}
	
	
	/**
	 * @return the dB_SCHEMA_GENERATOR_MODE
	 */
	public static int getDB_SCHEMA_GENERATOR_MODE() {
		return DB_SCHEMA_GENERATOR_MODE;
	}

	/**
	 * @param db_schema_generator_mode the dB_SCHEMA_GENERATOR_MODE to set
	 */
	public static void setDB_SCHEMA_GENERATOR_MODE(int db_schema_generator_mode) {
		DB_SCHEMA_GENERATOR_MODE = db_schema_generator_mode;
	}

	public static void main(String args[]) throws SIMPLTranslationException, IOException{
		sqlTranslator thisDBSchemaGenerator = new sqlTranslator();
		thisDBSchemaGenerator.testCreateMMDTableSchema(); 
		
	}
}
