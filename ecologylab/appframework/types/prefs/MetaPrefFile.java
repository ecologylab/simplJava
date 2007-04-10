/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ecologylab.appframework.types.prefs.MetaPref;
import ecologylab.xml.xml_inherit;

/**
 * Metadata about a File Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */

@xml_inherit
public class MetaPrefFile extends MetaPref<File>
{
    /**
     * Default value for this MetaPref
     */
    @xml_attribute  File      defaultValue;
    
    /**
     * Color chooser; static so we only have one.
     */
    static JFileChooser fileChooser = new JFileChooser();
    /**
     * Color dialog; static so we only have one.
     */
    static JDialog       fileChooserDialog;
    
    /**
     * Instantiate.
     */
    public MetaPrefFile()
    {
        super();
    }
    
    /**
     * Gets the default value of a MetaPref. 
     * 
     * @return Default value of MetaPref
     */
    public File getDefaultValue()
    {
        return defaultValue;
    }

    /**
     * Sets the widget value/selection to the default value/selection.
     * TODO: MOVE THIS
     */
    public @Override
    void revertToDefault()
    {
        JFileChooser fileChooser = (JFileChooser)lookupComponent(this.id+"fileChooser");
        fileChooser.setSelectedFile(this.getDefaultValue());
    }

    /**
     * Gets the JPanel containing the gui components for the choices 
     * or fields associated with a MetaPref.
     * 
     *  TODO: MOVE THIS
     * 
     * @return JPanel of choices/values JComponents.
     */
    public @Override
    JPanel getWidget()
    {
        JPanel panel = new JPanel();
        panel.setName(this.id);
        panel.setLayout(new GridBagLayout());

        createFileButton(panel);
        registerComponent("fileChooser", fileChooser);
        
        panel.setVisible(true);
        
        return panel;
    }

    /**
     * Sets the widget value/selection to the value/selection of the Pref.
     * 
     *  TODO: MOVE THIS
     * 
     * @param prefValue     Value of Pref
     */
    @Override
    public void setWidgetToPrefValue(File prefValue)
    {
        JFileChooser fileChooser = (JFileChooser)lookupComponent(this.id+"fileChooser");
        fileChooser.setSelectedFile(prefValue);
    }

    /**
     * Gets the Pref value for this MetaPref.
     * 
     *  TODO: MOVE THIS
     *  
     * @return Pref value
     */
    @Override
    public File getPrefValue()
    {
        JFileChooser fileChooser = (JFileChooser)lookupComponent(this.id+"fileChooser");
        return fileChooser.getSelectedFile();
    }

    /**
     * Construct a new instance of the Pref that matches this.
     * Use this to fill-in the default value.
     * 
     * @return
     */
    protected @Override Pref<File> getPrefInstance()
    {
        return new PrefFile();
    }
    
    private void createFileButton(JPanel panel)
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,0,0,RIGHT_GUI_INSET); // top,left,bottom,right
        
        JButton jButton = new JButton();
        jButton.setText("Choose File");
        jButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                fileChooser.showOpenDialog(fileChooser.getParent());
            }
        });
        panel.add(jButton,c);
    }
    
    
/*
    public boolean isWithinRange(File newValue)
    {
        return (range == null) ? true :  range.isWithinRange(newValue);
    }
    */
}