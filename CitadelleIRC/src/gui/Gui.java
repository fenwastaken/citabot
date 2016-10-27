package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Gui extends JFrame{

	public JLabel labRet = new JLabel("Connecting...");
	
	public Gui(){
		this.setSize(450, 100);
		this.setTitle("Citabot");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		initControles();
	}
	
	public void initControles(){
		JPanel zoneClient = (JPanel)this.getContentPane();
		zoneClient.add(labRet);
	}
	
}
