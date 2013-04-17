package simpl;

import ecologylab.appframework.PropertiesAndDirectories;
import ecologylab.generic.ReflectionTools;

public class PlatformSpecifics {
	private static SimplPlatformSpecifics	iFundamentalPlatformSpecifics;

	private static boolean												dead	= false;

	public static void set(SimplPlatformSpecifics that)
	{
		iFundamentalPlatformSpecifics = that;
	}

	public static SimplPlatformSpecifics get()
	{
		if (dead)
			throw new RuntimeException("Can't initialize FundamentalPlatformSpecifics");

		if (iFundamentalPlatformSpecifics == null)
			synchronized (SimplPlatformSpecifics.class)
			{
				String className = null;

				if (iFundamentalPlatformSpecifics == null)
				{
					if (PropertiesAndDirectories.os() == PropertiesAndDirectories.ANDROID)
						className = "FundamentalPlatformSpecificsAndroid";
					else
						className = "FundamentalPlatformSpecificsSun";
				}

				if (className != null)
				{
					Class platformSpecificsClass = null;
					try
					{
						platformSpecificsClass = Class.forName("ecologylab.platformspecifics." + className);
					}
					catch (ClassNotFoundException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
//						throw new RuntimeException("Can't initialize FundamentalPlatformSpecifics" + className);
					}
					if (platformSpecificsClass == null)
					{
						dead = true;
						throw new RuntimeException("Can't initialize FundamentalPlatformSpecifics");
					}
					else{
                        iFundamentalPlatformSpecifics = (SimplPlatformSpecifics)ReflectionTools.getInstance(platformSpecificsClass);
                    }
                }
			}

		return iFundamentalPlatformSpecifics;
	}
}
