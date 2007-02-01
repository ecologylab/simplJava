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
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PrefVisual {

	private JTabbedPane jTabbedPane = null;
	private JButton jButton = null;  //  @jve:decl-index=0:visual-constraint="708,77"
	private JDialog jDialog = null;  //  @jve:decl-index=0:visual-constraint="214,65"
	private JPanel jContentPane = null;
	private JButton jButton1 = null;
	private JButton jButton2 = null;
	private JButton jButton3 = null;
	private JPanel jContentPane1 = null;
	private JPanel jContentPane2 = null;
	private JRadioButton crawlYes = null;
	private JRadioButton crawlNo = null;
	private JLabel crawl = null;
	private JLabel download_images_automatically = null;
	private JRadioButton download_images_automaticallyYes = null;
	private JRadioButton download_images_automaticallyNo = null;
	private JLabel coolSpaceRatio = null;
	private JTextField coolSpaceRatioField = null;
	private JLabel spatial_grid = null;
	private JRadioButton spatial_gridYes = null;
	private JRadioButton spatial_gridNo = null;
	private JLabel incontext_slider = null;
	private JRadioButton incontext_sliderYes = null;
	private JRadioButton incontext_sliderNo = null;
	private JLabel use_dashboard = null;
	private JRadioButton use_dashboardYes = null;
	private JRadioButton use_dashboardNo = null;
	private JLabel elements_per_square_inch = null;
	private JTextField elements_per_square_inchField = null;
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
			jTabbedPane.addTab("General", null, getJContentPane1(), null);
			jTabbedPane.addTab("Developers", null, getJContentPane2(), null);
		}
		return jTabbedPane;
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

	/**
	 * This method initializes jContentPane1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane1() {
		if (jContentPane1 == null) {
			elements_per_square_inch = new JLabel();
			elements_per_square_inch.setBounds(new Rectangle(30, 300, 292, 16));
			elements_per_square_inch.setText("Density: Visible elements per square inch [.1,20]");
			elements_per_square_inch.setToolTipText("");
			use_dashboard = new JLabel();
			use_dashboard.setBounds(new Rectangle(30, 255, 224, 16));
			use_dashboard.setText("Enable the seeding dashboard?");
			use_dashboard.setToolTipText("");
			incontext_slider = new JLabel();
			incontext_slider.setBounds(new Rectangle(30, 210, 341, 16));
			incontext_slider.setText("Enable in-context slider?");
			incontext_slider.setToolTipText("");
			spatial_grid = new JLabel();
			spatial_grid.setBounds(new Rectangle(30, 165, 366, 16));
			spatial_grid.setText("Composition agent automatically clusters related elements?");
			spatial_grid.setToolTipText("");
			coolSpaceRatio = new JLabel();
			coolSpaceRatio.setBounds(new Rectangle(30, 120, 187, 16));
			coolSpaceRatio.setToolTipText("Cool Space Ratio : The hot space is the composition area that you share with the program. The cool space is exclusively yours to work with.");
			coolSpaceRatio.setText("Cool Space Ratio");
			download_images_automatically = new JLabel();
			download_images_automatically.setBounds(new Rectangle(30, 75, 289, 16));
			download_images_automatically.setText("Download images automatically?");
			crawl = new JLabel();
			crawl.setBounds(new Rectangle(30, 30, 358, 16));
			crawl.setText("Download linked documents automatically (crawl)?");
			jContentPane1 = new JPanel();
			jContentPane1.setLayout(null);
			jContentPane1.add(crawl, null);
			jContentPane1.add(getCrawlYes(), null);
			jContentPane1.add(getCrawlNo(), null);
			jContentPane1.add(download_images_automatically, null);
			jContentPane1.add(getDownload_images_automaticallyYes(), null);
			jContentPane1.add(getDownload_images_automaticallyNo(), null);
			jContentPane1.add(coolSpaceRatio, null);
			jContentPane1.add(getCoolSpaceRatioField(), null);
			jContentPane1.add(spatial_grid, null);
			jContentPane1.add(getSpatial_gridYes(), null);
			jContentPane1.add(getSpatial_gridNo(), null);
			jContentPane1.add(incontext_slider, null);
			jContentPane1.add(getIncontext_sliderYes(), null);
			jContentPane1.add(getIncontext_sliderNo(), null);
			jContentPane1.add(use_dashboard, null);
			jContentPane1.add(getUse_dashboardYes(), null);
			jContentPane1.add(getUse_dashboardNo(), null);
			jContentPane1.add(elements_per_square_inch, null);
			jContentPane1.add(getElements_per_square_inchField(), null);
		}
		return jContentPane1;
	}

	/**
	 * This method initializes jContentPane2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane2() {
		if (jContentPane2 == null) {
			jContentPane2 = new JPanel();
			jContentPane2.setLayout(null);
		}
		return jContentPane2;
	}

	/**
	 * This method initializes crawlYes	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getCrawlYes() {
		if (crawlYes == null) {
			crawlYes = new JRadioButton();
			crawlYes.setBounds(new Rectangle(417, 28, 54, 21));
			crawlYes.setSelected(true);
			crawlYes.setText("Yes");
		}
		return crawlYes;
	}

	/**
	 * This method initializes crawlNo	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getCrawlNo() {
		if (crawlNo == null) {
			crawlNo = new JRadioButton();
			crawlNo.setBounds(new Rectangle(493, 28, 46, 21));
			crawlNo.setName("No");
			crawlNo.setText("No");
		}
		return crawlNo;
	}

	/**
	 * This method initializes download_images_automaticallyYes	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDownload_images_automaticallyYes() {
		if (download_images_automaticallyYes == null) {
			download_images_automaticallyYes = new JRadioButton();
			download_images_automaticallyYes.setBounds(new Rectangle(418, 70, 46, 24));
			download_images_automaticallyYes.setSelected(true);
			download_images_automaticallyYes.setText("Yes");
		}
		return download_images_automaticallyYes;
	}

	/**
	 * This method initializes download_images_automaticallyNo	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDownload_images_automaticallyNo() {
		if (download_images_automaticallyNo == null) {
			download_images_automaticallyNo = new JRadioButton();
			download_images_automaticallyNo.setBounds(new Rectangle(494, 70, 40, 24));
			download_images_automaticallyNo.setText("No");
			download_images_automaticallyNo.setName("No");
		}
		return download_images_automaticallyNo;
	}

	/**
	 * This method initializes coolSpaceRatioField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getCoolSpaceRatioField() {
		if (coolSpaceRatioField == null) {
			coolSpaceRatioField = new JTextField();
			coolSpaceRatioField.setBounds(new Rectangle(451, 118, 55, 20));
			coolSpaceRatioField.setHorizontalAlignment(JTextField.CENTER);
			coolSpaceRatioField.setText("0.2");
		}
		return coolSpaceRatioField;
	}

	/**
	 * This method initializes spatial_gridYes	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getSpatial_gridYes() {
		if (spatial_gridYes == null) {
			spatial_gridYes = new JRadioButton();
			spatial_gridYes.setBounds(new Rectangle(419, 162, 46, 24));
			spatial_gridYes.setText("Yes");
			spatial_gridYes.setSelected(true);
		}
		return spatial_gridYes;
	}

	/**
	 * This method initializes spatial_gridNo	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getSpatial_gridNo() {
		if (spatial_gridNo == null) {
			spatial_gridNo = new JRadioButton();
			spatial_gridNo.setBounds(new Rectangle(494, 161, 40, 24));
			spatial_gridNo.setText("No");
			spatial_gridNo.setName("No");
		}
		return spatial_gridNo;
	}

	/**
	 * This method initializes incontext_sliderYes	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getIncontext_sliderYes() {
		if (incontext_sliderYes == null) {
			incontext_sliderYes = new JRadioButton();
			incontext_sliderYes.setBounds(new Rectangle(419, 206, 46, 24));
			incontext_sliderYes.setText("Yes");
			incontext_sliderYes.setEnabled(true);
			incontext_sliderYes.setSelected(false);
		}
		return incontext_sliderYes;
	}

	/**
	 * This method initializes incontext_sliderNo	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getIncontext_sliderNo() {
		if (incontext_sliderNo == null) {
			incontext_sliderNo = new JRadioButton();
			incontext_sliderNo.setBounds(new Rectangle(494, 206, 40, 24));
			incontext_sliderNo.setText("No");
			incontext_sliderNo.setSelected(true);
			incontext_sliderNo.setName("No");
		}
		return incontext_sliderNo;
	}

	/**
	 * This method initializes use_dashboardYes	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getUse_dashboardYes() {
		if (use_dashboardYes == null) {
			use_dashboardYes = new JRadioButton();
			use_dashboardYes.setBounds(new Rectangle(419, 251, 46, 24));
			use_dashboardYes.setSelected(false);
			use_dashboardYes.setText("Yes");
			use_dashboardYes.setEnabled(true);
		}
		return use_dashboardYes;
	}

	/**
	 * This method initializes use_dashboardNo	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getUse_dashboardNo() {
		if (use_dashboardNo == null) {
			use_dashboardNo = new JRadioButton();
			use_dashboardNo.setBounds(new Rectangle(494, 251, 40, 24));
			use_dashboardNo.setSelected(true);
			use_dashboardNo.setText("No");
			use_dashboardNo.setName("No");
		}
		return use_dashboardNo;
	}

	/**
	 * This method initializes elements_per_square_inchField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getElements_per_square_inchField() {
		if (elements_per_square_inchField == null) {
			elements_per_square_inchField = new JTextField();
			elements_per_square_inchField.setBounds(new Rectangle(451, 298, 55, 20));
			elements_per_square_inchField.setHorizontalAlignment(JTextField.CENTER);
			elements_per_square_inchField.setText("0.7");
		}
		return elements_per_square_inchField;
	}

}
