/*
 * Created on Dec 31, 2004
 */
package ecologylab.xml.types.scalar;

import java.io.IOException;
import java.lang.reflect.Field;

import ecologylab.generic.Debug;
import ecologylab.xml.*;

/**
 * Basic unit of the scalar type system.
 * Manages marshalling from a Java class that represents a scalar, 
 * to a String, and from a String to that Java class.
 * <p/>
 * The ScalarType object is a means for associating a type name with a type index. It also knows how to
 * create an instance of the type, given a String representation. If the ScalarType is a reference type,
 * this is done with getInstance(String); if the ScalarType is a primitive, this is done with
 * getValue(String), which cannot appear as a method in this, the base class, because it will return
 * a different primitive type for each such Type.
 * <p/>
 * Note: unlike with ElementState subtypes, translation of these is controlled entirely by the name of
 * the underlying Java class that gets translated, and not by the class name of subclasses of this.
 * 
 * @author andruid
 */
public class ScalarType<T> extends Debug
{
    Class<T>			thatClass;
    
    Class<T>			alternativeClass;

    // int index;
    boolean             isPrimitive;

	public static final Object	DEFAULT_VALUE			= null;
    public static final String	DEFAULT_VALUE_STRING 	= "null";

    /**
     * Constructor is protected because there should only be 1 instance that gets re-used, for each
     * type. To get the instance of this type object for use in translations, call
     * <code>TypeRegistry.get("type-string")</code>.
     * 
     */
    protected ScalarType(Class<T> thatClass)
    {
        this.thatClass = thatClass;
        // this.index = index;
        this.isPrimitive = thatClass.isPrimitive();
    }

    /**
     * If <code>this</code> is a reference type, build an appropriate Object, given a String
     * representation. If it is a primitive type, return a boxed value.
     * 
     * @param value
     *            String representation of the instance.
     */
    public T getInstance(String value)
    {
        return null;
    }

    /**
     * Set the field represented to the value of
     * 
     * Many different types of exceptions may be thrown. These include IllegalAccessException on the
     * one hand, which would come from problemswith using reflection to access the Field. This is
     * very unlikely. <p/> More likely are problems with conversion of the parameter value into into
     * an object or primitive of the proper type.
     * 
     * @param object
     *            The object whose field should be modified.
     * @param field
     *            The field to be set.
     * @param value
     *            String representation of the value to set the field to. This Type will convert the
     *            value to the appropriate type, using getInstance(String) for reference types, and
     *            type specific getValue(String) methods for primitive types.
     * 
     * @return true if the field is set properly, or if the parameter value that is passed in is
     *         null. false if the field cannot be accessed, or if value cannot be converted to the
     *         appropriate type.
     */
    public boolean setField(Object object, Field field, String value)
    {
        if (value == null)
            return true;

        boolean result = false;
        T referenceObject;

        try
        {
            referenceObject = getInstance(value);
            if (referenceObject != null)
            {
                field.set(object, referenceObject);
                result = true;
            }
        }
        catch (Exception e)
        {
            setFieldError(field, value, e);
        }
        return result;
    }

    /**
     * Display an error message that arose while setting field to value.
     * 
     * @param field
     * @param value
     * @param e
     */
	protected void setFieldError(Field field, String value, Exception e)
	{
		error("Got " + e + " while trying to set field " + field + " to " + value);
	}

    /**
     * @return Returns the simple className (unqualified) for this type.
     */
    public String getClassName()
    {
        return thatClass.getSimpleName();
    }

    /**
     * @return Returns the integer index associated with this type.
     */
    /*
     * public int getIndex() { return index; }
     */
    /**
     * Find out if this is a reference type or a primitive types.
     * 
     * @return true for a primitive type. false for a reference type.
     */
    public boolean isPrimitive()
    {
        return isPrimitive;
    }

    /**
     * Return true if this type may need escaping when emitted as XML.
     * 
     * @return true, by default, for all reference types (includes Strings, PURLs, ...); false otherwise.
     */
    public boolean needsEscaping()
    {
    	return isReference();
    }
    /**
     * Find out if this is a reference type or a primitive types.
     * 
     * @return true for a reference type. false for a primitive type.
     */
    public boolean isReference()
    {
        return !isPrimitive;
    }

