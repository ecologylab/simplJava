package ecologylab.appframework;

import java.io.File;
import java.util.Properties;

import ecologylab.appframework.types.Preference;
import ecologylab.generic.Debug;
import ecologylab.generic.Generic;
import ecologylab.io.Files;


/**
 * Collects paths and methods for accessing properties that store's on the
 * participant's machine and then accesses again.
 */
public class PropertiesAndDirectories 
extends Debug 
{
	protected static File		USER_DIR;
	protected static File		DESKTOP_DIR;
	protected static File 		LOG_DIR;

	protected static File		APPLICATION_DATA_DIR;
	protected static File		THIS_APPLICATION_DIR;

	protected static File		LAUNCH_DIR;
    protected static File		TEMP_DIR;
    protected static File		_DIR;
   
    public static final int UNKNOWN=0, WINDOWS=1, LINUX=2, MAC_OLD=3, MAC=4, OTHER_UNIX=5;
    public static final String OS_NAMES[] =
    {
    	"Unknown", "Windows", "Linux", "Mac old", "Mac OSX", "Unix",
    };
	
	protected static int os; //should be final
	
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
		if (os == UNKNOWN)
		{
			String osPreference = Preference.lookupString("os");
			if (osPreference != null)
			{
				osPreference	= osPreference.toLowerCase();
				if (osPreference.indexOf("windows") != -1)
					os			= WINDOWS;
				else if (osPreference.indexOf("mac") != -1)
					os			= MAC;
				else if (osPreference.indexOf("linux") != -1)
					os			= LINUX;				
			}
		}
		return os;
	}
	public static String getOsName()
	{
		return OS_NAMES[os()];
	}
	static String		applicationName;
	
	public static void setApplicationName(String apName)
	{
		applicationName = apName;
	}

	public static String applicationName()
	{
		return applicationName;
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
			//println("thisApplicationDir() apDataDir="+apDataDir+" applicationName="+applicationName +" os()="
			//	   +os());
			switch (os)
			{
			case WINDOWS:
			case MAC:
			case MAC_OLD: 
				result	= Files.newFile(apDataDir, applicationName);
				break;
			case LINUX:
			case OTHER_UNIX:
			case UNKNOWN:
				result	= Files.newFile(apDataDir, "." + applicationName);
			}
			
			result				= createDirsAsNeeded(result);
			if (result != null)
				THIS_APPLICATION_DIR= result;
		}
		return result;
	}
	
	/**
	 * @return		the appropriate directory for storing temporary files.
	 */
	public static File logDir()
	{
		File result		= LOG_DIR;
		if (result == null)
		{
			File apDataDir			= thisApplicationDir();
			
			if (apDataDir != null)
			{
				if (os() == WINDOWS)
					result			= Files.newFile(apDataDir, "log");
				else
					result			= Files.newFile(apDataDir, "." + "log");
				
				result				= createDirsAsNeeded(result);
				if (result != null)
					LOG_DIR		= result;
			}
			println("LOG_DIR = "+LOG_DIR);
		}
		return result;
	}
	/**
	 * Create directories associated with this path, if possible.
	 * 
	 * @param path
	 * @return	null if it was not possible to create the path, otherwise, the argument passed in.
	 */
	public static File createDirsAsNeeded(File path)
	{
		return path.exists() ? path : (path.mkdirs() ? path : null);
	}
