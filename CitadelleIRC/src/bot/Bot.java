package bot;

import java.io.IOException;
import java.util.Vector;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import dao.Player;
import gui.Gui;

public class Bot extends PircBot{

	public String nick = "citabot";
	public String server = "irc.esper.net";
	public String channel = "#citadelle-test";
	public String admin;
	public Gui gui = null;
	
	//game variables
	Vector<Player> vPlayer = new Vector<Player>();
	
	public Bot(Gui gui, String nick){
		this.setName(nick);
		this.setAutoNickChange(true);
		try {
			this.nick = nick;
			this.connect(server);
			this.joinChannel(channel);
			this.nick = this.getNick();
			this.gui = gui;
			System.out.println("connected on " + this.getServer());
		} catch (IOException | IrcException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to conect as " + nick);
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onConnect() {
		// TODO Auto-generated method stub
		super.onConnect();
	}
	
	@Override
	protected void onJoin(String channel, String sender, String login, String hostname) {
		// TODO Auto-generated method stub
		super.onJoin(channel, sender, login, hostname);
		if(sender.equals(this.nick)){
			gui.labRet.setText("Connected on: " + this.server + ", " + this.channel + " as " + this.nick);
		}
	}
	
	@Override
	protected void onMessage(String channel, String sender, String login, String hostname, String message) {
		// TODO Auto-generated method stub
		super.onMessage(channel, sender, login, hostname, message);
		addPlayer(sender, message);
		
		
		if(message.equals("hello")){
			this.sendMessage(channel, "Hello everybody");
		}
	}
	
	public void addPlayer(String sender, String message){
		if(message.equals("!add")){
			Player player = new Player(sender);
			vPlayer.add(player);
			this.sendMessage(channel, sender + " was added to the game!");
		}
	}
	
	public static Vector<String> cutter(String message, String separator){
		Vector<String> vec = new Vector<String>();
		int start = message.indexOf(separator) + 1;
		int stop = message.indexOf(separator, start);
		String ret = "";

		while(stop <= message.length() && stop != -1){
			ret = message.substring(start, stop);
			vec.add(ret);
			start = stop + 1;
			stop = message.indexOf(separator, start);
		}

		ret = message.substring(start);
		vec.add(ret);

		return vec;
}
}
