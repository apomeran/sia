package edu.itba.sia;

import java.util.Random;

public class BoardBuilder {

    public static Tile[][] buildBoard(int size) {
        if (!((size & (size - 1)) == 0)) {
            throw new IllegalArgumentException("The size is not a power of 2");
        }
        Tile[][] board = new Tile[size][size];

        board[0][0] = new Tile(0, randomColor(size), randomColor(size), 0);
        for (int i = 1; i < size-1; i++) {
            board[0][i] = new Tile(board[0][i-1].downColor(), randomColor(size), randomColor(size), 0);
        }
        board[0][size-1] = new Tile(board[0][size-2].downColor(), randomColor(size), 0, 0);
        for (int i = 1; i < size-1; i++) {
            board[i][size-1] = new Tile(randomColor(size), randomColor(size), 0, board[i-1][size-1].rightColor());
        }
        board[size-1][size-1] = new Tile(randomColor(size), 0, 0, board[size-2][size-1].rightColor());
        for (int i = size-2; i > 0; i--) {
            board[size-1][i] = new Tile(randomColor(size), 0, board[size-1][i+1].upColor(), randomColor(size));
        }
        board[size-1][0] = new Tile(0, randomColor(size), board[size-1][1].upColor(), 0);
        for (int i = size-2; i > 1; i--) {
            board[i][0] = new Tile(0, board[i+1][0].leftColor(), randomColor(size), randomColor(size));
        }
        board[1][0] = new Tile(0, board[2][0].leftColor(), randomColor(size), board[0][0].rightColor());

        for (int i = 1; i < size-2; i++) {
            for (int j = 1; j < size-2; i++) {
                board[i][j] = new Tile(board[i][j-1].downColor(), randomColor(size), randomColor(size), board[i-1][j].rightColor());
            }
            board[i][size-2] = new Tile(board[i][size-3].downColor(), randomColor(size), board[i][size-1].upColor(), board[i-1][size-2].rightColor());
        }
        for (int i = 1; i < size-2; i++) {
            board[size-2][i] = new Tile(board[size-2][i-1].downColor(), board[size-1][i].leftColor(), randomColor(size), board[size-3][i].rightColor());
        }
        board[size-2][size-2] = new Tile(board[size-2][size-3].downColor(), board[size-1][size-2].leftColor(), board[size-2][size-1].upColor(), board[size-3][size-2].rightColor());

        return board;
    }

    private static int randomColor(int size) {
        switch (size) {
            case 2:
                return new Random().nextInt(2);
            case 4:
                return new Random().nextInt(4);
            case 8:
                return new Random().nextInt(11);
            default:
                return new Random().nextInt(22);
        }
    }
}
