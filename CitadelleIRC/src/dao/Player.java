package dao;

import java.util.Vector;

/**
 * Created by Formation on 27/10/2016.
 */
public class Player {
	
	String name;
	int gold = 0;
	boolean isKing = false;
	Vector<Card> vCard= new Vector<Card>();
	
	
	public Player(String name){
		this.name = name;
		this.gold = 2;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getGold() {
		return gold;
	}


	public void setGold(int gold) {
		this.gold = gold;
	}


	public boolean isKing() {
		return isKing;
	}


	public void setKing(boolean isKing) {
		this.isKing = isKing;
	}


	public Vector<Card> getvCard() {
		return vCard;
	}


	public void setvCard(Vector<Card> vCard) {
		this.vCard = vCard;
	}
	
	
	
}
