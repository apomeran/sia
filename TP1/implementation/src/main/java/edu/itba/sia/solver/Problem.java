package edu.itba.sia.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.itba.sia.API.GPSProblem;
import edu.itba.sia.API.GPSRule;
import edu.itba.sia.API.GPSState;
import edu.itba.sia.classes.SearchStrategy;
import edu.itba.sia.model.Color;
import edu.itba.sia.model.Direction;
import edu.itba.sia.model.Board;
import edu.itba.sia.model.Tile;
import edu.itba.sia.rules.InsertTile;

public class Problem implements GPSProblem
{

    private static int size;
    private static int heuristic;
    protected static SearchStrategy strategy = SearchStrategy.AStar;
    int wallColor;

    List<Tile> tiles;

    public static void main(String[] args)
    {

        boolean debugging = false;
        heuristic = 2;
        size = 4;

        if (!debugging)
        {
            if (args.length != 2 && args.length != 3)
            {
                System.out.println("Wrong amount of parameters. Usage: <search method[BFS|DFS|IDFS|ASTAR|GREEDY]> <#heuristic[1|2|3|4|5]> <size of the board[2|4|5|6]>");
                return;
            }
            String searchMethod = args[0];
            int size = 0;
            int heuristic = 0;
            if (args.length == 2)
            {
                if (!searchMethod.equals("BFS") && !searchMethod.equals("DFS") && !searchMethod.equals("IDFS"))
                {
                    System.out.println("Wrong search method. BFS, DFS and IDFS don't use heuristics.");
                    System.out.println("Wrong amount of parameters. Usage: <search method[BFS|DFS|IDFS|ASTAR|GREEDY]> <#heuristic[1|2|3|4|5]> <size of the board[2|4|5|6]>");
                    return;
                }
                try
                {
                    size = Integer.valueOf(args[1]);
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Parameters should be numeric");
                    return;
                }
                if (size < 2 || size > 7)
                {
                    System.out.println("The board dimensions available are 2, 4, 5, 6,7");
                    return;
                }
            }
            else if (args.length == 3)
            {

                if (!searchMethod.equals("GREEDY") && !searchMethod.equals("ASTAR"))
                {
                    System.out.println("Wrong search method. Only Greedy and Astar use heuristics.");
                    System.out.println("Wrong amount of parameters. Usage: <search method[BFS|DFS|IDFS|ASTAR|GREEDY]> <#heuristic[1|2|3|4|5]> <size of the board[2|4|5|6]>");
                    return;
                }
                try
                {
                    heuristic = Integer.valueOf(args[1]);
                    size = Integer.valueOf(args[2]);
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Parameters should be numeric");
                    return;
                }
                if (heuristic != 1 && heuristic != 2 && heuristic != 3 && heuristic != 4 && heuristic != 5)
                {
                    System.out.println("Wrong parameter: first parameter should be 1, 2, 3, 4 or 5, indicating the number of heuristic selected");
                }
                if (size < 2 || size > 7)
                {
                    System.out.println("The board dimensions available are 2, 4, 5, 6,7");
                    return;
                }
            }

            Problem.size = size;
            Problem.heuristic = heuristic;

            if (searchMethod.equals("BFS"))
            {
                strategy = SearchStrategy.BFS;
            }
            if (searchMethod.equals("DFS"))
            {
                strategy = SearchStrategy.DFS;
            }
            if (searchMethod.equals("ASTAR"))
            {
                strategy = SearchStrategy.AStar;
            }
            if (searchMethod.equals("IDFS"))
            {
                strategy = SearchStrategy.IDFS;
            }
            if (searchMethod.equals("GREEDY"))
            {
                strategy = SearchStrategy.Greedy;
            }

        }
        long startTime = System.nanoTime();
        new Engine(strategy);
        long endTime = System.nanoTime() - startTime;
        System.out.println("Total time: " + endTime / 1000000000.0 + " seconds");
    }

