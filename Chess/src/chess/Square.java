package chess;

import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.event.*;

public class Square extends JButton{
	/**
	 * ignore this
	 */
	private static final long serialVersionUID = 1L;
	
	public int row;
	public int col;
	
	private Color original;
	private Color lr = new Color(180, 80, 90);

	/**
	 * Constructs a square at the specified row and column.
	 * @param r row of the square
	 * @param c column of the square
	 */
	public Square(int r, int c) 
	{
		super();
		original = this.getBackground();
		row = r;
		col = c;

		this.setOpaque(true);
		this.setBorderPainted(false);
		this.unhighlight();

	}

	/**
	 * Redraws the button with the specified Piece
	 * @param p - Piece which is on this square. null if Square is empty.
	 */
	public void update(Piece p)
	{
		if(p == null)
		{
			setIcon(null);
		}
		else
		{
			setIcon(p.getIcon());
		}
	}


	/**
	 * highlights the square
	 */
	public void highlight() {
		// Change this color if you want to.
		this.setBackground(Color.yellow);
	}

	/**
	 * set the color of this square back to the original color
	 */
	public void unhighlight()
	{
		if ((row + col) % 2 == 1) 
		{
			this.setBackground(lr);
		}
		else
		{
			this.setBackground(original);
		}
	}


}