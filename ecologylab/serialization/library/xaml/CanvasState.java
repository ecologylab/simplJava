package ecologylab.serialization.library.xaml;

import java.awt.Color;

import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * ecologylab.serialization representation of the Canvas WPF element for translating to XAML.
 * 
 * @author awebb
 *
 */
@simpl_inherit
@simpl_tag("Canvas")
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
