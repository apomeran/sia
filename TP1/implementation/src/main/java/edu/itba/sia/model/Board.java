package edu.itba.sia.model;


public class Board
{

    Tile[][] tiles;
    int size;

    public Board(int size)
    {
        tiles = new Tile[size][size];
        this.size = size;
    }

    public Board(Board board)
    {
        this.size = board.getSize();

        tiles = new Tile[size][];
        for (int i = 0; i < size; i++)
        {
            tiles[i] = board.getTiles()[i].clone();
        }
    }

    public Tile[][] getTiles()
    {
        return tiles;
    }

    public int getSize()
    {
        return size;
    }

    public boolean isSameBoardAs(Board otherBoard)
    {
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (tiles[i][j] != otherBoard.getTiles()[i][j])
                {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean insert(Tile t, int row, int col)
    {
        if (tiles[row][col] != null)
        {
            return false;
        }
        boolean valid = true;

        if (col + 1 < size)
        {
            valid = t.matches(tiles[row][col + 1], Direction.WEST);
        }
        else if (col - 1 >= 0)
        {
            valid = valid && t.matches(tiles[row][col - 1], Direction.EAST);
        }
        if (row - 1 >= 0)
        {
            valid = valid && t.matches(tiles[row - 1][col], Direction.SOUTH);
        }
        if (row + 1 < size)
        {
            valid = valid && t.matches(tiles[row + 1][col], Direction.NORTH);
        }
        tiles[row][col] = t;

        return true;
    }

    private void rotate()
    {
        Board b;
        b = new Board(size);
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                b.getTiles()[j][size - i - 1] = tiles[i][j];
            }
        }
        tiles = b.getTiles();
    }

    @Override
    public boolean equals(Object obj)
    {
        Board otherBoard = (Board) obj;
        for (int i = 0; i < 4; i++)
        {
            if (isSameBoardAs(otherBoard))
            {
                return true;
            }
            rotate();
        }

        return false;
    }

    @Override
    public String toString()
    {
        StringBuilder boardBuilder = new StringBuilder();
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles.length; j++)
            {
                String tile = tiles[i][j] == null ? "       " : tiles[i][j].toString();
                boardBuilder.append("[");
                boardBuilder.append(tile);
                boardBuilder.append("] ");
            }
            boardBuilder.append("\n");
        }
        return boardBuilder.toString();
    }

}
