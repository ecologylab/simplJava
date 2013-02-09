package simpl.tools;

import java.lang.reflect.Field;
import java.util.List;

import simpl.exceptions.SIMPLTranslationException;

public class ReflectionTools {

	
	public static Object getFieldValue(Field f, Object context) throws SIMPLTranslationException
	{
		try{
			return f.get(context);
		}
		catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			return f.get(context);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	public static boolean getFieldBooleanValue(Field f, Object context) throws SIMPLTranslationException
	{
		try{
			return f.getBoolean(context);
		}
		catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			return f.getBoolean(context);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	public static byte getFieldByteValue(Field f, Object context) throws SIMPLTranslationException
	{
		try{
			return f.getByte(context);
		}
		catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			return f.getByte(context);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	public static int getFieldIntValue(Field f, Object context) throws SIMPLTranslationException
	{
		try{
			return f.getInt(context);
		}
		catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			return f.getInt(context);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	public static short getFieldShortValue(Field f, Object context) throws SIMPLTranslationException
	{
		try{
			return f.getShort(context);
		}
		catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			return f.getShort(context);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	public static long getFieldLongValue(Field f, Object context) throws SIMPLTranslationException
	{
		try{
			return f.getLong(context);
		}
		catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			return f.getLong(context);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	public static char getFieldCharValue(Field f, Object context) throws SIMPLTranslationException
	{
		try{
			return f.getChar(context);
		}
		catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			return f.getChar(context);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	public static float getFieldFloatValue(Field f, Object context) throws SIMPLTranslationException
	{
		try{
			return f.getFloat(context);
		}
		catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			return f.getFloat(context);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	public static double getFieldDoubleValue(Field f, Object context) throws SIMPLTranslationException
	{
		try{
			return f.getDouble(context);
		}
		catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			return f.getDouble(context);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	public static void setFieldValue(Object value, Field f, Object context) throws SIMPLTranslationException
	{
		try{
			f.set(context, value);
		}catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			f.set(context, value);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	// All of these methods basically copy paste the functionality, because Java doesn't give us a nice way to delegate the functional piece.
	// Just keep that in mind if you make changes to this flow. 
	public static void setFieldIntValue(int value, Field f, Object context) throws SIMPLTranslationException
	{
		try{
			f.setInt(context, value);
		}catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			f.setInt(context, value);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	public static void setFieldShortValue(short value, Field f, Object context) throws SIMPLTranslationException
	{
		try{
			f.setShort(context, value);
		}catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			f.setShort(context, value);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	public static void setFieldLongValue(long value, Field f, Object context) throws SIMPLTranslationException
	{
		try{
			f.setLong(context, value);
		}catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			f.setLong(context, value);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	public static void setFieldFloatValue(float value, Field f, Object context) throws SIMPLTranslationException
	{
		try{
			f.setFloat(context, value);
		}catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			f.setFloat(context, value);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	public static void setFieldDoubleValue(double value, Field f, Object context) throws SIMPLTranslationException
	{
		try{
			f.setDouble(context, value);
		}catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			f.setDouble(context, value);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}

	public static void setFieldBooleanValue(boolean value, Field f, Object context) throws SIMPLTranslationException
	{
		try{
			f.setBoolean(context, value);
		}catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			f.setBoolean(context, value);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	public static void setFieldByteValue(byte value, Field f, Object context) throws SIMPLTranslationException
	{
		try{
			f.setByte(context, value);
		}catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			f.setByte(context, value);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}
	
	public static void setFieldCharValue(char value, Field f, Object context) throws SIMPLTranslationException
	{
		try{
			f.setChar(context, value);
		}catch(IllegalAccessException ex)
		{
			try{
			f.setAccessible(true);
			f.setChar(context, value);
			}
			catch(Exception e)
			{
				throw new SIMPLTranslationException(e);
			}
		}
		catch(Exception e)
		{
			throw new SIMPLTranslationException(e);
		}
	}

	
	
}
