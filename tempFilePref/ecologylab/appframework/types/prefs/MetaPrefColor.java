/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.Color;

import javax.swing.JPanel;

import ecologylab.xml.ElementState.xml_attribute;

/**
 * @author awebb
 *
 */
public class MetaPrefColor extends MetaPref<Color> 
{

	@xml_attribute	Color		defaultValue;
	
	public MetaPrefColor()
	{
		super();
	}
	
	@Override
	public Color getDefaultValue() 
	{
		return (defaultValue != null) ? defaultValue : Color.BLACK;
	}

	@Override
	public JPanel getWidget() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Pref<Color> getPrefInstance() 
	{
		return new PrefColor();
	}

	@Override
	public void revertToDefault() 
	{
		// TODO Auto-generated method stub	
	}

	@Override
	public void setWidgetToPrefValue(Color prefValue) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public Color getPrefValue() 
	{
		// TODO Auto-generated method stub
		return null;
	}

}
