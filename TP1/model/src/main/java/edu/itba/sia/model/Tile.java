package edu.itba.sia.model;

import edu.itba.sia.E2GlobalState;
import edu.itba.sia.enums.Direction;
import edu.itba.sia.enums.TileRotation;

/*
 * For each rotation the structure is 8 bits each color (up (8bits), right (8bits), down (8bits), left (8bits) = 32 bits int total)...
 */
public class Tile {

	private int upColor;
	private int downColor;
	private int leftColor;
	private int rightColor;

	// A complete list of every appearance of this Tile in the lookup table.
	// If the n bit of the int is on, then the array for the color pair contains
	// a reference to this tile on the index n.
	public int[][] lookUpPositions = new int[E2GlobalState.NUM_COLORS + 1][E2GlobalState.NUM_COLORS + 1];

	public Tile(int upColor, int rightColor, int downColor, int leftColor) {
		this.upColor = upColor;
		this.rightColor = rightColor;
		this.downColor = downColor;
		this.leftColor = leftColor;
	}

	public Tile rotateToRight() {
		int auxUp = this.upColor;
		int auxDown = this.downColor;
		int auxLeft = this.leftColor;
		int auxRight = this.rightColor;

		this.upColor = auxLeft;
		this.downColor = auxRight;
		this.leftColor = auxDown;
		this.rightColor = auxUp;

		return this;
	}

	public String toString() {
		// String str = "{ <TILE(up,right,down,left)>: ";
		String str = " (" + upColor() + "," + rightColor() + "," + downColor()
				+ "," + leftColor() + ") ";
		str += "";
		return str;
	}

	public int upColor() {
		return upColor;
	}

	public int rightColor() {
		return rightColor;
	}

	public int downColor() {
		return downColor;
	}

	public int leftColor() {
		return leftColor;
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
		return comparePattern(other);
	}

	public boolean comparePattern(Tile other) {
		return this.upColor() == other.upColor()
				&& this.downColor() == other.downColor()
				&& this.leftColor() == other.leftColor()
				&& this.rightColor() == other.rightColor();

	}

}
