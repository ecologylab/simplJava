/**
 * 
 */
package ecologylab.serialization.types;

import simpl.types.ScalarType;
import ecologylab.generic.Debug;
import ecologylab.serialization.types.scalar.BufferedImageType;
import ecologylab.serialization.types.scalar.ColorType;
import ecologylab.serialization.types.scalar.AwtImageType;
import ecologylab.serialization.types.scalar.RectangleType;

/**
 * This class initializes ScalarTypes that depend on java.awt.*, which does not exist in Android.
 * 
 * @author andruid
 */
public class PlatformSpecificTypesSun extends Debug
{
	public static final ScalarType COLOR_TYPE 					= new ColorType();
	
	public static final ScalarType RECTANGLE_TYPE 			= new RectangleType();
	
	public static final ScalarType IMAGE_TYPE 					= new AwtImageType();

	public static final ScalarType BUFFERED_IMAGE_TYPE 	= new BufferedImageType();


}
