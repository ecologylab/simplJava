package translators.sql;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.junit.Test;

import translators.sql.testing.ecologylabXmlTest.AcmProceedingTest;
import translators.sql.testing.ecologylabXmlTest.DocumentTest;
import ecologylab.generic.Debug;
import ecologylab.serialization.SIMPLTranslationException;

public class DBUtil extends Debug implements DBInterface
{
	private Connection	thisConnection;

	private Statement		thisStatement;

	@Test
	public void testDBUtilScenario()
	{
		System.out.println("this is first test");

		Connection isConnected = connectToDB();
		if (isConnected != null)
		{
			boolean isSerialized = serialize(DocumentTest.class);
			Object thisDeserializedClass = deserialize();

			DocumentTest thisDocumentClass = null;
			if (thisDeserializedClass instanceof DocumentTest)
			{
				thisDocumentClass = (DocumentTest) thisDeserializedClass;
				System.out.println(thisDocumentClass.getClassName());

			}

		}
		else
			System.out.println("db connection failed");

	}

	/**
	 * DBConnection cf. jdbc:[drivertype]:[database] //hostname:portnumber/databasename
	 * 
	 * @param dbURI
	 * @param userName
	 * @param password
	 * @return connection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public Connection connectToDB(String dbURI, String userName, String password)
			throws SQLException, ClassNotFoundException
	{
		Class.forName(POSTGRESQL_DRIVER);

		thisConnection = DriverManager.getConnection(dbURI, userName, password);
		thisConnection.setAutoCommit(POSTGRESQL_DEFAULT_COMMIT_MODE);
		if (thisConnection != null)
		{
			DatabaseMetaData thisDBMetadata = thisConnection.getMetaData();
			println("(" + thisDBMetadata.getUserName() + ") are connected to (" + thisDBMetadata.getURL()
					+ ") " + thisDBMetadata.getDatabaseProductName() + " using "
					+ thisDBMetadata.getDriverVersion());

		}
		else
		{
			println("DB connection is not created");
		}

		return thisConnection;
	}

	/**
	 * connect to default db
	 * 
	 * @return connection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public Connection connectToDB() throws SQLException, ClassNotFoundException
	{
		thisConnection =  this.connectToDB(POSTGRESQL_DEFAULT_URI, POSTGRESQL_DEFAULT_USER_NAME,
				POSTGRESQL_DEFAULT_PWD);
		
		return thisConnection; 

	}

	/**
	 * execute select Query
	 * 
	 * @param query
	 * @return resultSet
	 * @throws SQLException
	 */
	public ResultSet executeSelectQuery(String selectQuery) throws SQLException
	{
		if (thisConnection != null)
		{
			thisStatement = thisConnection.createStatement();
			ResultSet thisResultSet = thisStatement.executeQuery(selectQuery);

			return thisResultSet;
		}
		else
			println("db connection is not created : create db connection first.");

		return null;
	}

	/**
	 * execute update Query
	 * 
	 * @param updateQuery
	 * @return number of rows affected
	 * @throws SQLException
	 */
	public int executeUpdateQuery(String updateQuery) throws SQLException
	{
		if (thisConnection != null)
		{
			thisStatement = thisConnection.createStatement();
			int thisUpdatedRows = thisStatement.executeUpdate(updateQuery);
			return thisUpdatedRows;
		}
		else
			println("db connection is not created : create db connection first.");

		return -1;
	}

	/**
	 * show data
	 * 
	 * @param thisResultSet
	 * @throws SQLException
	 */
	public void showResultSetDBData(ResultSet thisResultSet) throws SQLException
	{
		StringBuilder thisColumnNameStringBuilder = new StringBuilder();
		StringBuilder thisColumnTypeStringBuilder = new StringBuilder();
		StringBuilder thisDataStringBuilder = new StringBuilder();

		Connection thisColumnCountthisResultSet;
		int thisColumnCount = thisResultSet.getMetaData().getColumnCount();

		// note that column count start from '1'
		for (int i = 1; i <= thisColumnCount; i++)
		{
			thisColumnNameStringBuilder.append(thisResultSet.getMetaData().getColumnName(i).trim()
					+ "|   ");
			thisColumnTypeStringBuilder.append(thisResultSet.getMetaData().getColumnTypeName(i).trim()
					+ "|   ");

		}
		thisColumnNameStringBuilder.append("\n").append(thisColumnTypeStringBuilder.toString().trim())
				.append("\n");

		System.out.println(thisColumnNameStringBuilder);

		// retrieve stored data
		while (thisResultSet.next())
		{
			for (int i = 1; i <= thisColumnCount; i++)
			{
				// should trim unnecessary space for pretty display
				thisDataStringBuilder.append(thisResultSet.getObject(i).toString().trim() + "  ");
			}
			thisDataStringBuilder.append("\n").trimToSize();
		}
		thisDataStringBuilder.append("\n").trimToSize();

		System.out.println(thisDataStringBuilder.toString().trim());
	}

