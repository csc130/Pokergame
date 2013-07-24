package game;

import gui.PokerPanel;

import java.util.Random;

import cardlogic.Card;
import cardlogic.Player;

public class AIRunnable implements Runnable{
	
	private boolean discardMode;
	private PokerPanel gamePanel;
	private PokerGame mainGame;
	
	private Player[]players;
	
	public AIRunnable(boolean discardMode, PokerPanel p, PokerGame mainGame){
		this.discardMode = discardMode;
		gamePanel = p;
		players = p.getPlayers();
		this.mainGame = mainGame;
	}
	
	@Override
	public void run() {
		if(discardMode){
			Random random = new Random();
			for(int i = 1; i < players.length; i++){
				if(!players[i].isFolded()){
					
					gamePanel.setCurrentPlayerIndex(i);
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					int numToSwap = random.nextInt(3);
					
					for(int j = 0; j < numToSwap; j++){
						
						if(players[i].getHand() == null){
							System.out.println("null hand");
						}
						
						Card c = null;
						try {
							c = players[i].getHand().getCard(j);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						Card newCard = gamePanel.getDeck().nextCard();
						newCard.copyOthersCoords(c);
						
						players[i].getHand().swapCardAtPosition(j, newCard);
						
						
					}
					
					players[i].updateHandPositions();
				}
			}
			
			for(int i = 0; i < players.length; i++){
				if(!players[i].isFolded()){
					gamePanel.setCurrentPlayerIndex(i);
					break;
				}
			}
			
		}else{
			Random randomGen = new Random();
			for(int i = 1; i < players.length; i++){
				if(!players[i].isFolded()){
					gamePanel.checkFolds();
					
					gamePanel.setCurrentPlayerIndex(i);
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					switch(randomGen.nextInt(2)){
					case 0:
						//fold
						players[i].fold();
						players[i].setHand(null);
						System.out.println("ai folded");
						break;
					case 1:
						//call
						mainGame.callForPlayer(players[i]);
						System.out.println("ai called");
						break;
					case 2:
						//raise
						mainGame.raiseByPlayer(randomGen.nextInt(100) + 1, players[i]);
						System.out.println("ai raised");
						break;
					}
					
				}
			}
			mainGame.increaseRoundNumber();
			gamePanel.checkFolds();
			
			gamePanel.setCurrentPlayerIndex(0);
			
			//dealCards();
			
			discardMode = false;
			
			if(mainGame.getRoundNumber() == 2){
				System.out.println("round 2");
				gamePanel.switchToDiscard(true);
				discardMode = true;
			}else if(mainGame.getRoundNumber() == 3){
				System.out.println("last round up");
				gamePanel.revealAI();
				
				Player winningPlayer = null;
				
				for(Player p : players){
					if(!p.isFolded()){
						gamePanel.updateRanks();
						if(winningPlayer == null){
							winningPlayer = p;
						}else if(winningPlayer.getHand().compareTo(p.getHand()) == -1){
							winningPlayer = p;
						}
					}
				}
				
				mainGame.givePotToPlayer(winningPlayer);
				System.out.println(winningPlayer.getName() + " wins");
				
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				gamePanel.resetPlayerVars();
				
				System.out.println("third round deal");
				gamePanel.dealCards();
			}
			
			if(players[Player.USER_PLAYER].isOut()){
				gamePanel.showMessage("YOU LOSE");
				return;
			}
			
			if(players[PokerPanel.USER_PLAYER].isFolded()){
				run();
			}
			
			gamePanel.setAllButtonsEnabled(true);
		}
		
		if(players[Player.USER_PLAYER].isOut()){
			System.out.println("player loses");
		}
	}

}
