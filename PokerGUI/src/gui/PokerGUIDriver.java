package gui;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;

import cardlogic.Deck;

public class PokerGUIDriver {
	public static void main(String[]args){
		
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
		} catch (Exception e){
			
		}
		
		JFrame pokerFrame = new JFrame("Poker");
		
		Deck d = null;
		
		try {
			d = new Deck(200,0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		d.shuffleDeck();
		
		PokerPanel pokerPanel = new PokerPanel(d, 5);
		
		pokerFrame.setContentPane(pokerPanel);
		
		//pokerFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);  
		pokerFrame.setSize(1015,570);
		pokerFrame.setVisible(true);
		pokerFrame.setResizable(false);
		pokerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try{
		
			Image img = ImageIO.read(new File("Images/54.png"));
			pokerFrame.setIconImage(img);
			
		}catch(Exception e){
			
		}
		
		pokerPanel.showInitFrame();
	}
}
