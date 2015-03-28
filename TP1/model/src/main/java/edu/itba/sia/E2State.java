package edu.itba.sia;

import edu.itba.sia.API.GPSState;

public class E2State implements GPSState{
	
	public int[][] board;
	public int[][] lookUpTableState;
	
	public E2State() {
		board = new int[E2GlobalState.SIZE][E2GlobalState.SIZE];
		lookUpTableState = new int[E2GlobalState.NUM_COLORS][E2GlobalState.NUM_COLORS];
	}
	
	public void addTile(Tile tile, int rot, int i, int j) {
		// TODO: implement...
		/*
			1) add tile with rot in board[i][j]
			2) remove tile from lookUpTableState using tile.lookUpPositions
		 */
	}
	
	@Override
	public boolean compare (GPSState state) {
		
		if (state.getClass() != this.getClass()) {
			return false;
		} else {
			E2State e2State = (E2State) state;
			if (!sameMatrix(this.board, e2State.board))
				return false;
			if (!sameMatrix(this.lookUpTableState, e2State.lookUpTableState))
				return false;
			if (!equivalentMatrix(this.lookUpTableState, e2State.lookUpTableState))
				return false;
		}
		return true;
	}
	
	private boolean sameMatrix (int[][] mat1, int[][]mat2) {
		if (mat1.length != mat2.length || mat1[0].length != mat2[0].length)
			return false;
		
		for (int i=0; i<mat1.length ;i++)
			for (int j=0; j<mat1.length ;j++)
				if (mat1[i][j] != mat2[i][j])
					return false;
		return true;
	}
	
	private boolean equivalentMatrix (int[][] mat1, int[][]mat2) {
		// TODO: (OPTIMIZATION) check mirrored cases...
		return false;
	}
	
	// ------- HEURISTICS ---------------------------------------------------------
	
	// check if any of the missing tiles in the board has a pattern that no remaining tile can match
	public int firstHeuristic() {
		
	}
	
	// a board is as good as remaining sides + remaining colors
	public int secondHeuristic() {
		
	}

}
