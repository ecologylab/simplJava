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
	public JPanel getWidget() 
	{
        JPanel panel = new JPanel();
        panel.setName(this.id);
        panel.setLayout(new GridBagLayout());
        
        setupColorChooser(panel,this);
        createColorButton(panel);
        registerComponent("colorChooser", colorChooser);
        
        panel.setVisible(true);

		return panel;
	}

    private void createColorButton(JPanel panel)
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,0,0,RIGHT_GUI_INSET); // top,left,bottom,right
        
        JButton jButton = new JButton();
        jButton.setText("Choose Color");
        jButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                colorChooserDialog.show();
            }
        });
        panel.add(jButton,c);
    }

	@Override
	protected Pref<Color> getPrefInstance() 
	{
		return new PrefColor();
	}

	@Override
	public void revertToDefault() 
	{
        JColorChooser colorChooser = (JColorChooser)lookupComponent(this.id+"colorChooser");
        colorChooser.setColor(this.getDefaultValue());
	}

	@Override
	public void setWidgetToPrefValue(Color prefValue) 
	{
        JColorChooser colorChooser = (JColorChooser)lookupComponent(this.id+"colorChooser");
        if (colorChooser != null)
        {
            colorChooser.setColor(prefValue);
        }
	}

	@Override
	public Color getPrefValue() 
	{
        JColorChooser colorChooser = (JColorChooser)lookupComponent(this.id+"colorChooser");
		return colorChooser.getColor();
	}

    public static void setupColorChooser(JPanel jPanel, MetaPref mp)
    {
        colorChooserDialog = 
        JColorChooser.createDialog(jPanel, "Choose Stroke Color", true,
                    colorChooser,
                    new ActionListener()
                    {   // ok listener
                        public void actionPerformed(ActionEvent e)
                        {
                            // nothing here
                        }
                    },
                    new ActionListener()
                    {   // cancel listener
                        public void actionPerformed(ActionEvent e)
                        {
                            // nothing here
                        }
                    }
                    );
    }
}
