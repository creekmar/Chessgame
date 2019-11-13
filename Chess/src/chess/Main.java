package chess;
import javax.swing.JFrame;

/*
Don't mess with the Main - it just creates a ChessGame and shows it.
 */
public class Main {

	public static void main(String[] args) 
	{
		JFrame f = new JFrame();
		ChessGame g = new ChessGame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(g);
		f.pack();
		f.setVisible(true);

	}
}

