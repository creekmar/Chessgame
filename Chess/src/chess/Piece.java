package chess;

import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.BufferedImage;

public class Piece
{
	private boolean isWhite;
	private PieceType pieceType;
	private boolean gone;
	
	private ImageIcon icon;

	public Piece(PieceType pieceType, boolean isWhite)
	{
		this.isWhite = isWhite;
		this.pieceType = pieceType;
		this.gone = false;
		
		String fileName = "images/" + pieceType.toString() + "-"+(isWhite?"W":"B")+"55.png";
		try
		{
			BufferedImage buttonIcon = ImageIO.read(
					new File(fileName));
			icon = new ImageIcon(buttonIcon);
		} 
		catch(IOException e)
		{
			System.err.println(e);
		}
	}

	public ImageIcon getIcon()
	{
		return icon;
	}

	public boolean isWhite() 
	{
		return isWhite;
	}

	public PieceType getPieceType() 
	{
		return pieceType;
	}
	
	public boolean hasGone()
	{
		return gone;
	}
	
	public void changeGone()
	{
		gone = true;
	}
	
}