    @Override
    public GPSState getInitState()
    {
        switch (size)
        {
            case 2:
                size2Board();
                break;
            case 4:
                size4Board();
                break;
            case 5:
                size5Board();
                break;
            case 6:
                size6Board();
                break;
            case 7:
                size7Board();
                break;
            default:
                size4Board();
        }
        wallColor = getWallsColor(tiles);
        shuffle(tiles);
        return new State(new Board(size), tiles, null, wallColor);
    }



    public void shuffle(List<Tile> t)
    {
        Collections.shuffle(t);
    }

    public void rotate(List<Tile> tiles)
    {
        for (Tile t : tiles)
        {
            t.rotate((int) (Math.random() * 4));
        }
    }

    @Override
    public GPSState getGoalState()
    {
        return new GoalState(new Board(size), new LinkedList<Tile>(),
                null, wallColor);
    }

    @Override
    public List<GPSRule> getRules()
    {
        List<GPSRule> rules = new ArrayList<GPSRule>();

        for (Tile t : tiles)
        {
            for (int i = 0; i < size; i++)
            {
                for (int j = 0; j < size; j++)
                {
                    for (int r = 0; r < 4; r++)
                    {
                        rules.add(new InsertTile(new Tile(t.getCompressedColors()), r, i, j, wallColor));
                    }
                }
            }
        }

        return rules;
    }

    @Override
    public Integer getHValue(GPSState state)
    {
        State e = (State) state;
        switch (heuristic)
        {
            case 1:
                return e.spiralHeuristic();
            case 2:
                return e.openEdgesHeuristic();
            case 3:
                return e.anotherCombinationHeuristic();
            case 4:
                return e.manhattanDistanceHeuristic();
            case 5:
                return e.combinationHeuristic();
            default:
                return e.spiralHeuristic();
        }
    }

    private int getWallsColor(List<Tile> tileList2)
    {
        Map<Integer, Integer> colors = new HashMap<Integer, Integer>();

        for (Tile tile : tileList2)
        {

            Integer northColor = (tile.getCompressedColors() & Direction.NORTH.getBitMask()) >> Direction.NORTH.getBitsToShift();
            Integer eastColor = (tile.getCompressedColors() & Direction.EAST.getBitMask()) >> Direction.EAST.getBitsToShift();
            Integer southColor = (tile.getCompressedColors() & Direction.SOUTH.getBitMask()) >> Direction.SOUTH.getBitsToShift();
            Integer westColor = (tile.getCompressedColors() & Direction.WEST.getBitMask()) >> Direction.WEST.getBitsToShift();

            colors.put(northColor, (colors.get(northColor) == null) ? 1 : colors.get(northColor) + 1);
            colors.put(eastColor, (colors.get(eastColor) == null) ? 1 : colors.get(eastColor) + 1);
            colors.put(southColor, (colors.get(southColor) == null) ? 1 : colors.get(southColor) + 1);
            colors.put(westColor, (colors.get(westColor) == null) ? 1 : colors.get(westColor) + 1);
        }
        for (Integer color : colors.keySet())
        {
            if (colors.get(color) == size * 4)
            {
                return color;
            }
        }
        return -1;
    }

    // --- MODEL BOARDS ---//

    public void size2Board()
    {
        size = 2;
        LinkedList<Tile> t = new LinkedList<Tile>();
        t.add(new Tile(Color.GREY.getColor(), Color.BROWN.getColor(),
                Color.BLUE.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.GREY.getColor(),
                Color.BLUE.getColor(), Color.BROWN.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.BROWN.getColor(),
                Color.GREY.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.GREY.getColor(),
                Color.GREY.getColor(), Color.BROWN.getColor()));
        this.tiles = t;
    }

