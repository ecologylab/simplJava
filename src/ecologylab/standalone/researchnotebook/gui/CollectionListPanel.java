package ecologylab.standalone.researchnotebook.gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CollectionListPanel extends JPanel{
	JTextArea a = new JTextArea(); 
	JScrollPane sp = new JScrollPane(a);
	
	private static boolean debug = true; 
	
	public CollectionListPanel(){
		super(new GridLayout(1,1));
		
		add(sp);	
		if(debug)
			setText("Collection List Panel");
	}
	
	public void setText(String text){
		a.setText(text);		
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
