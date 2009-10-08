/**
 * 
 */
package ecologylab.xml.library.xaml;

import java.awt.Color;

import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_tag;
import ecologylab.xml.types.element.ArrayListState;

/**
 * ecologylab.xml representation of the Microsoft WPF FrameworkElement object. FrameworkElement
 * is one of the base visual objects in WPF.
 * 
 * @author awebb
 *
 */
@xml_inherit
@xml_tag("FrameworkElement")
public class FrameworkElementState extends ArrayListState<ElementState>
{
	@xml_attribute @xml_tag("Name") String name;
	@xml_attribute @xml_tag("Background") Color background;
	@xml_attribute @xml_tag("Foreground") Color foreground;
	
	@xml_attribute @xml_tag("Height") double height;
	@xml_attribute @xml_tag("Width") double width;
	@xml_attribute @xml_tag("Opacity") double opacity;
	
	@xml_attribute @xml_tag("Mouse.MouseEnter") String mouseEnterEventHandler;
	@xml_attribute @xml_tag("Mouse.MouseLeave") String mouseExitEventHandler;
	@xml_attribute @xml_tag("Mouse.MouseMove") 	String mouseMoveEventHandler;
	@xml_attribute @xml_tag("Mouse.MouseUp") 	String mouseUpEventHandler;
	@xml_attribute @xml_tag("Mouse.MouseDown") 	String mouseDownEventHandler;
	@xml_attribute @xml_tag("Mouse.MouseWheel") String mouseWheelEventHandler;	
	
	@xml_attribute @xml_tag("x:Class") 			String wpfClass;

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
	
	
}
