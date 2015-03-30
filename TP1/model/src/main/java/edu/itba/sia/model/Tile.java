package edu.itba.sia.model;

import edu.itba.sia.enums.Direction;
import edu.itba.sia.enums.TileType;

/*
 * For each rotation the structure is 8 bits each color (up (8bits), right (8bits), down (8bits), left (8bits) = 32 bits int total)...
 */
public class Tile {

	// north, east, south, west
		int pattern;

		public Tile(byte up, byte right, byte down, byte left) {
			pattern = (int) up;
			pattern <<= 8;
			pattern = (int) (pattern | right);
			pattern <<= 8;
			pattern = (int) (pattern | down);
			pattern <<= 8;
			pattern = (int) (pattern | left);
		}

		public Tile(int pattern) {
			this.pattern = pattern;
		}

		public void rotate(int times) {
			for (int i = 0; i < times; i++) {
				rotateToRight();
			}
		}

		public void rotateToRight() {
			int mask = 0x000000ff;
			int aux = pattern & mask;
			aux <<= 24;
			pattern >>= 8;
			pattern |= aux;
		}

		public int getPattern() {
			return pattern;
		}

		public boolean fits(Tile tile, Direction dir) {
			if (tile == null)
				return true;
			int colorAtTileInDir = tile.getColor(dir);

			switch (dir) {
			case EAST:
				if (colorAtTileInDir != getColor(Direction.WEST)) {
					return false;
				}
				break;
			case NORTH:
				if (colorAtTileInDir != getColor(Direction.SOUTH)) {
					return false;
				}
				break;
			case SOUTH:
				if (colorAtTileInDir != getColor(Direction.NORTH)) {
					return false;
				}
				break;
			case WEST:
				if (colorAtTileInDir != getColor(Direction.EAST)) {
					return false;
				}
				break;
			}
			return true;
		}

		public int getColor(Direction dir) {
			return (pattern & dir.getMask()) >> dir.getShiftBits();
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
			for (int i = 0; i < 4; i++) {
				other.rotate(i);
				if (pattern == other.pattern)
					return true;
			}
			return false;
		}

		public String toString() {
			String s = "";
			s += getColor(Direction.NORTH) + ",";
			s += getColor(Direction.EAST) + ",";
			s += getColor(Direction.SOUTH) + ",";
			s += getColor(Direction.WEST);
			return s;
		}

		public TileType getType() {
			int count = 0;
			if (getColor(Direction.NORTH) == 0)
				count++;
			if (getColor(Direction.EAST) == 0)
				count++;
			if (getColor(Direction.SOUTH) == 0)
				count++;
			if (getColor(Direction.WEST) == 0)
				count++;
			if (count == 2)
				return TileType.CORNER;
			if (count == 1)
				return TileType.WALL;
			return TileType.INNER;

		}

}
