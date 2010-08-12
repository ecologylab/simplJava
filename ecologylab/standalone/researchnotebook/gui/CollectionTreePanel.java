package ecologylab.standalone.researchnotebook.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class CollectionTreePanel extends JPanel implements TreeSelectionListener{
	
	public CollectionTreePanel(){
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {
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
