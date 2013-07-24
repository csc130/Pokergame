package cardlogic;
import java.io.IOException;


public class PokerDriver {
	public static void main(String[]args){
		Deck d = null;
		try {
			d = new Deck();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(d);
		
		d.shuffleDeck();
		
		System.out.println(d);
	}
}
