package edu.itba.sia.model;

/*
 * For each rotation the structure is 8 bits each color (up (8bits), right (8bits), down (8bits), left (8bits) = 32 bits int total)...
 */
public class Tile
{
    int compressedColors;

    public Tile(int up, int right, int down, int left)
    {
        compressedColors = up;
        compressedColors <<= 8;
        compressedColors = (compressedColors | right);
        compressedColors <<= 8;
        compressedColors = (compressedColors | down);
        compressedColors <<= 8;
        compressedColors = (compressedColors | left);
    }

    public Tile(int compressedColors)
    {
        this.compressedColors = compressedColors;
    }

    public int getCompressedColors()
    {
        return compressedColors;
    }

    public int getColor(Direction dir)
    {
        return (compressedColors & dir.getBitMask()) >> dir.getBitsToShift();
    }

    public TileType getType()
    {
        int count = 0;
        if (getColor(Direction.NORTH) == 0)
        {
            count++;
        }
        if (getColor(Direction.EAST) == 0)
        {
            count++;
        }
        if (getColor(Direction.SOUTH) == 0)
        {
            count++;
        }
        if (getColor(Direction.WEST) == 0)
        {
            count++;
        }
        if (count == 2)
        {
            return TileType.CORNER;
        }
        if (count == 1)
        {
            return TileType.EDGE;
        }
        return TileType.INNER;

    }

    public void rotate(int times)
    {
        for (int i = 0; i < times; i++)
        {
            rotate();
        }
    }

    public boolean matches(Tile tile, Direction dir)
    {
        if (tile == null)
        {
            return true;
        }
        int colorToMatch = tile.getColor(dir);

        switch (dir)
        {
            case NORTH:
                if (colorToMatch != getColor(Direction.SOUTH))
                {
                    return false;
                }
                break;
            case EAST:
                if (colorToMatch != getColor(Direction.WEST))
                {
                    return false;
                }
                break;
            case SOUTH:
                if (colorToMatch != getColor(Direction.NORTH))
                {
                    return false;
                }
                break;
            case WEST:
                if (colorToMatch != getColor(Direction.EAST))
                {
                    return false;
                }
                break;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Tile other = (Tile) obj;
        for (int i = 0; i < 4; i++)
        {
            other.rotate(i);
            if (compressedColors == other.compressedColors)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(getColor(Direction.NORTH));
        s.append(",");
        s.append(getColor(Direction.EAST));
        s.append(",");
        s.append(getColor(Direction.SOUTH));
        s.append(",");
        s.append(getColor(Direction.WEST));
        return s.toString();
    }

    private void rotate()
    {
        int bitMask = 0x000000ff;
        int maskedColors = compressedColors & bitMask;
        compressedColors >>= 8;
        maskedColors <<= 24;
        compressedColors |= maskedColors;
    }
}
