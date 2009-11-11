package ecologylab.xml.library.xaml;

import java.awt.Color;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_tag;

/**
 * ecologylab.xml representation of the Canvas WPF element for translating to XAML.
 * 
 * @author awebb
 *
 */
@xml_inherit
@xml_tag("Canvas")
public class CanvasState extends FrameworkElementState
{
	public CanvasState()
	{
		this.background = Color.WHITE;
	}
	
	public CanvasState(double width, double height)
	{
		this();
		this.width 	= width;
		this.height = height;
	}
}
