package game;

import cardlogic.Player;

public class PokerGame {
	private int potAmount;
	
	private int currentBet;
	
	private int roundNumber;
	
	public PokerGame(){
		currentBet = 5;
	}
	
	public void resetRoundNumber(){
		roundNumber = 1;
	}
	
	public int getRoundNumber(){
		return roundNumber;
	}
	
	public void increaseRoundNumber(){
		roundNumber++;
	}
	
	public static Player[] getInPlayers(Player[]players){
		int count = 0;
		for(Player p : players){
			if(!p.isOut()){
				count++;
			}
		}
		
		Player [] ps = new Player[count];
		
		int psCount = 0;
		for(int i = 0; i < players.length; i++){
			if(!players[i].isOut()){
				ps[psCount++] = players[i];
			}
		}
		
		return ps;
	}
	
	public void addToPot(int amount){
		potAmount += amount;
	}
	
	public int getPotAmount(){
		return potAmount;
	}
	
	public void setCurrentBet(int amount){
		currentBet = amount;
	}
	
	public int getCurrentBet(){
		return currentBet;
	}
	
	public void givePotToPlayer(Player p){
		p.addMoney(potAmount);
		potAmount = 0;
		
		currentBet = 5;
		resetRoundNumber();
	}
	
	public boolean callForPlayer(Player p){
		if(p.getMoney() < currentBet){
			p.subtractMoney(p.getMoney());
			p.setPlayerBet(p.getMoney());
			potAmount += p.getMoney();
			return false;
		}
		p.subtractMoney(currentBet);
		p.setPlayerBet(currentBet);
		potAmount += currentBet;
		return true;
	}
	
	public void raiseByPlayer(int amount, Player p){

		currentBet += amount;
		
		if(p.getMoney() < currentBet){
			p.subtractMoney(p.getMoney());
			p.setPlayerBet(p.getMoney());
			potAmount += p.getMoney();
			return;
		}
		p.subtractMoney(currentBet);
		p.setPlayerBet(currentBet);
		potAmount += currentBet;
		
	}
}
