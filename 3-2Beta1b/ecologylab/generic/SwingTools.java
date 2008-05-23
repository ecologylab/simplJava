package ecologylab.generic;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Static convenience methods for Swing programing.
 *
 * @author andruid
 */
public class SwingTools
{
   public static Color showColorChooser(JColorChooser pane, 
										Component component,
										String title)
	  throws HeadlessException 
   {
	  ColorTracker ok = new ColorTracker(pane);
	  JDialog dialog = new ColorChooserDialog(component, title, true, pane, ok, null);
            
	  dialog.addWindowListener(new ColorChooserDialog.Closer());
	  dialog.addComponentListener(new ColorChooserDialog.DisposeOnClose());

	  dialog.show(); // blocks until user brings dialog down...

	  return ok.getColor();
   }
}

/*
 * Class which builds a color chooser dialog consisting of
 * a JColorChooser with "Ok", "Cancel", and "Reset" buttons.
 *
 * Note: This needs to be fixed to deal with localization!
 */
class ColorChooserDialog extends JDialog {
    private Color initialColor;
    private JColorChooser chooserPane;

    public ColorChooserDialog(Component c, String title, boolean modal,
        JColorChooser chooserPane,
        ActionListener okListener, ActionListener cancelListener)
        throws HeadlessException {
        super(JOptionPane.getFrameForComponent(c), title, modal);
        //setResizable(false);

        this.chooserPane = chooserPane;

	String okString = UIManager.getString("ColorChooser.okText");
	String cancelString = UIManager.getString("ColorChooser.cancelText");
	String resetString = UIManager.getString("ColorChooser.resetText");

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(chooserPane, BorderLayout.CENTER);

        /*
         * Create Lower button panel
         */
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton(okString);
	getRootPane().setDefaultButton(okButton);
        okButton.setActionCommand("OK");
        if (okListener != null) {
            okButton.addActionListener(okListener);
        }
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hide();
            }
        });
        buttonPane.add(okButton);

        JButton cancelButton = new JButton(cancelString);

	// The following few lines are used to register esc to close the dialog
/*	Action cancelKeyAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ((AbstractButton)e.getSource()).fireActionPerformed(e);
            }
        }; 
	KeyStroke cancelKeyStroke = KeyStroke.getKeyStroke((char)KeyEvent.VK_ESCAPE, false);
	InputMap inputMap = cancelButton.getInputMap(JComponent.
						     WHEN_IN_FOCUSED_WINDOW);
	ActionMap actionMap = cancelButton.getActionMap();
	if (inputMap != null && actionMap != null) {
	    inputMap.put(cancelKeyStroke, "cancel");
	    actionMap.put("cancel", cancelKeyAction);
	}
*/	// end esc handling

        cancelButton.setActionCommand("cancel");
        if (cancelListener != null) {
            cancelButton.addActionListener(cancelListener);
        }
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hide();
            }
        });
        buttonPane.add(cancelButton);

        JButton resetButton = new JButton(resetString);
        resetButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               reset();
           }
        });
        
        /*int mnemonic = UIManager.getInt("ColorChooser.resetMnemonic", -1);
        if (mnemonic != -1) {
            resetButton.setMnemonic(mnemonic);
        } */
        buttonPane.add(resetButton);
        contentPane.add(buttonPane, BorderLayout.SOUTH);

        if (JDialog.isDefaultLookAndFeelDecorated()) {
            boolean supportsWindowDecorations = 
            UIManager.getLookAndFeel().getSupportsWindowDecorations();
            if (supportsWindowDecorations) {
                getRootPane().setWindowDecorationStyle(JRootPane.COLOR_CHOOSER_DIALOG);
            }
        }
        applyComponentOrientation(((c == null) ? getRootPane() : c).getComponentOrientation());

        pack();
        setLocationRelativeTo(c);
    }

    public void show() {
        initialColor = chooserPane.getColor();
        super.show();
    }

    public void reset() {
        chooserPane.setColor(initialColor);
    }

    static class Closer extends WindowAdapter 
    {
        public void windowClosing(WindowEvent e) {
            Window w = e.getWindow();
            w.hide();
        }
    }

    static class DisposeOnClose extends ComponentAdapter 
    {
        public void componentHidden(ComponentEvent e) {
            Window w = (Window)e.getComponent();
            w.dispose();
        }
    }

}

class ColorTracker implements ActionListener {
    JColorChooser chooser;
    Color color;

    public ColorTracker(JColorChooser c) {
        chooser = c;
    }

    public void actionPerformed(ActionEvent e) {
        color = chooser.getColor();
    }

    public Color getColor() {
        return color;
    }
}
