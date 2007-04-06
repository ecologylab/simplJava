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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import ecologylab.appframework.ApplicationEnvironment;
import ecologylab.appframework.types.prefs.MetaPref;
import ecologylab.appframework.types.prefs.MetaPrefSet;
import ecologylab.appframework.types.prefs.Pref;
import ecologylab.appframework.types.prefs.PrefSet;
import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;
import ecologylab.xml.XmlTranslationException;

/**
 * Create the GUI for preference editing; also responsible for all
 * actions associated with the GUI.
 * 
 * @author Cae
 *
 */
public class PrefsEditor
extends Debug
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
                            int actualWidth = (this.getWidth()-val.getWidth());
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
        /*contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.yellow),
                contentPanel.getBorder()));*/
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setMaximumSize(new Dimension(scrollPane.getWidth(),Integer.MAX_VALUE));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 0.1;
        int rowNum = 0;
        for (MetaPref metaPref : metaPrefSet.getMetaPrefListByCategory(category))
        {
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
        /*separator.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.green),
                            separator.getBorder()));*/
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
        /*subDescription.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.red),
                subDescription.getBorder()));*/
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
        
        //nasty workaround because there is no API option to wrap tooltips
        String formattedToolTip = wrapTooltip(mp);
        
        label.setToolTipText(formattedToolTip);
        label.setHorizontalTextPosition(SwingConstants.LEADING);
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
                        //System.out.print("breakAt " + breakAt + " is past length of string " + tiplen + "\n");
                        //System.out.print("remaining string: " + this.helpText.substring(nowAt,tiplen) + "\n");
                        formattedToolTip = formattedToolTip.concat(hText.substring(nowAt, tiplen) + "<br>");
                    }
                    else if (breakAt > 0)
                    {
                        //System.out.print("cut is nowAt " + nowAt + " to breakAt " + breakAt + "\n");
                        formattedToolTip = formattedToolTip.concat(hText.substring(nowAt, breakAt) + "<br>");
                    }
                    else
                    {
                        //System.out.print("remaining string: " + this.helpText.substring(nowAt,tiplen) + "\n");
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
        JPanel subValue = metaPref.jPanel;
        if (subValue != null)
        {
            constraints.gridx = 1;
            constraints.gridy = rownum;
            constraints.gridwidth = 1;
            constraints.insets = new Insets(TOP_GUI_INSET,LEFT_GUI_INSET,BOTTOM_GUI_INSET,0); // top,left,bottom,right
            /*subValue.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.blue),
                    subValue.getBorder()));*/
            // if we have a prefs value, override it now
            if (Pref.hasPref(metaPref.getID()))
            {
                metaPref.setWidgetToPrefValue(Pref.lookupPref(metaPref.getID()).value());
            }
        }
        return subValue;
    }
    // end of bits of gui that are all or part auto-generated
    
    
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
    			// by casting here we get the proper return type
    			// for getPrefValue
    			String name = mp.getID();
    			//TODO -- i dont believe this lines makes sense -- andruid 3/12/07
    			//mp 			= mp.getClass().cast(mp);
    			Pref pref 	= mp.getAssociatedPref();
    			pref.setValue(mp.getPrefValue());
    			if (!prefSet.contains(pref))
    				prefSet.add(pref);
    			
    			//TODO -- this is not really needed because only the value has been changed. -- andruid 3/12/07
    			//prefSet.modifyPref(name,pref);
    			//prefSet.lookupPref(mp.getID()).print();
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
                mp.revertToDefault();
            }
        }
    }
    // end of gui actions for buttons

    public void windowActivated(WindowEvent e)
    {
    }

    public void windowClosed(WindowEvent e)
    {
    }

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
}
