package edu.itba.sia;

import java.util.LinkedList;
import java.util.List;

import edu.itba.sia.API.GPSState;

public class E2State implements GPSState {

	public Tile[][] tileBoard;
	public int[][] board;
	public int[][] lookUpTableState;

	public List<Tile> remainingTiles = new LinkedList<Tile>();

	public E2State() {
		tileBoard = new Tile[E2GlobalState.SIZE][E2GlobalState.SIZE];
		lookUpTableState = new int[E2GlobalState.NUM_COLORS][E2GlobalState.NUM_COLORS];
	}

	public E2State(Tile[][] board, int[][] lookUpTableState,
			List<Tile> remainingTiles) {
		this.tileBoard = board;
		this.lookUpTableState = lookUpTableState;
		this.remainingTiles = remainingTiles;

	}

	public boolean compare(GPSState state) {

		if (state == null)
			return false;
		E2State otherState = (E2State) state;
		return (tileBoard.equals(otherState.tileBoard) && remainingTiles.size() == otherState
				.getRemainingTiles().size());
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < tileBoard.length; i++) {
			for (int j = 0; j < tileBoard.length; j++) {
				String aux = tileBoard[i][j] == null ? "       " : tileBoard[i][j]
						.toString();
				s += "[" + aux + "] ";
			}
			s += '\n';
		}
		return s;
	}

	// ------- HEURISTICS
	// ---------------------------------------------------------

	// Check if any of the missing tiles in the board has a pattern that no
	// remaining tile can match
	public int firstHeuristic() {
		return 0;
	}

	// remaining tiles + open edges
	public int secondHeuristic() {
		return 0;
	}

	// remaining colors
	public int thirdHeuristic() {
		return E2GlobalState.getRemainingColors(lookUpTableState);
	}

	// all 3 heuristics combined
	public int fourthHeuristic() {
		return firstHeuristic() + secondHeuristic() + (thirdHeuristic() * 10);
	}

	public List<Tile> getRemainingTiles() {
		return remainingTiles;
	}

	public Tile[][] getBoard() {
		return tileBoard;
	}

	public int[][] getLookUpTableState() {
		return lookUpTableState;
	}

	public boolean insertTile(Tile tile, int row, int col) {
		if (tileBoard[row][col] != null)
			return false;
		boolean valid = true;

		if (col + 1 < E2GlobalState.SIZE) {
			valid = tile.validMove(tileBoard[row][col + 1], Direction.RIGHT);
		}
		if (col - 1 >= 0) {
			valid = valid
					&& tile.validMove(tileBoard[row][col - 1], Direction.LEFT);
		}
		if (row - 1 >= 0) {
			valid = valid
					&& tile.validMove(tileBoard[row - 1][col], Direction.DOWN);
		}
		if (row + 1 < E2GlobalState.SIZE) {
			valid = valid && tile.validMove(tileBoard[row + 1][col], Direction.UP);
		}
		if (valid) {
			tileBoard[row][col] = tile;
		}

		return valid;
	}

}
