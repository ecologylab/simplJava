/**
 * 
 */
package ecologylab.serialization.library.xaml;

import java.awt.Rectangle;

import ecologylab.serialization.simpl_inherit;

/**
 * A base class for representing WPF FrameworkElements that are children of a WPF
 * Panel object and require positioning specification by attributes in XAML. 
 * Currently supported Panel objects are Canvas, Grid, and Dock Panel. To support 
 * other Panel objects, simply add the necessary attributes to this class.
 * 
 * @author awebb
 *
 */
@simpl_inherit
public abstract class PanelChildState extends FrameworkElementState
{

	@simpl_scalar @xml_tag("Canvas.ZIndex") 	int zIndex;
	@simpl_scalar @xml_tag("Canvas.Top") 		int top;
	@simpl_scalar @xml_tag("Canvas.Left") 		int left;
	
	@simpl_scalar @xml_tag("Grid.Column")		int column;
	@simpl_scalar @xml_tag("Grid.Row")			int row;
	
	@simpl_scalar @xml_tag("DockPanel.Dock")	String dock;
	
	static class Dock
	{
		static String TOP 		= "Top";
		static String BOTTOM 	= "Bottom";
		static String LEFT		= "Left";
		static String RIGHT		= "Right";
	}
	
	public int getZIndex()
	{
		return zIndex;
	}
	public void setZIndex(int index)
	{
		zIndex = index;
	}
	public int getTop()
	{
		return top;
	}
	public void setTop(int top)
	{
		this.top = top;
	}
	public int getLeft()
	{
		return left;
	}
	public void setLeft(int left)
	{
		this.left = left;
	}
	
	public void setBounds(Rectangle bounds)
	{
		setTop(bounds.y);
		setLeft(bounds.x);
		setWidth(bounds.width);
		setHeight(bounds.height);
	}
}
