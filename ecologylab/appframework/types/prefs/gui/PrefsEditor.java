package ecologylab.appframework.types.prefs.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
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
import javax.swing.JFormattedTextField;
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
import javax.swing.JToolTip;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

import ecologylab.appframework.ApplicationEnvironment;
import ecologylab.appframework.ObjectRegistry;
import ecologylab.appframework.types.prefs.Choice;
import ecologylab.appframework.types.prefs.ChoiceBoolean;
import ecologylab.appframework.types.prefs.MetaPref;
import ecologylab.appframework.types.prefs.MetaPrefBoolean;
import ecologylab.appframework.types.prefs.MetaPrefFloat;
import ecologylab.appframework.types.prefs.MetaPrefInt;
import ecologylab.appframework.types.prefs.MetaPrefSet;
import ecologylab.appframework.types.prefs.MetaPrefString;
import ecologylab.appframework.types.prefs.Pref;
import ecologylab.appframework.types.prefs.PrefSet;
import ecologylab.appframework.types.prefs.ValueChangedListener;
import ecologylab.generic.Debug;
import ecologylab.io.Assets;
import ecologylab.net.ParsedURL;
import ecologylab.xml.XmlTranslationException;
import ecologylab.xml.types.element.ArrayListState;

/**
 * Create the GUI for preference editing; also responsible for all
 * actions associated with the GUI.
 * 
 * @author Cae
 *
 */
