package chess;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
import java.awt.event.*;

public class ChessGame extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	static int N = 8, SIZE = 70;

	/**
	 * should always be true if it is White's turn, false if it is Black's turn.
	 */
	private boolean isWhiteTurn;
	/**
	 * should be true during the first click of a turn (the "from") and false during the second click ("the to").
	 */
	private boolean isFirstClick;

	private Square[][] board;
	private Piece[][] pieces;
	
	/*private boolean w1check = false, w2check = false;
	private boolean b1check = false, b2check = false;
	private int wk1x = 7, wk1y = 0, wk2x = 7, wk2y = 7;
	private int bk1x = 0, bk1y = 0, bk2x = 0, bk2y = 7;*/

	private Square moveFrom;

	public ChessGame() 
	{
		super(new GridLayout(N, N));
		this.setPreferredSize(new Dimension(N * SIZE, N * SIZE));

		isWhiteTurn = true;
		isFirstClick = true;

		this.start();
	}

	/**
	 * This is where initialization happens. This puts pieces on the board. 
	 * If you want to set up your game differently than the normal way, you'll
	 * change this.
	 */
	public void start()
	{
		pieces = new Piece[N][N];
		
		// Pawns
		for(int col = 0; col < N; col++)
		{
			pieces[1][col] = new Piece(PieceType.Pawn, false);
			pieces[6][col] = new Piece(PieceType.Pawn, true);
		}
		
		// Rooks
		pieces[0][0] = new Piece(PieceType.Rook, false);
		pieces[0][7] = new Piece(PieceType.Rook, false);
		pieces[7][0] = new Piece(PieceType.Rook, true);
		pieces[7][7] = new Piece(PieceType.Rook, true);

		// Knights
		pieces[0][1] = new Piece(PieceType.Knight, false);
		pieces[0][6] = new Piece(PieceType.Knight, false);
		pieces[7][1] = new Piece(PieceType.Knight, true);
		pieces[7][6] = new Piece(PieceType.Knight, true);
		
		// Bishops
		pieces[0][2] = new Piece(PieceType.Bishop, false);
		pieces[0][5] = new Piece(PieceType.Bishop, false);
		pieces[7][2] = new Piece(PieceType.Bishop, true);
		pieces[7][5] = new Piece(PieceType.Bishop, true);

		// Queens
		pieces[0][3] = new Piece(PieceType.Queen, false);
		pieces[7][3] = new Piece(PieceType.Queen, true);
		
		// Kings
		pieces[0][4] = new Piece(PieceType.King, false);
		pieces[7][4] = new Piece(PieceType.King, true);


		// This sets up the physical board (buttons)
		board = new Square[N][N];
		for (int r = 0; r < N; r++) 
		{
			for(int c = 0; c < N; c++)
			{
				board[r][c] = new Square(r, c);
				board[r][c].addActionListener(this);
				this.add(board[r][c]);
			}
		}
		updateAllSquares();
	}
	
	/**
	 * redraws all squares to have their correct pieces on them.
	 */
	private void updateAllSquares()
	{
		for (int r = 0; r < board.length; r++) 
		{
			for(int c = 0; c < board[r].length; c++)
			{
				board[r][c].update(pieces[r][c]);
			}
		}
	}

	/*
	 * This method is called when a square is pressed. 
	 * It passes off the logic to either firstClick or secondClick.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof Square)
		{
			Square clicked = (Square) e.getSource();

			if(isFirstClick)
			{
				firstClick(clicked);
			}
			else
			{
				secondClick(clicked);
			}
		}
	}

	/**
	 * What to do during the first click.
	 * @param s Which square they clicked.
	 */
	public void firstClick(Square s)
	{
		Piece p = getPiece(s.row, s.col);
		if(p != null  && p.isWhite() == isWhiteTurn)
		{
			/*if(p.isWhite())//if white turn
			{
				//if in check using king, otherwise use whatever
				if ((w1check && s.row == wk1x && s.col == wk1y) || (w2check && s.row == wk2x && s.col == wk2y) || !(w1check || w2check)) 
				{
					// remember where we're moving from
					moveFrom = s;

					// Highlight square with the piece that's gonna move
					s.highlight();

					// make sure next time we click, it'll be a "second click"
					isFirstClick = false;
				}
			}
			else//if black turn
			{
				//if in check using king, otherwise use whatever
				if ((b1check && s.row == bk1x && s.col == bk1y) || (b2check && s.row == bk2x && s.col == bk2y) || !(b1check || b2check)) 
				{*/
					// remember where we're moving from
					moveFrom = s;

					// Highlight square with the piece that's gonna move
					s.highlight();

					// make sure next time we click, it'll be a "second click"
					isFirstClick = false;
				//}
			//}
		}
	}


	/**
	 * What to do during the second click.
	 * @param s Which square they clicked.
	 */
	public void secondClick(Square s)
	{
		if(isValidMove(s)) // if it's a valid move, do the move and switch the turn
		{
			executeMove(s);
			isWhiteTurn = !isWhiteTurn;	
		}

		// regardless, clear the first click.
		isFirstClick = true;
		moveFrom.unhighlight();
	}
	
	/**
	 * This is where the fun stuff happens.
	 * This method returns true if the piece 
	 * can move from the square "moveFrom" to the square s.
	 * 
	 * Add additional logic so that this works with all Pieces.
	 * 
	 * @param s The moveTo square
	 * @return whether the player can move the piece at moveFrom to s.
	 */
	private boolean isValidMove(Square s) 
	{
		if(moveFrom == null)
			return false;
		
		int fromRow = moveFrom.row;
		int fromCol = moveFrom.col;
		int toRow = s.row;
		int toCol = s.col;
		//PieceType is an enum. You don't really need to know what that means yet.
		PieceType pt = getPiece(fromRow,fromCol).getPieceType();
		boolean selectedIsWhite = getPiece(fromRow,fromCol).isWhite();
		
		//Basically this is how you check what kind of piece you are trying to move.
			if(pt == PieceType.Pawn)
			{	
				if(selectedIsWhite) // Selected Pawn is white
				{
					if(fromCol == toCol) // if you're staying in the same column
					{
						if(toRow == fromRow - 1 && isEmpty(toRow, toCol))
						{
							if (!getPiece(fromRow, fromCol).hasGone())
							{
								getPiece(fromRow, fromCol).changeGone();
							}
							return true;
						}
						if(toRow == fromRow - 2 && isEmpty(toRow, toCol) && isEmpty(toRow+1, toCol) && !getPiece(fromRow, fromCol).hasGone())
						{
							getPiece(fromRow, fromCol).changeGone();
							return true;
						}
					}
					else if(fromCol == toCol + 1) // if you're moving right one column
					{
						if(toRow == fromRow - 1 && isBlack(toRow, toCol))
						{
							return true;
						}
					}
					else if(fromCol == toCol - 1) // if you're moving left one column
					{
						if(toRow == fromRow - 1 && isBlack(toRow, toCol))
						{
							return true;
						}
					}
						return false; //ending return false if move not valid
				}
				else // Selected pawn is black
				{
					if(fromCol == toCol) // if you're staying in the same column
					{
						if(toRow == fromRow + 1 && isEmpty(toRow, toCol))
						{
							if (!getPiece(fromRow, fromCol).hasGone())
							{
								getPiece(fromRow, fromCol).changeGone();
							}
							return true;
						}
						//going forward two times
						if(toRow == fromRow + 2 && isEmpty(toRow, toCol) && isEmpty(toRow-1, toCol) && !getPiece(fromRow, fromCol).hasGone())
						{
							getPiece(fromRow, fromCol).changeGone();
							return true;
						}
					}
					else if(fromCol == toCol - 1) // if you're moving right one column
					{
						if(toRow == fromRow + 1 && isWhite(toRow, toCol))
						{
							return true;
						}
					}
					else if(fromCol == toCol + 1) // if you're moving left one column
					{
						if(toRow == fromRow + 1 && isWhite(toRow, toCol))
						{
							return true;
						}
					}
						return false;
				}//close if black
			}//close if pawn
		
			else if(pt == PieceType.Rook)
			{
					if(fromCol == toCol) // if you're staying in the same column
					{
						if(fromRow-toRow>0)  //if going forward
						{
							for (int k = 1; k<fromRow-toRow; k++)
							{
								if(!isEmpty(fromRow-k, toCol))
								{
									return false;
								}
							}
							if (isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
								return true;
						}
						else //else if going backward
						{
							for (int k = 1; k<toRow-fromRow; k++)
							{
								if(!isEmpty(fromRow+k, toCol)) //check empty tiles
								{
									return false;
								}
							}
							if (isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
								return true;
						}
						return false;
					}
					else if(fromRow == toRow) //if staying in same row
					{
						if(fromCol-toCol>0)  //if going left
						{
							for (int k = 1; k<fromCol-toCol; k++)
							{
								if(!isEmpty(toRow, fromCol-k))
								{
									return false;
								}
							}
							if (isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
								return true;
						}
						else //else if going right
						{
							for (int k = 1; k<toCol-fromCol; k++)
							{
								if(!isEmpty(toRow, fromCol+k))
								{
									return false;
								}
							}
							if (isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
								return true;
						}//close if going right
						return false; //if no valid move
					}//close if in same row
				}//close rook
		
			else if(pt == PieceType.Bishop)
			{
				if ((fromRow-toRow == fromCol-toCol) || (fromRow-toRow == toCol-fromCol))
				{
					if(fromRow > toRow)//going forward
					{
						if(fromCol > toCol)//going left
						{
							for (int k = 1; k<toCol-fromCol; k++)
							{
								if(!isEmpty(fromRow-k, fromCol-k))
								{
									return false;
								}
							}
							if (isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
								return true;
						}
						else //going right
						{
							for (int k = 1; k<toCol-fromCol; k++)
							{
								if(!isEmpty(fromRow-k, fromCol+k))
								{
									return false;
								}
							}
							if (isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
								return true;
						}
					}
					else //going backward
					{
						if(fromCol > toCol)//going left
						{
							for (int k = 1; k<toCol-fromCol; k++)
							{
								if(!isEmpty(fromRow+k, fromCol-k))
								{
									return false;
								}
							}
							if (isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
								return true;
						}
						else //going right
						{
							for (int k = 1; k<toCol-fromCol; k++)
							{
								if(!isEmpty(fromRow+k, fromCol+k))
								{
									return false;
								}
							}
							if (isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
								return true;
						}//close if going right backward
					}//close if going backward
				}//close if going diaganol
				return false;
			}//close bishop
		
			else if(pt == PieceType.Queen)
			{
				//rook moves
				if(fromCol == toCol) // if you're staying in the same column
				{
					if(fromRow-toRow>0)  //if going forward
					{
						for (int k = 1; k<fromRow-toRow; k++)
						{
							if(!isEmpty(fromRow-k, toCol))
							{
								return false;
							}
						}
						if (isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
							return true;
					}
					else //else if going backward
					{
						for (int k = 1; k<toRow-fromRow; k++)
						{
							if(!isEmpty(fromRow+k, toCol)) //check empty tiles
							{
								return false;
							}
						}
						if (isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
							return true;
					}
					return false;
				}
				else if(fromRow == toRow) //if staying in same row
				{
					if(fromCol-toCol>0)  //if going left
					{
						for (int k = 1; k<fromCol-toCol; k++)
						{
							if(!isEmpty(toRow, fromCol-k))
							{
								return false;
							}
						}
						if (isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
							return true;
					}
					else //else if going right
					{
						for (int k = 1; k<toCol-fromCol; k++)
						{
							if(!isEmpty(toRow, fromCol+k))
							{
								return false;
							}
						}
						if (isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
							return true;
					}//close if going right
					return false; //if no valid move for rook moves
				}//close if in same row
			
				//else bishop moves
				else if ((fromRow-toRow == fromCol-toCol) || (fromRow-toRow == toCol-fromCol))
				{
					if(fromRow > toRow)//going forward
					{
						if(fromCol > toCol)//going left
						{
							for (int k = 1; k<toCol-fromCol; k++)
							{
								if(!isEmpty(fromRow-k, fromCol-k))
								{
									return false;
								}
							}
							if (isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
								return true;
						}
						else //going right
						{
							for (int k = 1; k<toCol-fromCol; k++)
							{
								if(!isEmpty(fromRow-k, fromCol+k))
								{
									return false;
								}
							}
							if (isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
								return true;
						}
					}
					else //going backward
					{
						if(fromCol > toCol)//going left
						{
							for (int k = 1; k<toCol-fromCol; k++)
							{
								if(!isEmpty(fromRow+k, fromCol-k))
								{
									return false;
								}
							}
							if (isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
								return true;
						}
						else //going right
						{
							for (int k = 1; k<toCol-fromCol; k++)
							{
								if(!isEmpty(fromRow+k, fromCol+k))
								{
									return false;
								}
							}
							if (isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
								return true;
						}//close if going right backward
					}//close if going backward
				}//close if going diaganol
				return false;//if not valid move
			}//close queen
		
			else if (pt == PieceType.King)
			{
				if ((Math.abs(fromRow-toRow)<=1) && (Math.abs(fromCol-toCol)<=1)) //if moving to squares immediately nearby
				{
					if(isEmpty(toRow, toCol) || isEnemy(toRow,toCol))
					{
						/*if (fromRow == bk1x && fromCol == bk1y)
							{
								bk1x = toRow;
								bk1y = toCol;
							}
						if (fromRow == bk2x && fromCol == bk2y)
						{
							bk2x = toRow;
							bk2y = toCol;
						}
						if (fromRow == wk1x && fromCol == wk1y)
						{
							wk1x = toRow;
							wk1y = toCol;
						}
						if (fromRow == wk2x && fromCol == wk2y)
						{
							wk2x = toRow;
							wk2y = toCol;
						}*/
						return true;
					}
				}//close movement
				return false; //otherwise invalid move
			}//close King
		
			else if (pt == PieceType.Knight)
			{
				//counting whether in L shape
				if (((Math.abs(fromRow-toRow) == 2 && Math.abs(fromCol-toCol) == 1)) || (Math.abs(fromRow-toRow) == 1 && Math.abs(fromCol-toCol) == 2))
				{
					if(isEmpty(toRow, toCol) || isEnemy(toRow, toCol))
						return true;
				}
			}//close Knight
			return false;//if click bad thing return false
		}//close valid move
	
	
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @return if a specific row and column is empty (i.e. on the board, and no pieces)
	 */
	private boolean isEmpty(int row, int col) 
	{
		return row >= 0 && col >= 0 && row < N && col < N && pieces[row][col] == null;
	}
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @return if a specific row and column has a black piece on it.
	 */
	private boolean isBlack(int row, int col) 
	{
		return row >= 0 && col >= 0 && row < N && col < N && pieces[row][col] != null && !pieces[row][col].isWhite();
	}

	private boolean isEnemy(int row, int col)
	{
		if(isWhiteTurn)
		{
			return isBlack(row, col);
		}
		else 
		{
			return isWhite(row, col);
		}
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @return if a specific row and column has a white piece on it.
	 */
	private boolean isWhite(int row, int col) 
	{
		return row >= 0 && col >= 0 && row < N && col < N && pieces[row][col] != null && pieces[row][col].isWhite();
	}
	
	/**
	 * Gets the piece at a specified position. 0,0 is the top-left.
	 * 
	 * @param row which row (horizontal)
	 * @param col which column (vertical)
	 * @return the piece at (row, col). null if empty.
	 */
	private Piece getPiece(int row, int col) 
	{
		return pieces[row][col];
	}

	/**
	 * This method executes the move. Should only be run if isValidMove was run first.
	 * @param s The square to move to
	 */
	private void executeMove(Square s)
	{
		int fromRow = moveFrom.row;
		int fromCol = moveFrom.col;
		int toRow = s.row;
		int toCol = s.col;

		// move the piece from the old place to the new place
		pieces[toRow][toCol] = pieces[fromRow][fromCol];
		pieces[fromRow][fromCol] = null;

		// redraw the appropriate pieces
		board[toRow][toCol].update(pieces[toRow][toCol]);
		board[fromRow][fromCol].update(pieces[fromRow][fromCol]);
		
		////////////////////////////////////////////////////////////////////////////////
		
/*		moveFrom = board[s.row][s.col];
		if (isWhite(s.row, s.col)) //white king in check?
		{
			if(isValidMove(board[wk1x][wk1y])) //white king 1
			{
				w1check = true;
			}
			if(isValidMove(board[wk2x][wk2y])) //white king 2
			{
				w2check = true;
			}
		}
		if (isBlack(s.row, s.col)) //black king in check?
		{
			if(isValidMove(board[bk1x][bk1y])) //black king 1
			{
				b1check = true;
			}
			if(isValidMove(board[bk2x][bk2y])) //black king 2
			{
				b2check = true;
			}
		} */
	}
}








