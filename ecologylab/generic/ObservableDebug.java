package cm.generic;

import java.util.*;

public class ObservableDebug
extends Observable
{
   public void debug(String message)
   {
      Debug.println(this, message);
   }
   public void debugA(String message)
   {
      Debug.printlnA(this, message);
   }
   public void debugI(String message)
   {
      Debug.printlnI(this, message);
   }

   public void debug(int messageLevel, String message)
   {
      Debug.println(this, messageLevel, message);
   }
   
   public void debugA(int level, String message)
   {
      Debug.printlnA(this, level, message);
   }
   public void debugI(int level, String message)
   {
      Debug.printlnI(this, level, message);
   }
   
   public String getClassName()
   {
      return Debug.getClassName(this);
   }
   public static void println(String message)
   {   	
      Debug.println(message);
   }
   public static void println(StringBuffer buffy)
   {   	
      Debug.println(buffy);
   }
   public String toString()
   {
      return Debug.toString(this);
   }
   public static void println(String className,
			      int messageLevel, String message) 
   {
      Debug.println(className, messageLevel, message);
   }
}
