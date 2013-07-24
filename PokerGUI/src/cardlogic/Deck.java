package cardlogic;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class Deck {
	public static final int DECK_SIZE = 52;
	protected int currentPosition = 0;
	
	protected Card [] deck;
	
	private int x,y;
	
	private BufferedImage deckImage;
	private BufferedImage emptyDeckImage;
	private BufferedImage currentImage;
	
	public Deck(int x, int y) throws IOException{
		deck = new Card[DECK_SIZE];
		
		int curNumCards = 0;
		
		deckImage = ImageIO.read(new File("Images/b2fv.png"));
		emptyDeckImage = ImageIO.read(new File("Images/noCarsd.png"));
		
		currentImage = deckImage;
		
		Suit[]suits = Suit.values();
		Value[]values = Value.values();
		
		for(int v = values.length - 1; v >= 0; v--){
			for(int s = 0; s < suits.length; s++){
				deck[curNumCards++] = new Card(values[v], suits[s], "" + curNumCards);
			}
		}
		
		this.x = x;
		this.y = y;
		
	}
	
	public BufferedImage getDeckImage(){
		return currentImage;
	}
	
	public Deck() throws IOException{
		this(-1,-1);
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public int getY(){
		return y;
	}

	public void setY(int y){
		this.y = y;
	}
	
	public void shuffleDeck(){
		
		int randPosOne, randPosTwo;
		
		Random randGen = new Random();
		
		for(int i = 0; i < DECK_SIZE; i++){
			randPosOne = randGen.nextInt(DECK_SIZE);
			randPosTwo = randGen.nextInt(DECK_SIZE);
			
			Card tmp = deck[randPosOne];
			
			deck[randPosOne] = deck[randPosTwo]; 
			deck[randPosTwo] = tmp;
		}
	}
	
	public boolean hasMoreCards(){
		return currentPosition < DECK_SIZE;
	}
	
	public Card nextCard(){

		if(!hasMoreCards()){
			System.out.println("empty deck");
			reset();
		}
		Card copyCard = null;
		try {
			copyCard = new Card(deck[currentPosition++]);
		} catch (IOException e) {
			System.out.println("deck next card error");
			e.printStackTrace();
		}
		return copyCard;
	}
	
	public void reset(){
		currentPosition = 0;
		shuffleDeck();
		currentImage = deckImage;
	}
	
	public String toString(){
		String s = "";
		
		for(Card c : deck){
			s += c.getValue() + " of " + c.getSuit() + "\n";
		}
		
		return s;
	}
}
