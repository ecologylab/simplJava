package ecologylab.standalone.researchnotebook.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class CollectionListPanel extends JPanel implements TreeSelectionListener{
	public CollectionListPanel(){
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {
	}
	
	public static void main(String[] args) {
		CollectionListPanel p = new CollectionListPanel(); 
		JFrame f = new JFrame();
		f.setTitle("CollectionListPanel");
		f.add(p);
		f.setSize(300,300);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
