package board;

import pieces.Piece;

/** An abstract implementation of the chess board */
public abstract class AbstractBoard implements Board {
	
	/** A 2d array of pieces representing the chess board */
	Piece[][] board = new Piece[8][8];
	/** Number of points for white 
	 * Invariant: nonnegative
	 */
	int whitePoints = 0;
	/** Number of points for black 
	 * Invariant: nonnegative
	 */
	int blackPoints = 0;

	@Override
	public Piece squareInfo(int x, int y) {
		if (x < 0 || x > 7 || y < 0 || y > 7)
			return null;
		return board[x][y];
	}
	
	@Override
	public boolean move(int x, int y,int r, int c) {
		if(isValidMove(x,y,r,c))
		{
			if(board[r][c] == null)
			{
				board[r][c] = board[x][y];
				board[x][y] = null;
			}
			else
			{
				changeScore(r,c);
				board[r][c] = board[x][y];
				board[x][y] = null;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean isValidMove(int x, int y, int r, int c) {
		if (r > 7 || r < 0 || c > 7 || c < 0 ||
				x > 7 || x < 0 || y > 7 || y < 0) return false;
		if(x == r && y == c)
		{
			return false;
		}
		if(putsKingInCheck(x,y,r,c))
		{
			return false;
		}
		try {
			Piece.Type pieceType = ((Piece)(board[r][c])).getType();
			switch(pieceType)
			{
			case PAWN:
				return pawnMove(x,y,r,c);
			case KNIGHT:
				return knightMove(x,y,r,c);
			case BISHOP:
				return bishopMove(x,y,r,c);
			case ROOK:
				return rookMove(x,y,r,c);
			case QUEEN:
				return queenMove(x,y,r,c);
			default:
				return kingMove(x,y,r,c);
			}
		} catch (NullPointerException e) {
			return false;
		}
	}

	@Override
	public void changeScore(int r, int c)
	{
		if(whitePoints == blackPoints)
		{
			if(((Piece)(board[r][c])).getColor().equals(Piece.Color.BLACK))
			{
				whitePoints += ((Piece)(board[r][c])).getValue();
			}
		}
		else if(((Piece)(board[r][c])).getColor().equals(Piece.Color.BLACK))
		{
			if(whitePoints > blackPoints)
			{
				whitePoints += ((Piece)(board[r][c])).getValue();
			}
			else if((whitePoints < blackPoints) && (whitePoints + ((Piece)(board[r][c])).getValue() > blackPoints))
			{
				blackPoints = 0;
				whitePoints += ((Piece)(board[r][c])).getValue();
			}
			else if((whitePoints < blackPoints) && (whitePoints + ((Piece)(board[r][c])).getValue() < blackPoints))
			{
				blackPoints -= ((Piece)(board[r][c])).getValue();
			}
			else if((whitePoints < blackPoints) && (whitePoints + ((Piece)(board[r][c])).getValue() == blackPoints))
			{
				blackPoints = 0;
				whitePoints = 0;
			}
		}
		else if(((Piece)(board[r][c])).getColor().equals(Piece.Color.WHITE))
		{
			if(blackPoints > whitePoints)
			{
				blackPoints += ((Piece)(board[r][c])).getValue();
			}
			else if((blackPoints < whitePoints) && (blackPoints + ((Piece)(board[r][c])).getValue() > whitePoints))
			{
				whitePoints = 0;
				blackPoints += ((Piece)(board[r][c])).getValue();
			}
			else if((blackPoints < whitePoints) && (blackPoints + ((Piece)(board[r][c])).getValue() < whitePoints))
			{
				whitePoints -= ((Piece)(board[r][c])).getValue();
			}
			else if((blackPoints < whitePoints) && (blackPoints + ((Piece)(board[r][c])).getValue() == whitePoints))
			{
				whitePoints = 0;
				blackPoints = 0;
			}
		}

	}
	/**
	 * Checks to see if the move is valid
	 * @param x The row value of the piece
	 * @param y	The column value of the piece
	 * @param r The proposed row value of the move
	 * @param c The proposed column value of the move
	 * @return Whether or not the move is valid for a pawn
	 */
	public boolean pawnMove(int x, int y, int r, int c)
	{
		if(board[r][c] != null && ((Piece)(board[r][c])).getColor() != ((Piece)(board[x][y])).getColor())
		{
			if(((Piece)(board[x][y])).getColor() == Piece.Color.WHITE)
			{
				if(r == x + 1 && (c == y + 1 || c == y - 1))
				{
					return true;
				}
			}
			if(((Piece)(board[x][y])).getColor() == Piece.Color.BLACK)
			{
				if(r == x - 1 && (c == y + 1 || c == y - 1))
				{
					return true;
				}
			}
		}
		if(board[r][c] == null)
		{
			if(((Piece)(board[x][y])).getColor() == Piece.Color.WHITE)
			{
				if(r == x + 1 && c == y)
				{
					return true;
				}
			}
			if(((Piece)(board[x][y])).getColor() == Piece.Color.BLACK)
			{
				if(r == x - 1 && c == y)
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks to see if the move is valid
	 * @param x The row value of the piece
	 * @param y	The column value of the piece
	 * @param r The proposed row value of the move
	 * @param c The proposed column value of the move
	 * @return Whether or not the move is valid for a knight
	 */
	public boolean knightMove(int x, int y, int r, int c)
	{
		if(board[r][c] != null && ((Piece)(board[r][c])).getColor() != ((Piece)(board[x][y])).getColor())
		{
			if(Math.abs(x-r) == 1 && Math.abs(y-c) == 2)
			{
				return true;
			}
			if(Math.abs(x-r) == 2 && Math.abs(y-c) == 1)
			{
				return true;
			}
		}
		if(board[r][c] == null)
		{
			if(Math.abs(x-r) == 1 && Math.abs(y-c) == 2)
			{
				return true;
			}
			if(Math.abs(x-r) == 2 && Math.abs(y-c) == 1)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 *
	 * @param x The row value of the piece
	 * @param y	The column value of the piece
	 * @param r The proposed row value of the move
	 * @param c The proposed column value of the move
	 * @return Whether or not the move is valid for a bishop
	 */
	public boolean bishopMove(int x, int y, int r, int c)
	{
		if(board[r][c] != null && ((Piece)(board[r][c])).getColor() != ((Piece)(board[x][y])).getColor())
		{
			if(Math.abs(y-c) == Math.abs(x-r))
			{
				return true;
			}
		}
		if(board[r][c] == null)
		{
			if(Math.abs(y-c) == Math.abs(x-r))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * Checks to see if the move is valid
	 * @param x The row value of the piece
	 * @param y	The column value of the piece
	 * @param r The proposed row value of the move
	 * @param c The proposed column value of the move
	 * @return Whether or not the move is valid for a rook
	 */
	public boolean rookMove(int x, int y, int r, int c)
	{
		if(board[r][c] != null && ((Piece)(board[r][c])).getColor() != ((Piece)(board[x][y])).getColor())
		{
			if(r == x || c == y)
			{
				return true;
			}
		}
		if(board[r][c] == null)
		{
			if(r == x || c == y)
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * Checks to see if the move is valid
	 * @param x The row value of the piece
	 * @param y	The column value of the piece
	 * @param r The proposed row value of the move
	 * @param c The proposed column value of the move
	 * @return Whether or not the move is valid for a queen
	 */
	public boolean queenMove(int x, int y, int r, int c)
	{
		if(bishopMove(x,y,r,c) || rookMove(x,y,r,c))
		{
			return true;
		}
		return false;
	}
	/**
	 * Checks to see if the move is valid
	 * @param x The row value of the piece
	 * @param y	The column value of the piece
	 * @param r The proposed row value of the move
	 * @param c The proposed column value of the move
	 * @return Whether or not the move is valid for a queen
	 */
	public boolean kingMove(int x, int y, int r, int c)
	{
		if(board[r][c] != null && ((Piece)(board[r][c])).getColor() != ((Piece)(board[x][y])).getColor())
		{
			if(Math.abs(r - x) == 1 && Math.abs(c - y) == 1)
			{
				return true;
			}
			else if(r == x && Math.abs(c-y) == 1)
			{
				return true;
			}
			else if(c == y && Math.abs(r-x) == 1)
			{
				return true;
			}
		}
		if(board[r][c] == null)
		{
			if(Math.abs(r - x) == 1 && Math.abs(c - y) == 1)
			{
				return true;
			}
			else if(r == x && Math.abs(c-y) == 1)
			{
				return true;
			}
			else if(c == y && Math.abs(r-x) == 1)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks to see if the move is valid
	 * @param x The row value of the piece
	 * @param y	The column value of the piece
	 * @param r The proposed row value of the move
	 * @param c The proposed column value of the move
	 * @return Whether or not the move puts the king in check
	 */
	private boolean putsKingInCheck(int x, int y, int r, int c) {
		Piece.Color pieceColor = ((board[x][y])).getColor();
		Piece temp1 = board[x][y];
		Piece temp2 = board[r][c];
		board[r][c] = board[x][y];
		board[x][y] = null;
		@SuppressWarnings("unused")
		boolean returner;
		if(pieceColor == Piece.Color.WHITE)
		{
			for(int row = 0; r < board.length; row++)
			{
				for(int column = 0; c < board[0].length; column++)
				{
					if(board[row][column] != null && board[row][column].getType() == Piece.Type.KING && board[row][column].getColor() == Piece.Color.WHITE)
					{
						board[x][y] = temp1;
						board[r][c] = temp2;
						returner = isCheck(row,column);
					}
				}
			}
		}
		else
		{
			for(int row = board.length; r > 0 ; row--)
			{
				for(int column = board[0].length; c > 0; column--)
				{
					if(board[row][column] != null && board[row][column].getType() == Piece.Type.KING && board[row][column].getColor() == Piece.Color.BLACK)
					{
						board[x][y] = temp1;
						board[r][c] = temp2;
						returner = isCheck(row,column);
					}
				}
			}
		}
		return false;
	}
	/**
	 * Checks whether or not the king is in check.
	 * @return whether or not the king at the specified location is in check
	 */
	private boolean isCheck(int x, int y)
	{
		for(int r = 0; r < board.length; r++)
		{
			for(int c = 0; c < board[0].length; c++)
			{
				if(board[r][c] != null && board[r][c].getColor() != board[x][y].getColor())
				{
					if(isValidMove(r,c,x,y))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
}
