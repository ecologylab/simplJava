package ecologylab.standalone.researchnotebook.gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class CollectionTreePanel extends JPanel implements CollectionInfo{
	JTree tree ;
	
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Default_Collection");
	DefaultMutableTreeNode c1_1 = new DefaultMutableTreeNode("collection1");
	DefaultMutableTreeNode c1_2 = new DefaultMutableTreeNode("collection2");
	DefaultMutableTreeNode c1_3 = new DefaultMutableTreeNode("collection3");
	
	// TODO should be collected automatically 
	DefaultMutableTreeNode c1_1_leaf1 = new DefaultMutableTreeNode(new Collection("composition1", CollectionInfo.collection_path + "composition1.html"));
	DefaultMutableTreeNode c1_1_leaf2 = new DefaultMutableTreeNode(new Collection("composition2", CollectionInfo.collection_path + "composition2.html"));
	DefaultMutableTreeNode c1_1_leaf3 = new DefaultMutableTreeNode(new Collection("composition3", CollectionInfo.collection_path + "composition3.html"));
	
	private static boolean debug = true; 
	
	public CollectionTreePanel(){
		super(new GridLayout(1,1));
		top.add(c1_1);
		top.add(c1_2);
		top.add(c1_3);
		
		c1_1.add(c1_1_leaf1);
		c1_1.add(c1_1_leaf2);
		c1_1.add(c1_1_leaf3);
		
		tree = new JTree(top);
		add(tree);
		
		TreeListener l = new TreeListener();
		tree.addTreeSelectionListener(l); 
	}
	
	private static class TreeListener implements TreeSelectionListener{
		@Override
		public void valueChanged(TreeSelectionEvent e){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
			Object obj = node.getUserObject(); 
			
			if(node.isLeaf() && obj instanceof Collection){
				Collection col = (Collection)obj;
				if(debug)
					System.out.println("leaf url: " + col.link);
			}else{
				System.out.println("parent node : " + e.getPath().getLastPathComponent());
			}
		}	
	}
	
	//TODO should include comprehensive metadata - by parsing composition xml 
	private static class Collection{
		String title; 
		String link; 
		
		public Collection(String t, String l){
			title = t; 
			link = l; 
		}
		
		public String toString(){
			return title;  
		}
	}
	
	public static void main(String[] args) {
		CollectionTreePanel p = new CollectionTreePanel(); 
		JFrame f = new JFrame();
		f.setTitle("CollectionTreePanel");
		f.add(p);
		f.setSize(300,300);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	}
}
