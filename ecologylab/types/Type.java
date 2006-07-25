/*
 * Created on Dec 31, 2004
 */
package ecologylab.types;

import java.lang.reflect.Field;

import ecologylab.xml.*;

/**
 * Basic unit of the type system.
 * 
 * The Type object is a means for associating a type name with a type index.
 * It also knows how to create an instance of the type, given a String representation.
 * If the Type is a reference type, this is done with getInstance(String);
 * if the Type is a primitive, this is done with getValue(String), which cannot appear as a method
 * in this, the base class, because it will return a different primitive type for each such Type.
 * 
 * @author andruid
 */
public class Type extends IO 
{
	String	className;
//	int		index;
	boolean	isPrimitive;
	
	public static final String NULL_STRING		= "null";

/**
 * Constructor is protected because there should only be 1 instance
 * that gets re-used, for each type.
 * To get the instance of this type object for use in translations, call
 * <code>TypeRegistry.get("type-string")</code>.
 * 
 */
	protected Type(String className, /* int index, */ boolean isPrimitive)
	{
		this.className		= className;
//		this.index			= index;
		this.isPrimitive	= isPrimitive;
		TypeRegistry.register(this);
	}
	
	/**
	 * If <code>this</code> is a reference type, build an appropriate Object, given a String representation.
	 * If it is a primitive type, return null.
	 * 
	 * @param value	String representation of the instance.
	 */
	public Object getInstance(String value)
	{
		return null;
	}
	
	/**
	 * Set the field represented by @param field in <code>this</code> to the
	 * value of @param nestedObj. 
	 * 
	 * Many different types of exceptions may be thrown.
	 * These include IllegalAccessException on the one hand, which would come
	 * from  problemswith using reflection to access the Field. 
	 * This is very unlikely.
	 * <p/>
	 * More likely are problems with conversion of the parameter value into
	 * into an object or primitive of the proper type.
	 * 
	 * @param	object	The object whose field should be modified.
	 * @param	field	The field to be set.
	 * @param	value	String representation of the value to set the field to.
	 *					This Type will convert the value to the appropriate
	 * type, using getInstance(String) for reference types, and type specific
	 * getValue(String) methods for primitive types.
	 * 
	 * @return	true	if the field is set properly, or if the parameter
	 *					value that is passed in is null.
	 *			false	if the field cannot be accessed, or if value cannot be
	 *					converted to the appropriate type.
	 */
	public boolean setField(Object object, Field field, String value)
	{
		if (value == null)
			return true;
		
		boolean result	= false;
		Object referenceObject = "";
		
		try
		{
			referenceObject	= getInstance(value);
			if (referenceObject != null)
			{
				field.set(object, referenceObject); 
				result		= true;
			}
		} catch (IllegalAccessException e)
		{
			debug("Got " + e + " while setting field " +
					field + " to " + value+"->"+referenceObject+" "+
					referenceObject.getClass());
		}
		return result;
	}
	
	/**
	 * @return Returns the className for this type.
	 */
	public String getClassName() 
	{
		return className;
	}
	/**
	 * @return Returns the integer index associated with this type.
	 */
/*
	public int getIndex() 
	{
		return index;
	}
 */
	/**
	 * Find out if this is a reference type or a primitive types.
	 * 
	 * @return	true  for a primitive type.
	 * 			false for a reference type.
	 */
	public boolean isPrimitive() 
	{
		return isPrimitive;
	}
	/**
	 * Find out if this is a reference type or a primitive types.
	 * 
	 * @return	true  for a reference type.
	 * 			false for a primitive type.
	 */
	public boolean isReference() 
	{
		return !isPrimitive;
	}

/**
 * The string representation for a Field of this type
 * 
 * Default implementation uses the Object's toString() method.
 * This is usually going to be wrong.
 */
	public String toString(Object object, Field field)
	{
	   String result	= "COULDNT CONVERT!";
	   try
	   {
		  Object fieldObj	= field.get(object);
		  if (fieldObj == null)
			 result			= NULL_STRING;
		  else
			 result			= fieldObj.toString();
	   } catch (Exception e)
	   {
		  e.printStackTrace();
	   }
	   return result;
	}

/**
 * The default value for this type, as a String.
 * This value is the one that translateToXML(...) wont bother emitting.
 * 
 * In this case, "null".
 */
	public String defaultValue()
	{
	   return NULL_STRING;
	}
	public boolean isDefaultValue(String value)
	{
	   return defaultValue().equals(value);
	}
}
