package ecologylab.generic;

import java.util.*;
import java.io.File;

import ecologylab.generic.*;
import ecologylab.generic.AssetsCache.Assets;

/**
 * Collects paths and methods for accessing properties that store's on the
 * participant's machine and then accesses again.
 */
public class PropertiesAndDirectories 
extends Debug 
{
	protected static File		USER_DIR;
	protected static File		DESKTOP_DIR;

	protected static File		APPLICATION_DATA_DIR;
	protected static File		THIS_APPLICATION_DIR;

	protected static File		LAUNCH_DIR;
    protected static File		TEMP_DIR;
    protected static File		_DIR;
   
    public static final int UNKNOWN=0, WINDOWS=1, LINUX=2, MAC_OLD=3, MAC=4, OTHER_UNIX=5;
	
	protected static final int os; //should be final
	
	static
	{
		String osName	= System.getProperty("os.name");
		if (osName != null)
		{
			osName		= osName.toLowerCase();
			if (Generic.contains(osName, "windows"))
				os		= WINDOWS;
			else if (Generic.contains(osName, "mac os x"))
				os		= MAC;
			else if (Generic.contains(osName, "mac os"))
				os		= MAC_OLD;
			else if (Generic.contains(osName, "linux"))
				os		= LINUX;
			else if (Generic.contains(osName, "sunos") || Generic.contains(osName, "solaris") ||
					  Generic.contains(osName, "hp-ux") || Generic.contains(osName, "freebsd") ||
					  Generic.contains(osName, "irix") || Generic.contains(osName, "aix"))
				os		= OTHER_UNIX;
			else
				os		= UNKNOWN;			
		}
		else
			os		= UNKNOWN;		
		
		println("System Properties: " +System.getProperties());
	}
	
	public static int os()
	{
		return os;
	}
	static String		applicationName;
	
	public static void setApplicationName(String apName)
	{
		applicationName = apName;
	}
/**
 * This function now uses the Assets class that manages caching and retrieval of
 * assets.
 * 
 * @return		The directory where we like to cache files.
 *				not user files (information spaces), but our application files.
 */
	public static File thisApplicationDir()
	{
		File result = THIS_APPLICATION_DIR;
		if (result == null)
		{
		   File apDataDir			= applicationDataDir();		
		   if (PropertiesAndDirectories.os() == WINDOWS)
			   result				= Files.newFile(apDataDir, applicationName);
		   else
			   result				= Files.newFile(apDataDir, "." + applicationName);
		   
			if (!result.exists())
				result.mkdir();
			
			THIS_APPLICATION_DIR		= result;
//			Assets.setCacheRoot(result);
		}
		return result;
	}
/**
 * @return		the appropriate directory for storing temporary files.
 */
	public static File tempDir()
	{
	   File result		= TEMP_DIR;
	   if (result == null)
	   {
		  File sysTempDirName	= sysTempDirName();
		  if (sysTempDirName != null)
			 result		= sysTempDirName;
		  else
		  {
			 result		= Files.newFile(thisApplicationDir(), "temp");
			 result.mkdir();
			 if (!result.exists())
			 {
			 	result		= Files.newFile(userDir(), "temp");
				result.mkdir();
			 }
		  }
		  TEMP_DIR		= result;
		  println("TEMP_DIR = "+TEMP_DIR);
	   }
	   return result;
	}
	
/**
 * @return		name of the appropriate directory for storing temporary files.
 */
	public static File sysTempDirName()
	{
		File result			= null;
		switch (PropertiesAndDirectories.os())
		{
			case WINDOWS:
				String s1		= "c:/temp/";
				File f1			= new File(s1);
				if (f1.exists() && f1.canRead() && f1.canWrite())
				{
				   result		= f1;
				}
				else
				{
				   String s2	= "c:/wutemp/";
				   File f2		= new File(s2);
				   if (f2.exists() && f2.canRead() && f2.canWrite())
				   {
					  result	= f2;
				   }
				   else
				   {
					  String osDirName	= 
						 System.getProperty("deployment.system.profile");
					  if (osDirName != null)
					  {
						 File osDir		= Files.newFile(osDirName);
						 File osTempDir	= Files.newFile(osDir, "temp");
						 if (osTempDir.exists() && osTempDir.canRead() && osTempDir.canWrite())
							result		= osTempDir;
						 else
						 {
							if (f1.mkdir())
							   result	= f1;
							else
							{
								// use (and make if necessary) temp dir inside our application dir
								result = Files.newFile (thisApplicationDir(), "\temp");
								if( !result.exists() )
									result.mkdir();
							}
						 }
					  }
					  else
					  {
							// use (and make if necessary) temp dir inside our application dir
							result = Files.newFile(thisApplicationDir(), "temp");
							if( !result.exists() )
								result.mkdir();
					  }
				   }
				}
				break;
			default:
				result	= Files.newFile("/tmp/");
		}
		return result;
	}
	
	static String sysProperty(String propName)
	{
	   return System.getProperty(propName);
	}

/**
 * On Windows, this is c:\Documents and Settings\User Name\Application Data
 */
	public static File applicationDataDir()
	{
	   File result = APPLICATION_DATA_DIR;
	   if (result == null)
	   {
		   String fileName = sysProperty("deployment.user.profile");
		   
		   if (fileName == null) //for Mac OS X (and some windows!!)
		   {
			   fileName = sysProperty("user.home");
			   File appDataDir = new File(fileName, "Application Data");
			   
			   if (appDataDir.exists()) //use the windows 'application data' directory if possible
				   result = appDataDir;
			   else
				   result = Files.newFile(fileName);
		   }
		   else //use the windows 'application data' directory if possible
		   {
			   File appDataDir = new File(fileName, "Application Data");
			   if (appDataDir.exists())
				   result = appDataDir;
			   else
				   result = Files.newFile(fileName);
		   }
		   
		  APPLICATION_DATA_DIR	= result;
	   }
	   return result;
	}

/**
 * A default place for storing the user's files.
 * 
 * In Windows, this is typically c:\\Documents and Settings\\username
 */
	public static File userDir()
	{
	   File result		= USER_DIR;
	   if (result == null)
	   {
	   	  String httpAgent	= System.getProperty("http.agent");
	   	  if (httpAgent == null)
	   	  	result	= Files.newFile(sysProperty("user.home"));
		  else if ((PropertiesAndDirectories.os() == WINDOWS) && 
			  (httpAgent.startsWith("Mozilla")))
		  {
			 File appDataDir	= applicationDataDir();
			 result	= Files.newFile(appDataDir.getParent(), "My Documents");
		  }
		  else
			 result	= Files.newFile(sysProperty("user.dir"));

		  if (result == null)
			  result = Files.newFile(sysProperty("user.home"));

		  USER_DIR		= result;
	   }
	   return result;
	}
/**
 * A default place for storing the user's files.
 * 
 * In Windows, this is typically c:\\Documents and Settings\\username
 */
	public static File desktopDir()
	{
	   File result		= DESKTOP_DIR;
	   if (result == null)
	   {
		  //make sure that this works with the application version of cF too.
		  if ((PropertiesAndDirectories.os() == WINDOWS) && 
			  (System.getProperty("http.agent") == null || //since the app version has no agent  
			  System.getProperty("http.agent").startsWith("Mozilla")))
		  {
			 File appDataDir	= applicationDataDir();
			 System.out.println("appDataDir: " + appDataDir);
			 if (appDataDir.toString().matches("Application Data"))
				 result	= Files.newFile(appDataDir.getParentFile().getParent(), "Desktop");
			 else
				 result	= Files.newFile(appDataDir.getParent(), "Desktop");
		  }
		  else if (PropertiesAndDirectories.os() == MAC)
		  {
			  result = (Files.newFile(sysProperty("user.home") + "/Desktop"));
		  }
		  else
			 result	= Files.newFile(sysProperty("user.dir"));

		  DESKTOP_DIR		= result;
		  println("DESKTOP_DIR = "+DESKTOP_DIR);
	   }
	   return result;
	}
/**
 * For an application, the directory launched from.
 * Derived from System property = user.home, which doesn't seem to contain
 * what you might think it would.
 */	
	public static File getLaunchDir()
	{
	   String launchDirName = System.getProperty("user.home");
	   return Files.newFile(launchDirName);
	}
		
	public static void printProperties()
	{
		Properties p = System.getProperties();
		println("\nProperties TEST\n" + p);				
	}
	
	public static void main(String args[])
	{
		PropertiesAndDirectories.printProperties();		
	}				
	
	/**
	 * Root directory of the current user's workspace.
 	**/
	public static String userDirName()
	{
		return "";
	}
	
	public static File javaDeploymentProfile()
	{
		File deploymentFile;
		if (os() == WINDOWS) //tested with XP and 2000
		{
			deploymentFile = 
				new File(applicationDataDir(), "Sun/Java/Deployment/deployment.properties");
		}
		//tested on OSX. 
		else if(os() == MAC)
		{
			deploymentFile = 
				new File(sysProperty("deployment.user.home"), "deployment.properties");
		}
		else //Probably works in linux (not tested on linux yet!!!) TODO test on linux
		{
			deploymentFile = 
				new File(sysProperty("user.home"), ".java/deployment/deployment.properties");
		}
		
		if (deploymentFile.exists())
			return deploymentFile;
		else
			return null;
	}
	
	public static boolean runningAsApplication()
	{
		return System.getProperty("http.agent") == null;
	}
}