public class PrefsEditor
extends Debug
implements WindowListener, ChangeListener
{
    /**
     * The string that will be added to the metapref's id, to create
     * a unique identifier for a jComponent.
     */
    private static final String IDENTIFIER_CHECK_BOX = "checkBox";
    /**
     * The string that will be added to the metapref's id, to create
     * a unique identifier for a jComponent.
     */
    private static final String IDENTIFIER_BOOLEAN_NO = "No";
    /**
     * The string that will be added to the metapref's id, to create
     * a unique identifier for a jComponent.
     */
    private static final String IDENTIFIER_BOOLEAN_YES = "Yes";
    /**
     * The string that will be added to the metapref's id, to create
     * a unique identifier for a jComponent.
     */
    private static final String IDENTIFIER_FILE_CHOOSER = "fileChooser";
    /**
     * The string that will be added to the metapref's id, to create
     * a unique identifier for a jComponent.
     */
    private static final String IDENTIFIER_COLOR_CHOOSER = "colorChooser";
    /**
     * The string that will be added to the metapref's id, to create
     * a unique identifier for a jComponent.
     */
    private static final String IDENTIFIER_SPINNER = "spinner";
    /**
     * The string that will be added to the metapref's id, to create
     * a unique identifier for a jComponent.
     */
    private static final String IDENTIFIER_SLIDER = "slider";
    /**
     * The string that will be added to the metapref's id, to create
     * a unique identifier for a jComponent.
     */
    private static final String IDENTIFIER_DROPDOWN = "dropdown";
    /**
     * The string that will be added to the metapref's id, to create
     * a unique identifier for a jComponent.
     */
    private static final String IDENTIFIER_TEXT_FIELD = "textField";
    
    /**
     * The amount by which floats should be multiplied for use with sliders.
     * If this number is 10, you will be able to see the 1 digit after the
     * decimal when the number is changed back (10.1). For 100, you will have 
     * 2 digits after the decimal (10.11). Etc.
     */
    private static final int FLOAT_SLIDER_MODIFIER = 10;
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
    /**
     * The padding between the default value in a text field and either 
     * side of the text field.
     */
    protected static final int TEXT_FIELD_PADDING = 50;

    /**
     * The inset between the right side of the gui panel and the right 
     * side of values.
     */
    protected static final int RIGHT_GUI_INSET = 20;
    /**
     * Set of MetaPrefs
     */
    MetaPrefSet metaPrefSet;
    /**
     * Set of Prefs
     */
    PrefSet     prefSet;
    /**
     * PURL to save prefs.xml to
     */
    ParsedURL   savePrefsPURL;
    
    // base setup for gui
    /**
     * Base window for the GUI
     */
    JFrame 		jFrame = null;
    /**
     * Content pane within base window for GUI
     */
    JPanel 		jContentPane = null;
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
     * Okay, this is really, really important. The map goes like this:
     * CategoryName to (MetaPrefName to (Array of JComponents))
     * So for each category, you get a sorted map of the metaprefs in
     * that category. From that map you can get a list of all the components
     * for a metapref by name.
     */
    protected HashMap<String,LinkedHashMap<String,ObjectRegistry<JComponent>>> jCatComponentsMap = new HashMap<String,LinkedHashMap<String,ObjectRegistry<JComponent>>>();
    
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
    public PrefsEditor(MetaPrefSet metaPrefSet, PrefSet prefSet, ParsedURL savePrefsPURL, final boolean isStandalone)
    {
        this.metaPrefSet 	= metaPrefSet;
        this.prefSet     	= prefSet;
        this.savePrefsPURL  = savePrefsPURL;
        this.isStandalone	= isStandalone;
        
        final JFrame jFrame = fetchJFrame();
        jFrame.setLocation(startLocation);
        jFrame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
            	closeWindow();
            }
       });
        jFrame.setVisible(true);
    }
    
    /**
     * Calls createJFrame
     * @see #createJFrame()
     * @return Base window (JFrame) for the GUI
     */
    public JFrame fetchJFrame()
    {
        return createJFrame();
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
    private JFrame createJFrame() 
    {
        if (jFrame == null) 
        {
            jFrame = new JFrame();
            jFrame.setPreferredSize(new Dimension(603, 532));
            jFrame.setSize(new Dimension(603, 532));
            jFrame.setTitle("combinFormation Preferences");
            jFrame.setContentPane(createJContentPane());
            jFrame.addWindowListener(this);
        }
        return jFrame;
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
    private JButton createSaveButton() 
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
    private JButton createApplyButton() 
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
     * and {@link #createValueSection(GridBagConstraints, MetaPref, int)},
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
            public void paintComponent(Graphics g)
            {
                if (firstTime)
                {
                    firstTime = false;
                    int numberOfEntries = this.getComponentCount();
                    for (int i=0; i < numberOfEntries; i+=3)
                    {
                        // TODO: this only works because we alternate adding JLabels and JPanels and JSeparators
                        if (((JLabel)this.getComponent(i) instanceof JLabel) && ((JPanel)this.getComponent(i+1) instanceof JPanel) && ((JSeparator)this.getComponent(i+2) instanceof JSeparator))
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
            if (jCatComponentsMap.get(category)==null)
            {
                LinkedHashMap<String,ObjectRegistry<JComponent>> catHash = new LinkedHashMap<String,ObjectRegistry<JComponent>>();
                jCatComponentsMap.put(category, catHash);
            }
            ObjectRegistry<JComponent> mpComponents = jCatComponentsMap.get(category).get(metaPref.getID());
            if (mpComponents == null)
            {
                LinkedHashMap<String,ObjectRegistry<JComponent>> catHash = jCatComponentsMap.get(category);
                mpComponents = new ObjectRegistry<JComponent>();
                catHash.put(metaPref.getID(), mpComponents);
            }
            
            JLabel subDescription = createDescriptionSection(contentPanel, constraints, rowNum, metaPref);
            JPanel subValue       = createValueSection(constraints, metaPref, rowNum);
            
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
     * Create the value section for a preference.
     * This is contained within {@link #getTabbedBodyFrame(String, JScrollPane)}.
     * This function includes xml-dependent code.
     * 
     * @param constraints       GridBagConstraints - insets, row, col, etc
     * @param metaPref          MetaPref this description is for
     * @param rowNum            Row number for GridBagLayout
     * 
     * @return JLabel           Value section
     */
    private JPanel createValueSection(GridBagConstraints constraints, MetaPref metaPref, int rownum)
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
                createBooleanDefaultRadio(metaPref, panel);
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
            // not implemented right now
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
     * Get the pref value (if applicable) for a given metapref.
     * 
     * @param mp    metapref to fetch pref value for
     * @return      pref value or null
     */
    //FIXME use generics throughout!
    private <T> /*T*/ Object getPrefValue(MetaPref<T> mp)
    {
        if (mp.widgetIsTextField())
        {
            JTextField textField = (JTextField)lookupComponent(mp, mp.getID()+IDENTIFIER_TEXT_FIELD);
            //TODO 1 -- why doesnt this line work -- check flow for MetaPrefInt first!
            //return mp.getInstance(textField.getText());
            
            if (mp instanceof MetaPrefString)
            {
                return new String(textField.getText());
            }
            else if (mp instanceof MetaPrefInt)
            {
                return new Integer(textField.getText());
            }
            else if (mp instanceof MetaPrefFloat)
            {
                return new Float(textField.getText());
            }
            
        }
        else if (mp.widgetIsRadio())
        {
            if (mp instanceof MetaPrefBoolean)
            {
                JRadioButton yesButton = (JRadioButton)lookupComponent(mp, mp.getID()+IDENTIFIER_BOOLEAN_YES);
                MetaPref<Boolean> mpb   = (MetaPrefBoolean) mp;
                return mpb.getInstance(yesButton.isSelected());
            }
            else
            {
                // TODO: if we could fetch the ButtonGroup, we could do
                //       this more efficiently.
                // find the selected one and return it
                ArrayListState<Choice<T>> choices = mp.getChoices();
                for (Choice choice: choices)
                {
                    String regName = mp.getID() + choice.getName();
                    JRadioButton choiceButton = (JRadioButton) lookupComponent(mp,regName);
                    if (choiceButton.isSelected())
                    {
                        if (mp instanceof MetaPrefFloat)
                            return (Float)choice.getValue();
                        else if (mp instanceof MetaPrefInt)
                            return (Integer)choice.getValue();
                        else
                            return null;
                    }
                }
            }
        }
        else if (mp.widgetIsDropDown())
        {
            if (mp instanceof MetaPrefFloat)
            {
                MetaPrefFloat mpf   = (MetaPrefFloat) mp;
                ArrayListState<Choice<Float>> choices = mpf.getChoices();
                JComboBox comboBox = (JComboBox)lookupComponent(mp,mp.getID()+IDENTIFIER_DROPDOWN);
                int selectedIndex = comboBox.getSelectedIndex();
                return new Float(choices.get(selectedIndex).getValue());
            }
            else if (mp instanceof MetaPrefInt)
            {
                JComboBox comboBox = (JComboBox)lookupComponent(mp,mp.getID()+IDENTIFIER_DROPDOWN);
                return new Integer(comboBox.getSelectedIndex());
            }
        }
        else if (mp.widgetIsCheckBox())
        {
            JCheckBox checkBox = (JCheckBox)lookupComponent(mp, mp.getID() + IDENTIFIER_CHECK_BOX);
            return new Boolean(checkBox.isSelected());
        }
        else if (mp.widgetIsSlider())
        {
            if (mp instanceof MetaPrefInt)
            {
                JSlider jSlider = (JSlider)lookupComponent(mp,mp.getID()+IDENTIFIER_SLIDER);
                return new Integer(jSlider.getValue());
            }
            else if (mp instanceof MetaPrefFloat)
            {
                JSlider jSlider = (JSlider)lookupComponent(mp,mp.getID()+IDENTIFIER_SLIDER);
                int sliderValue = jSlider.getValue();
                return new Float(((float)sliderValue)/FLOAT_SLIDER_MODIFIER);
            }
        }
        else if (mp.widgetIsSpinner())
        {
            JSpinner jSpinner = (JSpinner)lookupComponent(mp,mp.getID()+IDENTIFIER_SPINNER);
            if (mp instanceof MetaPrefInt)
                return new Integer((Integer)jSpinner.getValue());
            else if (mp instanceof MetaPrefFloat)
                return new Float((Float)jSpinner.getValue());
        }
        else if (mp.widgetIsColorChooser())
        {
            JColorChooser colorChooser = (JColorChooser)lookupComponent(mp,mp.getID()+IDENTIFIER_COLOR_CHOOSER);
            return (Color)colorChooser.getColor();
        }
        else if (mp.widgetIsFileChooser())
        {
            JFileChooser fileChooser = (JFileChooser)lookupComponent(mp,mp.getID()+IDENTIFIER_FILE_CHOOSER);
            return (File)fileChooser.getSelectedFile();
        }
        return null;
    }
    
    // gui actions for buttons
    /**
     * Save the preferences; called by {@link #createApplyButton()}
     * and {@link #createSaveButton()}.
     * Saves the prefs to {@link #savePrefsPURL}.
     */
    private void actionSavePreferences()
    {
        //debug("we pressed the save button");

    	/* we do this with metaprefs because we will always have
    	 * all metaprefs. we may not always have a prefs file to start
    	 * with. */
    	// this iterator organizes them by category
    	for (String cat : metaPrefSet.getCategories())
    	{
    		for (MetaPref mp : metaPrefSet.getMetaPrefListByCategory(cat))
    		{
    			String name = mp.getID();
    			Pref pref 	= mp.getAssociatedPref();
    			Object editedValue      = getPrefValue(mp);
                Object previousValue    = pref.value();
                pref.setValue(editedValue);
                ValueChangedListener valueChangedListener   = mp.getValueChangedListener();
                if ((valueChangedListener != null) && (editedValue != null))
                {
                    if (!editedValue.equals(previousValue))
                        valueChangedListener.valueChanged(pref);
                }
    			if (!prefSet.contains(pref))
    				prefSet.add(pref);
    		}
    	}
    	if (savePrefsPURL == null)
    	{
    		//TODO provide better feedback to the user here!!!
    		warning("Not saving Prefs persistently cause savePrefsURL == null.");
    	}
    	else
    	{
    		try
    		{
    			prefSet.saveXmlFile(savePrefsPURL.file(), true, false);
    		}
    		catch (XmlTranslationException e)
    		{
    			// TODO auto-generated catch block
    			e.printStackTrace();
    		}
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
    
    /**
     * Creates a text field.
     * 
     * @param panel         JPanel this field will be associated with.
     * @param mp            MetaPref this field is for.
     * 
     * @return JTextField with properties initialized to parameters.
     */
    protected JTextField createTextField(JPanel panel, MetaPref mp)
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        
        JTextField textField = new JTextField();
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setText(mp.getDefaultValue().toString());
        textField.setName(IDENTIFIER_TEXT_FIELD);
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,0,0,RIGHT_GUI_INSET); // top,left,bottom,right
        c.ipadx = TEXT_FIELD_PADDING;
        
        panel.add(textField, c);
        
        // add metapref's component to array
        ObjectRegistry<JComponent> mpComponents = jCatComponentsMap.get(mp.getCategory()).get(mp.getID());
        if (mpComponents != null)
        {
            registerComponent(mp, IDENTIFIER_TEXT_FIELD, textField);
        }
        
        return textField;
    }
    
    /**
     * Creates a check box.
     * 
     * @param panel         JPanel this button will be associated with.
     * @param mp            MetaPref this checkbox is being created for.
     * 
     * @return JCheckBox with properties initialized to parameters.
     */
    protected JCheckBox createCheckBox(JPanel panel, MetaPref mp)
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        
        JCheckBox checkBox = new JCheckBox();
        
        checkBox.setSelected((Boolean)mp.getDefaultValue());
        checkBox.setName(IDENTIFIER_CHECK_BOX);
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,0,0,RIGHT_GUI_INSET); // top,left,bottom,right
        
        panel.add(checkBox, c);
        
        // add metapref's component to array
        ObjectRegistry<JComponent> mpComponents = jCatComponentsMap.get(mp.getCategory()).get(mp.getID());
        if (mpComponents != null)
        {
            registerComponent(mp, IDENTIFIER_CHECK_BOX, checkBox);
        }
        
        return checkBox;
    }
    
    /**
     * Creates a drop down menu; combo box.
     * 
     * @param panel         JPanel this field will be associated with.
     * @param mp            MetaPref this combo box is being created for.
     * 
     * @return JComboBox with properties initialized to parameters.
     */
    protected JComboBox createDropDown(JPanel panel, MetaPref mp)
    {
        ArrayListState<Choice<Object>> choices = mp.getChoices();
        Object defValue = mp.getDefaultValue();
        if (choices != null)
        {
            String[] choiceLabels = new String[choices.size()];
            int i = 0;
            int selected = 0;
            for (Choice choice : choices)
            {
                choiceLabels[i] = choice.getLabel();
                if (choice.getValue()==defValue)
                    selected=i;
                i++;
            }
            
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.anchor = GridBagConstraints.FIRST_LINE_START;

            JComboBox comboBox = new JComboBox(choiceLabels);
            comboBox.setSelectedIndex(selected);
            comboBox.setName(IDENTIFIER_DROPDOWN);
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(0,0,0,RIGHT_GUI_INSET); // top,left,bottom,right
            c.ipadx = TEXT_FIELD_PADDING;

            panel.add(comboBox, c);

            // add metapref's component to array
            ObjectRegistry<JComponent> mpComponents = jCatComponentsMap.get(mp.getCategory()).get(mp.getID());
            if (mpComponents != null)
            {
                registerComponent(mp, IDENTIFIER_DROPDOWN, comboBox);
            }

            return comboBox;
        }
        return null;
    }
    
    /**
     * Creates a slider.
     * 
     * @param panel             JPanel this slider will be associated with.
     * @param mp                MetaPref this slider is being created for.
     * @param labelAndName      Name of slider
     * @param valuesAreAltered  Whether or not the values of the metapref/pref
     *                          need to be altered for the slider.
     * @param valueAlteredBy    How much the metapref/pref values are multiplied/
     *                          divided by. Only used if valuesAreAltered=true.
     * 
     * @return JSlider with properties initialized to parameters.
     */
    protected JSlider createSlider(JPanel panel, MetaPref mp, String labelAndName, boolean valuesAreAltered, int valueAlteredBy)
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        
        JSlider jSlider = new JSlider();
        if (valuesAreAltered)
        {
            // values must be altered by the modifier parameter
            int newMin = (int)((Float)mp.getMinValue()*FLOAT_SLIDER_MODIFIER);
            int newMax = (int)((Float)mp.getMaxValue()*FLOAT_SLIDER_MODIFIER);
            int defVal = (int)((Float)mp.getDefaultValue()*FLOAT_SLIDER_MODIFIER);
            jSlider.setMinimum(newMin);
            jSlider.setMaximum(newMax);
            jSlider.setValue(defVal);
            jSlider.setMajorTickSpacing(FLOAT_SLIDER_MODIFIER*5);
            jSlider.setMinorTickSpacing(FLOAT_SLIDER_MODIFIER/5);
            Float curValue = new Float((float)(jSlider.getValue())/FLOAT_SLIDER_MODIFIER);
            jSlider.setToolTipText(curValue.toString());
            
            // we need to mess with the labels so they display the correct values.
            Hashtable<Integer,JComponent> labelTable = jSlider.createStandardLabels(FLOAT_SLIDER_MODIFIER*5, newMin);
            // add a label for the max value to the table if one doesn't exist already
            if (!labelTable.containsKey(newMax))
            {
                Hashtable<Integer,JComponent> maxLabelTable = jSlider.createStandardLabels(FLOAT_SLIDER_MODIFIER*5, newMax);
                labelTable.putAll(maxLabelTable);
            }
            for (Map.Entry<Integer,JComponent> entry : labelTable.entrySet())
            {
                // changes here ARE reflected in labelTable
                
                // NOTE: this is the way you would change/remove entries from list of labels.
                // you cannot add entries here. see the Java API and:
                // http://java.sun.com/docs/books/tutorial/uiswing/components/slider.html
                // for more details
                JLabel label = (JLabel)entry.getValue();
                Float value = new Float(label.getText());
                value = value/FLOAT_SLIDER_MODIFIER;
                label.setText(value.toString());
            }
            jSlider.setLabelTable(labelTable);
            jSlider.setName(labelAndName+"thisisafloat");
        }
        else
        {
            // default behavior
            jSlider.setMinimum((Integer)mp.getMinValue());
            jSlider.setMaximum((Integer)mp.getMaxValue());
            jSlider.setValue((Integer)mp.getDefaultValue());
            jSlider.setMajorTickSpacing(10);
            jSlider.setMinorTickSpacing(1);
            Integer curValue = jSlider.getValue();
            jSlider.setToolTipText(curValue.toString());
            jSlider.setName(labelAndName);
        }
        
        
        jSlider.setPaintTicks(true);
        jSlider.setPaintLabels(true);
        jSlider.addChangeListener(this);
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,0,0,RIGHT_GUI_INSET); // top,left,bottom,right
        panel.add(jSlider, c);

        // add metapref's component to array
        ObjectRegistry<JComponent> mpComponents = jCatComponentsMap.get(mp.getCategory()).get(mp.getID());
        if (mpComponents != null)
        {
            registerComponent(mp, labelAndName, jSlider);
        }
        
        return jSlider;
    }
    
    /**
     * Creates a spinner. This is a text field with up/down arrows
     * that allow you to increase/decrease the value in the text box.
     * 
     * @param panel         JPanel this spinner will be associated with.
     * @param mp            MetaPref this spinner is being created for.
     * 
     * @return JSpinner with properties initialized to parameters.
     */
    protected JSpinner createSpinner(JPanel panel, MetaPref mp)
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        
        double stepSize = 1.0;
        if (mp instanceof MetaPrefFloat)
            stepSize = 0.1;
        
        SpinnerNumberModel numModel = new SpinnerNumberModel();
        numModel.setMinimum((Float)mp.getMinValue());
        numModel.setMaximum((Float)mp.getMaxValue());
        numModel.setValue(mp.getDefaultValue());
        numModel.setStepSize(stepSize);
        
        JSpinner jSpinner = new JSpinner();
        jSpinner.setModel(numModel);
        jSpinner.setValue(mp.getDefaultValue());
        jSpinner.setName(IDENTIFIER_SPINNER);
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,0,0,RIGHT_GUI_INSET); // top,left,bottom,right
        panel.add(jSpinner, c);

        // add metapref's component to array
        ObjectRegistry<JComponent> mpComponents = jCatComponentsMap.get(mp.getCategory()).get(mp.getID());
        if (mpComponents != null)
        {
            registerComponent(mp, IDENTIFIER_SPINNER, jSpinner);
        }
        
        return jSpinner;
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
                colorChooserDialog.show();
            }
        });
        panel.add(jButton,c);
        registerComponent(mp, IDENTIFIER_COLOR_CHOOSER, colorChooser);
    }
    
    /**
     * This creates a radio button for a metapref that lists choices, rather
     * than depending on default values. This is what you want for anything that is
     * not: two options with the labels Yes and No.
     * See also {@link #createBooleanDefaultRadio(MetaPref, JPanel)}.
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
                boolean isDefault = metaPref.getDefaultValue().equals(choice.getValue());
                createRadio(panel, metaPref, buttonGroup, isDefault, choice.getLabel(), choice.getName(), rnum, 0);
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
    private void createBooleanDefaultRadio(MetaPref metaPref, JPanel panel)
    {
        Boolean defVal = (Boolean)metaPref.getDefaultValue();
        ArrayListState<Choice<Boolean>> choices = metaPref.getChoices();
        ButtonGroup radioPair = new ButtonGroup();
        
        if (choices != null)
        {
            ChoiceBoolean choice0   = (ChoiceBoolean) choices.get(0);
            boolean isDefault       = metaPref.getDefaultValue().equals(choice0.getValue());
            String name             = isDefault ? IDENTIFIER_BOOLEAN_YES : IDENTIFIER_BOOLEAN_NO;
            createRadio(panel, metaPref, radioPair, isDefault, choice0.getLabel(), name, 0, 0);
            name                    = !isDefault ? IDENTIFIER_BOOLEAN_YES : IDENTIFIER_BOOLEAN_NO;
            ChoiceBoolean choice1   = (ChoiceBoolean) choices.get(1);
            createRadio(panel, metaPref, radioPair, isDefault, choice1.getLabel(), name, 1, 0);
        }
        else
        {
            boolean yesVal  = defVal;
            boolean noVal   = !defVal;
            createRadio(panel, metaPref, radioPair, yesVal, IDENTIFIER_BOOLEAN_YES, IDENTIFIER_BOOLEAN_YES, 0, 0);
            createRadio(panel, metaPref, radioPair, noVal, IDENTIFIER_BOOLEAN_NO, IDENTIFIER_BOOLEAN_NO, 1, 0);
        }
    }
    
    /**
     * Returns the ObjectRegistry for this MetaPref's jComponents.
     * 
     * @param mp        MetaPref whose jComponents we want.
     *  
     * @return          ObjectRegistry for MetaPref's jComponents.
     */
    private ObjectRegistry<JComponent> jCatComponentsMap(MetaPref mp)
    {
        ObjectRegistry<JComponent> result   = this.jCatComponentsMap.get(mp.getCategory()).get(mp.getID());
        if (result == null)
        {
            LinkedHashMap<String,ObjectRegistry<JComponent>> catHash = jCatComponentsMap.get(mp.getCategory());
            result                          = new ObjectRegistry<JComponent>();
            catHash.put(mp.getID(), result);
        }
        return result;
    }

    /**
     * Registers a JComponent with the ObjectRegistry
     * 
     * @param mp            metapref we want to register
     * @param labelAndName  label/name to add to the metapref's id to identify
     *                      a particular jComponent. must be UNIQUE.
     * @param jComponent    jComponent to register
     */
    protected void registerComponent(MetaPref mp, String labelAndName, JComponent jComponent)
    {
        //println("Registering: " + this.id+labelAndName);
        jCatComponentsMap(mp).registerObject(mp.getID()+labelAndName,jComponent);
    }
    
    /**
     * Returns a JComponent from the ObjectRegistry by name
     * 
     * @param mp            metapref to look up
     * @param labelAndName  the label/name that was added to the metapref's id
     *                      to identify a particular jComponent
     * @return JComponent matching labelAndName from ObjectRegistry
     */
    protected JComponent lookupComponent(MetaPref mp, String labelAndName)
    {
        //println("Trying to fetch: " + labelAndName);
        JComponent jComponent = jCatComponentsMap(mp).lookupObject(labelAndName);
        return jComponent;
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
     * This is for sliders; when we move the slider, we need to update
     * the tooltip and force it to display.
     * TODO: change placement of tooltip
     */
    public void stateChanged(ChangeEvent e)
    {
        JSlider source = (JSlider)e.getSource();
        String name = source.getName();
        Float curVal = new Float(source.getValue());
        if (name.indexOf("thisisafloat")!=-1)
        {
            curVal = curVal/FLOAT_SLIDER_MODIFIER;
            source.setToolTipText(curVal.toString());
        }
        else
        {
            source.setToolTipText(curVal.toString());
        }
        Action action = source.getActionMap().get("postTip");
        if (action != null) 
        {
            // show me the tooltip, NOW
            action.actionPerformed(new ActionEvent(source, ActionEvent.ACTION_PERFORMED, "postTip"));
        }
    }
}
