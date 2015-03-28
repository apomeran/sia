package edu.itba.sia;

import java.util.List;

import edu.itba.sia.API.GPSProblem;
import edu.itba.sia.API.GPSRule;
import edu.itba.sia.API.GPSState;
import edu.itba.sia.classes.SearchStrategy;

public class E2Problem implements GPSProblem
{


	private static int dimension;
	private static int heuristic;
	protected static SearchStrategy strategy = SearchStrategy.AStar;

	public static void main(String[] args) {

		boolean debugging = false;
		heuristic = 2;
		dimension = 4;

		if (!debugging) {
			if (args.length != 2 && args.length != 3) {
				System.out.println("Wrong amount of parameters. Usage: <search method[BFS|DFS|IDFS|ASTAR|GREEDY]> <#heuristic[1|2]> <dimension of the board[2|4|5|6]>");
				return;
			}
			String searchMethod = args[0];
			int dim = 0;
			int heur = 0;
			if (args.length == 2) {
				if (!searchMethod.equals("BFS") && !searchMethod.equals("DFS") && !searchMethod.equals("IDFS")) {
					System.out.println("Wrong search method. BFS, DFS and IDFS don't use heuristics.");
					System.out.println("Wrong amount of parameters. Usage: <search method[BFS|DFS|IDFS|ASTAR|GREEDY]> <#heuristic[1|2]> <dimension of the board[2|4|5|6]>");
					return;
				}
				try {
					dim = Integer.valueOf(args[1]);
				} catch (NumberFormatException e) {
					System.out.println("Parameters should be numeric");
					return;
				}
				if (dim < 2 || dim > 6) {
					System.out.println("The board dimensions available are 2, 4, 5, 6");
					return;
				}
			} else if (args.length == 3) {

				if (!searchMethod.equals("GREEDY") && !searchMethod.equals("ASTAR")) {
					System.out.println("Wrong search method. Only Greedy and Astar use heuristics.");
					System.out.println("Wrong amount of parameters. Usage: <search method[BFS|DFS|IDFS|ASTAR|GREEDY]> <#heuristic[1|2]> <dimension of the board[2|4|5|6]>");
					return;
				}
				try {
					heur = Integer.valueOf(args[1]);
					dim = Integer.valueOf(args[2]);
				} catch (NumberFormatException e) {
					System.out.println("Parameters should be numeric");
					return;
				}
				if (heur != 1 && heur != 2) {
					System.out.println("Wrong parameter: first parameter should be 1 or 2, indicating the first or second heuristic");
				}
				if (dim < 2 || dim > 6) {
					System.out.println("The board dimensions available are 2, 4, 5, 6");
					return;
				}
			}

			dimension = dim;
			heuristic = heur;

			if (searchMethod.equals("BFS"))
				strategy = SearchStrategy.BFS;
			if (searchMethod.equals("DFS"))
				strategy = SearchStrategy.DFS;
			if (searchMethod.equals("ASTAR"))
				strategy = SearchStrategy.AStar;
			if (searchMethod.equals("IDFS"))
				strategy = SearchStrategy.IDFS;
			if (searchMethod.equals("GREEDY"))
				strategy = SearchStrategy.Greedy;

		}
		long startTime = System.nanoTime();
		new E2Engine(strategy);
		long endTime = System.nanoTime() - startTime;
		System.out.println("Total time: " + endTime / 1000000000.0 + " seconds");

	}
	
	
	@Override
	public GPSState getInitState() {
		int numColors;
		switch (dimension) {
            case 2:
                numColors = 3;
                break;
            case 4:
                numColors = 5;
                break;
            case 8:
                numColors = 11;
                break;
            default:
                numColors = 22;
                break;
		}
        Tile[][] board = BoardBuilder.buildBoard(dimension, numColors);
        Tile[] tiles = new Tile[dimension*dimension];
        for (int i = 0; i < dimension; i ++) {
            for (int j = 0; j < dimension; j++) {
                tiles[i*dimension+j] = board[i][j];
            }
        }
		E2GlobalState.LoadTiles(tiles, dimension, numColors);	// sets up the static structures...
		return new E2State();
	}

	@Override
	public GPSState getGoalState() {
		return new GPSState(){

			@Override
			public boolean compare(GPSState state) {
				if (state == null)
					return false;
				E2State e2State = (E2State) state;
				int[][] board = e2State.board;
				for (int i=0; i<board.length ;i++)
					for (int j=0; j<board.length ;j++)
						if (board[i][j] == 0)
							return false;
				return true;
			}
		};
	}

	@Override
	public List<GPSRule> getRules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getHValue(GPSState state) {
		// TODO Auto-generated method stub
		return 0;
	}

}
