package dao;

public abstract class Card {

	public String name = "";
	public int cost = 0;
	public boolean isWonder = false;

	public Card(String name, int cost, boolean isWonder){
		this.name = name;
		this.cost = cost;
		this.isWonder = isWonder;
	}

	
	public void action(){
		
	}

}


