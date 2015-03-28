package edu.itba.sia;


/*
 * For each rotation the structure is 8 bits each color (up (8bits), right (8bits), down (8bits), left (8bits) = 32 bits int total)...
 */
public class Tile {

	// Each tile has 4 possible configurations/rotations
	private int[] rotations = new int[TileRotation.values().length];
	private TileRotation currentRotation = TileRotation.REGULAR; /* VALUES 0 TO 3 */

	// A complete list of every appearance of this Tile in the lookup table.
	// If the n bit of the int is on, then the array for the color pair contains
	// a reference to this tile on the index n.
	public int[][] lookUpPositions = new int[E2GlobalState.NUM_COLORS + 1][E2GlobalState.NUM_COLORS + 1];

	public Tile(int completeColors) {
		init(completeColors);
	}

	public Tile(int upColor, int rightColor, int downColor, int leftColor) {
		int completeColors = ((upColor << 24) & 0xFF000000)
				| ((rightColor << 16) & 0x00FF0000)
				| ((downColor << 8) & 0x0000FF00) | ((leftColor) & 0x000000FF);
		init(completeColors);
	}

	public void rotateToRight() {
		if (currentRotation.equals(TileRotation.REGULAR))
			currentRotation = TileRotation.CLOCKWISE;
		if (currentRotation.equals(TileRotation.CLOCKWISE))
			currentRotation = TileRotation.DOUBLEROT;
		if (currentRotation.equals(TileRotation.DOUBLEROT))
			currentRotation = TileRotation.COUNTERCLOCKWISE;
		if (currentRotation.equals(TileRotation.COUNTERCLOCKWISE))
			currentRotation = TileRotation.REGULAR;
		return;
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
		// String str = "{ <TILE(up,right,down,left)>: ";
		String str = "(" + upColor() + "," + rightColor() + "," + downColor()
				+ "," + leftColor() + ") ";
		str += "}";
		return str;
	}

	public int upColor() {
		return (rotations[currentRotation.ordinal()] & 0xFF000000) >>> 24;
	}

	public int rightColor() {
		return (rotations[currentRotation.ordinal()] & 0x00FF0000) >> 16;
	}

	public int downColor() {
		return (rotations[currentRotation.ordinal()] & 0x0000FF00) >> 8;
	}

	public int leftColor() {
		return (rotations[currentRotation.ordinal()] & 0x000000FF);
	}

	public TileRotation getCurrentRotation() {
		return currentRotation;
	}

	public void setCurrentRotation(TileRotation currentRotation) {
		this.currentRotation = currentRotation;
	}

	public int[] getRotations() {
		return rotations;
	}

	public boolean validMove(Tile tile, Direction dir) {
		if (tile == null)
			return true;
		switch (dir) {
		case UP:
			if (tile.downColor() != upColor()) {
				return false;
			}
			break;
		case DOWN:
			if (tile.upColor() != downColor()) {
				return false;
			}
			break;
		case LEFT:
			if (tile.rightColor() != leftColor()) {
				return false;
			}
			break;
		case RIGHT:
			if (tile.leftColor() != rightColor()) {
				return false;
			}
			break;
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (rotations[TileRotation.REGULAR.ordinal()] == other.rotations[TileRotation.REGULAR
				.ordinal()]) {
			return true;
		}

		return false;
	}

}
