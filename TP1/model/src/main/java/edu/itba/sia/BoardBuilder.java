package edu.itba.sia;

import java.util.Random;

import edu.itba.sia.model.Tile;

public class BoardBuilder {

	public static Tile[][] buildBoard(int size, int numColors) {
		if (!((size & (size - 1)) == 0)) {
			throw new IllegalArgumentException("The size is not a power of 2");
		}
		Tile[][] board = new Tile[size][size];

		board[0][0] = new Tile(0, randomColor(numColors),
				randomColor(numColors), 0);
		for (int i = 1; i < size - 1; i++) {
			board[0][i] = new Tile(0, randomColor(numColors),
					randomColor(numColors), board[0][i - 1].rightColor());
		}
		board[0][size - 1] = new Tile(0, 0, randomColor(numColors),
				board[0][size - 2].rightColor());
		for (int i = 1; i < size - 1; i++) {
			board[i][size - 1] = new Tile(board[i - 1][size - 1].downColor(),
					0, randomColor(numColors), randomColor(numColors));
		}
		board[size - 1][size - 1] = new Tile(
				board[size - 2][size - 1].downColor(), 0, 0,
				randomColor(numColors));
		for (int i = size - 2; i > 0; i--) {
			board[size - 1][i] = new Tile(randomColor(numColors),
					board[size - 1][i + 1].leftColor(), 0,
					randomColor(numColors));
		}
		if (size == 2) {
			board[size - 1][0] = new Tile(board[size - 2][0].downColor(),
					board[size - 1][1].leftColor(), 0, 0);
			return board; // WE ARE DONE!
		} else {
			board[size - 1][0] = new Tile(randomColor(numColors),
					board[size - 1][1].leftColor(), 0, 0);
		}

		for (int i = size - 2; i > 1; i--) {
			board[i][0] = new Tile(randomColor(numColors), randomColor(numColors),
					board[size - 1][0].upColor(), 0);
		}

		board[1][0] = new Tile(board[0][0].downColor(), randomColor(numColors),
				board[2][0].upColor(), 0);

		for (int i = 1; i < size - 2; i++) {
			for (int j = 1; j < size - 2; j++) {
				board[i][j] = new Tile(board[i-1][j].downColor(),
						randomColor(numColors), randomColor(numColors),
						board[i][j-1].rightColor());
			}
			board[i][size - 2] = new Tile(board[i-1][size - 2].downColor(),
					board[i][size - 1].leftColor(), randomColor(numColors),
					board[i][size - 3].rightColor());
		}
		for (int i = 1; i < size - 2; i++) {
			board[size - 2][i] = new Tile(board[size - 3][i].downColor(),
					randomColor(numColors), board[size-1][i].upColor(),
					board[size - 2][i-1].rightColor());
		}
		board[size - 2][size - 2] = new Tile(
				board[size - 3][size - 2].downColor(),
				board[size - 2][size - 1].leftColor(),
				board[size - 1][size - 2].upColor(),
				board[size - 2][size - 3].rightColor());

		return board;
	}

	private static int randomColor(int numColors) {
		return new Random().nextInt(numColors) + 1;
	}
}
