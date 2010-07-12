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

import ecologylab.generic.Debug;

public class DBUtil extends Debug
{	
	@Test
	public void testDBUtilScenario(){
		System.out.println("this is first test");
//		
//		boolean isConnected = dbConnect("postgres", "postgresql", "127.0.0.1", "5432");
//		if(isConnected){
//			boolean isSerialized = serialize(DocumentTest.class);
//			Object thisDeserializedClass = deserialize();
//			
//			DocumentTest thisDocumentClass = null; 
//			if(thisDeserializedClass instanceof DocumentTest){
//				thisDocumentClass = (DocumentTest) thisDeserializedClass; 
//				System.out.println(thisDocumentClass.getClassName());
//				
//			}
//			
//		}else
//			System.out.println("db connection failed");
//		
	}
	
	@Test
	public void testDBConnection() throws ClassNotFoundException, SQLException{
		System.out.println("this is second test");
		
			Class<?> thisPostgreSqlDriver = Class.forName("org.postgresql.Driver");
			
			/*
			 * db connection 
			 */
			Connection thisConnection = null; 
			thisConnection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/", 
					"comma78", "postgresql"); 
			
			/*
			 * get db metadata
			 */
			if(thisConnection != null){
				DatabaseMetaData thisDBMetadata = thisConnection.getMetaData();
				System.out.println("(" + thisDBMetadata.getUserName() + ") are connected to : " 
						+ thisDBMetadata.getURL() 
						+ " (" + thisDBMetadata.getDatabaseProductName() 
						+ ") using " + thisDBMetadata.getDriverVersion()); 

			}else{
				Debug.println("db connection failed");
			}
			
			Statement thisStatement = thisConnection.createStatement();
			
			
			/*
			 * executeQuery
			 */ 
			ResultSet thisQueryResults = thisStatement.executeQuery("select * from bookinfo;");
			
			int thisRetrievedColumnCount = thisQueryResults.getMetaData().getColumnCount();
			StringBuilder thisResultSets = new StringBuilder(); 
			
			while(thisQueryResults.next()){
				//TODO multiple resultsets ref. java advanced how-to
				
				
			}
			
			
			
			
//			
//			while(thisQueryResults.next()){
//				String thisResult = thisQueryResults.getString(1);
//				String thisResult2 = thisQueryResults.getString(2);
//				String thisResult3 = thisQueryResults.getString(3);
//				String thisResult4 = thisQueryResults.getString(4);
//				String thisResult5 = thisQueryResults.getString(5);
//				
//				System.out.println(thisResult + " " + thisResult2 + " " 
//						+ thisResult3 + " " + thisResult4 + " " + thisResult5);
//			}
			
			/*
			 * updateQuery
			 */ 
//			int thisExecuteUpdateResult = thisStatement.executeUpdate("update bookinfo set sell_price = 12000 where book_name='Korean'");
//			System.out.println(thisExecuteUpdateResult + " rows has been updated");
			
			
			
	}
	
	@Test
	public void testDBSerializer(){
		System.out.println("this is third test");
		
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

}
