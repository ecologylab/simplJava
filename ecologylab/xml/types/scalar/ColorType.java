/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.xml.types.scalar;

import java.awt.Color;
import java.lang.reflect.Field;

import ecologylab.xml.ScalarUnmarshallingContext;

/**
 * Type system entry for java.awt.Color. Uses a hex string as initialization.
 * 
 * @author andruid
 */
public class ColorType extends ScalarType<Color>
{
	
	
/**
 * This constructor should only be called once per session, through
 * a static initializer, typically in TypeRegistry.
 * <p>
 * To get the instance of this type object for use in translations, call
 * <code>TypeRegistry.get("java.awt.Color")</code>.
 * 
 */
	public ColorType()
	{
		super(Color.class);
	}

	/**
	 * @param value is interpreted as hex-encoded RGB value, in the
	 * same style as HTML & CSS. A # character at the start is unneccesary,
	 * but acceptable.
	 * 
	 * @see ecologylab.xml.types.scalar.ScalarType#getInstance(java.lang.String, String[], ScalarUnmarshallingContext)
	 */
	public Color getInstance(String value, String[] formatStrings, ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
	   if (value.indexOf('#') == 0)
		  value			= value.substring(1);
	   
	   // we *should* be able to use int and parseInt() here, but
	   // apparently, there's a bug in the JDK, so we use long
	   // and then cast down
	   long argb		= Long.parseLong(value, 16);
	   boolean hasAlpha = (argb >= 0x01000000);
	   return new Color((int) argb, hasAlpha);
	}
	
	/**
	 * Get a String representation of the instance, using this.
	 * The default just calls the toString() method on the instance.
	 * @param color
	 * @return
	 */
	@Override
	public String marshall(Color color)
	{
		String result;
		int argb		= color.getRGB();
		int alpha		= argb & 0xff000000;
		if (alpha == 0xff000000)
			argb		= argb & 0xffffff;
//		debugA("rgba="+Integer.toHexString(argb)+" alpha="+alpha);
		result		= Integer.toHexString(argb);
		return '#' + result;
	}
}
