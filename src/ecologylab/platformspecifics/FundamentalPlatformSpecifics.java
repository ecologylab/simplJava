package ecologylab.platformspecifics;

import ecologylab.appframework.PropertiesAndDirectories;
import ecologylab.generic.ReflectionTools;



public class FundamentalPlatformSpecifics {
	private static IFundamentalPlatformSpecifics iFundamentalPlatformSpecifics;
	
	private static boolean dead = false;
	
	public static void set (IFundamentalPlatformSpecifics that)
	{
		iFundamentalPlatformSpecifics = that;
	}
	
	public static IFundamentalPlatformSpecifics get() 
	{
		if (dead) 
			throw new RuntimeException("Can't initialize FundamentalPlatformSpecifics");
		
		if (iFundamentalPlatformSpecifics == null)
			synchronized(FundamentalPlatformSpecifics.class)
			{
				String className  =  null;
				
				if (iFundamentalPlatformSpecifics == null)
				{
					if (PropertiesAndDirectories.os() ==  PropertiesAndDirectories.ANDROID)
						className 	=  "FundamentalPlatformSpecificsAndroid";
					else
						className   =  "FundamentalPlatformSpecificsSun";	
				}
				
				if (className != null) 
				{
					Class platformSpecificsClass;
					try {
						platformSpecificsClass = Class.forName("ecologylab.platformspecifics." + className);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw new RuntimeException("Can't initialize FundamentalPlatformSpecifics");
					}
					if (platformSpecificsClass == null)
					{
						dead = true;
						throw new RuntimeException("Can't initialize FundamentalPlatformSpecifics");
					}
					else
						iFundamentalPlatformSpecifics = ReflectionTools.getInstance(platformSpecificsClass);
				}
			}
		
		return iFundamentalPlatformSpecifics;
	}
}
