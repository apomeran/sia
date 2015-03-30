package edu.itba.sia.enums;

public enum Direction {
	NORTH(0XFF000000, 24), EAST(0X00FF0000, 16), SOUTH(0X0000FF00, 8), WEST(0X000000FF, 0);
	
	private int mask;
	private int decBits;
	
	private Direction(int mask, int decBits) {
		this.mask = mask;
		this.decBits = decBits;
	}
	
	public int getShiftBits() {
		return decBits;
	}

	public int getMask() {
		return mask;
	}
}
