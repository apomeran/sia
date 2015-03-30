package edu.itba.sia.enums;

public enum Colors {

	GREY(0), WHITE(1), BLACK(2), RED(3), BLUE(4), ORANGE(5), PURPLE(6), YELLOW(
			7), BROWN(8), GREEN(9), GOLD(10), CYAN(11), MAGENTA(12), PINK(13), SILVER(
			14), CRYSTAL(15), RUBY(16);

	byte b;

	private Colors(int b) {
		this.b = (byte) b;
	}

	public byte getColor() {
		return b;
	}

}