package edu.itba.sia.model;

public enum Direction
{
    NORTH(0XFF000000, 24), EAST(0X00FF0000, 16), SOUTH(0X0000FF00, 8), WEST(0X000000FF, 0);

    private int bitMask;
    private int bitsToShift;

    private Direction(int bitMask, int bitsToShift)
    {
        this.bitMask = bitMask;
        this.bitsToShift = bitsToShift;
    }

    public int getBitsToShift()
    {
        return bitsToShift;
    }

    public int getBitMask()
    {
        return bitMask;
    }
}
