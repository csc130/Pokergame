package gui;

import flip.FlipTimerTask;
import game.AIRunnable;
import game.PokerGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cardlogic.Card;
import cardlogic.Deck;
import cardlogic.Hand;
import cardlogic.Player;

public class PokerPanel extends JPanel{
	
	private Deck deck;
	
	private Player[]players;
	public static final int USER_PLAYER = 0;
	
	private JButton foldButton, callButton, raiseButton, checkButton;
	
	private JButton discardButton;

	private JPanel buttonPanel;
	
	private JPanel gamePanel;
	private PokerGame mainGame;
	
	private Timer animTimer;

	private BufferedImage tableImage;
	
	private boolean isDealing = false;
	
	private JLabel raiseLabel;
	private JSlider raiseSlider;
	
	private int currentPlayerIndex = 0;
	
	private PokerPanel selfPanel;

	public PokerPanel(Deck d, int numCompPlayers){
		setLayout(new BorderLayout());
		
		selfPanel = this;
		
		deck = d;
		
		mainGame = new PokerGame();
		
		gamePanel = new GamePanel();
		add(gamePanel, BorderLayout.CENTER);
		
		buttonPanel = new JPanel();
		
		foldButton = new JButton("Fold");
		foldButton.setFocusable(false);
		foldButton.addActionListener(new PlayerActionListener(0));
		
		callButton = new JButton("Call");
		callButton.setFocusable(false);
		callButton.addActionListener(new PlayerActionListener(1));
		raiseLabel = new JLabel("$1");
		
		raiseButton = new JButton("Raise");
		raiseButton.setFocusable(false);
		raiseButton.addActionListener(new PlayerActionListener(2));
		
		checkButton = new JButton("Check");
		checkButton.setFocusable(false);
		
		raiseSlider = new JSlider(JSlider.HORIZONTAL, 5, 100, 5);
		raiseSlider.setFocusable(false);
		raiseSlider.addChangeListener(new ChangeListener(){
			
			public void stateChanged(ChangeEvent arg0) {
				raiseLabel.setText("$" + raiseSlider.getValue());
			}
			
		});
		
		/*
		 * Players
		 */
		
		players = new Player[numCompPlayers + 1];
		
		/*
		 * Need to remove draw button
		 */
		
		try {
			tableImage = ImageIO.read(new File("Images/pokerTable.png"));

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		discardButton = new JButton("Discard");
		discardButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				
				for(int i = 0; i < players[USER_PLAYER].getHand().numCards(); i++){
					Card c = null;
					try {
						c = players[USER_PLAYER].getHand().getCard(i);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(c.isSelected()){
						Card newCard = deck.nextCard();
						newCard.faceUp();
						newCard.copyOthersCoords(c);
						newCard.setSelected(false);
						newCard.setY(newCard.getY() + 15);
						
						players[USER_PLAYER].getHand().swapCardAtPosition(i, newCard);
					}
				}
				
				players[USER_PLAYER].updateHandPositions();
				
				discardButton.setEnabled(false);
				
				Thread aiThread = new Thread(new AIRunnable(true, selfPanel, mainGame));
				aiThread.start();
				
				switchToDiscard(false);
			}
			
		});
		discardButton.setVisible(false);
		
		buttonPanel.add(discardButton);
		
		buttonPanel.add(foldButton);
		buttonPanel.add(callButton);
		buttonPanel.add(checkButton);
		buttonPanel.add(raiseButton);	
		
		buttonPanel.add(raiseLabel);
		buttonPanel.add(raiseSlider);
		
