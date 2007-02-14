package ecologylab.appframework.types.prefs.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import ecologylab.appframework.types.prefs.MetaPref;
import ecologylab.appframework.types.prefs.MetaPrefSet;

public class PrefWidgetManager
{
    MetaPrefSet metaPrefSet;
    
    public PrefWidgetManager(MetaPrefSet set)
    {
        metaPrefSet = set;
        JDialog window = getJDialog();
        window.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        window.setVisible(true);
    }
       
    // base setup for gui
    JDialog jDialog = null;
    JPanel jContentPane = null;
    JButton cancelButton = null;
    JButton saveButton = null;
    JButton revertButton = null;
    JTabbedPane jTabbedPane = null;

    // static bits of gui
    private JDialog getJDialog() 
    {
        if (jDialog == null) 
        {
            jDialog = new JDialog();
            jDialog.setSize(new Dimension(603, 532));
            jDialog.setTitle("combinFormation Preferences");
            jDialog.setContentPane(getJContentPane());
        }
        return jDialog;
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
            // TODO increment positions for elements!
            if (subPanel != null)
            {
                int height = subPanel.getHeight();
                int width = subPanel.getWidth();
                //System.out.println("subpanel found for " + category + "; height: " + height + ", width: " + width);
                subPanel.setLocation(30, yval);
                yval += subPanel.getHeight();
                newPanel.add(subPanel);
            }
        }
        
        return newPanel;
    }
    // end of bits of gui that are all or part auto-generated
    
    // gui actions for buttons
    private void actionSavePreferences()
    {
        System.out.println("we pressed the save button");
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