    /**
     * The string representation for a Field of this type
     * 
     * Default implementation uses the Object's toString() method. This is usually going to be
     * wrong.
     */
    public String toString(Object object, Field field)
    {
        String result = "COULDNT CONVERT!";
        try
        {
            Object fieldObj = field.get(object);
            if (fieldObj == null)
                result = DEFAULT_VALUE_STRING;
            else
                result = fieldObj.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get the value from the Field, in the context.
     * Append its value to the buffy.
     * <p/>
     * Should only be called *after* checking !isDefault() yourself.
     * 
     * @param buffy
     * @param field
     * @param context
     * @param needsEscaping TODO
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    public void appendValue(Appendable buffy, Field field, Object context, boolean needsEscaping) 
    throws IllegalArgumentException, IllegalAccessException, IOException
    {
        Object instance = field.get(context);
           
        appendValue((T) instance, buffy, needsEscaping);
    }
    
    /**
     * Get the value from the Field, in the context.
     * Append its value to the buffy.
     * <p/>
     * Should only be called *after* checking !isDefault() yourself.
     * 
     * @param buffy
     * @param field
     * @param context
     * @param needsEscaping TODO
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    public void appendValue(StringBuilder buffy, Field field, Object context, boolean needsEscaping) 
    throws IllegalArgumentException, IllegalAccessException
    {
        Object instance = field.get(context);
           
        appendValue((T) instance, buffy, needsEscaping);
    }
    
    protected void appendValue(T instance, StringBuilder buffy, boolean needsEscaping)
    {
    	buffy.append(instance.toString());
    }

    protected void appendValue(T instance, Appendable buffy, boolean needsEscaping)
    throws IOException
    {
    	buffy.append(instance.toString());
    }

    /**
     * The default value for this type, as a String. This value is the one that translateToXML(...)
     * wont bother emitting.
     * 
     * In this case, "null".
     */
    protected String defaultValueString()
    {
        return DEFAULT_VALUE_STRING;
    }
    
    /**
     * The default value for this, in its own type. Not meaningful for primitive types.
     * 
     * @return
     */
    protected T defaultValue()
    {
    	return null;
    }
    
    public final int defaultValueLength()
    {
    	return defaultValueString().length();
    }

    public boolean isDefaultValue(String value)
    {
        String defaultValue = defaultValueString();
		return (defaultValue.length() == value.length()) && defaultValue.equals(value);
    }
    
    public boolean isDefaultValue(Field field, Object context) 
    throws IllegalArgumentException, IllegalAccessException
    {
    	return field.get(context) == null;
    }

    /**
     * Returns whether or not this is a floating point value of some sort; Types that are floating
     * point values should override this method to return true.
     * 
     * The implication of returning true is that the precision of this can be controlled when it is
     * emitted as XML.
     * 
     * @return false
     */
    public boolean isFloatingPoint()
    {
        return false;
    }
    
    public boolean allowNewLines()
    {
    	return true;
    }
    
    /**
     * Get the class object for the Type for which this manages conversion.
     * 
     * @return	Class associated with this Type.
     */
    public Class getTypeClass()
    {
    	return thatClass;
    }
    
	public static final String DEFAULT_DELIMS = " \n\t";

	/**
	 * For editing: these are the valid delimiters for separating tokens that make up a field
	 * of this type.
	 * 
	 * @return
	 */
	public String delimeters()
	{
		return DEFAULT_DELIMS;
	}
	
	/**
	 * When editing, determines whether delimiters can be included in token strings.
	 * 
	 * @return
	 */
	//FIXME -- Add String delimitersAfter to TextChunk -- interleaved with TextTokens, and
	//get rid of this!!!
	public boolean allowDelimitersInTokens()
	{
		return false;
	}
	/**
	 * When editing, do not allow the user to include these characters in the resulting value String.
	 * @return
	 */
	public String illegalChars()
	{
		return "";
	}
	/**
	 * When editing, is the field one that should be part of the Term model?
	 * 
	 * @return	true for Strings
	 */
	public boolean composedOfTerms()
	{
		return true;
	}
	/**
	 * True if the user should be able to express interest in fields of this type.
	 * 
	 * @return	true for Strings
	 */
	public boolean affordsInterestExpression()
	{
		return true;
	}
}
