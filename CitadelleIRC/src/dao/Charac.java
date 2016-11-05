package dao;

/**
 * Created by Formation on 27/10/2016.
 */
	public class Charac {
	
	String name = "";
	String color = "none";
	int number = 0;
	String owner;
	boolean isDead = false;
	
	
	public Charac(int number, String name, String color, String owner){
		this.name = name;
		this.color = color;
		this.number = number;
		this.owner = owner;
	}


	public String getName() {
		return "[" + this.getNumber()+ "] " + name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}


	public int getNumber() {
		return number;
	}


	public void setNumber(int number) {
		this.number = number;
	}


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public boolean isDead() {
		return isDead;
	}


	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	
	
	
}
