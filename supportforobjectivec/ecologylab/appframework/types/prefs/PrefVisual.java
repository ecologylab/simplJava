package ecologylab.appframework.types.prefs;

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
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;

public class PrefVisual {

	private JTabbedPane jTabbedPane = null;
	private JButton jButton = null;  //  @jve:decl-index=0:visual-constraint="708,77"
	private JDialog jDialog = null;  //  @jve:decl-index=0:visual-constraint="159,42"
	private JPanel jContentPane = null;
	private JButton jButton1 = null;
	private JButton jButton2 = null;
	private JButton jButton3 = null;
	private JPanel jContentPane1 = null;
	private JPanel jContentPane2 = null;
	private JPanel jContentPane3 = null;
	private JRadioButton crawlYes = null;
	private JRadioButton crawlNo = null;
	private JLabel crawl = null;
	private JLabel download_images_automaticallyLabel = null;
	private JRadioButton download_images_automaticallyYes = null;
	private JRadioButton download_images_automaticallyNo = null;
	private JLabel coolSpaceRatioLabel = null;
	private JTextField coolSpaceRatioField = null;
	private JLabel spatial_grid = null;
	private JRadioButton spatial_gridYes = null;
	private JRadioButton spatial_gridNo = null;
	private JLabel incontext_slider = null;
	private JRadioButton incontext_sliderYes = null;
	private JRadioButton incontext_sliderNo = null;
	private JLabel dashboard_enabled = null;
	private JRadioButton dashboard_enabledYes = null;
	private JRadioButton dashboard_enabledNo = null;
	private JLabel elements_per_square_inch = null;
	private JTextField elements_per_square_inchField = null;
	private JLabel undo_levels = null;
	private JTextField undo_levelsField = null;
	private JLabel log_mode = null;
	private JRadioButton log_modeNone = null;
	private JRadioButton log_modeDesktop = null;
	private JRadioButton log_modeServer = null;
	private JLabel javascript_debug_mode = null;
	private JRadioButton javascript_debug_modeYes = null;
	private JRadioButton javascript_debug_modeNo = null;
	private JLabel codebase = null;
	private JTextField codebaseField = null;
	private JLabel undo_levels1 = null;
	private JTextField undo_levelsField1 = null;
	private JLabel log_mode1 = null;
	private JRadioButton log_modeNone1 = null;
	private JRadioButton log_modeDesktop1 = null;
	private JRadioButton log_modeServer1 = null;
	private JLabel javascript_debug_mode1 = null;
	private JRadioButton javascript_debug_modeYes1 = null;
	private JRadioButton javascript_debug_modeNo1 = null;
	private JLabel codebase1 = null;
	private JTextField codebaseField1 = null;
	private JScrollPane jScrollPane = null;
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
			jTabbedPane.addTab("Gen2", null, getJScrollPane(), null);
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
			elements_per_square_inch.setToolTipText("Controls how many elements will appear in the information space, relative to the area of the space. The default value is .5 elements per square inch.<br><br>This lets you decide how many total elements the program will place into the space. Doubling this number, will double the number of elements.");
			
			dashboard_enabled = new JLabel();
			dashboard_enabled.setBounds(new Rectangle(30, 255, 224, 16));
			dashboard_enabled.setText("Enable the dashboard?");
			dashboard_enabled.setToolTipText("Use the dashboard to modify searches and other seeds.");
			
			incontext_slider = new JLabel();
			incontext_slider.setBounds(new Rectangle(30, 210, 341, 16));
			incontext_slider.setText("Enable in-context slider?");
			incontext_slider.setToolTipText("Use the in-context slider to enable fine-grained expression of interest in metadata fields and individual words.");
			
			spatial_grid = new JLabel();
			spatial_grid.setBounds(new Rectangle(30, 165, 366, 16));
			spatial_grid.setText("Composition agent automatically clusters related elements?");
			spatial_grid.setHorizontalTextPosition(SwingConstants.LEADING);
			spatial_grid.setToolTipText("Use the visual composition that automatically clusters related elements into piles. You may wish to move elements around, and sometimes, to re-cluster.");
			
			coolSpaceRatioLabel = new JLabel();
			coolSpaceRatioLabel.setBounds(new Rectangle(30, 120, 187, 16));
			coolSpaceRatioLabel.setToolTipText("The hot space is the composition area that you share with the program. The cool space is exclusively yours  to work with.<br><br>This parameter defines the proportion of the whole information space that is dedicated as the cool space, at startup time.");
			coolSpaceRatioLabel.setText("Cool Space Ratio");
			
