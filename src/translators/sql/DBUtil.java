package translators.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import translators.sql.testing.ecologylabXmlTest.AcmProceedingTest;
import translators.sql.testing.ecologylabXmlTest.PdfTest;

import ecologylab.generic.Debug;

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
		return this.connectToDB(POSTGRESQL_DEFAULT_URI, POSTGRESQL_DEFAULT_USER_NAME,
				POSTGRESQL_DEFAULT_PWD);

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
					+ "|\t");
			thisColumnTypeStringBuilder.append(thisResultSet.getMetaData().getColumnTypeName(i).trim()
					+ "|\t");

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
				thisDataStringBuilder.append(thisResultSet.getObject(i).toString().trim() + "\t");
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
		this.disconnectDB();

	}

	@Test
	public void testDBSerializer()
	{
		// TODO target class to be serialized
		// AcmProceedingTest.class; PdfTest.class;

	}

	/**
	 * disconnect db
	 * 
	 * @throws SQLException
	 */
	public void disconnectDB() throws SQLException
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