    public void size4Board()
    {
        size = 4;
        LinkedList<Tile> t = new LinkedList<Tile>();
        t.add(new Tile(Color.GREY.getColor(), Color.BLUE.getColor(),
                Color.RED.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.RED.getColor(),
                Color.GREEN.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.BLUE.getColor(),
                Color.GREEN.getColor(), Color.RED.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.GREY.getColor(),
                Color.BLUE.getColor(), Color.BLUE.getColor()));

        t.add(new Tile(Color.RED.getColor(), Color.GREEN.getColor(),
                Color.RED.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.GREEN.getColor(), Color.PURPLE.getColor(),
                Color.PURPLE.getColor(), Color.GREEN.getColor()));
        t.add(new Tile(Color.GREEN.getColor(), Color.GREEN.getColor(),
                Color.GREEN.getColor(), Color.PURPLE.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.GREY.getColor(),
                Color.BLUE.getColor(), Color.GREEN.getColor()));

        t.add(new Tile(Color.RED.getColor(), Color.PURPLE.getColor(),
                Color.RED.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.PURPLE.getColor(), Color.GREEN.getColor(),
                Color.PURPLE.getColor(), Color.PURPLE.getColor()));
        t.add(new Tile(Color.GREEN.getColor(), Color.PURPLE.getColor(),
                Color.PURPLE.getColor(), Color.GREEN.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.GREY.getColor(),
                Color.RED.getColor(), Color.PURPLE.getColor()));

        t.add(new Tile(Color.RED.getColor(), Color.BLUE.getColor(),
                Color.GREY.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.PURPLE.getColor(), Color.BLUE.getColor(),
                Color.GREY.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.PURPLE.getColor(), Color.RED.getColor(),
                Color.GREY.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.RED.getColor(), Color.GREY.getColor(),
                Color.GREY.getColor(), Color.RED.getColor()));
        this.tiles = t;
    }

    public void size5Board()
    {
        size = 5;
        // WHITE(1), BLACK(2), RED(3), BLUE(4), ORANGE(5)
        LinkedList<Tile> t = new LinkedList<Tile>();

        t.add(new Tile(Color.GREY.getColor(), Color.BLACK.getColor(),
                Color.BLUE.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.RED.getColor(),
                Color.RED.getColor(), Color.BLACK.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.ORANGE.getColor(),
                Color.BLACK.getColor(), Color.RED.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.BLACK.getColor(),
                Color.ORANGE.getColor(), Color.ORANGE.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.GREY.getColor(),
                Color.WHITE.getColor(), Color.BLACK.getColor()));

        t.add(new Tile(Color.BLUE.getColor(), Color.BLUE.getColor(),
                Color.RED.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.RED.getColor(), Color.ORANGE.getColor(),
                Color.BLUE.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.BLACK.getColor(), Color.WHITE.getColor(),
                Color.WHITE.getColor(), Color.ORANGE.getColor()));
        t.add(new Tile(Color.ORANGE.getColor(), Color.ORANGE.getColor(),
                Color.RED.getColor(), Color.WHITE.getColor()));
        t.add(new Tile(Color.WHITE.getColor(), Color.GREY.getColor(),
                Color.BLACK.getColor(), Color.ORANGE.getColor()));

        t.add(new Tile(Color.RED.getColor(), Color.BLACK.getColor(),
                Color.WHITE.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.WHITE.getColor(),
                Color.BLUE.getColor(), Color.BLACK.getColor()));
        t.add(new Tile(Color.WHITE.getColor(), Color.WHITE.getColor(),
                Color.WHITE.getColor(), Color.WHITE.getColor()));
        t.add(new Tile(Color.RED.getColor(), Color.BLUE.getColor(),
                Color.ORANGE.getColor(), Color.WHITE.getColor()));
        t.add(new Tile(Color.BLACK.getColor(), Color.GREY.getColor(),
                Color.WHITE.getColor(), Color.BLUE.getColor()));

        t.add(new Tile(Color.WHITE.getColor(), Color.ORANGE.getColor(),
                Color.ORANGE.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.BLACK.getColor(),
                Color.ORANGE.getColor(), Color.ORANGE.getColor()));
        t.add(new Tile(Color.WHITE.getColor(), Color.ORANGE.getColor(),
                Color.RED.getColor(), Color.BLACK.getColor()));
        t.add(new Tile(Color.ORANGE.getColor(), Color.RED.getColor(),
                Color.WHITE.getColor(), Color.ORANGE.getColor()));
        t.add(new Tile(Color.WHITE.getColor(), Color.GREY.getColor(),
                Color.BLUE.getColor(), Color.RED.getColor()));

        t.add(new Tile(Color.ORANGE.getColor(), Color.BLUE.getColor(),
                Color.GREY.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.ORANGE.getColor(), Color.ORANGE.getColor(),
                Color.GREY.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.RED.getColor(), Color.RED.getColor(),
                Color.GREY.getColor(), Color.ORANGE.getColor()));
        t.add(new Tile(Color.WHITE.getColor(), Color.BLUE.getColor(),
                Color.GREY.getColor(), Color.RED.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.GREY.getColor(),
                Color.GREY.getColor(), Color.BLUE.getColor()));

        this.tiles = t;
    }

