package edu.itba.sia.model;

public enum Color
{
    WHITE(),

    BLACK(),

    YELLOW(),

    GREY(),

    ORANGE(),

    RED(),

    BLUE(),

    PINK(),

    PURPLE(),

    BROWN(),

    GREEN(),

    GOLD();


    public int getColor()
    {
        return this.ordinal();
    }

}
