package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class GameInitPanel extends JPanel{
	
	public GameInitPanel(final PokerPanel pokerPanel){
		
		String [] choices = {"1 opponent", "2 opponents", "3 opponents", "4 opponents", "5 opponents"};
		
		setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		JPanel comboPanel = new JPanel();
		final JComboBox playerChoices = new JComboBox(choices);
		comboPanel.add(playerChoices);
		add(comboPanel);
		
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				String s = (String)playerChoices.getSelectedItem();
				s = s.substring(0, 1);
				pokerPanel.startGame(Integer.parseInt(s));
				
			}
			
		});
		
		add(startButton);
		
		
	}

}