	@Test
	public void testShowResultSetDBData() throws SQLException, ClassNotFoundException
	{
		this.connectToDB();
		ResultSet thisQueryResults = this.executeSelectQuery("select * from bookinfo;");

		this.showResultSetDBData(thisQueryResults);

	}

	@Test
	/*
	 * * Main test method
	 */
	public void testDBConnection() throws ClassNotFoundException, SQLException
	{
		this.connectToDB();
		ResultSet thisQueryResults = this.executeSelectQuery("select * from bookinfo;");

		int thisRetrievedColumnCount = thisQueryResults.getMetaData().getColumnCount();

		// note that column count start from 1
		while (thisQueryResults.next())
		{
			for (int i = 1; i <= thisRetrievedColumnCount; i++)
			{
				System.out.println("data type : " + thisQueryResults.getMetaData().getColumnClassName(i));
				System.out.println("column name : " + thisQueryResults.getMetaData().getColumnName(i));
				System.out.println("thisQueryResults: " + thisQueryResults.getObject(i));
			}
		}

		/*
		 * updateQuery
		 */
		// int thisExecuteUpdateResult =
		// thisStatement.executeUpdate("update bookinfo set sell_price = 12000 where book_name='Korean'");
		// System.out.println(thisExecuteUpdateResult + " rows has been updated");
		this.closeDBConnection();

	}

	
	@Test
	public void testDBSerializer() throws SQLException, ClassNotFoundException, SIMPLTranslationException, IOException
	{
		// ** cf. http://www.postgresql.org/docs/7.1/static/jdbc-ext.html -> large object, 
		// largeobjectManager
		// ** http://www.postgresql.org/docs/7.4/interactive/jdbc-binary-data.html
		// cf2. http://jdbc.postgresql.org/documentation/publicapi/index.html
		// ***cf3. http://www.javabeginner.com/uncategorized/java-serialization
		// * cf4. 'java database object serialization' in Google 
		// *postgresql jdbc api - http://jdbc.postgresql.org/documentation/publicapi/index.html
		// *serialize in mysql - http://www.java2s.com/Code/Java/Database-SQL-JDBC/HowtoserializedeserializeaJavaobjecttotheMySQLdatabase.htm
		
		// TODO target class to be serialized
		// AcmProceedingTest.class; PdfTest.class; --> does not implements Serializable
		
		connectToDB(); 
		String thisStringForWriteObject = "insert into java_objects(name, object_value) values (?, ?)";
		String thisStringForReadObject = "select object_value from java_objects where id = ?";
		
		String className = AcmProceedingTest.class.getName();
		
		PreparedStatement thisWritePreparedStatement = thisConnection.prepareStatement(thisStringForWriteObject);
		
		// test object 1
		List<Object> thisList = new ArrayList<Object>(); 
		thisList.add("hello");
		thisList.add(new Integer(1234));
		thisList.add(new Date());
		
		// test object 2
		//add serializable
		/**
		 * TODO convert class to byte array
		 */
		thisWritePreparedStatement.setString(1, "testObject");
		thisWritePreparedStatement.setBytes(2, 
				this.convertClassToByteArray(AcmProceedingTest.class));

		thisWritePreparedStatement.executeUpdate(); 
		
		ResultSet thisResultSets = thisWritePreparedStatement.getGeneratedKeys();
		int id = -1;
		if(thisResultSets.next()){
			id = thisResultSets.getInt(1);
			
		}
		
		//should commit to be stored into db 
		thisConnection.commit(); 
		
		System.out.println("writing java object is done");
		

	}
	
	@Test
	public void testByteArray() throws UnsupportedEncodingException{
		String thisByteArray = "byteaArrayzZ";
		byte[] thisBytes = thisByteArray.getBytes();
		
		for (byte b : thisBytes)
		{
			System.out.println(b);
		}
	}
	
	/**
	 * object byte-array converter 
	 * target object should implement Serializable 
	 * 
	 * @param obj
	 * @return
	 * @throws IOException 
	 */
	public byte[] convertClassToByteArray(Object obj) throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.flush(); 
		oos.close(); 
		bos.close(); 
		byte[] thisConvertedByteArray = bos.toByteArray(); 
		return thisConvertedByteArray; 
		
	}
	
	@Test
	public void testConvertClassToByteArray() throws IOException{
		byte[] thisByteArray = this.convertClassToByteArray(AcmProceedingTest.class);
		System.out.println(thisByteArray);
		
	}

	/**
	 * close db connection 
	 * 
	 * @throws SQLException
	 */
	public void closeDBConnection() throws SQLException
	{
		if (thisStatement != null && thisConnection != null)
		{
			thisStatement.close();
			thisConnection.close();

			println("db is disconnected successfully");

		}
		else
			println("DB is already disconnected");

	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

}
