package model;

import gps.API.GPSState;

public class E2State implements GPSState{
	
	// ----------------> Static Variables and Methods <-----------------------------------------------------------------------------------------------------
	
	public static int SIZE;
	public static int NUM_COLORS;
	
	// The Up, Right color pair lookup table. The short is formated to save the TileId in the first 8 bits and the rotation on the last 4 bits.
	public static short[][][] LOOK_UP_TABLE;
	public static Tile[] BOARD;
	
	public static void LoadTiles(Tile[] board, int size, int numColors) {
		SIZE = size;
		NUM_COLORS = numColors;
		LOOK_UP_TABLE = new short[NUM_COLORS][NUM_COLORS][32];
		E2State.BOARD = board;
		
		short[][] lookUpTableCurrentSizes = new short[NUM_COLORS][NUM_COLORS]; // saves the array's current sizes to know in what position to insert!
		
		for (short tileId = 0; tileId < board.length ; tileId++) {
			for (short i=0; i<TileRotation.values().length ;i++) {
				Tile tile = board[tileId];
				int tileConfig = tile.rotations[i];
				short upColor = (short)((tileConfig & 0xFF000000) >> 24), rightColor = (short)((tileConfig & 0x00FF0000) >> 16);
				
				// the number of elements already added to the array for this color pair
				short curArrSize = lookUpTableCurrentSizes[upColor][rightColor];
				
				// 8 bits tileID, 4 bits unused, 4 bits rotation (total 16 bits short)
				LOOK_UP_TABLE[upColor][rightColor][curArrSize] = (short) (((tileId << 8) & 0xFF00) | (i & 0x000F));
				
				// This one is tricky. If the n bit of the int is on, then the array for the color pair contains a reference to this tile on the index n.
				// Since the Tile can appear more than once for a certain color pair we do a bitwise or with what we had previously.
				tile.lookUpPositions[upColor][rightColor] = tile.lookUpPositions[upColor][rightColor] | (0x00000001 << (curArrSize-1));
			}
		}
	}
	
	
	
	// ----------------> Regular Variables and Methods <-----------------------------------------------------------------------------------------------------

	public int[][] board;
	public int[][] lookUpTableState;
	
	public E2State() {
		board = new int[SIZE][SIZE];
		lookUpTableState = new int[NUM_COLORS][NUM_COLORS];
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

}