			download_images_automaticallyLabel = new JLabel();
			download_images_automaticallyLabel.setBounds(new Rectangle(30, 75, 289, 16));
			download_images_automaticallyLabel.setToolTipText("At startup, do you want the information collecting agent to download the images from the web pages it processes?");
			download_images_automaticallyLabel.setText("Download images automatically?");
			
			crawl = new JLabel();
			crawl.setBounds(new Rectangle(30, 30, 358, 16));
			crawl.setToolTipText("At startup, do you want the information collecting agent to act as a web crawler, by periodically following hyperlinks to download more documents?");
			crawl.setText("Download linked documents automatically (crawl)?");
			
			
			
			jContentPane1 = new JPanel();
			jContentPane1.setLayout(null);
			jContentPane1.add(crawl, null);
			jContentPane1.add(getCrawlYes(), null);
			jContentPane1.add(getCrawlNo(), null);
			jContentPane1.add(download_images_automaticallyLabel, null);
			jContentPane1.add(getDownload_images_automaticallyYes(), null);
			jContentPane1.add(getDownload_images_automaticallyNo(), null);
			jContentPane1.add(coolSpaceRatioLabel, null);
			jContentPane1.add(getCoolSpaceRatioField(), null);
			jContentPane1.add(spatial_grid, null);
			jContentPane1.add(getSpatial_gridYes(), null);
			jContentPane1.add(getSpatial_gridNo(), null);
			jContentPane1.add(incontext_slider, null);
			jContentPane1.add(getIncontext_sliderYes(), null);
			jContentPane1.add(getIncontext_sliderNo(), null);
			jContentPane1.add(dashboard_enabled, null);
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
			codebase = new JLabel();
			codebase.setBounds(new Rectangle(30, 240, 268, 16));
			codebase.setText("Jar File Directory");
			codebase.setToolTipText("Directory for the project a developer is working on.");
			
			javascript_debug_mode = new JLabel();
			javascript_debug_mode.setBounds(new Rectangle(30, 180, 197, 16));
			javascript_debug_mode.setText("Javascript Debug Mode");
			javascript_debug_mode.setToolTipText("Shows or hides extra buttons that can be used for debugging the javascript. Allows direct access to cookies & arrays");
			
			log_mode = new JLabel();
			log_mode.setBounds(new Rectangle(30, 105, 271, 16));
			log_mode.setText("Log Mode");
			log_mode.setToolTipText("Controls whether or not logging of actions to a file by you and the program is performed during each session. The default is log to Desktop. With this option, you will have trace information available to help us, in case a bug is discovered.");
			
			undo_levels = new JLabel();
			undo_levels.setBounds(new Rectangle(30, 30, 271, 16));
			undo_levels.setText("Undo Levels");
			undo_levels.setToolTipText("The number of steps backwards you can go, by using undo, or the reverse button.");
			
