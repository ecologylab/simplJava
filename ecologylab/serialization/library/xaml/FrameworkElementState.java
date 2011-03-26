/**
 * 
 */
package ecologylab.serialization.library.xaml;

import java.awt.Color;
import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.simpl_inherit;

/**
 * ecologylab.serialization representation of the Microsoft WPF FrameworkElement object. FrameworkElement
 * is one of the base visual objects in WPF.
 * 
 * @author awebb
 *
 */
@simpl_inherit
@xml_tag("FrameworkElement")
public class FrameworkElementState extends ElementState
{
	@simpl_scalar @xml_tag("Name") String name;
	@simpl_scalar @xml_tag("Background") Color background;
	@simpl_scalar @xml_tag("Foreground") Color foreground;
	
	@simpl_scalar @xml_tag("Height") double height;
	@simpl_scalar @xml_tag("Width") double width;
	@simpl_scalar @xml_tag("Opacity") double opacity;
	
	@simpl_scalar @xml_tag("Mouse.MouseEnter") String mouseEnterEventHandler;
	@simpl_scalar @xml_tag("Mouse.MouseLeave") String mouseExitEventHandler;
	@simpl_scalar @xml_tag("Mouse.MouseMove") 	String mouseMoveEventHandler;
	@simpl_scalar @xml_tag("Mouse.MouseUp") 	String mouseUpEventHandler;
	@simpl_scalar @xml_tag("Mouse.MouseDown") 	String mouseDownEventHandler;
	@simpl_scalar @xml_tag("Mouse.MouseWheel") String mouseWheelEventHandler;	
	
	@simpl_scalar @xml_tag("x:Class") 			String wpfClass;
	
	@simpl_collection("Element") 
	ArrayList<ElementState>   frameWorkElements;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public double getHeight()
	{
		return height;
	}

	public void setHeight(double height)
	{
		this.height = height;
	}

	public double getWidth()
	{
		return width;
	}

	public void setWidth(double width)
	{
		this.width = width;
	}

	public double getOpacity()
	{
		return opacity;
	}

	public void setOpacity(double opacity)
	{
		this.opacity = opacity;
	}

	public Color getBackground()
	{
		return background;
	}

	public void setBackground(Color background)
	{
		this.background = background;
	}

	public Color getForeground()
	{
		return foreground;
	}

	public void setForeground(Color foreground)
	{
		this.foreground = foreground;
	}

	public String getMouseEnterEventHandler()
	{
		return mouseEnterEventHandler;
	}

	public void setMouseEnterEventHandler(String mouseEnterEventHandler)
	{
		this.mouseEnterEventHandler = mouseEnterEventHandler;
	}

	public String getMouseExitEventHandler()
	{
		return mouseExitEventHandler;
	}

	public void setMouseExitEventHandler(String mouseExitEventHandler)
	{
		this.mouseExitEventHandler = mouseExitEventHandler;
	}

	public String getMouseMoveEventHandler()
	{
		return mouseMoveEventHandler;
	}

	public void setMouseMoveEventHandler(String mouseMoveEventHandler)
	{
		this.mouseMoveEventHandler = mouseMoveEventHandler;
	}

	public String getMouseUpEventHandler()
	{
		return mouseUpEventHandler;
	}

	public void setMouseUpEventHandler(String mouseUpEventHandler)
	{
		this.mouseUpEventHandler = mouseUpEventHandler;
	}

	public String getMouseDownEventHandler()
	{
		return mouseDownEventHandler;
	}

	public void setMouseDownEventHandler(String mouseDownEventHandler)
	{
		this.mouseDownEventHandler = mouseDownEventHandler;
	}

	public String getMouseWheelEventHandler()
	{
		return mouseWheelEventHandler;
	}

	public void setMouseWheelEventHandler(String mouseWheelEventHandler)
	{
		this.mouseWheelEventHandler = mouseWheelEventHandler;
	}

	public String getWPFClass()
	{
		return wpfClass;
	}

	public void setWPFClass(String wpfClass)
	{
		this.wpfClass = wpfClass;
	}
	
	public void add(FrameworkElementState element)
	{
		frameWorkElements.add(element);
	}
	
}
