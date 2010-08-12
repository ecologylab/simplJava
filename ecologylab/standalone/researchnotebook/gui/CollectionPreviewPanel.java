package ecologylab.standalone.researchnotebook.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CollectionPreviewPanel extends JPanel implements ActionListener, ListSelectionListener{
	public CollectionPreviewPanel(){
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {	
	}
	
	@Override
	public void valueChanged(ListSelectionEvent arg0) {	
	}
	
	public static void main(String[] args) {
		CollectionPreviewPanel p = new CollectionPreviewPanel(); 
		JFrame f = new JFrame();
		f.setTitle("CollectionPreviewPanel");
		f.add(p);
		f.setSize(300,300);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
