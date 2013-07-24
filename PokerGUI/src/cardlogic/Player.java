package cardlogic;

import java.awt.Point;

public class Player {
	private Hand hand;
	private int money;
	private int score;
	private Point playerPosition; 
	private int playerBet;
	
	private String playerName;
	
	private boolean folded;
	
	public static final int USER_PLAYER = 0;
	
	public static final int DEFAULT_STARTING_MONEY = 200;
	
	public Player(String playerName){
		this.playerName = playerName;
		score = 0;
		folded = false;
		playerBet = 0;
		money = DEFAULT_STARTING_MONEY;
	}
	
	public boolean isFolded(){
		return folded;
	}
	
	public boolean isOut(){
		return money <= 0;
	}
	
	public void setPlayerPosition(Point p) throws Exception{
		playerPosition = p;
		
		for(int i = 0; i < hand.numCards(); i++){
			hand.getCard(i).setEndX(p.x + 14*i);
			hand.getCard(i).setEndY(p.y);
		}
	}
	
	public void setPlayerBet(int amount){
		playerBet = amount;
	}
	
	public int getPlayerBet(){
		return playerBet;
	}
	
	public Point getPlayerPosition(){
		return playerPosition;
	}
	
	public Player(Hand h, String playerName){
		this(playerName);
		hand = h;
	}
	
	public String getName(){
		return playerName;
	}
	
	public void setIsFolded(boolean b){
		folded = b;
	}
	
	public void setHand(Hand h){
		hand = h;
	}
	
	public Hand getHand(){
		return hand;
	}
	
	public void addMoney(int m){
		money += m;
	}
	
	public void subtractMoney(int m){
		money -= m;
	}
	
	public synchronized int getMoney(){
		return money;
	}
	
	public int numSelectedCards(){
		int count = 0;
		for(Card c : hand.getCards()){
			if(c.isSelected()){
				count++;
			}
		}
		
		return count;
	}
	
	public void updateHandPositions(){
		for(int i = 0; i < hand.numCards(); i++){
			try{
				hand.getCard(i).setX(playerPosition.x + 14*i);
				hand.getCard(i).setY(playerPosition.y);
			}catch(Exception e){
				
			}
		}
	}
	
	public void fold(){
		folded = true;
		hand = null;
	}
	
	public void raise(){
		
	}
	
	public void call(){
		
	}
}
