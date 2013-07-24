package flip;

import java.util.TimerTask;

import cardlogic.Card;

public class FlipTimerTask extends TimerTask{
	
	boolean front, back;
	Card card;
	
	public FlipTimerTask(Card c){
		back = true;
		front = false;
		card = c;
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
