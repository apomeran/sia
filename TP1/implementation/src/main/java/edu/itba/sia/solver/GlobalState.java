package edu.itba.sia.solver;

import edu.itba.sia.model.Tile;

public class GlobalState
{

    public static int SIZE;
    public static int NUM_COLORS;

    // The Up, Right color pair lookup table. The short is formated to save the
    // TileId in the first 8 bits and the rotation on the last 4 bits.
    public static short[][][] LOOK_UP_TABLE;
    public static Tile[] TILES;

    public static void setNUM_COLOR(int numColors)
    {
        NUM_COLORS = numColors;
    }

    public static void LoadTiles(Tile[] tiles, int size, int numColors)
    {
        SIZE = size;
        NUM_COLORS = numColors;
        LOOK_UP_TABLE = new short[NUM_COLORS + 1][NUM_COLORS + 1][32];
        TILES = tiles;

        short[][] lookUpTableCurrentSizes = new short[NUM_COLORS + 1][NUM_COLORS + 1]; // saves
        // the
        // array's
        // current
        // sizes
        // to
        // know
        // in
        // what
        // position
        // to
        // insert!

//		for (short tileId = 0; tileId < tiles.length; tileId++) {
//			for (short i = 0; i < TileRotation.values().length; i++) {
//				Tile tile = tiles[tileId];
//				int tileConfig = tile.getRotations()[i];
//				short upColor = (short) ((tileConfig & 0xFF000000) >> 24), leftColor = (short) (tileConfig & 0x000000FF);
//
//				// the number of elements already added to the array for this
//				// color pair
//
//				short curArrSize = lookUpTableCurrentSizes[upColor][leftColor];
//				lookUpTableCurrentSizes[upColor][leftColor] = (short) (curArrSize + 1);
//
//				// 8 bits tileID, 4 bits unused, 4 bits rotation (total 16 bits
//				// short)
//				LOOK_UP_TABLE[upColor][leftColor][curArrSize] = (short) (((tileId << 8) & 0xFF00) | (i & 0x000F));
//
//				// This one is tricky. If the n bit of the int is on, then the
//				// array for the color pair contains a reference to this tile on
//				// the index n.
//				// Since the Tile can appear more than once for a certain color
//				// pair we do a bitwise or with what we had previously.
//				tile.lookUpPositions[upColor][leftColor] = tile.lookUpPositions[upColor][leftColor]
//						| (0x00000001 << curArrSize);
//			}
//		}
    }


    public static int getRemainingColors(int[][] lookUpTableState)
    {
        int sum = 0;
        for (int i = 0; i < lookUpTableState.length; i++)
        {
            for (int j = 0; j < lookUpTableState.length; j++)
            {
                if (lookUpTableState[i][j] != 0)
                {
                    sum++;
                    break;
                }
            }
        }
        return sum;
    }

    public String toString()
    {
        // String str;
        // if (LOOK_UP_TABLE == null || TILES == null) {
        // str = "GlobalState Initialization Failed!";
        // } else {
        // str = "{ GlobalState: \n";
        //
        // str += "[ LOOK_UP_TABLE: \n";
        // for (int i = 0; i < board.length; i++) {
        // for (int j = 0; j < board.length; j++) {
        // for (short tileIdAndRot : LOOK_UP_TABLE[i][j]) {
        // int tilePattern = TILES[(tileIdAndRot & 0xFF00) >>>
        // 8].rotations[tileIdAndRot & 0x000F];
        // str += "< ID:" + ((tileIdAndRot & 0xFF00) >>> 8)
        // + " ROT:" + (tileIdAndRot & 0x000F);
        // str += " TILE(" + ((tilePattern & 0xFF000000) >> 24)
        // + "," + ((tilePattern & 0x00FF0000) >> 16)
        // + "," + ((tilePattern & 0x0000FF00) >> 8) + ","
        // + (tilePattern & 0x000000FF) + ")>";
        // }
        // }
        // str += "\n";
        // }
        // str += "]\n";
        //
        // str += "[ TILES: \n";
        // for (int i = 0; i < TILES.length; i++)
        // str += "<" + i + ": " + TILES[i][j].toString() + ">";
        // str += "]\n";
        //
        // str += "}";
        // }
        // return str;
        return "NO";
    }

}
