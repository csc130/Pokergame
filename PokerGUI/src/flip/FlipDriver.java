package flip;

import javax.swing.JFrame;

public class FlipDriver {
	public static void main(String[]args){
		JFrame frame = new JFrame();
		
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  
		frame.setContentPane(new FlipPanel());
		frame.setVisible(true);
	}
}	
