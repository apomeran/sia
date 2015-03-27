package model;

public class E2GlobalState {

	public static int SIZE;
	public static int NUM_COLORS;
	
	// The Up, Right color pair lookup table. The short is formated to save the TileId in the first 8 bits and the rotation on the last 4 bits.
	public static short[][][] LOOK_UP_TABLE;
	public static Tile[] TILES;
	
	public static void LoadTiles(Tile[] tiles, int size, int numColors) {
		SIZE = size;
		NUM_COLORS = numColors;
		LOOK_UP_TABLE = new short[NUM_COLORS][NUM_COLORS][32];
		TILES = tiles;
		
		short[][] lookUpTableCurrentSizes = new short[NUM_COLORS][NUM_COLORS]; // saves the array's current sizes to know in what position to insert!
		
		for (short tileId = 0; tileId < tiles.length ; tileId++) {
			for (short i=0; i<TileRotation.values().length ;i++) {
				Tile tile = tiles[tileId];
				int tileConfig = tile.rotations[i];
				short upColor = (short)((tileConfig & 0xFF000000) >> 24), leftColor = (short)(tileConfig & 0x000000FF);
				
				// the number of elements already added to the array for this color pair
				short curArrSize = lookUpTableCurrentSizes[upColor][leftColor];
				lookUpTableCurrentSizes[upColor][leftColor] = (short) (curArrSize +1);
				
				// 8 bits tileID, 4 bits unused, 4 bits rotation (total 16 bits short)
				LOOK_UP_TABLE[upColor][leftColor][curArrSize] = (short) (((tileId << 8) & 0xFF00) | (i & 0x000F));
				
				// This one is tricky. If the n bit of the int is on, then the array for the color pair contains a reference to this tile on the index n.
				// Since the Tile can appear more than once for a certain color pair we do a bitwise or with what we had previously.
				tile.lookUpPositions[upColor][leftColor] = tile.lookUpPositions[upColor][leftColor] | (0x00000001 << curArrSize);
			}
		}
	}
	
	
	public static short[] getTilesMatching(int up, int right, int down, int left, int[][] lookUpTableState) {
			short[] availableTiles = new short[32];
			int availableTilesQty = 0;
			
			// Look for the tiles that match [up][left]
			int availableTilesBitMask = lookUpTableState[up][left];
			for (int shifts=0; (availableTilesBitMask >>> shifts) != 0 ; shifts++) {
				if (((availableTilesBitMask >>> shifts) & 0x00000001) != 0) {
					short tileIDandRot = LOOK_UP_TABLE[up][left][shifts];
					int curTilePattern = E2GlobalState.TILES[(tileIDandRot & 0xFF00) >>> 8].rotations[tileIDandRot & 0x000F];
					
					if ( ( down == 0 || down == (curTilePattern & 0x0000FF00) ) && (right == 0 || right == (curTilePattern & 0x00FF0000) ) ) {
						availableTiles[availableTilesQty] = tileIDandRot;
						availableTilesQty++;
					}
				}
			}		
	
			return (availableTilesQty != 0)? availableTiles : null;
		}
	
}