		add(buttonPanel, BorderLayout.SOUTH);
		
	}
	
	class PlayerActionListener implements ActionListener{
		
		private int actionID;

		public PlayerActionListener(int id){
			actionID = id;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			
			
			
			switch(actionID){
			case 0:
				//fold
				players[USER_PLAYER].fold();
				break;
			case 1:
				//call
				mainGame.callForPlayer(players[USER_PLAYER]);
				break;
			case 2:
				//raise
				int raiseAmount = Integer.parseInt(raiseLabel.getText().substring(1));
				
				mainGame.raiseByPlayer(raiseAmount, players[USER_PLAYER]);
				break;
			}
			
			setAllButtonsEnabled(false);
			
			Thread aiThread = new Thread(new AIRunnable(false, selfPanel, mainGame));
			aiThread.start();
			//simulateComputers();
		}
		
	}
	
	private JFrame initGameFrame;
	
	public void showInitFrame(){
		initGameFrame = new JFrame("init");
		initGameFrame.setUndecorated(true);
		
		initGameFrame.setContentPane(new GameInitPanel(this));
		initGameFrame.setSize(160,100);
		initGameFrame.setLocation(getWidth()/2 - 80, getHeight()/2 - 50);
		initGameFrame.setResizable(false);
		initGameFrame.setVisible(true);

	}
	
	public void setAllButtonsEnabled(boolean enabled){
		raiseButton.setEnabled(enabled);
		callButton.setEnabled(enabled);
		foldButton.setEnabled(enabled);
		raiseSlider.setEnabled(enabled);
		checkButton.setEnabled(enabled);
	}
	
	public void startGame(int numPlayers){
		
		players = new Player[numPlayers + 1];
		
		for(int i = 0; i < players.length; i++){
			if(players[i] == null){
				System.out.println("player created");
				players[i] = new Player("Player " + (i+1));
			}
		}
		
		initGameFrame.dispose();
		initGameFrame = null;
		
		System.out.println("startGame method");
		dealCards();
	}
	
	public Player[] getPlayers(){
		return players;
	}
	
	public int getCurrentPlayerIndex(){
		return currentPlayerIndex;
	}
	
	public void setCurrentPlayerIndex(int i){
		currentPlayerIndex = i;
	}
	
	public Deck getDeck(){
		return deck;
	}
	
	public void revealAI(){
		for(Player p : players){
			if(!p.isFolded()){
				for(Card c : p.getHand().getCards()){
					c.faceUp();
				}
			}
		}
	}
	
	public void switchToDiscard(boolean b){
		discardButton.setVisible(b);
		discardButton.setEnabled(true);
		
		callButton.setVisible(!b);
		raiseButton.setVisible(!b);
		foldButton.setVisible(!b);
		raiseLabel.setVisible(!b);
		raiseSlider.setVisible(!b);
		checkButton.setVisible(!b);
	}
	
	public void checkFolds(){
		
		int numFolds = 0;
		for(Player p : players){
			if(p.isFolded()){
				numFolds++;
			}
		}
		
		if(numFolds == players.length - 1){
			for(Player p : players){
				if(!p.isFolded()){
					mainGame.givePotToPlayer(p);
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					resetPlayerVars();
					
					System.out.println("folded deal");
					dealCards();
					return;
				}
			}
		}
	}
	
	public void resetPlayerVars(){
		for(Player p : players){
			p.setIsFolded(false);
		}
	}
	
	public void dealCards(){
		
		isDealing = true;
		
		mainGame.resetRoundNumber();
		
//		try{
//			hand.addCard(new Card(Value.SEVEN, Suit.CLUB));
//			hand.addCard(new Card(Value.SEVEN, Suit.SPADE));
//			hand.addCard(new Card(Value.SEVEN, Suit.DIAMOND));
//			hand.addCard(new Card(Value.TWO, Suit.HEART));
//			hand.addCard(new Card(Value.TWO, Suit.CLUB));
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		try{
//			hand2.addCard(new Card(Value.SIX, Suit.HEART));
//			hand2.addCard(new Card(Value.SIX, Suit.CLUB));
//			hand2.addCard(new Card(Value.SIX, Suit.DIAMOND));
//			hand2.addCard(new Card(Value.THREE, Suit.SPADE));
//			hand2.addCard(new Card(Value.THREE, Suit.DIAMOND));
//		}catch(Exception e){
//			//System.out.println("here");
//		}
		
		for(Player p : players){
			
			if(!p.isOut()){
			
				p.setHand(new Hand());
				System.out.println("numcards: " + p.getHand().numCards());
	
				while(p.getHand().numCards() != 5){
					Card c = deck.nextCard();
					c.faceDown();
					p.getHand().addCard(c);
					
				}
				mainGame.callForPlayer(p);
			}else{
				p.setHand(null);
			}
		}
		
		try{
		
			for(Player p : players){
				if(p.getHand() != null){
					for(int i = 0; i < p.getHand().numCards(); i++){
						p.getHand().getCard(i).setX(gamePanel.getWidth()/2);
						p.getHand().getCard(i).setY(-100);
						
						/*
						 * need to customly set each player
						 */
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		 
		try{
			for(int i = 0; i < players.length; i++){
				switch(i){
				case 0:
					players[i].setPlayerPosition(new Point(310,363));
					break;
				case 1:
					players[i].setPlayerPosition(new Point(640,363));
					break;
				case 2:
					players[i].setPlayerPosition(new Point(850,200));
					break;
				case 3:
					players[i].setPlayerPosition(new Point(640,50));
					break;
				case 4:
					players[i].setPlayerPosition(new Point(310,50));
					break;
				case 5:
					players[i].setPlayerPosition(new Point(100,200));
					break;
				}
			}
		}
		catch(Exception e){
			
		}
		
		AnimTimer timerTask = new AnimTimer(15,players);
		animTimer = new Timer();
		animTimer.schedule(timerTask, 0, 1);
	}
	
	public void updateRanks(){

		for(Player p : players){
			if(p.getHand() != null){
				p.getHand().updateRank();
			}
		}
	}
	
	public void showMessage(String msg){
		JOptionPane.showMessageDialog(this, msg);
	}

	class AnimTimer extends TimerTask{
		
		private boolean firstRun = true;
		
		private float xMov, yMov;
		
		private Card curr = null;
		private Player[] animPlayers = null;
		
		private int currentHandIndex = 0;
		
		private int ttc;
		
		public AnimTimer(int ttc, Player[] animPlayers){
			
			this.animPlayers = PokerGame.getInPlayers(animPlayers);
			try {
				curr = animPlayers[currentHandIndex].getHand().nextCard();
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.ttc = ttc;
		}
		
		public void run() {
			try{
				if(firstRun){
					
					//curr = currHand.getCard(currentAnimIndex);
					
					xMov = (curr.getX() - curr.getEndX()) / ((float)ttc);
					
					yMov = (curr.getY() - curr.getEndY()) / ((float)ttc);
					
					firstRun = false;
					
					if(animPlayers[currentHandIndex] == players[USER_PLAYER]){
						
						FlipTimerTask flipTask = new FlipTimerTask(curr);
						Timer flipTimer = new Timer();
						flipTimer.schedule(flipTask, 100, 10);
						
					}
				}
				
				curr.setX((curr.getX() - xMov));
				curr.setY((curr.getY() - yMov));
				
				if(withinRange(3,(int)curr.getX(), curr.getEndX()) && withinRange(3,(int)curr.getY(), curr.getEndY())){

					currentHandIndex++;
					currentHandIndex %= players.length;

					curr = players[currentHandIndex].getHand().nextCard();
					firstRun = true;

				}
			}catch(Exception e){
				//currentAnimIndex = 0;
				for(Player p : animPlayers){
					p.getHand().reset();
				}
				
				isDealing = false;
				this.cancel();
				
				updateRanks();
			}
		}
		
	}
	
	public boolean withinRange(int range, int val1, int val2){
		if(Math.abs(val1 - val2) <= range){
			return true;
		}
		return false;
	}
	
	class GamePanel extends JPanel{
		
		int mouseX, mouseY;
		
		public GamePanel(){
			
			/*
			 * mouse movement
			 */
			this.addMouseMotionListener(new MouseMotionListener(){

				public void mouseDragged(MouseEvent arg0) {

				}

				public void mouseMoved(MouseEvent me) {
					mouseX = me.getX();
					mouseY = me.getY();
				}
				
			});
			
			/*
			 * mouse actions
			 */
			this.addMouseListener(new MouseListener(){

				public void mouseClicked(MouseEvent me) {
					if(players[USER_PLAYER] != null){
						for(int i = players[USER_PLAYER].getHand().numCards() - 1; i >= 0; i--){
							Card c = null;
							
							try {
								c = players[USER_PLAYER].getHand().getCard(i);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							int clickX = me.getX();
							int clickY = me.getY();
							
							if(c.contains(new Point(clickX, clickY))){
								if(!c.isSelected() && players[USER_PLAYER].numSelectedCards() < 3){
									c.setSelected(true);
									c.setY(c.getY() - 15);
								}else if(c.isSelected()){
									c.setSelected(false);
									c.setY(c.getY() + 15);
								}
								break;
							}
							
						}
					}
					
				}

				public void mouseEntered(MouseEvent arg0) {

				}

				public void mouseExited(MouseEvent arg0) {

				}

				public void mousePressed(MouseEvent arg0) {

				}

				public void mouseReleased(MouseEvent arg0) {

				}
				
			});
		}
		
		protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			
			Graphics2D g2d = (Graphics2D)g;
			g2d.drawImage(tableImage,0,0,null);
			
			g2d.setColor(Color.white);
			
			for(int j = 0; j < players.length; j++){
				if(players[j] != null){
					Player p = players[j];
	
					if(p.getHand() != null){
						
						try{
							for(int i = 0; i < p.getHand().numCards(); i++){
								if(p.getHand().getCard(i) != null){
									Card card = p.getHand().getCard(i);
								
									AffineTransform transformer = new AffineTransform();
									
									transformer.translate((card.getX()) - (card.getImage().getWidth()*(card.getScaleX()))/2, card.getY());
									transformer.scale(card.getScaleX(),1);
									
									g2d.drawImage(card.getImage(), transformer, null);
							
								}
							}
						}catch(Exception e){
							System.out.println("paint error");
						}
					}
					
					if(currentPlayerIndex == j){
						g2d.setColor(Color.red);
					}else{
						g2d.setColor(Color.white);
					}
					
					g2d.drawString("Player " + (j+1) + "    $" + p.getMoney(), p.getPlayerPosition().x - 35, p.getPlayerPosition().y + 115);
				}
			}
		
			
			g2d.setColor(Color.white);
			
			g2d.drawString("Current Pot: $" + mainGame.getPotAmount() , gamePanel.getWidth()/2, gamePanel.getHeight()/2);
			g2d.drawString("Current Bet: $" + mainGame.getCurrentBet() , gamePanel.getWidth()/2, gamePanel.getHeight()/2 + 15);
			
			g2d.drawString(mouseX + ", " + mouseY, mouseX, mouseY);
			
			this.repaint();
		}
		
	}
	
}