			jContentPane2 = new JPanel();
			jContentPane2.setLayout(null);
			jContentPane2.setSize(new java.awt.Dimension(586,500));
			jContentPane2.add(undo_levels, null);
			jContentPane2.add(getUndo_levelsField(), null);
			jContentPane2.add(log_mode, null);
			jContentPane2.add(getLog_modeNone(), null);
			jContentPane2.add(getLog_modeDesktop(), null);
			jContentPane2.add(getLog_modeServer(), null);
			jContentPane2.add(javascript_debug_mode, null);
			jContentPane2.add(getJavascript_debug_modeYes(), null);
			jContentPane2.add(getJavascript_debug_modeNo(), null);
			jContentPane2.add(codebase, null);
			jContentPane2.add(getCodebaseField(), null);
		}
		return jContentPane2;
	}
	
	/**
	 * This method initializes jContentPane2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane3() {
		if (jContentPane3 == null) {
			codebase1 = new JLabel();
			codebase1.setBounds(new Rectangle(30, 240, 268, 16));
			codebase1.setText("Jar File Directory");
			codebase1.setToolTipText("Directory for the project a developer is working on.");
			
			javascript_debug_mode1 = new JLabel();
			javascript_debug_mode1.setBounds(new Rectangle(30, 180, 197, 16));
			javascript_debug_mode1.setText("Javascript Debug Mode");
			javascript_debug_mode1.setToolTipText("Shows or hides extra buttons that can be used for debugging the javascript. Allows direct access to cookies & arrays");
			
			log_mode1 = new JLabel();
			log_mode1.setBounds(new Rectangle(30, 105, 271, 16));
			log_mode1.setText("Log Mode");
			log_mode1.setToolTipText("Controls whether or not logging of actions to a file by you and the program is performed during each session. The default is log to Desktop. With this option, you will have trace information available to help us, in case a bug is discovered.");
			
			undo_levels1 = new JLabel();
			undo_levels1.setBounds(new Rectangle(30, 30, 271, 16));
			undo_levels1.setText("Undo Levels");
			undo_levels1.setToolTipText("The number of steps backwards you can go, by using undo, or the reverse button.");
			
			jContentPane3 = new JPanel();
			jContentPane3.setLayout(null);
			jContentPane3.setSize(new java.awt.Dimension(586,500));
			jContentPane3.add(undo_levels1, null);
			jContentPane3.add(getUndo_levelsField1(), null);
			jContentPane3.add(log_mode1, null);
			jContentPane3.add(getLog_modeNone1(), null);
			jContentPane3.add(getLog_modeDesktop1(), null);
			jContentPane3.add(getLog_modeServer1(), null);
			jContentPane3.add(javascript_debug_mode1, null);
			jContentPane3.add(getJavascript_debug_modeYes1(), null);
			jContentPane3.add(getJavascript_debug_modeNo1(), null);
			jContentPane3.add(codebase1, null);
			jContentPane3.add(getCodebaseField1(), null);
		}
		return jContentPane3;
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
		if (dashboard_enabledYes == null) {
			dashboard_enabledYes = new JRadioButton();
			dashboard_enabledYes.setBounds(new Rectangle(419, 251, 46, 24));
			dashboard_enabledYes.setSelected(false);
			dashboard_enabledYes.setText("Yes");
			dashboard_enabledYes.setEnabled(true);
		}
		return dashboard_enabledYes;
	}

	/**
	 * This method initializes use_dashboardNo	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getUse_dashboardNo() {
		if (dashboard_enabledNo == null) {
			dashboard_enabledNo = new JRadioButton();
			dashboard_enabledNo.setBounds(new Rectangle(494, 251, 40, 24));
			dashboard_enabledNo.setSelected(true);
			dashboard_enabledNo.setText("No");
			dashboard_enabledNo.setName("No");
		}
		return dashboard_enabledNo;
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

	/**
	 * This method initializes undo_levelsField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getUndo_levelsField() {
		if (undo_levelsField == null) {
			undo_levelsField = new JTextField();
			undo_levelsField.setBounds(new Rectangle(451, 28, 55, 20));
			undo_levelsField.setHorizontalAlignment(JTextField.CENTER);
			undo_levelsField.setText("32");
		}
		return undo_levelsField;
	}

	/**
	 * This method initializes log_modeNone	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getLog_modeNone() {
		if (log_modeNone == null) {
			log_modeNone = new JRadioButton();
			log_modeNone.setBounds(new Rectangle(417, 78, 85, 24));
			log_modeNone.setText("no logging");
			log_modeNone.setHorizontalTextPosition(SwingConstants.TRAILING);
			log_modeNone.setSelected(false);
		}
		return log_modeNone;
	}

	/**
	 * This method initializes log_modeDesktop	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getLog_modeDesktop() {
		if (log_modeDesktop == null) {
			log_modeDesktop = new JRadioButton();
			log_modeDesktop.setBounds(new Rectangle(417, 100, 131, 24));
			log_modeDesktop.setSelected(true);
			log_modeDesktop.setText("log to desktop file");
			log_modeDesktop.setHorizontalTextPosition(SwingConstants.TRAILING);
		}
		return log_modeDesktop;
	}

	/**
	 * This method initializes log_modeServer	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getLog_modeServer() {
		if (log_modeServer == null) {
			log_modeServer = new JRadioButton();
			log_modeServer.setBounds(new Rectangle(417, 122, 148, 24));
			log_modeServer.setSelected(false);
			log_modeServer.setText("log to logging server");
			log_modeServer.setHorizontalTextPosition(SwingConstants.TRAILING);
		}
		return log_modeServer;
	}

	/**
	 * This method initializes javascript_debug_modeYes	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJavascript_debug_modeYes() {
		if (javascript_debug_modeYes == null) {
			javascript_debug_modeYes = new JRadioButton();
			javascript_debug_modeYes.setBounds(new Rectangle(417, 176, 46, 24));
			javascript_debug_modeYes.setText("Yes");
			javascript_debug_modeYes.setSelected(true);
		}
		return javascript_debug_modeYes;
	}

	/**
	 * This method initializes javascript_debug_modeNo	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJavascript_debug_modeNo() {
		if (javascript_debug_modeNo == null) {
			javascript_debug_modeNo = new JRadioButton();
			javascript_debug_modeNo.setBounds(new Rectangle(494, 176, 40, 24));
			javascript_debug_modeNo.setText("No");
			javascript_debug_modeNo.setName("No");
		}
		return javascript_debug_modeNo;
	}

	/**
	 * This method initializes codebaseField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getCodebaseField() {
		if (codebaseField == null) {
			codebaseField = new JTextField();
			codebaseField.setHorizontalAlignment(JTextField.CENTER);
			codebaseField.setBounds(new java.awt.Rectangle(421,238,114,20));
			codebaseField.setText("code/java");
		}
		return codebaseField;
	}
	
	/**
	 * This method initializes undo_levelsField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getUndo_levelsField1() {
		if (undo_levelsField1 == null) {
			undo_levelsField1 = new JTextField();
			undo_levelsField1.setBounds(new Rectangle(451, 28, 55, 20));
			undo_levelsField1.setHorizontalAlignment(JTextField.CENTER);
			undo_levelsField1.setText("32");
		}
		return undo_levelsField1;
	}

	/**
	 * This method initializes log_modeNone	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getLog_modeNone1() {
		if (log_modeNone1 == null) {
			log_modeNone1 = new JRadioButton();
			log_modeNone1.setBounds(new Rectangle(417, 78, 85, 24));
			log_modeNone1.setText("no logging");
			log_modeNone1.setHorizontalTextPosition(SwingConstants.TRAILING);
			log_modeNone1.setSelected(false);
		}
		return log_modeNone1;
	}

	/**
	 * This method initializes log_modeDesktop	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getLog_modeDesktop1() {
		if (log_modeDesktop1 == null) {
			log_modeDesktop1 = new JRadioButton();
			log_modeDesktop1.setBounds(new Rectangle(417, 100, 131, 24));
			log_modeDesktop1.setSelected(true);
			log_modeDesktop1.setText("log to desktop file");
			log_modeDesktop1.setHorizontalTextPosition(SwingConstants.TRAILING);
		}
		return log_modeDesktop1;
	}

	/**
	 * This method initializes log_modeServer	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getLog_modeServer1() {
		if (log_modeServer1 == null) {
			log_modeServer1 = new JRadioButton();
			log_modeServer1.setBounds(new Rectangle(417, 122, 148, 24));
			log_modeServer1.setSelected(false);
			log_modeServer1.setText("log to logging server");
			log_modeServer1.setHorizontalTextPosition(SwingConstants.TRAILING);
		}
		return log_modeServer1;
	}

	/**
	 * This method initializes javascript_debug_modeYes	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJavascript_debug_modeYes1() {
		if (javascript_debug_modeYes1 == null) {
			javascript_debug_modeYes1 = new JRadioButton();
			javascript_debug_modeYes1.setBounds(new Rectangle(417, 176, 46, 24));
			javascript_debug_modeYes1.setText("Yes");
			javascript_debug_modeYes1.setSelected(true);
		}
		return javascript_debug_modeYes1;
	}

	/**
	 * This method initializes javascript_debug_modeNo	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJavascript_debug_modeNo1() {
		if (javascript_debug_modeNo1 == null) {
			javascript_debug_modeNo1 = new JRadioButton();
			javascript_debug_modeNo1.setBounds(new Rectangle(494, 176, 40, 24));
			javascript_debug_modeNo1.setText("No");
			javascript_debug_modeNo1.setName("No");
		}
		return javascript_debug_modeNo1;
	}

	/**
	 * This method initializes codebaseField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getCodebaseField1() {
		if (codebaseField1 == null) {
			codebaseField1 = new JTextField();
			codebaseField1.setHorizontalAlignment(JTextField.CENTER);
			codebaseField1.setBounds(new java.awt.Rectangle(421,238,114,20));
			codebaseField1.setText("code/java");
		}
		return codebaseField1;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new java.awt.Dimension(500,500));
			jScrollPane.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jScrollPane.setName("");
			jScrollPane.setViewportView(getJContentPane3());
		}
		return jScrollPane;
	}

}
