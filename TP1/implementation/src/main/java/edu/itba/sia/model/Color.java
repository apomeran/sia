package edu.itba.sia.model;

public enum Color {
	GREY(), WHITE(),

	BLACK(),

	YELLOW(),

	ORANGE(),

	RED(),

	BLUE(),

	PINK(),

	PURPLE(),

	BROWN(),

	GREEN(),

	GOLD();

	public int getColor() {
		return this.ordinal();
	}

}
