/*
 * Created on Dec 31, 2004
 */
package ecologylab.types;

import java.lang.reflect.Field;

import ecologylab.generic.Debug;
import ecologylab.xml.*;

/**
 * Basic unit of the type system.
 * Manages marshalling from a Java class to a String, and from a String to that Java class.
 * <p/>
 * The Type object is a means for associating a type name with a type index. It also knows how to
 * create an instance of the type, given a String representation. If the Type is a reference type,
 * this is done with getInstance(String); if the Type is a primitive, this is done with
 * getValue(String), which cannot appear as a method in this, the base class, because it will return
 * a different primitive type for each such Type.
 * <p/>
 * Note: unlike ElementState subtypes, translation of these is controlled entirely by the name of
 * the underlying Java class that gets translated, and not by the class name of subclasses of this.
 * 
 * @author andruid
 */
public class Type extends Debug
{
    Class				thatClass;

    // int index;
    boolean             isPrimitive;

    public static final String NULL_STRING = "null";

    /**
     * Constructor is protected because there should only be 1 instance that gets re-used, for each
     * type. To get the instance of this type object for use in translations, call
     * <code>TypeRegistry.get("type-string")</code>.
     * 
     */
    protected Type(Class thatClass)
    {
        this.thatClass = thatClass;
        // this.index = index;
        this.isPrimitive = thatClass.isPrimitive();
    }

    /**
     * If <code>this</code> is a reference type, build an appropriate Object, given a String
     * representation. If it is a primitive type, return null.
     * 
     * @param value
     *            String representation of the instance.
     */
    public Object getInstance(String value)
    {
        return null;
    }

    /**
     * Set the field represented by
     * 
     * @param field
     *            in <code>this</code> to the value of
     * @param nestedObj.
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
        Object referenceObject = "";

        try
        {
            referenceObject = getInstance(value);
            if (referenceObject != null)
            {
                field.set(object, referenceObject);
                result = true;
            }
        }
//      catch (IllegalAccessException e)
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
                result = NULL_STRING;
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
     * The default value for this type, as a String. This value is the one that translateToXML(...)
     * wont bother emitting.
     * 
     * In this case, "null".
     */
    public String defaultValue()
    {
        return NULL_STRING;
    }
    
    public final int defaultValueLength()
    {
    	return defaultValue().length();
    }

    public boolean isDefaultValue(String value)
    {
        String defaultValue = defaultValue();
		return (defaultValue.length() == value.length()) && defaultValue.equals(value);
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
    
    /**
     * Get the class object for the Type for which this manages conversion.
     * 
     * @return
     */
    public Class getTypeClass()
    {
    	return thatClass;
    }
}
