package ecologylab.standalone.researchnotebook.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CollectionPreviewPanel extends JPanel implements CollectionInfo{
	JTextPane previewPanel = new JTextPane();  
	JScrollPane sp = new JScrollPane(previewPanel);
	
	private static boolean debug = true; 
	
	public CollectionPreviewPanel(){
		super(new GridLayout(1,1));
		add(sp);
	}
	
	public void displayUrl(String name) throws IOException{
		if(debug)
			System.out.println("displayUrl: " + CollectionInfo.collection_path + name);
		URL re = ClassLoader.getSystemResource(CollectionInfo.collection_path + name);
		previewPanel.setPage(re); 
	}
	
	public static void main(String[] args) throws IOException {
		CollectionPreviewPanel p = new CollectionPreviewPanel(); 
		JFrame f = new JFrame();
		f.setTitle("CollectionPreviewPanel");
		f.add(p);
		f.setSize(700,500);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
