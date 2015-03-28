package edu.itba.sia;

/*
 * For each rotation the structure is 8 bits each color (up (8bits), right (8bits), down (8bits), left (8bits) = 32 bits int total)...
 */
public class Tile {

	// Each tile has 4 possible configurations/rotations
	public int[] rotations = new int[TileRotation.values().length];
	// A complete list of every appearance of this Tile in the lookup table.
	// If the n bit of the int is on, then the array for the color pair contains
	// a reference to this tile on the index n.
	public int[][] lookUpPositions = new int[E2GlobalState.NUM_COLORS][E2GlobalState.NUM_COLORS];

	public Tile(int completeColors) {
		init(completeColors);
	}

	public Tile(int upColor, int rightColor, int downColor, int leftColor) {
		int completeColors = ((upColor << 24) & 0xFF000000)
				| ((rightColor << 16) & 0x00FF0000)
				| ((downColor << 8) & 0x0000FF00) | ((leftColor) & 0x000000FF);
		init(completeColors);
	}

	private void init(int completeColors) {
		rotations[TileRotation.REGULAR.ordinal()] = (completeColors >>> 0)
				| (completeColors << 32);
		rotations[TileRotation.CLOCKWISE.ordinal()] = (completeColors >>> 8)
				| (completeColors << 24);
		rotations[TileRotation.DOUBLEROT.ordinal()] = (completeColors >>> 16)
				| (completeColors << 16);
		rotations[TileRotation.COUNTERCLOCKWISE.ordinal()] = (completeColors >>> 24)
				| (completeColors << 8);
	}

	public String toString() {
		String str = "{ <TILE(up,right,down,left)>: ";
		for (TileRotation rot : TileRotation.values()) {
			int up = (rotations[rot.ordinal()] & 0xFF000000) >>> 24, right = (rotations[rot
					.ordinal()] & 0x00FF0000) >>> 16, down = (rotations[rot
					.ordinal()] & 0x0000FF00) >>> 8, left = (rotations[rot
					.ordinal()] & 0x000000FF);
			str += rot.name() + "(" + up + "," + right + "," + down + ","
					+ left + ") ";
		}
		str += "}";
		return str;
	}

	public int upColor() {
		return (rotations[TileRotation.REGULAR.ordinal()] & 0xFF000000) >>> 24;
	}

	public int rightColor() {
		return (rotations[TileRotation.REGULAR.ordinal()] & 0x00FF0000) >> 16;
	}

	public int downColor() {
		return (rotations[TileRotation.REGULAR.ordinal()] & 0x0000FF00) >> 8;
	}

	public int leftColor() {
		return (rotations[TileRotation.REGULAR.ordinal()] & 0x000000FF);
	}
}