/**
 * @return		the appropriate directory for storing temporary files.
 */
	public static File tempDir()
	{
	   File result		= TEMP_DIR;
	   if (result == null)
	   {
		  File sysTempDir	= sysTempDir();
		  if (sysTempDir != null)
			 result		= sysTempDir;
		  else
		  {
			 result		= Files.newFile(thisApplicationDir(), "temp");
			 result		= createDirsAsNeeded(result);
			 if (result == null)
			 {
			 	result	= Files.newFile(userDir(), "temp");
			 	result	= createDirsAsNeeded(result);
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
	public static File sysTempDir()
	{
		File result			= null;
		String javaTmpDirStr= System.getProperty("java.io.tmpdir");
		File javaTmpDir		= new File(javaTmpDirStr);
		if (javaTmpDir.exists())
			result			= javaTmpDir;
		else if (javaTmpDir.mkdirs())
			result			= javaTmpDir;
		else
		{
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
								if (createDirsAsNeeded(f1) != null)
								   result	= f1;
								else
								{
									// use (and make if necessary) temp dir inside our application dir
									result = Files.newFile (thisApplicationDir(), "\temp");
									result = createDirsAsNeeded(result);
								}
							 }
						  }
						  else
						  {
								// use (and make if necessary) temp dir inside our application dir
								result = Files.newFile(thisApplicationDir(), "temp");
								result = createDirsAsNeeded(result);
						  }
					   }
					}
					break;
				default:
					result	= Files.newFile("/tmp/");
					result = createDirsAsNeeded(result);
			}
		}
		return result;
	}
	
	public static String sysProperty(String propName)
	{
	   return System.getProperty(propName);
	}

/**
 * On Windows, this is typically c:\Documents and Settings\User Name\Application Data
 */
	public static File applicationDataDir()
	{
	   File result = APPLICATION_DATA_DIR;
	   if (result == null)
	   {
		   String fileName = sysProperty("deployment.user.profile");
		   println("deployment.user.profile=" + fileName);
		   
		   if (fileName == null) //for Mac OS X (and some windows!!)
		   {
			   fileName = sysProperty("user.home");
			   println("user.home=" + fileName);
		   }
		   File appDataDir;
		   switch (os) 
		   {
		   case WINDOWS: 	appDataDir = new File(fileName, "Application Data");
		   					break;
		   case MAC:		appDataDir = new File(fileName, "Library/Application Support");
		   					break;
		   case LINUX:
		   case OTHER_UNIX:
		   case UNKNOWN:
		   default:			appDataDir = new File(fileName);
				   
		   }
		   result		= createDirsAsNeeded(appDataDir);
		   
		   if (result == null)
		   {
			   result	= createDirsAsNeeded(Files.newFile(fileName));
		   }
		  println("applicationDataDir() = " + result);
		  APPLICATION_DATA_DIR	= result;
	   }
	   return result;
	}

/**
 * A default place for storing the user's files.
 * 
 * In Windows, this is typically c:\\Documents and Settings\\username\\My Documents
 * 
 * In unix-based OSes, it is typically ~.
 */
	public static File userDir()
	{
	   File result		= USER_DIR;
	   if (result == null)
	   {
		  switch (PropertiesAndDirectories.os())
		  {
		  case WINDOWS:
			  File appDataDir	= applicationDataDir();
			  File appDataParent= Files.newFile(appDataDir.getParent());
			  result	= Files.newFile(appDataParent, "My Documents");
			  if (!result.exists())
				  result		= Files.newFile(appDataParent, "Personal");
			  break;
		  default:
			  result = Files.newFile(sysProperty("user.home"));
			  break;
		  }

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
	
	/**
	 * Path to the Java deployment properties file.
	 * This is, among other things, where Java Plug-in properties are stored.
	 * 
	 * @return	Properties File for Java deployment.
	 */
	public static File javaDeploymentProfile()
	{
		File deploymentFile;
		int os = os();
		switch (os)
		{
		case WINDOWS: 	//tested with XP and 2000
			deploymentFile = 
				new File(applicationDataDir(), "Sun/Java/Deployment/deployment.properties");
			break;
		case MAC:		//tested on OSX
			deploymentFile = 
				new File(sysProperty("deployment.user.home"), "deployment.properties");
			break;
		default:
			// Probably works in linux (not tested on linux yet!!!) TODO test on linux
			deploymentFile = 
				new File(sysProperty("user.home"), ".java/deployment/deployment.properties");
			break;
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
	
	public static void setOSSpecificProperties()
	{
		switch (os())
		{
		case MAC:
			System.setProperty("com.apple.macos.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", applicationName);
		}
		
	}
	public static final boolean	isMac	= (os() == MAC);
}
