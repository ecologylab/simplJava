package ecologylab.appframework.types.prefs.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import ecologylab.appframework.ApplicationEnvironment;
import ecologylab.appframework.types.prefs.MetaPref;
import ecologylab.appframework.types.prefs.MetaPrefSet;
import ecologylab.appframework.types.prefs.Pref;
import ecologylab.appframework.types.prefs.PrefSet;
import ecologylab.net.ParsedURL;
import ecologylab.xml.XmlTranslationException;


public class PrefWidgetManager
{
    MetaPrefSet metaPrefSet;
    PrefSet     prefSet;
    ParsedURL   prefsPURL;
    
    // base setup for gui
    JFrame jFrame = null;
    JPanel jContentPane = null;
    JButton cancelButton = null;
    JButton saveButton = null;
    JButton revertButton = null;
    JTabbedPane jTabbedPane = null;
    
    public PrefWidgetManager(MetaPrefSet set, PrefSet prefs, ParsedURL prefsURL)
    {
        metaPrefSet = set;
        prefSet     = prefs;
        prefsPURL   = prefsURL;
        JFrame window = getJFrame();
        window.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        window.setVisible(true);
    }
    
    public JFrame fetchJFrame()
    {
        return getJFrame();
    }

    // static bits of gui
    private JFrame getJFrame() 
    {
        if (jFrame == null) 
        {
            jFrame = new JFrame();
            jFrame.setSize(new Dimension(603, 532));
            jFrame.setTitle("combinFormation Preferences");
            jFrame.setContentPane(getJContentPane());
        }
        return jFrame;
    }

    private JPanel getJContentPane() 
    {
        if (jContentPane == null) 
        {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJTabbedPane(), null);
            jContentPane.add(getCancelButton(), null);
            jContentPane.add(getSaveButton(), null);
            jContentPane.add(getRevertButton(), null);
        }
        return jContentPane;
    }

    private JButton getCancelButton() 
    {
        if (cancelButton == null) 
        {
            cancelButton = new JButton();
            cancelButton.setBounds(new Rectangle(482, 435, 89, 35));
            cancelButton.setText("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
                });
        }
        return cancelButton;
    }

    private JButton getSaveButton() 
    {
        if (saveButton == null) 
        {
            saveButton = new JButton();
            saveButton.setBounds(new Rectangle(379, 435, 89, 35));
            saveButton.setText("Save");
            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    actionSavePreferences();
                }
                });
        }
        return saveButton;
    }

    private JButton getRevertButton() 
    {
        if (revertButton == null) 
        {
            revertButton = new JButton();
            revertButton.setBounds(new Rectangle(15, 435, 137, 35));
            revertButton.setText("Revert to Default");
            revertButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    actionRevertPreferencesToDefault();
                }
                });
        }
        return revertButton;
    }
    // end of static bits of gui
    
    // bits of gui that are all or part auto-generated
    private JTabbedPane getJTabbedPane() 
    {
        if (jTabbedPane == null) 
        {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.setName("jTabbedPane");
            jTabbedPane.setBounds(new Rectangle(0, 0, 595, 416));
            
            for (String cat : metaPrefSet.categoryToMetaPrefs.keySet())
            {
                JScrollPane p = new JScrollPane();
                p.setPreferredSize(new java.awt.Dimension(500,500));
                p.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
                p.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                p.setName("");
                p.setViewportView(getTabbedBodyFrame(cat));
                jTabbedPane.addTab(cat, null, p, null);
            }
        }
        return jTabbedPane;
    }

    private JPanel getTabbedBodyFrame(String category)
    {
        JPanel newPanel = new JPanel();
        newPanel.setLayout(null);
        int yval = 30;
        newPanel.setSize(new java.awt.Dimension(586,500));
        for (MetaPref mp : metaPrefSet.categoryToMetaPrefs.get(category))
        {
            JPanel subPanel = mp.jPanel;
            if (subPanel != null)
            {
                //int height = subPanel.getHeight();
                //int width = subPanel.getWidth();
                //System.out.println("subpanel found for " + category + "; height: " + height + ", width: " + width);
                subPanel.setLocation(30, yval);
                yval += subPanel.getHeight();
                // if we have a prefs value, override it now
                if (prefSet.hasPref(mp.getID()))
                {
                    mp.setWidgetToPrefValue(prefSet.lookupPref(mp.getID()).value());
                }
                newPanel.add(subPanel);
            }
        }
        
        return newPanel;
    }
    // end of bits of gui that are all or part auto-generated
    
    // gui actions for buttons
    private void actionSavePreferences()
    {
        //System.out.println("we pressed the save button");
        
        // we do this with metaprefs because we will always have
        // all metaprefs. we may not always have a prefs file to start
        // with.
        for (String cat : metaPrefSet.categoryToMetaPrefs.keySet())
        {
            for (MetaPref mp : metaPrefSet.categoryToMetaPrefs.get(cat))
            {
                // by casting here we get the proper return type
                // for getPrefValue
                String name = mp.getID();
                mp = mp.getClass().cast(mp);
                Pref pref = prefSet.lookupPref(name);
                //pref.print();
                pref.setValue(mp.getPrefValue());
                prefSet.modifyPref(name,pref);
                //prefSet.lookupPref(mp.getID()).print();
            }
        }
        // save file
        
        try
        {
            prefSet.saveXmlFile(prefsPURL.file(), true, false);
        }
        catch (XmlTranslationException e)
        {
            // TODO auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * The function that actually performs the revert-to-default actions
     * is in the MetaPrefType classes
     */
    private void actionRevertPreferencesToDefault()
    {
        for (String cat : metaPrefSet.categoryToMetaPrefs.keySet())
        {
            for (MetaPref mp : metaPrefSet.categoryToMetaPrefs.get(cat))
            {
                mp.revertToDefault();
            }
        }
    }
    // end of gui actions for buttons
}
