package ecologylab.standalone.researchnotebook.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ResearchNotebook extends JFrame{
	private JLabel bottom = new JLabel("research notebook");
	
	private JMenuBar  bar = new JMenuBar(); 
	private JMenu file = new JMenu("File"); 
	private JMenuItem item_open = new JMenuItem("Open"); 
	private JMenuItem item_exit = new JMenuItem("Exit"); 
	
	private JMenu help = new JMenu("Help"); 
	private JMenuItem item_about = new JMenuItem("About");
	
	private ActionHandler h = new ActionHandler(); 
	
	private static boolean DEBUG = true; 
	
	public ResearchNotebook(){
		super("research notebook"); 
		super.setJMenuBar(bar);
		super.add(bottom, BorderLayout.SOUTH);
		
		bar.add(file);
		file.add(item_open);
		file.add(item_exit);
		
		bar.add(help);
		help.add(item_about); 
		
		//action handler 
		item_open.addActionListener(h);
		item_exit.addActionListener(h);
		item_about.addActionListener(h); 
	}
	
	private static class ActionHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String action = e.getActionCommand(); 
			if(DEBUG)
				System.out.println("[INFO] action requested :" + action);
			if("About" == action)
				JOptionPane.showMessageDialog(null, "this is a research notebook containing a collection of user-authored cF compositions.", "About", JOptionPane.INFORMATION_MESSAGE);
			else if("Exit" == action)
				System.exit(0); 
		}
	}
	
	private static void showGui(){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			System.out.println("[Exception] cannot apply lookAndFeel");
		}
        
		ResearchNotebook r = new ResearchNotebook();  
		r.setVisible(true);
		r.setSize(900,700); 
		r.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {	
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showGui(); 
            }
        }); 
	}
}
