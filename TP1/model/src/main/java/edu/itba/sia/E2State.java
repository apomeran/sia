package edu.itba.sia;

import edu.itba.sia.API.GPSState;

public class E2State implements GPSState {

	public int[][] board;
	public int[][] lookUpTableState;

	public E2State() {
		board = new int[E2GlobalState.SIZE][E2GlobalState.SIZE];
		lookUpTableState = new int[E2GlobalState.NUM_COLORS][E2GlobalState.NUM_COLORS];
	}

	public void addTile(Tile tile, int rot, int i, int j) {
		// TODO: implement...
		/*
		 * 1) add tile with rot in board[i][j] 2) remove tile from
		 * lookUpTableState using tile.lookUpPositions
		 */
	}

	public boolean compare(GPSState state) {

		if (state.getClass() != this.getClass()) {
			return false;
		} else {
			E2State e2State = (E2State) state;
			if (!sameMatrix(this.board, e2State.board))
				return false;
			if (!sameMatrix(this.lookUpTableState, e2State.lookUpTableState))
				return false;
			if (!equivalentMatrix(this.lookUpTableState,
					e2State.lookUpTableState))
				return false;
		}
		return true;
	}

	private boolean sameMatrix(int[][] mat1, int[][] mat2) {
		if (mat1.length != mat2.length || mat1[0].length != mat2[0].length)
			return false;

		for (int i = 0; i < mat1.length; i++)
			for (int j = 0; j < mat1.length; j++)
				if (mat1[i][j] != mat2[i][j])
					return false;
		return true;
	}

	private boolean equivalentMatrix(int[][] mat1, int[][] mat2) {
		// TODO: (OPTIMIZATION) check mirrored cases...
		return false;
	}

	public String toString() {
		String str = "{ E2State: \n";

		str += "[ Board: \n";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				int tilePattern = E2GlobalState.TILES[(board[i][j] & 0xFF00) >>> 8].rotations[board[i][j] & 0x000F];
				str += "<" + ((tilePattern & 0xFF000000) >> 24) + ","
						+ ((tilePattern & 0x00FF0000) >> 16) + ","
						+ ((tilePattern & 0x0000FF00) >> 8) + ","
						+ (tilePattern & 0x000000FF) + ">";
			}
			str += "\n";
		}
		str += "]\n";

		str += "[ LookUpTableState: \n";
		for (int i = 0; i < lookUpTableState.length; i++) {
			for (int j = 0; j < lookUpTableState.length; j++) {
				str += Integer.toBinaryString(lookUpTableState[i][j]) + " ";
			}
			str += "\n";
		}
		str += "]\n";

		str += "}";

		return str;
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

}