    public void size6Board()
    {
        size = 6;
        // WHITE(1), BLACK(2), RED(3), BLUE(4), ORANGE(5), PURPLE(6)
        LinkedList<Tile> t = new LinkedList<Tile>();
        t.add(new Tile(Color.GREY.getColor(), Color.BLUE.getColor(),
                Color.ORANGE.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.RED.getColor(),
                Color.PURPLE.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.PURPLE.getColor(),
                Color.WHITE.getColor(), Color.RED.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.WHITE.getColor(),
                Color.BLACK.getColor(), Color.PURPLE.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.ORANGE.getColor(),
                Color.BLUE.getColor(), Color.WHITE.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.GREY.getColor(),
                Color.BLUE.getColor(), Color.ORANGE.getColor()));

        t.add(new Tile(Color.ORANGE.getColor(), Color.BLACK.getColor(),
                Color.BLUE.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.PURPLE.getColor(), Color.BLUE.getColor(),
                Color.WHITE.getColor(), Color.BLACK.getColor()));
        t.add(new Tile(Color.WHITE.getColor(), Color.BLUE.getColor(),
                Color.PURPLE.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.BLACK.getColor(), Color.ORANGE.getColor(),
                Color.BLUE.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.PURPLE.getColor(),
                Color.RED.getColor(), Color.ORANGE.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.GREY.getColor(),
                Color.WHITE.getColor(), Color.PURPLE.getColor()));

        t.add(new Tile(Color.BLUE.getColor(), Color.BLUE.getColor(),
                Color.PURPLE.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.WHITE.getColor(), Color.RED.getColor(),
                Color.PURPLE.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.PURPLE.getColor(), Color.WHITE.getColor(),
                Color.BLUE.getColor(), Color.RED.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.WHITE.getColor(),
                Color.BLUE.getColor(), Color.WHITE.getColor()));
        t.add(new Tile(Color.RED.getColor(), Color.ORANGE.getColor(),
                Color.ORANGE.getColor(), Color.WHITE.getColor()));
        t.add(new Tile(Color.WHITE.getColor(), Color.GREY.getColor(),
                Color.RED.getColor(), Color.ORANGE.getColor()));

        t.add(new Tile(Color.PURPLE.getColor(), Color.RED.getColor(),
                Color.ORANGE.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.PURPLE.getColor(), Color.ORANGE.getColor(),
                Color.WHITE.getColor(), Color.RED.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.RED.getColor(),
                Color.BLACK.getColor(), Color.ORANGE.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.PURPLE.getColor(),
                Color.PURPLE.getColor(), Color.RED.getColor()));
        t.add(new Tile(Color.ORANGE.getColor(), Color.BLUE.getColor(),
                Color.RED.getColor(), Color.PURPLE.getColor()));
        t.add(new Tile(Color.RED.getColor(), Color.GREY.getColor(),
                Color.BLACK.getColor(), Color.BLUE.getColor()));

        t.add(new Tile(Color.ORANGE.getColor(), Color.PURPLE.getColor(),
                Color.BLACK.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.WHITE.getColor(), Color.BLUE.getColor(),
                Color.ORANGE.getColor(), Color.PURPLE.getColor()));
        t.add(new Tile(Color.BLACK.getColor(), Color.BLUE.getColor(),
                Color.RED.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.PURPLE.getColor(), Color.WHITE.getColor(),
                Color.WHITE.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.RED.getColor(), Color.ORANGE.getColor(),
                Color.BLACK.getColor(), Color.WHITE.getColor()));
        t.add(new Tile(Color.BLACK.getColor(), Color.GREY.getColor(),
                Color.ORANGE.getColor(), Color.ORANGE.getColor()));

        t.add(new Tile(Color.BLACK.getColor(), Color.BLUE.getColor(),
                Color.GREY.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.ORANGE.getColor(), Color.WHITE.getColor(),
                Color.GREY.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.RED.getColor(), Color.BLUE.getColor(),
                Color.GREY.getColor(), Color.WHITE.getColor()));
        t.add(new Tile(Color.WHITE.getColor(), Color.PURPLE.getColor(),
                Color.GREY.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.BLACK.getColor(), Color.WHITE.getColor(),
                Color.GREY.getColor(), Color.PURPLE.getColor()));
        t.add(new Tile(Color.ORANGE.getColor(), Color.GREY.getColor(),
                Color.GREY.getColor(), Color.WHITE.getColor()));
        this.tiles = t;
    }

