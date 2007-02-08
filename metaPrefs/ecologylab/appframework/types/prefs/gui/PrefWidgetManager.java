package ecologylab.appframework.types.prefs.gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import ecologylab.appframework.types.prefs.MetaPref;
import ecologylab.appframework.types.prefs.MetaPrefSet;

public class PrefWidgetManager
{
    HashMap<String, HashMap<JComponent, MetaPref>> categoryToWidgets = new HashMap<String, HashMap<JComponent, MetaPref>>();
    
    public PrefWidgetManager(MetaPrefSet set)
    {
        for (MetaPref metaPref : set)
        {
            HashMap widgets = categoryToWidgets.get(metaPref.getCategory());

            if (widgets == null)
            {
                widgets = new HashMap<JComponent, MetaPref>();
                categoryToWidgets.put(metaPref.getCategory(), widgets);
            }

            widgets.put(metaPref.getWidget(), metaPref);
        }
        
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
        }
        return revertButton;
    }
    // end of static bits of gui
    
    // TODO rendering code -- iterate through each category
    // (categoryToWidgets.get(<categoryname>)) & render each widget
    // (categoryToWidgets.get(<categoryname>).keySet())

    // bits of gui that are all or part auto-generated
    private JTabbedPane getJTabbedPane() 
    {
        if (jTabbedPane == null) 
        {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.setName("jTabbedPane");
            jTabbedPane.setBounds(new Rectangle(0, 0, 595, 416));
            for (String cat : categoryToWidgets.keySet())
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
        newPanel.setSize(new java.awt.Dimension(586,500));
        for (JComponent subPanel : categoryToWidgets.get(category).keySet())
        {
            // TODO increment positions for elements!
            if (subPanel != null)
            {
                newPanel.add(subPanel);
            }
        }
        
        return newPanel;
    }
}
