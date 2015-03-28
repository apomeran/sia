package edu.itba.sia;

public class Test {

	public static void main(String[] args) {
		Tile tile = new Tile(218301697);
		System.out.println(tile.toString());
		tile = new Tile(13, 3, 5, 1);
		System.out.println(tile.toString());
	}
}