    public void size7Board()
    {
        size = 7;
        LinkedList<Tile> t = new LinkedList<Tile>();
        t.add(new Tile(Color.GREY.getColor(), Color.GOLD.getColor(),
                Color.ORANGE.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.GOLD.getColor(),
                Color.PURPLE.getColor(), Color.GOLD.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.ORANGE.getColor(),
                Color.YELLOW.getColor(), Color.GOLD.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.PINK.getColor(),
                Color.BLUE.getColor(), Color.ORANGE.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.YELLOW.getColor(),
                Color.YELLOW.getColor(), Color.PINK.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.BLUE.getColor(),
                Color.GOLD.getColor(), Color.YELLOW.getColor()));
        t.add(new Tile(Color.GREY.getColor(), Color.GREY.getColor(),
                Color.ORANGE.getColor(), Color.BLUE.getColor()));

        t.add(new Tile(Color.ORANGE.getColor(), Color.BLUE.getColor(),
                Color.YELLOW.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.PURPLE.getColor(), Color.GOLD.getColor(),
                Color.GOLD.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.YELLOW.getColor(), Color.YELLOW.getColor(),
                Color.GREEN.getColor(), Color.GOLD.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.ORANGE.getColor(),
                Color.PINK.getColor(), Color.YELLOW.getColor()));
        t.add(new Tile(Color.YELLOW.getColor(), Color.ORANGE.getColor(),
                Color.ORANGE.getColor(), Color.ORANGE.getColor()));
        t.add(new Tile(Color.GOLD.getColor(), Color.GREEN.getColor(),
                Color.PURPLE.getColor(), Color.ORANGE.getColor()));
        t.add(new Tile(Color.ORANGE.getColor(), Color.GREY.getColor(),
                Color.PINK.getColor(), Color.GREEN.getColor()));

        t.add(new Tile(Color.YELLOW.getColor(), Color.GREEN.getColor(),
                Color.GOLD.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.GOLD.getColor(), Color.YELLOW.getColor(),
                Color.GREEN.getColor(), Color.GREEN.getColor()));
        t.add(new Tile(Color.GREEN.getColor(), Color.GREEN.getColor(),
                Color.GOLD.getColor(), Color.YELLOW.getColor()));
        t.add(new Tile(Color.PINK.getColor(), Color.PINK.getColor(),
                Color.PINK.getColor(), Color.GREEN.getColor()));
        t.add(new Tile(Color.ORANGE.getColor(), Color.ORANGE.getColor(),
                Color.PINK.getColor(), Color.PINK.getColor()));
        t.add(new Tile(Color.PURPLE.getColor(), Color.BLUE.getColor(),
                Color.YELLOW.getColor(), Color.ORANGE.getColor()));
        t.add(new Tile(Color.PINK.getColor(), Color.GREY.getColor(),
                Color.PINK.getColor(), Color.BLUE.getColor()));

        t.add(new Tile(Color.GOLD.getColor(), Color.YELLOW.getColor(),
                Color.BLUE.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.GREEN.getColor(), Color.BLUE.getColor(),
                Color.BLUE.getColor(), Color.YELLOW.getColor()));
        t.add(new Tile(Color.GOLD.getColor(), Color.BLUE.getColor(),
                Color.YELLOW.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.PINK.getColor(), Color.PURPLE.getColor(),
                Color.BLUE.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.PINK.getColor(), Color.GREEN.getColor(),
                Color.PURPLE.getColor(), Color.PURPLE.getColor()));
        t.add(new Tile(Color.YELLOW.getColor(), Color.BLUE.getColor(),
                Color.PINK.getColor(), Color.GREEN.getColor()));
        t.add(new Tile(Color.PINK.getColor(), Color.GREY.getColor(),
                Color.GREEN.getColor(), Color.BLUE.getColor()));

        t.add(new Tile(Color.BLUE.getColor(), Color.PURPLE.getColor(),
                Color.YELLOW.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.PURPLE.getColor(),
                Color.ORANGE.getColor(), Color.PURPLE.getColor()));
        t.add(new Tile(Color.YELLOW.getColor(), Color.GOLD.getColor(),
                Color.ORANGE.getColor(), Color.PURPLE.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.PURPLE.getColor(),
                Color.ORANGE.getColor(), Color.GOLD.getColor()));
        t.add(new Tile(Color.PURPLE.getColor(), Color.ORANGE.getColor(),
                Color.GREEN.getColor(), Color.PURPLE.getColor()));
        t.add(new Tile(Color.PINK.getColor(), Color.GREEN.getColor(),
                Color.PINK.getColor(), Color.ORANGE.getColor()));
        t.add(new Tile(Color.GREEN.getColor(), Color.GREY.getColor(),
                Color.GOLD.getColor(), Color.GREEN.getColor()));

        t.add(new Tile(Color.YELLOW.getColor(), Color.GOLD.getColor(),
                Color.GREEN.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.ORANGE.getColor(), Color.PURPLE.getColor(),
                Color.BLUE.getColor(), Color.GOLD.getColor()));
        t.add(new Tile(Color.ORANGE.getColor(), Color.GOLD.getColor(),
                Color.GOLD.getColor(), Color.PURPLE.getColor()));
        t.add(new Tile(Color.ORANGE.getColor(), Color.BLUE.getColor(),
                Color.GOLD.getColor(), Color.GOLD.getColor()));
        t.add(new Tile(Color.GREEN.getColor(), Color.ORANGE.getColor(),
                Color.PINK.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.PINK.getColor(), Color.BLUE.getColor(),
                Color.BLUE.getColor(), Color.ORANGE.getColor()));
        t.add(new Tile(Color.GOLD.getColor(), Color.GREY.getColor(),
                Color.PINK.getColor(), Color.BLUE.getColor()));

        t.add(new Tile(Color.GREEN.getColor(), Color.ORANGE.getColor(),
                Color.GREY.getColor(), Color.GREY.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.PURPLE.getColor(),
                Color.GREY.getColor(), Color.ORANGE.getColor()));
        t.add(new Tile(Color.GOLD.getColor(), Color.GREEN.getColor(),
                Color.GREY.getColor(), Color.PURPLE.getColor()));
        t.add(new Tile(Color.GOLD.getColor(), Color.BLUE.getColor(),
                Color.GREY.getColor(), Color.GREEN.getColor()));
        t.add(new Tile(Color.PINK.getColor(), Color.ORANGE.getColor(),
                Color.GREY.getColor(), Color.BLUE.getColor()));
        t.add(new Tile(Color.BLUE.getColor(), Color.BLUE.getColor(),
                Color.GREY.getColor(), Color.ORANGE.getColor()));
        t.add(new Tile(Color.PINK.getColor(), Color.GREY.getColor(),
                Color.GREY.getColor(), Color.BLUE.getColor()));
        this.tiles = t;
    }

}
