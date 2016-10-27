package bot;

import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

public class Bot extends PircBot{

	public String nick = "citabot";
	public String server = "irc.esper.net";
	public String channel = "#citadelle-test";
	
	public Bot(){
		this.setName(nick);
		this.setAutoNickChange(true);
		try {
			this.connect(server);
			this.joinChannel(channel);;
			System.out.println("connected on " + this.getServer());
		} catch (IOException | IrcException e) {
			// TODO Auto-generated catch block
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
	}
	
	@Override
	protected void onMessage(String channel, String sender, String login, String hostname, String message) {
		// TODO Auto-generated method stub
		super.onMessage(channel, sender, login, hostname, message);
		if(message.equals("hello")){
			this.sendMessage(channel, "Hello everybody");
		}
	}
	
}
