package edu.itba.sia.model;
import edu.itba.sia.enums.Direction;


public class Board {

	int dimension;

	Tile[][] tiles;

	public Board(int dimension) {
		this.dimension = dimension;
		tiles = new Tile[dimension][dimension];
	}

	public Board(Board board) {
		this.dimension = board.getTiles().length;
		tiles = new Tile[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				tiles[i][j] = board.getTiles()[i][j];
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Board otherBoard = (Board) obj;
		boolean matched = false;
		for (int i = 0; i < 4 && !matched; i++) {
			if (tiles[0][0] == otherBoard.getTiles()[0][0]) {
				if (compareBoard(otherBoard)) {
					return true;
				}
			}
			rotate(1);
		}

		return false;
	}

	public boolean compareBoard(Board otherBoard) {
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (tiles[i][j] != otherBoard.getTiles()[i][j])
					return false;
			}
		}
		return true;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	private void rotate(int times) {
		Board b;
		while (times > 0) {
			b = new Board(dimension);
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {
					b.getTiles()[j][dimension - i - 1] = tiles[i][j];
				}
			}
			tiles = b.getTiles();
			times--;
		}
	}

	public boolean insert(Tile t, int row, int col) {
		if (tiles[row][col] != null)
			return false;
		boolean valid = true;	

		if (col + 1 < dimension) {
			valid = t.fits(tiles[row][col + 1], Direction.WEST);
		}
		if (col - 1 >= 0) {
			valid = valid && t.fits(tiles[row][col - 1], Direction.EAST);
		}
		if (row - 1 >= 0) {
			valid = valid && t.fits(tiles[row - 1][col], Direction.SOUTH);
		}
		if (row + 1 < dimension) {
			valid = valid && t.fits(tiles[row + 1][col], Direction.NORTH);
		}
		if (valid) {
			tiles[row][col] = t;
		}

		return valid;
	}

	public boolean isComplete() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				if (tiles[i][j] == null) {
					return false;
				}
			}
		}
		return true;
	}

	public int getDimension() {
		return dimension;
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				String aux = tiles[i][j] == null ? "       " : tiles[i][j]
						.toString();
				s += "[" + aux + "] ";
			}
			s += '\n';
		}
		return s;
	}

}
