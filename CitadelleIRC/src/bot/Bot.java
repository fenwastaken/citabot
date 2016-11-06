package bot;

import java.io.IOException;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import dao.Charac;
import dao.Player;
import gui.Gui;

public class Bot extends PircBot{

	public String nick = "citabot";
	public String server = "abyss.darkmyst.org";
	public String channel = "#danaus-ooc";
	public String admin;
	public Gui gui = null;

	//game variables------------
	Vector<Player> vPlayer = new Vector<Player>();
	Vector<Charac> vChar = new Vector<Charac>();

	Player currentPlayer = null;
	
	int currentRole = 1;
	int turn = 0;
	
	boolean isGameOn = false;
	boolean pickingPhase = false;
	boolean rolePhase = false;
	
	boolean acted = false;
	boolean drewCoin = false;
	

	//--------------------------


	public Bot(Gui gui, String nick){
		this.setName(nick);
		this.setAutoNickChange(true);
		try {
			this.nick = nick;
			this.connect(server);
			this.joinChannel(channel);
			this.nick = this.getNick();
			this.gui = gui;
			this.admin = "Duskie";
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
		initiateGame();
	}

	@Override
	protected void onMessage(String channel, String sender, String login, String hostname, String message) {
		// TODO Auto-generated method stub
		super.onMessage(channel, sender, login, hostname, message);


		managePlayers(sender, message);
		roleList(message);
		playerList(message);
		startGame(sender, message);
		initiateCommand(sender, message);
	}

	@Override
	protected void onPrivateMessage(String sender, String login,
			String hostname, String message) {
		// TODO Auto-generated method stub
		super.onPrivateMessage(sender, login, hostname, message);
		
		rolePicker(sender, message);
	}

	public void playerList(String message){
		if(message.equals("!player list")){
			String ret = playerListString();
			sendMessage(channel, "[" + vPlayer.size() + "] Players are: " + ret + ".");
		}
	}


	public String playerListString(){
		String ret = "";
		if(vPlayer.isEmpty() || vPlayer == null){
			sendMessage(channel, "There is no player yet.");
		}
		else{
			for(Player pl : vPlayer){
				ret += pl.getName() + ", ";
			}
			ret = ret.substring(0, ret.length() - 2);

		}
		return ret;
	}

	public void roleList(String message){
		if(message.equals("!roles")){
			String ret = roleListString();
			sendMessage(channel, "Available roles are: " + ret);
		}
	}

	public String roleListString(){
		String ret = "";
		for(Charac cha : vChar){
			if(cha.getOwner().isEmpty()){
				ret += cha.getName() + ", ";
			}
		}
		ret = ret.substring(0, ret.length() - 2);
		return ret;
	}

	public void managePlayers(String sender, String message){
		if(sender.toLowerCase().equals(admin.toLowerCase())){
			if(message.startsWith("!player ")){
				Vector<String> param = cutter(message, " ");
				String param1 = "";
				String target = "";
				if(param.size() > 1){
					param1 = param.elementAt(0);
					target = param.elementAt(1);
				}
				if(param1.equals("add")){
					if(vPlayer.size() < 7){
						if(playerFinder(target) == null){
							Player player = new Player(target);
							vPlayer.add(player);
							this.sendMessage(channel, target + " was added to the game!");
							playerList(message);
						}
						else{
							sendMessage(channel, target + " is in the game already.");
						}

					}
					else{
						sendMessage(channel, "There's no spot left for additional players.");
					}
				}
				else if(param1.equals("remove")){
					Player play = playerFinder(target);
					if(play != null){
						vPlayer.remove(play);
						sendMessage(channel, "Player " + target + " was removed.");
						playerList(message);
					}
					else{
						sendMessage(channel, "Player " + target + " not found among players.");
					}
				}
			}
		}
	}



	public Player playerFinder(String name){
		Player play = null;
		for(Player pl : vPlayer){
			if(pl.getName().toLowerCase().equals(name.toLowerCase())){
				play = pl;
			}
		}

		return play;
	}

	public void initiateGame(){
		isGameOn = false;
		pickingPhase = false;
		turn = 0;
		currentRole = 1;

		vChar.removeAllElements();
		vPlayer.removeAllElements();

		dao.Charac c1 = new dao.Charac(1, "assassin", "none", "");
		dao.Charac c2 = new dao.Charac(2, "thief", "none", "");
		dao.Charac c3 = new dao.Charac(3, "magician", "none", "");
		dao.Charac c4 = new dao.Charac(4, "king", "yellow", "");
		dao.Charac c5 = new dao.Charac(5, "bishop", "blue", "");
		dao.Charac c6 = new dao.Charac(6, "merchant", "green", "");
		dao.Charac c7 = new dao.Charac(7, "architect", "none", "");
		dao.Charac c8 = new dao.Charac(8, "warlord", "red", "");

		vChar.add(c1);
		vChar.add(c2);
		vChar.add(c3);
		vChar.add(c4);
		vChar.add(c5);
		vChar.add(c6);
		vChar.add(c7);
		vChar.add(c8);

		sendMessage(channel, "Game initialised, you may add players.");
	}

	public void initiateCommand(String sender, String message){
		if(sender.equals(admin) && message.equals("!reset")){
			initiateGame();
		}
	}

	public void startGame(String sender, String message){
		if(sender.equals(admin) && message.equals("!start") && !isGameOn){
			isGameOn = true;
			Collections.shuffle(vPlayer);
			sendMessage(channel, "Game starts with " + playerListString());
			Player king = vPlayer.firstElement();
			sendMessage(channel, king.getName() + " is king! long live the king! [You pick your role first]");
			sendMessage(channel, "You all recieve 2 golds and 4 cards!");
			setKing(king);
			currentPlayer = king;
			pickingPhase();
		}
	}

	public void pickingPhase(){
		sendMessage(channel, "turn[" + turn + "] Picking phase!");
		pickingPhase = true;

		Charac visible = getRandomRole();

		while(visible.getNumber() == 4){
			visible = getRandomRole();
		}

		visible.setOwner("removed");

		Charac notVisible = getRandomRole();
		while(notVisible.getOwner().equals("removed")){
			notVisible = getRandomRole();
		}
		notVisible.setOwner("removed");
		sendMessage(channel, "This turn, " + visible.getName() + " and a secret second role are out!");
		System.out.println(notVisible.getName() + " is the hidden card");

		sendMessage(channel, currentPlayer.getName() + " is picking a role!");
		sendMessage(currentPlayer.getName(), "Available roles: " + roleListString());
		sendMessage(currentPlayer.getName(), "Use !pick 1 to get the thief etc");
	}

	public void rolePicker(String sender, String message){
		if(pickingPhase && sender.equals(currentPlayer.getName()) && message.startsWith("!pick ")){
			int num = Integer.parseInt(cutter(message, " ").elementAt(0));
			Charac ch = getRoleFromNumber(num);
			if(ch != null && ch.getOwner().equals("") && getRoleFromOwner(sender) == null){
				ch.setOwner(sender);
				sendMessage(sender, "you now are the " + ch.getName());
				nextPlayer();
				if(getRoleFromOwner(currentPlayer.getName()) == null){
					sendMessage(channel, sender + " picked a role, now it's " + currentPlayer.getName() + " turn!");
					sendMessage(currentPlayer.getName(), "Available roles: " + roleListString());
					sendMessage(currentPlayer.getName(), "Use !pick 1 to get the thief etc");
				}
				else{
					sendMessage(channel, "Everyone has a role, let's start the role phase!");
					pickingPhase = false;
					rolePhase = true;
					currentRole = 1;
					phaseDecider();
				}
			}
			else{
				sendMessage(currentPlayer.getName(), "This is not a valid pick, try again.");
			}
		}
	}

	public void phaseDecider(){
		String currentOwner = getRoleFromNumber(currentRole).getOwner();
		if(rolePhase && currentRole == 1){// assassin phase--------------------------
			if(!currentOwner.equals("") && !currentOwner.equalsIgnoreCase("removed")){
				sendMessage(channel, currentOwner + " is the " + getRoleFromNumber(currentRole).getName() + "!");
				sendMessage(channel, "Who do you wish to assassinate, " + currentOwner + "?");
			}
			else{
				sendMessage(channel, "There is no assassin this turn!");
				currentRole++;
				phaseDecider();
			}
		}
		
		else if(rolePhase && currentRole == 2){ //thief phase--------------------------
			if(!currentOwner.equals("") && !currentOwner.equalsIgnoreCase("removed")){
				sendMessage(channel, currentOwner + " is the " + getRoleFromNumber(currentRole).getName() + "!");
				sendMessage(channel, "Who do you wish to steal from, " + currentOwner + "?");
			}
			else{
				sendMessage(channel, "There is no thief this turn!");
				currentRole++;
				phaseDecider();
			}
			
		}
		
		else if(rolePhase && currentRole == 3){ //magician phase--------------------------
			if(!currentOwner.equals("") && !currentOwner.equalsIgnoreCase("removed")){
				sendMessage(channel, currentOwner + " is the " + getRoleFromNumber(currentRole).getName() + "!");
				sendMessage(channel, "You can exchange your cards whenever, " + currentOwner + "!");
			}
			else{
				sendMessage(channel, "There is no magician this turn!");
				currentRole++;
				phaseDecider();
			}
			
		}
		
		else if(rolePhase && currentRole == 4){ // king phase--------------------------
			if(!currentOwner.equals("") && !currentOwner.equalsIgnoreCase("removed")){
				sendMessage(channel, currentOwner + " is the " + getRoleFromNumber(currentRole).getName() + "!");
				sendMessage(channel, "You will now pick your role first, " + currentOwner + "!");
			}
			else{
				sendMessage(channel, "There is no king this turn!");
				currentRole++;
				phaseDecider();
			}
			
		}
		
		else if(rolePhase && currentRole == 5){ //bishop phase--------------------------
			if(!currentOwner.equals("") && !currentOwner.equalsIgnoreCase("removed")){
				sendMessage(channel, currentOwner + " is the " + getRoleFromNumber(currentRole).getName() + "!");
				sendMessage(channel, "Your citadel can't be attacked, " + currentOwner + "!");
			}
			else{
				sendMessage(channel, "There is no bishop this turn!");
				currentRole++;
				phaseDecider();
			}
			
		}
		
		else if(rolePhase && currentRole == 6){ //merchant phase--------------------------
			if(!currentOwner.equals("") && !currentOwner.equalsIgnoreCase("removed")){
				sendMessage(channel, currentOwner + " is the " + getRoleFromNumber(currentRole).getName() + "!");
				sendMessage(channel, "You get one free coin, " + currentOwner + "!");
			}
			else{
				sendMessage(channel, "There is no merchant this turn!");
				currentRole++;
				phaseDecider();
			}
			
		}
		
		else if(rolePhase && currentRole == 7){ //architect phase--------------------------
			if(!currentOwner.equals("") && !currentOwner.equalsIgnoreCase("removed")){
				sendMessage(channel, currentOwner + " is the " + getRoleFromNumber(currentRole).getName() + "!");
				sendMessage(channel, "You get two cards and get to build up to three districts, " + currentOwner + "!");
			}
			else{
				sendMessage(channel, "There is no architect this turn!");
				currentRole++;
				phaseDecider();
			}
			
		}
		
		else if(rolePhase && currentRole == 8){ //warlord phase--------------------------
			if(!currentOwner.equals("") && !currentOwner.equalsIgnoreCase("removed")){
				sendMessage(channel, currentOwner + " is the " + getRoleFromNumber(currentRole).getName() + "!");
				sendMessage(channel, "You may destruct one district, " + currentOwner + "!");
			}
			else{
				sendMessage(channel, "There is no warlord this turn!");
				//reset + début du tour suivant
			}
			
		}
		
	}
	
	public Charac getRoleFromOwner(String name){
		Charac ch = null;
		for(Charac chara : vChar){
			if(chara.getOwner().equals(name)){
				ch = chara;
			}
		}
		return ch;
	}

	public void nextPlayer(){
		int index = vPlayer.indexOf(currentPlayer);
		System.out.println("Current player is " + currentPlayer.getName() + " index = " + index);
		index++;
		if(vPlayer.size() > index){
			currentPlayer = vPlayer.elementAt(index);
			System.out.println("incremented!");
		}
		else{
			currentPlayer = vPlayer.elementAt(0);
			System.out.println("back to zero!");
		}
		System.out.println("Current player is " + currentPlayer.getName());
	}

	public Charac getRoleFromNumber(int number){
		Charac chara = null;
		for(Charac ch : vChar){
			if(ch.getNumber() == number){
				chara = ch;
			}
		}
		return chara;
	}

	public Charac getRandomRole(){
		Charac chara = null;
		int maxIndex = vChar.size();
		Random generator = new Random();
		int rn = generator.nextInt(maxIndex);
		chara = vChar.elementAt(rn);
		return chara;
	}

	public void setKing(Player player){
		for(Player pl : vPlayer){
			pl.setKing(false);
		}
		player.setKing(true);
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
