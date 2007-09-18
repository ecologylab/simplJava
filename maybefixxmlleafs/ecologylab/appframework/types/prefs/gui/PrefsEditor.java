package ecologylab.appframework.types.prefs.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.LinkedHashMap;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeListener;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.appframework.types.prefs.Choice;
import ecologylab.appframework.types.prefs.ChoiceBoolean;
import ecologylab.appframework.types.prefs.MetaPref;
import ecologylab.appframework.types.prefs.MetaPrefBoolean;
import ecologylab.appframework.types.prefs.MetaPrefFloat;
import ecologylab.appframework.types.prefs.MetaPrefInt;
import ecologylab.appframework.types.prefs.MetaPrefSet;
import ecologylab.appframework.types.prefs.Pref;
import ecologylab.appframework.types.prefs.PrefSet;
import ecologylab.io.Assets;
import ecologylab.net.ParsedURL;
import ecologylab.xml.types.element.ArrayListState;

/**
 * Create the GUI for preference editing; also responsible for all
 * actions associated with the GUI.
 * 
 * @author Cae
 *
 */
public class PrefsEditor
extends PrefEditorWidgets
implements WindowListener
{

    /**
     * The number of characters/columns at which the tooltips will try to wrap.
     * Actual wrapping will be the nearest space AFTER this number.
     */
    private static final int TOOLTIP_WRAP_WIDTH = 80;
    /**
     * A small inset for the top of an object in the GUI
     */
    private static final int SMALL_TOP_GUI_INSET = 0;
    /**
     * The inset between the left side of the gui panel and the left 
     * side of the descriptions.
     */
    private static final int LEFT_GUI_INSET = 20;
    /**
     * The inset between the top side of the gui panel and the object above it.
     */
    private static final int TOP_GUI_INSET = 10;
    /**
     * The inset between the bottom of the gui section and the top of the 
     * next object.
     */
    private static final int BOTTOM_GUI_INSET = 10;

    // base setup for gui
    /**
     * Base window for the GUI
     */
    JFrame 		jFrame = null;
    /**
     * Cancel button for GUI
     */
    JButton 	cancelButton = null;
    /**
     * Save button for GUI
     */
    JButton 	saveButton = null;
    /**
     * Apply button for GUI
     */
    JButton     applyButton = null;
    /**
     * Revert button for GUI
     */
    JButton 	revertButton = null;
    /**
     * Tabbed pane within content pane within base window for GUI
     */
    JTabbedPane jTabbedPane = null;
    
    /**
     * Whether we're called this standalone or not
     */
    boolean		isStandalone;
    
    /**
     * Start location on the screen for the gui
     */
    static Point startLocation = new Point(100,100);
    
    /**
     * Color picker; static so we only have one.
     */
    static JColorChooser colorChooser = new JColorChooser();
    static JDialog       colorChooserDialog;
    
    /**
     * File chooser; static so we only have one.
     */
    static JFileChooser fileChooser = new JFileChooser();
    /**
     * File dialog; static so we only have one.
     */
    static JDialog       fileChooserDialog;
    
    /**
     * The base function that you call to construct the prefs editor GUI.
     * This requires that the MetaPrefSet and PrefSet be instantiated and populated
     * prior to call. This function creates the entire GUI and handles all actions
     * for it.
     * 
     * @param metaPrefSet       Set of MetaPrefs
     * @param prefSet           Set of Prefs
     * @param savePrefsPURL     ParsedURL to save prefs.xml to
     * @param isStandalone      Whether or not we're calling this standalone
     */
    public PrefsEditor(MetaPrefSet metaPrefSet, PrefSet prefSet, ParsedURL savePrefsPURL, final boolean createJFrame, final boolean isStandalone)
    {
        super (metaPrefSet, prefSet, savePrefsPURL);

        this.isStandalone	= isStandalone;
        
        final Container container = setupContainer(createJFrame, isStandalone);
        //container.setLocation(startLocation);
    }
    
    /**
     * Calls createJFrame
     * @see #createJFrame()
     * @return Base window (JFrame) for the GUI
     */
    public Container setupContainer(final boolean createJFrame, final boolean isStandalone)
    {
    	Container result	= createContainer(createJFrame);
    	if (createJFrame)
    	{
    		jFrame.addWindowListener(new WindowAdapter()
    		{
    			public void windowClosing(WindowEvent e)
    			{
    				closeWindow();
    			}
    		});
    		jFrame.setVisible(true);
    	}
    	return result;
    }

    /**
     * Create the base window for the GUI.
     * 
     * This contains {@link #createJContentPane()}.
     * 
     * This is a static part of the GUI.
     * 
     * @return Base window (JFrame) for the GUI
     */
    // static bits of gui
    private Container createContainer(boolean createJFrame) 
    {
    	Container result	= null;
    	if (createJFrame)
    	{
	        if (jFrame == null) 
	        {
	            jFrame = new JFrame();
	            jFrame.setContentPane(createJContentPane());
	            jFrame.addWindowListener(this);
	    		String title	= metaPrefSet.getTitle();
	    		if (title == null)
	    			title	= "Preferences Editor";
	    		jFrame.setTitle(title);
	        }
	        result			= jFrame;
    	}
    	else
    	{
    		result	= createJContentPane();
    	}
        setSize(result);
    	return result;
    }

	private void setSize(Container jContainer)
	{
		int width	= metaPrefSet.getWidth();
		if (width == 0)
			width	= 603;
		int height	= metaPrefSet.getHeight();
		if (height == 0)
			height	= 532;
		jContainer.setPreferredSize(new Dimension(width, height));
		jContainer.setSize(new Dimension(width, height));
	}

    /**
     * Function to close the window
     */
	private void closeWindow()
	{
		if (jFrame != null)
		{
			jFrame.setVisible(false);
	    	jFrame.dispose();
		}
    	if (PrefsEditor.this.isStandalone)
    		System.exit(0);
	}
    
    /**
     * Function to close the window
     */
    private void saveLocation()
    {
        startLocation = jFrame.getLocation();
    }
	
    /**
     * Create the content pane within the base window for the GUI.
     * 
     * This is contained within {@link #createJFrame()}.
     * This contains {@link #createApplyButton()}, {@link #createCancelButton()},
     * and {@link #createRevertButton()}, {@link #createSaveButton()},
     * and {@link #createJTabbedPane()}.
     * 
     * This is a static part of the GUI.
     * 
     * @return Content pane (JPanel) within the base window for the GUI
     */
    private JPanel createJContentPane() 
    {
        if (jContentPane == null) 
        {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(createJTabbedPane(), null);
            jContentPane.add(createCancelButton(), null);
            jContentPane.add(createSaveButton(), null);
            jContentPane.add(createApplyButton(), null);
            jContentPane.add(createRevertButton(), null);
        }
        return jContentPane;
    }

    /**
     * Create the cancel button for the GUI. This button closes
     * the window without saving the prefs.
     * This is contained within {@link #createJContentPane()}.
     * This is a static part of the GUI.
     * 
     * @return Cancel button (JButton) for GUI
     */
    private JButton createCancelButton() 
    {
        if (cancelButton == null) 
        {
            cancelButton = new JButton();
            cancelButton.setBounds(new Rectangle(482, 435, 89, 35));
            cancelButton.setText("Cancel");
            cancelButton.addActionListener(new ActionListener()
            		{
            			public void actionPerformed(ActionEvent e)
            			{
                            saveLocation();
            				closeWindow();
            			}
	                });
        }
        return cancelButton;
    }

    /**
     * Create the save button for the GUI. This button saves
     * prefs and closes the window.
     * 
     * This is contained within {@link #createJContentPane()}.
     * This contains {@link #actionSavePreferences()}.
     * 
     * This is a static part of the GUI.
     * 
     * @return Save button (JButton) for GUI
     */
    JButton createSaveButton() 
    {
        if (saveButton == null) 
        {
            saveButton = new JButton();
            saveButton.setBounds(new Rectangle(379, 435, 89, 35));
            saveButton.setText("Save");
            saveButton.addActionListener(new ActionListener() 
            		{
            			public void actionPerformed(ActionEvent e) 
            			{
                            saveLocation();
            				actionSavePreferences();
                            closeWindow();
            			}
            		});
        }
        return saveButton;
    }
    
    /**
     * Create the apply button for the GUI. This saves the prefs
     * and doesn't close the window.
     * 
     * This is contained within {@link #createJContentPane()}.
     * This contains {@link #actionSavePreferences()}.
     * 
     * This is a static part of the GUI.
     * 
     * @return Apply button (JButton) for GUI
     */
    JButton createApplyButton() 
    {
        if (applyButton == null) 
        {
            applyButton = new JButton();
            applyButton.setBounds(new Rectangle(276, 435, 89, 35));
            applyButton.setText("Apply");
            applyButton.addActionListener(new ActionListener() 
                    {
                        public void actionPerformed(ActionEvent e) 
                        {
                            actionSavePreferences();
                        }
                    });
        }
        return applyButton;
    }

    /**
     * Create the revert button for the GUI. This reverts the
     * prefs to their default values when pressed.
     * 
     * This is contained within {@link #createJContentPane()}.
     * This contains {@link #actionRevertPreferencesToDefault()}.
     * 
     * This is a static part of the GUI.
     * 
     * @return Revert button (JButton) for GUI
     */
    private JButton createRevertButton() 
    {
        if (revertButton == null) 
        {
            revertButton = new JButton();
            revertButton.setBounds(new Rectangle(15, 435, 137, 35));
            revertButton.setText("Revert to Default");
            revertButton.addActionListener(new ActionListener() 
            		{
            			public void actionPerformed(ActionEvent e) 
            			{
            				actionRevertPreferencesToDefault();
            			}
            		});
        }
        return revertButton;
    }
    // end of static bits of gui
    
    // bits of gui that are all or part auto-generated
    /**
     * Create the tabbed pane within the main frame within the main
     * window of the GUI. This pane contains the content panes which define
     * the actual tabs of the GUI.
     * 
     * This is contained within {@link #createJContentPane()}.
     * This contains {@link #getTabbedBodyFrame(String, JScrollPane)}.
     * 
     * This function includes xml-dependent code.
     * 
     * @return Tabbed pane within which the tabs for the GUI are defined
     */
    private JTabbedPane createJTabbedPane() 
    {
        if (jTabbedPane == null) 
        {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.setName("jTabbedPane");
            jTabbedPane.setBounds(new Rectangle(0, 0, 595, 416));
            
            String[] orderedTabNames = new String[metaPrefSet.getNumberOfTabs()];
            metaPrefSet.getOrderedTabNames(orderedTabNames);
            for (String cat : orderedTabNames)
            {
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setSize(new Dimension(jTabbedPane.getWidth(),jTabbedPane.getHeight()));
                scrollPane.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setName(cat);
                scrollPane.setViewportView(getTabbedBodyFrame(cat,scrollPane));
                jTabbedPane.addTab(cat, null, scrollPane, null);
                
                // add category to components map
                LinkedHashMap<String,ObjectRegistry<JComponent>> catHash = jCatComponentsMap.get(cat);
                if (catHash == null)
                {
                    catHash = new LinkedHashMap<String,ObjectRegistry<JComponent>>();
                    jCatComponentsMap.put(cat, catHash);
                    //System.err.print("Adding: " + cat + '\n');
                }
            }
        }
        return jTabbedPane;
    }

    /**
     * The actual tab for a category within the GUI.
     * 
     * This is contained within {@link #createJTabbedPane()}
     * This contains {@link #createDescriptionSection(JPanel, GridBagConstraints, int, MetaPref)},
     * and {@link #createWidgetFromMetaPref(GridBagConstraints, MetaPref, int)},
     * and {@link #createSeparator(GridBagConstraints, int)}.
     * 
     * This function includes xml-dependent code.
     * 
     * @param category      Name of category
     * @param scrollPane    The pane that scrolls within this panel
     * 
     * @return Tab for a category within the GUI
     */
    @SuppressWarnings("serial")
    private JPanel getTabbedBodyFrame(String category, JScrollPane scrollPane)
    {
        JPanel contentPanel = new JPanel()
        {
            private boolean firstTime = true;
            @Override public void paintComponent(Graphics g)
            {
                if (firstTime)
                {
                    firstTime = false;
                    int numberOfEntries = this.getComponentCount();
                    for (int i=0; i < numberOfEntries; i+=3)
                    {
                        // TODO: this only works because we alternate adding JLabels and JPanels and JSeparators
                        if ((this.getComponent(i) instanceof JLabel) && (this.getComponent(i+1) instanceof JPanel) && (this.getComponent(i+2) instanceof JSeparator))
                        {
                            JLabel desc = (JLabel)this.getComponent(i);
                            JPanel val  = (JPanel)this.getComponent(i+1);
                            JSeparator sep = (JSeparator)this.getComponent(i+2);

                            FontMetrics fm = desc.getFontMetrics(desc.getFont());
                            int actualWidth = (this.getWidth()-val.getWidth())-LEFT_GUI_INSET;
                            int stringWidth = SwingUtilities.computeStringWidth(fm, desc.getText());
                            
                            desc.setPreferredSize(new Dimension(actualWidth,((stringWidth/actualWidth)+1)*fm.getHeight()));
                            sep.setPreferredSize(new Dimension(this.getWidth()-(LEFT_GUI_INSET*2),3));
                            if (i+3 == numberOfEntries)
                            {
                                // make last separator invisible
                                sep.setVisible(false);
                            }
                        }
                    }
                }
                super.paintComponent(g);
            }
        };

        // make tooltips display quickly
        ToolTipManager.sharedInstance().setInitialDelay(10);
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setMaximumSize(new Dimension(scrollPane.getWidth(),Integer.MAX_VALUE));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 0.1;
        int rowNum = 0;
        for (MetaPref metaPref : metaPrefSet.getMetaPrefListByCategory(category))
        {
            // add metapref to components map
            //System.err.print(category + '\n' + metaPref.getID() + '\n');
            LinkedHashMap<String, ObjectRegistry<JComponent>> catHash = jCatComponentsMap.get(category);
			if (catHash == null)
            {
                catHash = new LinkedHashMap<String,ObjectRegistry<JComponent>>();
                jCatComponentsMap.put(category, catHash);
            }
            ObjectRegistry<JComponent> mpComponentRegistry = catHash.get(metaPref.getID());
            if (mpComponentRegistry == null)
            {
                mpComponentRegistry = new ObjectRegistry<JComponent>();
                catHash.put(metaPref.getID(), mpComponentRegistry);
            }
            
            JLabel subDescription = createDescriptionSection(contentPanel, constraints, rowNum, metaPref);
            JPanel subValue       = createWidgetFromMetaPref(constraints, metaPref, rowNum);
            
            subValue.setMaximumSize(new Dimension(scrollPane.getWidth()/2,100));
            subDescription.setMaximumSize(new Dimension(scrollPane.getWidth()/2,50));

            // we have to do this in order for our redraw code to work properly.
            subDescription.setPreferredSize(new Dimension(1,1));

            //add these suckers to the contentpanel.
            constraints.anchor = GridBagConstraints.FIRST_LINE_START;
            contentPanel.add(subDescription, constraints);
            if (subValue != null)
            {
                constraints.anchor = GridBagConstraints.FIRST_LINE_END;
                contentPanel.add(subValue, constraints);
            }
            JSeparator separator  = createSeparator(constraints, rowNum);
            constraints.anchor = GridBagConstraints.NORTH;
            contentPanel.add(separator,constraints);
            rowNum++;
        }
        
        return contentPanel;
    }

    /**
     * Create the UI component (aka widget) that enables the user to manipulate the value of the preference.
     * This is contained within {@link #getTabbedBodyFrame(String, JScrollPane)}.
     * This function includes xml-dependent code.
     * 
     * @param constraints       GridBagConstraints - insets, row, col, etc
     * @param metaPref          Description of what widget type to use, default values, ...
     * @param rowNum            Row number for GridBagLayout
     * 
     * @return JLabel           Value section
     */
    private JPanel createWidgetFromMetaPref(GridBagConstraints constraints, MetaPref<?> metaPref, int rownum)
    {
        JPanel panel = new JPanel();
        panel.setName(metaPref.getID());
        panel.setLayout(new GridBagLayout());
        
        if (metaPref.widgetIsTextField())
        {
            createTextField(panel, metaPref);
        }
        else if (metaPref.widgetIsRadio())
        {
            if (metaPref instanceof MetaPrefBoolean)
            {
                createYesNoBooleanRadio((MetaPrefBoolean) metaPref, panel);
            }
            else
            {
                createChoicesBasedRadio(metaPref, panel);
            }
        }
        else if (metaPref.widgetIsDropDown())
        {
            createDropDown(panel, metaPref);
        }
        else if (metaPref.widgetIsCheckBox())
        {
            createCheckBox(panel, metaPref);
        }
        else if (metaPref.widgetIsSlider())
        {
            if (metaPref instanceof MetaPrefInt)
            {
                createSlider(panel, metaPref, IDENTIFIER_SLIDER, false, 0);
            }
            else if (metaPref instanceof MetaPrefFloat)
            {
                createSlider(panel, metaPref, IDENTIFIER_SLIDER, true, FLOAT_SLIDER_MODIFIER);
            }
        }
        else if (metaPref.widgetIsSpinner())
        {
            createSpinner(panel, metaPref);
        }
        else if (metaPref.widgetIsColorChooser())
        {
            createColorChooser(panel, metaPref);
        }
        else if (metaPref.widgetIsFileChooser())
        {
            createFileButton(metaPref, panel);
        }
        
        panel.setVisible(true);
        
        if (panel != null)
        {
            constraints.gridx = 1;
            constraints.gridy = rownum;
            constraints.gridwidth = 1;
            constraints.insets = new Insets(TOP_GUI_INSET,LEFT_GUI_INSET,BOTTOM_GUI_INSET,0); // top,left,bottom,right
            // if we have a prefs value, override it now
            if (Pref.hasPref(metaPref.getID()))
            {
                setWidgetToPrefValue(metaPref, Pref.lookupPref(metaPref.getID()));
            }
        }
        return panel;
    }

    /**
     * Set the metapref's widget in the prefs editor to the value in 
     * the prefs xml file.
     * 
     * @param mp        metapref
     * @param pref      pref to set metapref's widget to
     */
    private void setWidgetToPrefValue(MetaPref mp, Pref pref)
    {
        if (mp.widgetIsTextField())
        {
            JTextField textField = (JTextField)lookupComponent(mp, mp.getID()+IDENTIFIER_TEXT_FIELD);
            textField.setText(pref.value().toString());
        }
        else if (mp.widgetIsRadio())
        {
            if (mp instanceof MetaPrefBoolean)
            {
                boolean prefValue = (Boolean)pref.value();
                if (prefValue)
                {
                    JRadioButton yesButton = (JRadioButton)lookupComponent(mp, mp.getID()+IDENTIFIER_BOOLEAN_YES);
                    ButtonModel yesModel = yesButton.getModel();
                    yesModel.setSelected(true);
                }
                else
                {
                    JRadioButton noButton = (JRadioButton)lookupComponent(mp, mp.getID()+IDENTIFIER_BOOLEAN_NO);
                    ButtonModel noModel = noButton.getModel();
                    noModel.setSelected(true);
                }
            }
            else
            {
                // registered name
                String regName = mp.getID() + mp.getChoiceNameByValue(pref.value().toString());
                JRadioButton defaultButton = (JRadioButton) lookupComponent(mp,regName);
                ButtonModel buttonModel = defaultButton.getModel();
                buttonModel.setSelected(true);
            }
        }
        else if (mp.widgetIsDropDown())
        {
            JComboBox comboBox = (JComboBox)lookupComponent(mp, mp.getID() + IDENTIFIER_DROPDOWN);
            comboBox.setSelectedIndex(mp.getIndexByValue(pref.value().toString()));
        }
        else if (mp.widgetIsCheckBox())
        {
            JCheckBox checkBox = (JCheckBox)lookupComponent(mp, mp.getID() + IDENTIFIER_CHECK_BOX);
            checkBox.setSelected((Boolean)pref.value());
        }
        else if (mp.widgetIsSlider())
        {
            if (mp instanceof MetaPrefInt)
            {
                Integer prefValue = (Integer)pref.value();
                JSlider jSlider = (JSlider)lookupComponent(mp,mp.getID()+IDENTIFIER_SLIDER);
                jSlider.setValue(prefValue);
            }
            else if (mp instanceof MetaPrefFloat)
            {
                Float prefValue = (Float)pref.value();
                JSlider jSlider = (JSlider)lookupComponent(mp,mp.getID()+IDENTIFIER_SLIDER);
                jSlider.setValue((int)(prefValue*FLOAT_SLIDER_MODIFIER));
            }
        }
        else if (mp.widgetIsSpinner())
        {
            JSpinner jSpinner = (JSpinner)lookupComponent(mp,mp.getID()+IDENTIFIER_SPINNER);
            jSpinner.setValue(pref.value());
        }
        else if (mp.widgetIsColorChooser())
        {
            Color prefValue = (Color)pref.value();
            JColorChooser colorChooser = (JColorChooser)lookupComponent(mp,mp.getID()+IDENTIFIER_COLOR_CHOOSER);
            if (colorChooser != null)
            {
                colorChooser.setColor(prefValue);
            }
        }
        else if (mp.widgetIsFileChooser())
        {
            JFileChooser fileChooser = (JFileChooser)lookupComponent(mp,mp.getID()+IDENTIFIER_FILE_CHOOSER);
            fileChooser.setSelectedFile((File)pref.value());
        }
    }
    
    /**
     * Revert the metapref's widget to the default value for it, defined
     * in the metaprefs xml file.
     * 
     * @param mp        metapref to revert
     */
    private void revertToDefault(MetaPref mp)
    {
        if (mp.widgetIsTextField())
        {
            JTextField textField = (JTextField)lookupComponent(mp, mp.getID()+IDENTIFIER_TEXT_FIELD);
            textField.setText(mp.getDefaultValue().toString());
        }
        else if (mp.widgetIsRadio())
        {
            if (mp instanceof MetaPrefBoolean)
            {
                // get button
                JRadioButton yesButton = (JRadioButton)lookupComponent(mp, mp.getID()+IDENTIFIER_BOOLEAN_YES);
                ButtonModel yesModel = yesButton.getModel();
                boolean yesVal = (Boolean)mp.getDefaultValue();
                
                if (yesVal)
                {
                    yesModel.setSelected(true);
                }
                else
                {
                    JRadioButton noButton = (JRadioButton)lookupComponent(mp, mp.getID()+IDENTIFIER_BOOLEAN_NO);
                    ButtonModel noModel = noButton.getModel();
                    noModel.setSelected(true);
                }
            }
            else
            {
                String regName = mp.getID() + mp.getChoiceNameByValue(mp.getDefaultValue().toString());
                //println("we think the name is: " + regName);
                JRadioButton defaultButton = (JRadioButton)lookupComponent(mp,regName);
                ButtonModel buttonModel = defaultButton.getModel();
                buttonModel.setSelected(true);
            }
        }
        else if (mp.widgetIsDropDown())
        {
            JComboBox comboBox = (JComboBox)lookupComponent(mp,mp.getID()+IDENTIFIER_DROPDOWN);
            comboBox.setSelectedIndex(mp.getIndexByValue(mp.getDefaultValue().toString()));
        }
        else if (mp.widgetIsCheckBox())
        {
            JCheckBox checkBox = (JCheckBox)lookupComponent(mp, mp.getID() + IDENTIFIER_CHECK_BOX);
            checkBox.setSelected((Boolean)mp.getDefaultValue());
        }
        else if (mp.widgetIsSlider())
        {
            if (mp instanceof MetaPrefInt)
            {
                JSlider jSlider = (JSlider)lookupComponent(mp,mp.getID()+IDENTIFIER_SLIDER);
                jSlider.setValue((Integer)mp.getDefaultValue());
            }
            else if (mp instanceof MetaPrefFloat)
            {
                int alteredDefault = (int)((Float)mp.getDefaultValue()*FLOAT_SLIDER_MODIFIER);
                JSlider jSlider = (JSlider)lookupComponent(mp,mp.getID()+IDENTIFIER_SLIDER);
                jSlider.setValue(alteredDefault);
            }
        }
        else if (mp.widgetIsSpinner())
        {
            JSpinner jSpinner = (JSpinner)lookupComponent(mp,mp.getID()+IDENTIFIER_SPINNER);
            jSpinner.setValue(mp.getDefaultValue());
        }
        else if (mp.widgetIsColorChooser())
        {
            JColorChooser colorChooser = (JColorChooser)lookupComponent(mp,mp.getID()+IDENTIFIER_COLOR_CHOOSER);
            colorChooser.setColor((Color)mp.getDefaultValue());
        }
        else if (mp.widgetIsFileChooser())
        {
            JFileChooser fileChooser = (JFileChooser)lookupComponent(mp,mp.getID()+IDENTIFIER_FILE_CHOOSER);
            fileChooser.setSelectedFile((File)mp.getDefaultValue());
        }
    }
    
    /**
     * The function that actually performs the revert-to-default actions
     * is in the MetaPrefType classes.
     */
    private void actionRevertPreferencesToDefault()
    {
        for (String cat : metaPrefSet.getCategories())
        {
            for (MetaPref mp : metaPrefSet.getMetaPrefListByCategory(cat))
            {
                revertToDefault(mp);
            }
        }
    }
    // end of gui actions for buttons

    /**
     * Creates a separator to go between two preferences in the GUI.
     * This is contained within {@link #getTabbedBodyFrame(String, JScrollPane)}.
     * This function includes xml-dependent code.
     * 
     * @param constraints   GridBagConstraints - insets, row, col, etc
     * @param rowNum        Row number for GridBagLayout
     * 
     * @return JSeparator for tab frame in GUI
     */
    private JSeparator createSeparator(GridBagConstraints constraints, int rowNum)
    {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        constraints.gridx = 0;
        constraints.gridy = rowNum + 1;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(SMALL_TOP_GUI_INSET,0,0,0);
        return separator;
    }

    /**
     * Create the description section for a preference.
     * 
     * This is contained within {@link #getTabbedBodyFrame(String, JScrollPane)}.
     * This contains {@link #createLabel(JPanel, MetaPref, int, int)}.
     * 
     * This function includes xml-dependent code.
     * 
     * @param contentPanel      The panel that contains this description panel.
     * @param constraints       GridBagConstraints - insets, row, col, etc
     * @param rowNum            Row number for GridBagLayout
     * @param mp                MetaPref this description is for
     * 
     * @return JLabel   Description section
     */
    private JLabel createDescriptionSection(JPanel contentPanel, GridBagConstraints constraints, int rowNum, MetaPref mp)
    {
        JLabel subDescription = createLabel(contentPanel,mp,0,0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.gridx = 0;
        constraints.gridy = rowNum;
        constraints.gridwidth = 1;
        constraints.insets = new Insets(TOP_GUI_INSET,LEFT_GUI_INSET,BOTTOM_GUI_INSET,0); // top,left,bottom,right
        return subDescription;
    }
    
    /**
     * Creates a label for a preference.
     * 
     * This is contained within {@link #createDescriptionSection(JPanel, GridBagConstraints, int, MetaPref)}.
     * This contains {@link #wrapTooltip(MetaPref)}.
     * 
     * This function includes xml-dependent code.
     * 
     * @param panel         JPanel this label will be associated with.
     * @param mp            MetaPref this label is for.
     * @param row           Row this label is in for GridBagLayout
     * @param col           Column this label is in for GridBagLayout
     * 
     * @return JLabel with properties initialized to parameters.
     */
    private JLabel createLabel(JPanel panel, MetaPref mp, int row, int col)
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        
        JLabel label = new JLabel();
        String wrapText = "<html>" + mp.getDescription() + "</html>";
        label.setText(wrapText);
        File prefsRoot = Assets.getAsset("preferences");
        File imgFile = new File(prefsRoot, "question1.png");
        ImageIcon imgIcon = new ImageIcon(imgFile.getAbsolutePath());
        label.setIcon(imgIcon);
        
        //nasty workaround because there is no API option to wrap tooltips
        String formattedToolTip = wrapTooltip(mp);
        
        label.setToolTipText(formattedToolTip);
        label.setHorizontalAlignment(SwingConstants.LEADING);
        label.setHorizontalTextPosition(SwingConstants.RIGHT);
        label.setVerticalAlignment(SwingConstants.TOP);
        c.gridx = col;
        c.gridy = row;
        c.weightx = 0.5;
        c.insets = new Insets(0,LEFT_GUI_INSET,0,0); // top,left,bottom,right
        
        panel.add(label, c);
        
        return label;
    }

    /**
     * This allows you to wrap the help tooltip text, because there is no
     * way to normally do this.
     * 
     * This is contained within {@link #createLabel(JPanel, MetaPref, int, int)}.
     * 
     * @param mp    MetaPref this tooltip is for.
     * 
     * @return Tool tip wrapped via HTML.
     */
    private String wrapTooltip(MetaPref mp)
    {
        String formattedToolTip = "<html>";
        String hText = mp.getHelpText();
        if (hText != null && hText != "")
        {
            int tiplen = hText.length();
            int wrapAt = TOOLTIP_WRAP_WIDTH;
            int nowAt = 0;
            int breakAt = 0;
            if (wrapAt > tiplen-1)
            {
                formattedToolTip = formattedToolTip.concat(hText.substring(nowAt, tiplen) + "<br>");
            }
            else
            {
                do
                {
                    nowAt = breakAt;
                    breakAt = hText.indexOf(" ", (nowAt+wrapAt));
                    if (breakAt > tiplen-1)
                    {
                        formattedToolTip = formattedToolTip.concat(hText.substring(nowAt, tiplen) + "<br>");
                    }
                    else if (breakAt > 0)
                    {
                        formattedToolTip = formattedToolTip.concat(hText.substring(nowAt, breakAt) + "<br>");
                    }
                    else
                    {
                        formattedToolTip = formattedToolTip.concat(hText.substring(nowAt, tiplen) + "<br>");
                        break;
                    }
                } while(nowAt < tiplen-1);
            }
        }
        formattedToolTip = formattedToolTip.concat("</html>");
        return formattedToolTip;
    }
    
    /**
     * Creates a "change file" button for file chooser types
     * and places it in the panel.
     * 
     * @param mp        metapref the button is associated with
     * @param panel     panel the button will be associated with
     */
    private void createFileButton(MetaPref metaPref, JPanel panel)
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
        registerComponent(metaPref, IDENTIFIER_FILE_CHOOSER, fileChooser);
        panel.add(jButton,c);
    }
    
    /**
     * Creates a color chooser dialog for a color chooser metapref 
     * and places it on the panel.
     * 
     * @param jPanel    panel the dialog will be associated with
     * @param mp        metapref the chooser is being created for
     */
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
    
    /**
     * Creates a color chooser button and places it on the panel.
     * 
     * @param panel     panel the button will be associated with
     * @param mp        MetaPref the button is for
     */
    private void createColorChooser(JPanel panel, MetaPref mp)
    {
        setupColorChooser(panel,mp);

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
                colorChooserDialog.setVisible(true);
            }
        });
        panel.add(jButton,c);
        registerComponent(mp, IDENTIFIER_COLOR_CHOOSER, colorChooser);
    }
    
    /**
     * This creates a radio button for a metapref that lists choices, rather
     * than depending on default values. This is what you want for anything that is
     * not: two options with the labels Yes and No.
     * See also {@link #createYesNoBooleanRadio(MetaPref, JPanel)}.
     * 
     * @param metaPref
     * @param panel
     */
    private void createChoicesBasedRadio(MetaPref metaPref, JPanel panel)
    {
        ArrayListState<Choice<Object>> choices = metaPref.getChoices();
        if (choices != null)
        {
            ButtonGroup buttonGroup = new ButtonGroup();
            int rnum = 0;
            for (Choice choice : choices)
            {
                final Object currentValue = metaPref.getDefaultValue();
				boolean isSelected = currentValue.equals(choice.getValue());
                createRadio(panel, metaPref, buttonGroup, isSelected, choice.getLabel(), choice.getName(), rnum, 0);
                rnum++;
            }
        }
    }

    /**
     * This creates a radio button for a standard Yes/No boolean preference.
     * See also {@link #createChoicesBasedRadio(MetaPref, JPanel)}.
     * 
     * @param metaPref
     * @param panel
     */
    private void createYesNoBooleanRadio(MetaPrefBoolean metaPref, JPanel panel)
    {
       // Boolean currentValue 		= (Boolean) metaPref.getDefaultValue(); -- changed by andruid 
        Boolean currentValue 		= metaPref.usePrefBoolean().value();
        ArrayListState<Choice<Boolean>> choices = metaPref.getChoices();
        ButtonGroup radioPair = new ButtonGroup();
        
        if (choices != null)
        {
            ChoiceBoolean choice0   = (ChoiceBoolean) choices.get(0);
            boolean isDefault       = currentValue.equals(choice0.getValue());
            String name             = isDefault ? IDENTIFIER_BOOLEAN_YES : IDENTIFIER_BOOLEAN_NO;
            createRadio(panel, metaPref, radioPair, isDefault, choice0.getLabel(), name, 0, 0);
            name                    = !isDefault ? IDENTIFIER_BOOLEAN_YES : IDENTIFIER_BOOLEAN_NO;
            ChoiceBoolean choice1   = (ChoiceBoolean) choices.get(1);
            createRadio(panel, metaPref, radioPair, isDefault, choice1.getLabel(), name, 1, 0);
        }
        else
        {
            boolean yesVal  = currentValue;
            boolean noVal   = !currentValue;
            createRadio(panel, metaPref, radioPair, yesVal, IDENTIFIER_BOOLEAN_YES, IDENTIFIER_BOOLEAN_YES, 0, 0);
            createRadio(panel, metaPref, radioPair, noVal, IDENTIFIER_BOOLEAN_NO, IDENTIFIER_BOOLEAN_NO, 1, 0);
        }
    }
    
    public void windowActivated(WindowEvent e)
    {
    }

    public void windowClosed(WindowEvent e)
    {
    }

    /**
     * Altered behavior:
     *      print that we are closing
     *      save our window location
     */
    public void windowClosing(WindowEvent e)
    {
        System.err.print("window closing");
        saveLocation();
    }

    public void windowDeactivated(WindowEvent e)
    {
    }

    public void windowDeiconified(WindowEvent e)
    {
    }

    public void windowIconified(WindowEvent e)
    {
    }

    public void windowOpened(WindowEvent e)
    {
    }

    /**
     * Creates a radio button.
     * 
     * @param panel         JPanel this button will be associated with.
     * @param mp            MetaPref this button is being created for.
     * @param buttonGroup   ButtonGroup this button is a member of.
     * @param initialValue  boolean; true=selected. false=not selected.
     * @param label         Text label for button
     * @param name          Name of button
     * @param row           Row this button is in for GridBagLayout
     * @param col           Column this button is in for GridBagLayout
     * 
     * @return JRadioButton with properties initialized to parameters.
     */
    protected JRadioButton createRadio(JPanel panel, MetaPref mp, ButtonGroup buttonGroup, boolean initialValue, String label, String name, int row, int col)
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        
        JRadioButton radioButton = new JRadioButton();
        
        radioButton.setSelected(initialValue);
        radioButton.setName(name);
        radioButton.setText(label);
        c.gridx = col;
        c.gridy = row;
        c.insets = new Insets(0,0,0,RIGHT_GUI_INSET); // top,left,bottom,right
        
        buttonGroup.add(radioButton);
        panel.add(radioButton, c);
    
        // add metapref's component to array
        ObjectRegistry<JComponent> mpComponents = jCatComponentsMap.get(mp.getCategory()).get(mp.getID());
        if (mpComponents != null)
        {
            registerComponent(mp, name, radioButton);
        }
        
        return radioButton;
    }
}
