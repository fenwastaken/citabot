package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bot.Bot;

public class Gui extends JFrame{

	public JLabel labRet = new JLabel("Pick a name:");
	public JButton btOk = new JButton("Ok");
	public JTextField tfName = new JTextField();
	
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
		zoneClient.setLayout(new BoxLayout(zoneClient, BoxLayout.Y_AXIS));
		zoneClient.add(labRet);
		labRet.setAlignmentX(CENTER_ALIGNMENT);
		zoneClient.add(tfName);
		tfName.setAlignmentX(CENTER_ALIGNMENT);
		btOk.setAlignmentX(CENTER_ALIGNMENT);
		zoneClient.add(btOk);
		
		btOk.addActionListener(new appActionListener());
		
		tfName.setMaximumSize(new Dimension(250, 25));
	}
	

	class appActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == btOk){
				
				String name = tfName.getText();
				Bot bot = new Bot(Gui.this, name);
				Gui.this.btOk.setEnabled(false);
			}
		
		}
		
	}
	
}

