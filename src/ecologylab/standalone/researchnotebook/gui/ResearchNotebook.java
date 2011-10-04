package ecologylab.standalone.researchnotebook.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import ecologylab.standalone.researchnotebook.gui.CollectionTreePanel.Collection;

public class ResearchNotebook extends JFrame{
	private JLabel bottom = new JLabel("research notebook");
	
	private JMenuBar  bar = new JMenuBar(); 
	private JMenu file = new JMenu("File"); 
	private JMenuItem item_open = new JMenuItem("Open"); 
	private JMenuItem item_exit = new JMenuItem("Exit"); 
	
	private JMenu help = new JMenu("Help"); 
	private JMenuItem item_about = new JMenuItem("About");
	
	private ActionHandler h = new ActionHandler(); 
	
	//add panel 
	CollectionTreePanel tp = new CollectionTreePanel(); 
	CollectionListPanel lp = new CollectionListPanel(); 
	CollectionPreviewPanel pp = new CollectionPreviewPanel();
	
	JTree tree; 
	
	JSplitPane sp_sub = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); 
	
	private static boolean debug = true; 
	
	public ResearchNotebook(){
		super("research notebook");
		super.setJMenuBar(bar);
		super.add(bottom, BorderLayout.SOUTH);
		
		bar.add(file);
		file.add(item_open);
		file.add(item_exit);
		
		bar.add(help);
		help.add(item_about); 
		
		//menu action handler 
		item_open.addActionListener(h);
		item_exit.addActionListener(h);
		item_about.addActionListener(h); 
		 
		sp_sub.setLeftComponent(tp); 
		sp_sub.setRightComponent(lp); 
		sp_sub.setDividerLocation(150);
		   
		sp.setLeftComponent(sp_sub); 
		sp.setRightComponent(pp); 
		sp.setDividerLocation(500);
		
		TreeListener l = new TreeListener(); 
		
		tree = tp.getTree();
		tree.addTreeSelectionListener(l);  
		
		add(sp);
	}
	
	private class TreeListener implements TreeSelectionListener{
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
			Object obj = node.getUserObject();
			
			if(node.isLeaf() && obj instanceof Collection){
				Collection col = (Collection)obj;
				if(debug)
					System.out.println("[ResearchNotebook] leaf url: " + col.link);
				try {
					lp.setText(CollectionInfo.collection_path + col.link);
					pp.displayUrl(col.link);
				} catch (IOException e1) {
					System.out.println("[ResearchNotebook] fail loading htmls");
				}
			}else{
				System.out.println("parent node : " + e.getPath().getLastPathComponent());
			}
		}
	}
	
	private static class ActionHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String action = e.getActionCommand(); 
			if(debug)
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
		r.setSize(1200,700); 
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
