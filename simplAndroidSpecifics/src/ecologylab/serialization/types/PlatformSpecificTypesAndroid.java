package ecologylab.serialization.types;


import ecologylab.generic.Debug;
import ecologylab.serialization.types.ScalarType;
import ecologylab.serialization.types.scalar.BitmapImageType;

/**
 * This class initializes ScalarTypes that depend on android.graphics.*, which does not exist in regular Java.
 * 
 * @author bill
 */
public class PlatformSpecificTypesAndroid extends Debug
{
	public static final ScalarType IMAGE_TYPE 					= new BitmapImageType();
}
