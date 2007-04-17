/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ecologylab.xml.ElementState.xml_attribute;

/**
 * @author awebb
 *
 */
public class MetaPrefColor extends MetaPref<Color> 
{

	@xml_attribute	Color		defaultValue;
    
    /**
     * Color chooser; static so we only have one.
     */
    static JColorChooser colorChooser = new JColorChooser();
    /**
     * Color dialog; static so we only have one.
     */
    static JDialog       colorChooserDialog;
	
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
	protected Pref<Color> getPrefInstance() 
	{
		return new PrefColor();
	}

}
