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

public class Problem implements GPSProblem {

	private static int dimension;
	private static int heuristic;
	protected static SearchStrategy strategy = SearchStrategy.AStar;
	int wallColor;

	List<Tile> tileList;

	public static void main(String[] args) {

		boolean debugging = false;
		heuristic = 2;
		dimension = 4;

		if (!debugging) {
			if (args.length != 2 && args.length != 3) {
				System.out
						.println("Wrong amount of parameters. Usage: <search method[BFS|DFS|IDFS|ASTAR|GREEDY]> <#heuristic[1|2|3|4|5]> <dimension of the board[2|4|5|6]>");
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
							.println("Wrong amount of parameters. Usage: <search method[BFS|DFS|IDFS|ASTAR|GREEDY]> <#heuristic[1|2|3|4|5]> <dimension of the board[2|4|5|6]>");
					return;
				}
				try {
					dim = Integer.valueOf(args[1]);
				} catch (NumberFormatException e) {
					System.out.println("Parameters should be numeric");
					return;
				}
				if (dim < 2 || dim > 6) {
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
							.println("Wrong amount of parameters. Usage: <search method[BFS|DFS|IDFS|ASTAR|GREEDY]> <#heuristic[1|2|3|4|5]> <dimension of the board[2|4|5|6]>");
					return;
				}
				try {
					heur = Integer.valueOf(args[1]);
					dim = Integer.valueOf(args[2]);
				} catch (NumberFormatException e) {
					System.out.println("Parameters should be numeric");
					return;
				}
				if (heur != 1 && heur != 2 && heur != 3 && heur != 4
						&& heur != 5) {
					System.out
							.println("Wrong parameter: first parameter should be 1, 2, 3, 4 or 5, indicating the number of heuristic selected");
				}
				if (dim < 2 || dim > 6) {
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
		new Engine(strategy);
		long endTime = System.nanoTime() - startTime;
		System.out
				.println("Total time: " + endTime / 1000000000.0 + " seconds");

	}

	@Override
	public GPSState getInitState() {
		switch (dimension) {
		case 2:
			use2By2Board();
			break;
		case 4:
			use4By4Board();
			break;
		case 5:
			use5By5Board();
			break;
		case 6:
			use6By6Board();
			break;
		case 7:
			use7By7Board();
			break;
		default:
			use4By4Board();
		}
		wallColor = getWallsColor(tileList);
		shuffle(tileList);
		return new State(new Board(dimension), tileList, null, wallColor);
	}

	private int getWallsColor(List<Tile> tileList2) {
		Map<Integer, Integer> colors = new HashMap<Integer, Integer>();

		for (Tile tile : tileList2) {

			Integer northColor = (tile.getCompressedColors() & Direction.NORTH.getBitMask()) >> Direction.NORTH
					.getBitsToShift();
			Integer eastColor = (tile.getCompressedColors() & Direction.EAST.getBitMask()) >> Direction.EAST
					.getBitsToShift();
			Integer southColor = (tile.getCompressedColors() & Direction.SOUTH.getBitMask()) >> Direction.SOUTH
					.getBitsToShift();
			Integer westColor = (tile.getCompressedColors() & Direction.WEST.getBitMask()) >> Direction.WEST
					.getBitsToShift();

			colors.put(northColor, (colors.get(northColor) == null) ? 1
					: colors.get(northColor) + 1);
			colors.put(eastColor,
					(colors.get(eastColor) == null) ? 1
							: colors.get(eastColor) + 1);
			colors.put(southColor, (colors.get(southColor) == null) ? 1
					: colors.get(southColor) + 1);
			colors.put(westColor,
					(colors.get(westColor) == null) ? 1
							: colors.get(westColor) + 1);
		}
		for (Integer color : colors.keySet()) {
			if (colors.get(color) == dimension * 4) {
				return color;
			}
		}
		return -1;
	}

	public void shuffle(List<Tile> t) {
		Collections.shuffle(t);
	}

	public void rotate(List<Tile> tiles) {
		for (Tile t : tiles)
			t.rotate((int) (Math.random() * 4));
	}

	@Override
	public GPSState getGoalState() {
		return new GoalState(new Board(dimension), new LinkedList<Tile>(),
				null, wallColor);
	}

	@Override
	public List<GPSRule> getRules() {
		List<GPSRule> rules = new ArrayList<GPSRule>();

		for (Tile t : tileList) {
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {
					for (int r = 0; r < 4; r++) {
						rules.add(new InsertTile(new Tile(t.getCompressedColors()), r,
								i, j, wallColor));
					}
				}
			}
		}

		return rules;
	}

	@Override
	public Integer getHValue(GPSState state) {
		State e = (State) state;
		switch (heuristic) {
		case 1:
			return e.spiralHeuristic();
		case 2:
			return e.openEdgesHeuristic();
		case 3:
			return e.remainingColorsHeuristic();
		case 4:
			return e.manhattanDistanceHeuristic();
		case 5:
			return e.combinationHeuristic();
		default:
			return e.spiralHeuristic();
		}
	}

	// --- MODEL BOARDS ---//

	public void use2By2Board() {
		dimension = 2;
		LinkedList<Tile> t = new LinkedList<Tile>();
		t.add(new Tile(Color.GREY.getColor(), Color.BROWN.getColor(),
				Color.BLUE.getColor(), Color.GREY.getColor()));
		t.add(new Tile(Color.GREY.getColor(), Color.GREY.getColor(),
				Color.BLUE.getColor(), Color.BROWN.getColor()));
		t.add(new Tile(Color.BLUE.getColor(), Color.BROWN.getColor(),
				Color.GREY.getColor(), Color.GREY.getColor()));
		t.add(new Tile(Color.BLUE.getColor(), Color.GREY.getColor(),
				Color.GREY.getColor(), Color.BROWN.getColor()));
		this.tileList = t;
	}

	public void use4By4Board() {
		dimension = 4;
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
		this.tileList = t;
	}

	public void use5By5Board() {
		dimension = 5;
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

		this.tileList = t;
	}

	public void use6By6Board() {
		dimension = 6;
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
		this.tileList = t;
	}

	public void use7By7Board() {
		dimension = 7;
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
		this.tileList = t;
	}

}
