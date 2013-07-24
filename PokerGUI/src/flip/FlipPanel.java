package flip;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JPanel;

import cardlogic.Card;
import cardlogic.Suit;
import cardlogic.Value;

public class FlipPanel extends JPanel{
	
	Card card;
	
	public FlipPanel(){
		try {
			
			card = new Card(Value.ACE, Suit.HEART);
			card.faceDown();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JButton flip = new JButton("flip");
		flip.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FlipTimer timerTask = new FlipTimer();
				Timer animTimer = new Timer();
				animTimer.schedule(timerTask, 0, 20);
			}
			
		});
		add(flip);
	}
	
	class FlipTimer extends TimerTask{

		boolean front, back;
		
		public FlipTimer(){
			back = true;
			front = false;
		}
		
		public void run() {
			if(card.getScaleX() > 0 && back == true){
				card.setScaleX(card.getScaleX() - .1f);
			}else if(card.getScaleX() <= 0 && back == true){
				back = false;
				front = true;
				card.flipCard();
			}else if(card.getScaleX() < 1 && front == true){
				card.setScaleX(card.getScaleX() + .1f);
			}else{
				this.cancel();
			}
		}
		
	}
	
	protected void paintComponent(Graphics g){
		
		super.paintComponent(g);
//		//g.drawImage(cardImage, 0,0, null);
//		Image scaledImage = card.getImage().getScaledInstance((int)card.getXwidth(), (int)card.getImage().getHeight(), BufferedImage.SCALE_FAST);
//	    
//	    g2d.drawImage(scaledImage, (int) (50 + card.getXwidth()/2), 50, null);
//		//g.drawImage(backImage, 0, 0,null);
	    
	    AffineTransform transformer = new AffineTransform();
	    transformer.translate(300 - (card.getImage().getWidth()*(card.getScaleX()))/2,
  			  300 - (card.getImage().getHeight())/2);
		transformer.scale(card.getScaleX(),1);
		
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.drawImage(card.getImage(), transformer, null);
		//g2d.drawImage(card.getImage(),200 - card.getImage().getWidth()/2,200 - card.getImage().getHeight()/2, null);
		
		g2d.drawOval(300, 300,3,3);
	    
		
	    this.repaint();
	}
}
