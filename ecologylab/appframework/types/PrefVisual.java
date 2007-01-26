package ecologylab.appframework.types;

import javax.swing.JTabbedPane;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JInternalFrame;
import java.awt.BorderLayout;
import javax.swing.JWindow;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import javax.swing.JDialog;
import java.awt.Rectangle;

public class PrefVisual {

	private JTabbedPane jTabbedPane = null;
	private JTabbedPane jTabbedPane1 = null;
	private JTabbedPane jTabbedPane2 = null;
	private JTabbedPane jTabbedPane3 = null;
	private JTabbedPane jTabbedPane4 = null;
	private JTabbedPane jTabbedPane5 = null;
	private JButton jButton = null;  //  @jve:decl-index=0:visual-constraint="708,77"
	private JDialog jDialog = null;  //  @jve:decl-index=0:visual-constraint="214,65"
	private JPanel jContentPane = null;
	private JButton jButton1 = null;
	private JButton jButton2 = null;
	private JButton jButton3 = null;
	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setName("jTabbedPane");
			jTabbedPane.setBounds(new Rectangle(0, 0, 595, 416));
			jTabbedPane.addTab("category 1", null, getJTabbedPane1(), null);
			jTabbedPane.addTab("category 2", null, getJTabbedPane2(), null);
			jTabbedPane.addTab("category 3", null, getJTabbedPane3(), null);
			jTabbedPane.addTab("category 4", null, getJTabbedPane4(), null);
			jTabbedPane.addTab("category 5", null, getJTabbedPane5(), null);
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jTabbedPane1	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane1() {
		if (jTabbedPane1 == null) {
			jTabbedPane1 = new JTabbedPane();
			jTabbedPane1.setName("jTabbedPane1");
			jTabbedPane1.setPreferredSize(new Dimension(100, 100));
		}
		return jTabbedPane1;
	}

	/**
	 * This method initializes jTabbedPane2	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane2() {
		if (jTabbedPane2 == null) {
			jTabbedPane2 = new JTabbedPane();
		}
		return jTabbedPane2;
	}

	/**
	 * This method initializes jTabbedPane3	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane3() {
		if (jTabbedPane3 == null) {
			jTabbedPane3 = new JTabbedPane();
		}
		return jTabbedPane3;
	}

	/**
	 * This method initializes jTabbedPane4	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane4() {
		if (jTabbedPane4 == null) {
			jTabbedPane4 = new JTabbedPane();
		}
		return jTabbedPane4;
	}

	/**
	 * This method initializes jTabbedPane5	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane5() {
		if (jTabbedPane5 == null) {
			jTabbedPane5 = new JTabbedPane();
		}
		return jTabbedPane5;
	}

	/**
	 * This method initializes jDialog	
	 * 	
	 * @return javax.swing.JDialog	
	 */
	private JDialog getJDialog() {
		if (jDialog == null) {
			jDialog = new JDialog();
			jDialog.setSize(new Dimension(603, 532));
			jDialog.setTitle("combinFormation Preferences");
			jDialog.setContentPane(getJContentPane());
		}
		return jDialog;
	}

	/**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJTabbedPane(), null);
			jContentPane.add(getJButton1(), null);
			jContentPane.add(getJButton2(), null);
			jContentPane.add(getJButton3(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setBounds(new Rectangle(482, 435, 89, 35));
			jButton1.setText("Cancel");
		}
		return jButton1;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setBounds(new Rectangle(379, 435, 89, 35));
			jButton2.setText("Save");
		}
		return jButton2;
	}

	/**
	 * This method initializes jButton3	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton3() {
		if (jButton3 == null) {
			jButton3 = new JButton();
			jButton3.setBounds(new Rectangle(15, 435, 137, 35));
			jButton3.setText("Revert to Default");
		}
		return jButton3;
	}

}
