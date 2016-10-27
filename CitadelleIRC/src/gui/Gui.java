package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Gui extends JFrame{

	public Gui(){
		this.setSize(300, 200);
		this.setTitle("Citabot");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		initControles();
	}
	
	public void initControles(){
		JPanel zoneClient = (JPanel)this.getContentPane();
	}
	
}
