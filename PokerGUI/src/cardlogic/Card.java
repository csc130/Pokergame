package cardlogic;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Card implements Comparable{
	
	private Value val;
	private Suit suit;
	
	private BufferedImage cardImage;
	private BufferedImage frontImage;
	private BufferedImage backImage;
	
	private float x, y;
	private int width,height;
	
	private float scaleX;
	
	private int endX = 0, endY = 0;
	
	private boolean isSelected = false;
	
	public Card(Value v, Suit s, String imageName) throws IOException{
		val = v;
		suit = s;
		frontImage = ImageIO.read(new File("Images" + "/" + imageName + ".png"));
		backImage = ImageIO.read(new File("Images/b2fv.png"));
		cardImage = backImage;
		width = cardImage.getWidth();
		height = cardImage.getHeight();
		scaleX = 1;
		x = 0;
		y = 0;
	}
	
	public Card(Value v, Suit s) throws IOException{
		this(v, s, "" + (((12 - v.ordinal()) *4) + (s.ordinal() + 1)));
	}
	
	public Card(Card other) throws IOException{
		this(other.getValue(), other.getSuit());
	}
	
	public void copyOthersCoords(Card o){
		x = o.getX();
		y = o.getY();
		endX = o.getEndX();
		endY = o.getEndY();
	}
	
	public void setScaleX(float xw){
		scaleX = xw;
	}
	
	public void setSelected(boolean sel){
		isSelected = sel;
	}
	
	public boolean isSelected(){
		return isSelected;
	}
	
	public float getScaleX(){
		return scaleX;
	}

	
	public void faceUp(){
		cardImage = frontImage;
	}
	
	public void faceDown(){
		cardImage = backImage;
	}
	
	public void flipCard(){
		if(cardImage == frontImage){
			cardImage = backImage;
		}else{
			cardImage = frontImage;
		}
	}
	
	public void setEndX(int x){
		endX = x;
	}
	
	public void setEndY(int y){
		endY = y;
	}
	
	public int getEndX(){
		return endX;
	}
	
	public int getEndY(){
		return endY;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public Value getValue(){
		return val;
	}
	
	public Suit getSuit(){
		return suit;
	}
	
	public BufferedImage getImage(){
		return cardImage;
	}
	
	public String toString(){
		return val + " of " + suit;
	}
	
	public boolean contains(Point p){
		if(p.x + width/2 > x && p.x + width/2 < (x + width) && p.y > y && p.y < (y + height)){
			return true;
		}
		return false;
	}
	
	@Override
	public int compareTo(Object o) {
		if(!(o instanceof Card)){
			return -1;
		}
		
		if(val.ordinal() < ((Card)o).getValue().ordinal()){
			return -1;
		}else if(val.ordinal() > ((Card)o).getValue().ordinal()){
			return 1;
		}else{
			return 0;
		}
	}
	
	public static int compareValues(Value v1, Value v2){
		if(v1.ordinal() > v2.ordinal()){
			return 1;
		}else if(v1.ordinal() < v2.ordinal()){
			return -1;
		}else{
			return 0;
		}
	}
}
