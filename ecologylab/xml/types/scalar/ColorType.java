/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.types;

import java.awt.Color;

/**
 * Type system entry for java.awt.Color. Uses a hex string as initialization.
 * 
 * @author andruid
 */
public class ColorType extends Type
{
	public ColorType()
	{
		super("java.awt.Color", /*TYPE_COLOR, */ false);
	}

	/**
	 * @param value is interpreted as hex-encoded RGB value, in the
	 * same style as HTML & CSS. A # character at the start is unneccesary,
	 * but acceptable.
	 * 
	 * @see ecologylab.types.Type#getInstance(java.lang.String)
	 */
	public Object getInstance(String value)
	{
	   if (value.indexOf('#') == 0)
		  value			= value.substring(1);
	   
	   int rgb			= Integer.parseInt(value, 16);
	   return new Color(rgb);
	}
}
