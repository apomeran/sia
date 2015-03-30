package edu.itba.sia;

import java.util.LinkedList;
import java.util.List;

import edu.itba.sia.API.GPSState;
import edu.itba.sia.enums.Direction;
import edu.itba.sia.model.Tile;

public class E2State implements GPSState {

	public Tile[][] tileBoard;
	public int[][] board;
	public int[][] lookUpTableState;

	public List<Tile> remainingTiles = new LinkedList<Tile>();

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
		for (int i = 0; i < E2GlobalState.SIZE; i++) {
			for (int j = 0; j < E2GlobalState.SIZE; j++) {
				if (tileBoard[i][j] != otherState.tileBoard[i][j])
					return false;
			}
		}
		return (remainingTiles.size() == otherState.getRemainingTiles().size());
	}

	public String toString() {
		System.out.println("Remaining Tiles: ");
		for (Tile t : remainingTiles) {
			System.out.println(t);
		}
		System.out.println("------- BOARD ---------");
		String s = "";
		for (int i = 0; i < tileBoard.length; i++) {
			for (int j = 0; j < tileBoard.length; j++) {
				String aux = tileBoard[i][j] == null ? "       "
						: tileBoard[i][j].toString();
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

	// Open edges
	public int secondHeuristic() {
		int total = 0;
		int size = tileBoard.length;

		// Corners
		total += (tileBoard[0][1] == null ? 1 : 0);
		total += (tileBoard[1][0] == null ? 1 : 0);
		total += (tileBoard[size - 1][1] == null ? 1 : 0);
		total += (tileBoard[size - 2][0] == null ? 1 : 0);
		total += (tileBoard[1][size - 1] == null ? 1 : 0);
		total += (tileBoard[0][size - 2] == null ? 1 : 0);
		total += (tileBoard[size - 2][size - 1] == null ? 1 : 0);
		total += (tileBoard[size - 1][size - 2] == null ? 1 : 0);

		// Edges
		for (int i = 1; i < size - 1; i++) {
			total += (tileBoard[0][i - 1] == null ? 1 : 0);
			total += (tileBoard[0][i + 1] == null ? 1 : 0);
			total += (tileBoard[1][i] == null ? 1 : 0);
		}
		for (int i = 1; i < size - 1; i++) {
			total += (tileBoard[i - 1][0] == null ? 1 : 0);
			total += (tileBoard[i + 1][0] == null ? 1 : 0);
			total += (tileBoard[i][1] == null ? 1 : 0);
		}
		for (int i = size - 2; i > 0; i--) {
			total += (tileBoard[size - 1][i - 1] == null ? 1 : 0);
			total += (tileBoard[size - 1][i + 1] == null ? 1 : 0);
			total += (tileBoard[size - 2][1] == null ? 1 : 0);
		}
		for (int i = size - 2; i > 0; i--) {
			total += (tileBoard[i - 1][size - 1] == null ? 1 : 0);
			total += (tileBoard[i + 1][size - 1] == null ? 1 : 0);
			total += (tileBoard[i][size - 2] == null ? 1 : 0);
		}

		// Inner Board
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < tileBoard.length; j++) {
				if (tileBoard[i][j] == null) {
					total += (tileBoard[i - 1][j] == null ? 1 : 0);
					total += (tileBoard[i + 1][j] == null ? 1 : 0);
					total += (tileBoard[i][j - 1] == null ? 1 : 0);
					total += (tileBoard[i][j + 1] == null ? 1 : 0);
				}
			}
		}
		return size;
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
			valid = valid
					&& tile.validMove(tileBoard[row + 1][col], Direction.UP);
		}
		if (valid) {
			tileBoard[row][col] = tile;
		}

		return valid;
	}

}
