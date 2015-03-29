package edu.itba.sia.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.itba.sia.BoardBuilder;
import edu.itba.sia.E2GlobalState;
import edu.itba.sia.E2State;
import edu.itba.sia.API.GPSProblem;
import edu.itba.sia.API.GPSRule;
import edu.itba.sia.API.GPSState;
import edu.itba.sia.classes.SearchStrategy;
import edu.itba.sia.enums.TileRotation;
import edu.itba.sia.model.Tile;
import edu.itba.sia.rules.InsertTile;

public class E2Problem implements GPSProblem {

	private static int dimension;
	private static int heuristic;
	private static E2State state;
	protected static SearchStrategy strategy = SearchStrategy.AStar;
	List<Tile> tileList = new LinkedList<Tile>();

	public static void main(String[] args) {

		boolean debugging = false;
		heuristic = 2;
		dimension = 4;

		if (!debugging) {
			if (args.length != 2 && args.length != 3) {
				System.out
						.println("Wrong amount of parameters. Usage: <search method[BFS|DFS|IDFS|ASTAR|GREEDY]> <#heuristic[1|2]> <dimension of the board[2|4|5|6]>");
				return;
			}
			String searchMethod = args[0];
			int dim = 0;
			int heur = 0;
			if (args.length == 2) {
				if (!searchMethod.equals("BFS") && !searchMethod.equals("DFS")
						&& !searchMethod.equals("IDFS")) {
					System.out
							.println("Wrong search method. BFS, DFS and IDFS don't use heuristics.");
					System.out
							.println("Wrong amount of parameters. Usage: <search method[BFS|DFS|IDFS|ASTAR|GREEDY]> <#heuristic[1|2]> <dimension of the board[2|4|5|6]>");
					return;
				}
				try {
					dim = Integer.valueOf(args[1]);
				} catch (NumberFormatException e) {
					System.out.println("Parameters should be numeric");
					return;
				}
				if (dim < 2 || dim > 8) {
					System.out
							.println("The board dimensions available are 2, 4, 5, 6");
					return;
				}
			} else if (args.length == 3) {

				if (!searchMethod.equals("GREEDY")
						&& !searchMethod.equals("ASTAR")) {
					System.out
							.println("Wrong search method. Only Greedy and Astar use heuristics.");
					System.out
							.println("Wrong amount of parameters. Usage: <search method[BFS|DFS|IDFS|ASTAR|GREEDY]> <#heuristic[1|2]> <dimension of the board[2|4|5|6]>");
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
					System.out
							.println("Wrong parameter: first parameter should be 1 or 2, indicating the first or second heuristic");
				}
				if (dim < 2 || dim > 8) {
					System.out
							.println("The board dimensions available are 2, 4, 5, 6");
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
		System.out
				.println("Total time: " + endTime / 1000000000.0 + " seconds");

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
		E2GlobalState.setNUM_COLOR(numColors);
		Tile[][] board = BoardBuilder.buildBoard(dimension, numColors);
		Tile[] tiles = new Tile[dimension * dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				tiles[i * dimension + j] = board[i][j];
			}
		}
		E2GlobalState.LoadTiles(tiles, dimension, numColors); // sets up the
																// static

		for (Tile tile : tiles) {
			System.out.println(tile);
			tileList.add(tile);
		}// structures...

		Collections.shuffle(tileList);
		E2State e = new E2State(
				new Tile[E2GlobalState.SIZE][E2GlobalState.SIZE],
				new int[E2GlobalState.NUM_COLORS][E2GlobalState.NUM_COLORS],
				tileList);
		return e;
	}

	@Override
	public GPSState getGoalState() {
		return new E2State(new Tile[E2GlobalState.SIZE][E2GlobalState.SIZE],
				new int[E2GlobalState.NUM_COLORS][E2GlobalState.NUM_COLORS],
				new LinkedList<Tile>()) {

			@Override
			public boolean compare(GPSState otherState) {
				return ((E2State) otherState).getRemainingTiles().isEmpty();
			}
		};
	}

	@Override
	public Integer getHValue(GPSState state) {
		E2State e = (E2State) state;
		switch (heuristic) {
		case 1:
			return e.firstHeuristic();
		case 2:
			return e.secondHeuristic();
		case 3:
			return e.thirdHeuristic();
		case 4:
			return e.fourthHeuristic();
		default:
			return e.firstHeuristic();
		}
	}

	@Override
	public List<GPSRule> getRules() {
		List<GPSRule> rules = new ArrayList<GPSRule>();
		for (Tile t : tileList) {
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {

					rules.add(new InsertTile(new Tile(t.upColor(), t
							.rightColor(), t.downColor(), t.leftColor()),
							TileRotation.REGULAR, i, j));

					rules.add(new InsertTile(new Tile(t.upColor(), t
							.rightColor(), t.downColor(), t.leftColor()),
							TileRotation.CLOCKWISE, i, j));

					rules.add(new InsertTile(new Tile(t.upColor(), t
							.rightColor(), t.downColor(), t.leftColor()),
							TileRotation.COUNTERCLOCKWISE, i, j));

					rules.add(new InsertTile(new Tile(t.upColor(), t
							.rightColor(), t.downColor(), t.leftColor()),
							TileRotation.DOUBLEROT, i, j));
				}
			}
		}

		return rules;
	}

}
