package edu.itba.sia.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.itba.sia.E2GoalState;
import edu.itba.sia.E2State;
import edu.itba.sia.API.GPSProblem;
import edu.itba.sia.API.GPSRule;
import edu.itba.sia.API.GPSState;
import edu.itba.sia.classes.SearchStrategy;
import edu.itba.sia.enums.Colors;
import edu.itba.sia.enums.Direction;
import edu.itba.sia.model.Board;
import edu.itba.sia.model.Tile;
import edu.itba.sia.rules.InsertTile;

public class E2Problem implements GPSProblem {

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
				if (heur != 1 && heur != 2 && heur != 3 && heur != 4 && heur != 5) {
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
		new E2Engine(strategy);
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
		return new E2State(new Board(dimension), tileList, null);
	}

	private int getWallsColor(List<Tile> tileList2) {
		Map<Integer, Integer> colors = new HashMap<Integer, Integer>();

		for (Tile tile : tileList2) {

			Integer northColor = (tile.getPattern() & Direction.NORTH.getMask()) >> Direction.NORTH
					.getShiftBits();
			Integer eastColor = (tile.getPattern() & Direction.EAST.getMask()) >> Direction.EAST
					.getShiftBits();
			Integer southColor = (tile.getPattern() & Direction.SOUTH.getMask()) >> Direction.SOUTH
					.getShiftBits();
			Integer westColor = (tile.getPattern() & Direction.WEST.getMask()) >> Direction.WEST
					.getShiftBits();

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
		return new E2GoalState(new Board(dimension), new LinkedList<Tile>(),
				null);
	}

	@Override
	public List<GPSRule> getRules() {
		List<GPSRule> rules = new ArrayList<GPSRule>();

		for (Tile t : tileList) {
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {
					for (int r = 0; r < 4; r++) {
						rules.add(new InsertTile(new Tile(t.getPattern()), r,
								i, j));
					}
				}
			}
		}

		return rules;
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
		case 5:
			return e.fifthHeuristic();
		default:
			return e.firstHeuristic();
		}
	}

	// --- MODEL BOARDS ---//

	public void use2By2Board() {
		dimension = 2;
		LinkedList<Tile> t = new LinkedList<Tile>();
		t.add(new Tile(Colors.GREY.getColor(), Colors.BROWN.getColor(),
				Colors.BLUE.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.GREY.getColor(),
				Colors.BLUE.getColor(), Colors.BROWN.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.BROWN.getColor(),
				Colors.GREY.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.GREY.getColor(),
				Colors.GREY.getColor(), Colors.BROWN.getColor()));
		this.tileList = t;
	}

	public void use4By4Board() {
		dimension = 4;
		LinkedList<Tile> t = new LinkedList<Tile>();
		t.add(new Tile(Colors.GREY.getColor(), Colors.BLUE.getColor(),
				Colors.RED.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.RED.getColor(),
				Colors.GREEN.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.BLUE.getColor(),
				Colors.GREEN.getColor(), Colors.RED.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.GREY.getColor(),
				Colors.BLUE.getColor(), Colors.BLUE.getColor()));

		t.add(new Tile(Colors.RED.getColor(), Colors.GREEN.getColor(),
				Colors.RED.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.GREEN.getColor(), Colors.PURPLE.getColor(),
				Colors.PURPLE.getColor(), Colors.GREEN.getColor()));
		t.add(new Tile(Colors.GREEN.getColor(), Colors.GREEN.getColor(),
				Colors.GREEN.getColor(), Colors.PURPLE.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.GREY.getColor(),
				Colors.BLUE.getColor(), Colors.GREEN.getColor()));

		t.add(new Tile(Colors.RED.getColor(), Colors.PURPLE.getColor(),
				Colors.RED.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.PURPLE.getColor(), Colors.GREEN.getColor(),
				Colors.PURPLE.getColor(), Colors.PURPLE.getColor()));
		t.add(new Tile(Colors.GREEN.getColor(), Colors.PURPLE.getColor(),
				Colors.PURPLE.getColor(), Colors.GREEN.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.GREY.getColor(),
				Colors.RED.getColor(), Colors.PURPLE.getColor()));

		t.add(new Tile(Colors.RED.getColor(), Colors.BLUE.getColor(),
				Colors.GREY.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.PURPLE.getColor(), Colors.BLUE.getColor(),
				Colors.GREY.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.PURPLE.getColor(), Colors.RED.getColor(),
				Colors.GREY.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.RED.getColor(), Colors.GREY.getColor(),
				Colors.GREY.getColor(), Colors.RED.getColor()));
		this.tileList = t;
	}

	public void use5By5Board() {
		dimension = 5;
		// WHITE(1), BLACK(2), RED(3), BLUE(4), ORANGE(5)
		LinkedList<Tile> t = new LinkedList<Tile>();

		t.add(new Tile(Colors.GREY.getColor(), Colors.BLACK.getColor(),
				Colors.BLUE.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.RED.getColor(),
				Colors.RED.getColor(), Colors.BLACK.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.ORANGE.getColor(),
				Colors.BLACK.getColor(), Colors.RED.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.BLACK.getColor(),
				Colors.ORANGE.getColor(), Colors.ORANGE.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.GREY.getColor(),
				Colors.WHITE.getColor(), Colors.BLACK.getColor()));

		t.add(new Tile(Colors.BLUE.getColor(), Colors.BLUE.getColor(),
				Colors.RED.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.RED.getColor(), Colors.ORANGE.getColor(),
				Colors.BLUE.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.BLACK.getColor(), Colors.WHITE.getColor(),
				Colors.WHITE.getColor(), Colors.ORANGE.getColor()));
		t.add(new Tile(Colors.ORANGE.getColor(), Colors.ORANGE.getColor(),
				Colors.RED.getColor(), Colors.WHITE.getColor()));
		t.add(new Tile(Colors.WHITE.getColor(), Colors.GREY.getColor(),
				Colors.BLACK.getColor(), Colors.ORANGE.getColor()));

		t.add(new Tile(Colors.RED.getColor(), Colors.BLACK.getColor(),
				Colors.WHITE.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.WHITE.getColor(),
				Colors.BLUE.getColor(), Colors.BLACK.getColor()));
		t.add(new Tile(Colors.WHITE.getColor(), Colors.WHITE.getColor(),
				Colors.WHITE.getColor(), Colors.WHITE.getColor()));
		t.add(new Tile(Colors.RED.getColor(), Colors.BLUE.getColor(),
				Colors.ORANGE.getColor(), Colors.WHITE.getColor()));
		t.add(new Tile(Colors.BLACK.getColor(), Colors.GREY.getColor(),
				Colors.WHITE.getColor(), Colors.BLUE.getColor()));

		t.add(new Tile(Colors.WHITE.getColor(), Colors.ORANGE.getColor(),
				Colors.ORANGE.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.BLACK.getColor(),
				Colors.ORANGE.getColor(), Colors.ORANGE.getColor()));
		t.add(new Tile(Colors.WHITE.getColor(), Colors.ORANGE.getColor(),
				Colors.RED.getColor(), Colors.BLACK.getColor()));
		t.add(new Tile(Colors.ORANGE.getColor(), Colors.RED.getColor(),
				Colors.WHITE.getColor(), Colors.ORANGE.getColor()));
		t.add(new Tile(Colors.WHITE.getColor(), Colors.GREY.getColor(),
				Colors.BLUE.getColor(), Colors.RED.getColor()));

		t.add(new Tile(Colors.ORANGE.getColor(), Colors.BLUE.getColor(),
				Colors.GREY.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.ORANGE.getColor(), Colors.ORANGE.getColor(),
				Colors.GREY.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.RED.getColor(), Colors.RED.getColor(),
				Colors.GREY.getColor(), Colors.ORANGE.getColor()));
		t.add(new Tile(Colors.WHITE.getColor(), Colors.BLUE.getColor(),
				Colors.GREY.getColor(), Colors.RED.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.GREY.getColor(),
				Colors.GREY.getColor(), Colors.BLUE.getColor()));

		this.tileList = t;
	}

	public void use6By6Board() {
		dimension = 6;
		// WHITE(1), BLACK(2), RED(3), BLUE(4), ORANGE(5), PURPLE(6)
		LinkedList<Tile> t = new LinkedList<Tile>();
		t.add(new Tile(Colors.GREY.getColor(), Colors.BLUE.getColor(),
				Colors.ORANGE.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.RED.getColor(),
				Colors.PURPLE.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.PURPLE.getColor(),
				Colors.WHITE.getColor(), Colors.RED.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.WHITE.getColor(),
				Colors.BLACK.getColor(), Colors.PURPLE.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.ORANGE.getColor(),
				Colors.BLUE.getColor(), Colors.WHITE.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.GREY.getColor(),
				Colors.BLUE.getColor(), Colors.ORANGE.getColor()));

		t.add(new Tile(Colors.ORANGE.getColor(), Colors.BLACK.getColor(),
				Colors.BLUE.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.PURPLE.getColor(), Colors.BLUE.getColor(),
				Colors.WHITE.getColor(), Colors.BLACK.getColor()));
		t.add(new Tile(Colors.WHITE.getColor(), Colors.BLUE.getColor(),
				Colors.PURPLE.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.BLACK.getColor(), Colors.ORANGE.getColor(),
				Colors.BLUE.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.PURPLE.getColor(),
				Colors.RED.getColor(), Colors.ORANGE.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.GREY.getColor(),
				Colors.WHITE.getColor(), Colors.PURPLE.getColor()));

		t.add(new Tile(Colors.BLUE.getColor(), Colors.BLUE.getColor(),
				Colors.PURPLE.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.WHITE.getColor(), Colors.RED.getColor(),
				Colors.PURPLE.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.PURPLE.getColor(), Colors.WHITE.getColor(),
				Colors.BLUE.getColor(), Colors.RED.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.WHITE.getColor(),
				Colors.BLUE.getColor(), Colors.WHITE.getColor()));
		t.add(new Tile(Colors.RED.getColor(), Colors.ORANGE.getColor(),
				Colors.ORANGE.getColor(), Colors.WHITE.getColor()));
		t.add(new Tile(Colors.WHITE.getColor(), Colors.GREY.getColor(),
				Colors.RED.getColor(), Colors.ORANGE.getColor()));

		t.add(new Tile(Colors.PURPLE.getColor(), Colors.RED.getColor(),
				Colors.ORANGE.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.PURPLE.getColor(), Colors.ORANGE.getColor(),
				Colors.WHITE.getColor(), Colors.RED.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.RED.getColor(),
				Colors.BLACK.getColor(), Colors.ORANGE.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.PURPLE.getColor(),
				Colors.PURPLE.getColor(), Colors.RED.getColor()));
		t.add(new Tile(Colors.ORANGE.getColor(), Colors.BLUE.getColor(),
				Colors.RED.getColor(), Colors.PURPLE.getColor()));
		t.add(new Tile(Colors.RED.getColor(), Colors.GREY.getColor(),
				Colors.BLACK.getColor(), Colors.BLUE.getColor()));

		t.add(new Tile(Colors.ORANGE.getColor(), Colors.PURPLE.getColor(),
				Colors.BLACK.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.WHITE.getColor(), Colors.BLUE.getColor(),
				Colors.ORANGE.getColor(), Colors.PURPLE.getColor()));
		t.add(new Tile(Colors.BLACK.getColor(), Colors.BLUE.getColor(),
				Colors.RED.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.PURPLE.getColor(), Colors.WHITE.getColor(),
				Colors.WHITE.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.RED.getColor(), Colors.ORANGE.getColor(),
				Colors.BLACK.getColor(), Colors.WHITE.getColor()));
		t.add(new Tile(Colors.BLACK.getColor(), Colors.GREY.getColor(),
				Colors.ORANGE.getColor(), Colors.ORANGE.getColor()));

		t.add(new Tile(Colors.BLACK.getColor(), Colors.BLUE.getColor(),
				Colors.GREY.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.ORANGE.getColor(), Colors.WHITE.getColor(),
				Colors.GREY.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.RED.getColor(), Colors.BLUE.getColor(),
				Colors.GREY.getColor(), Colors.WHITE.getColor()));
		t.add(new Tile(Colors.WHITE.getColor(), Colors.PURPLE.getColor(),
				Colors.GREY.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.BLACK.getColor(), Colors.WHITE.getColor(),
				Colors.GREY.getColor(), Colors.PURPLE.getColor()));
		t.add(new Tile(Colors.ORANGE.getColor(), Colors.GREY.getColor(),
				Colors.GREY.getColor(), Colors.WHITE.getColor()));
		this.tileList = t;
	}

	public void use7By7Board() {
		dimension = 7;
		LinkedList<Tile> t = new LinkedList<Tile>();
		t.add(new Tile(Colors.GREY.getColor(), Colors.CYAN.getColor(),
				Colors.ORANGE.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.CYAN.getColor(),
				Colors.PURPLE.getColor(), Colors.CYAN.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.ORANGE.getColor(),
				Colors.YELLOW.getColor(), Colors.CYAN.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.PINK.getColor(),
				Colors.BLUE.getColor(), Colors.ORANGE.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.YELLOW.getColor(),
				Colors.YELLOW.getColor(), Colors.PINK.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.BLUE.getColor(),
				Colors.CYAN.getColor(), Colors.YELLOW.getColor()));
		t.add(new Tile(Colors.GREY.getColor(), Colors.GREY.getColor(),
				Colors.ORANGE.getColor(), Colors.BLUE.getColor()));

		t.add(new Tile(Colors.ORANGE.getColor(), Colors.BLUE.getColor(),
				Colors.YELLOW.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.PURPLE.getColor(), Colors.CYAN.getColor(),
				Colors.CYAN.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.YELLOW.getColor(), Colors.YELLOW.getColor(),
				Colors.GREEN.getColor(), Colors.CYAN.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.ORANGE.getColor(),
				Colors.PINK.getColor(), Colors.YELLOW.getColor()));
		t.add(new Tile(Colors.YELLOW.getColor(), Colors.ORANGE.getColor(),
				Colors.ORANGE.getColor(), Colors.ORANGE.getColor()));
		t.add(new Tile(Colors.CYAN.getColor(), Colors.GREEN.getColor(),
				Colors.PURPLE.getColor(), Colors.ORANGE.getColor()));
		t.add(new Tile(Colors.ORANGE.getColor(), Colors.GREY.getColor(),
				Colors.PINK.getColor(), Colors.GREEN.getColor()));

		t.add(new Tile(Colors.YELLOW.getColor(), Colors.GREEN.getColor(),
				Colors.CYAN.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.CYAN.getColor(), Colors.YELLOW.getColor(),
				Colors.GREEN.getColor(), Colors.GREEN.getColor()));
		t.add(new Tile(Colors.GREEN.getColor(), Colors.GREEN.getColor(),
				Colors.CYAN.getColor(), Colors.YELLOW.getColor()));
		t.add(new Tile(Colors.PINK.getColor(), Colors.PINK.getColor(),
				Colors.PINK.getColor(), Colors.GREEN.getColor()));
		t.add(new Tile(Colors.ORANGE.getColor(), Colors.ORANGE.getColor(),
				Colors.PINK.getColor(), Colors.PINK.getColor()));
		t.add(new Tile(Colors.PURPLE.getColor(), Colors.BLUE.getColor(),
				Colors.YELLOW.getColor(), Colors.ORANGE.getColor()));
		t.add(new Tile(Colors.PINK.getColor(), Colors.GREY.getColor(),
				Colors.PINK.getColor(), Colors.BLUE.getColor()));

		t.add(new Tile(Colors.CYAN.getColor(), Colors.YELLOW.getColor(),
				Colors.BLUE.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.GREEN.getColor(), Colors.BLUE.getColor(),
				Colors.BLUE.getColor(), Colors.YELLOW.getColor()));
		t.add(new Tile(Colors.CYAN.getColor(), Colors.BLUE.getColor(),
				Colors.YELLOW.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.PINK.getColor(), Colors.PURPLE.getColor(),
				Colors.BLUE.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.PINK.getColor(), Colors.GREEN.getColor(),
				Colors.PURPLE.getColor(), Colors.PURPLE.getColor()));
		t.add(new Tile(Colors.YELLOW.getColor(), Colors.BLUE.getColor(),
				Colors.PINK.getColor(), Colors.GREEN.getColor()));
		t.add(new Tile(Colors.PINK.getColor(), Colors.GREY.getColor(),
				Colors.GREEN.getColor(), Colors.BLUE.getColor()));

		t.add(new Tile(Colors.BLUE.getColor(), Colors.PURPLE.getColor(),
				Colors.YELLOW.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.PURPLE.getColor(),
				Colors.ORANGE.getColor(), Colors.PURPLE.getColor()));
		t.add(new Tile(Colors.YELLOW.getColor(), Colors.CYAN.getColor(),
				Colors.ORANGE.getColor(), Colors.PURPLE.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.PURPLE.getColor(),
				Colors.ORANGE.getColor(), Colors.CYAN.getColor()));
		t.add(new Tile(Colors.PURPLE.getColor(), Colors.ORANGE.getColor(),
				Colors.GREEN.getColor(), Colors.PURPLE.getColor()));
		t.add(new Tile(Colors.PINK.getColor(), Colors.GREEN.getColor(),
				Colors.PINK.getColor(), Colors.ORANGE.getColor()));
		t.add(new Tile(Colors.GREEN.getColor(), Colors.GREY.getColor(),
				Colors.CYAN.getColor(), Colors.GREEN.getColor()));

		t.add(new Tile(Colors.YELLOW.getColor(), Colors.CYAN.getColor(),
				Colors.GREEN.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.ORANGE.getColor(), Colors.PURPLE.getColor(),
				Colors.BLUE.getColor(), Colors.CYAN.getColor()));
		t.add(new Tile(Colors.ORANGE.getColor(), Colors.CYAN.getColor(),
				Colors.CYAN.getColor(), Colors.PURPLE.getColor()));
		t.add(new Tile(Colors.ORANGE.getColor(), Colors.BLUE.getColor(),
				Colors.CYAN.getColor(), Colors.CYAN.getColor()));
		t.add(new Tile(Colors.GREEN.getColor(), Colors.ORANGE.getColor(),
				Colors.PINK.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.PINK.getColor(), Colors.BLUE.getColor(),
				Colors.BLUE.getColor(), Colors.ORANGE.getColor()));
		t.add(new Tile(Colors.CYAN.getColor(), Colors.GREY.getColor(),
				Colors.PINK.getColor(), Colors.BLUE.getColor()));

		t.add(new Tile(Colors.GREEN.getColor(), Colors.ORANGE.getColor(),
				Colors.GREY.getColor(), Colors.GREY.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.PURPLE.getColor(),
				Colors.GREY.getColor(), Colors.ORANGE.getColor()));
		t.add(new Tile(Colors.CYAN.getColor(), Colors.GREEN.getColor(),
				Colors.GREY.getColor(), Colors.PURPLE.getColor()));
		t.add(new Tile(Colors.CYAN.getColor(), Colors.BLUE.getColor(),
				Colors.GREY.getColor(), Colors.GREEN.getColor()));
		t.add(new Tile(Colors.PINK.getColor(), Colors.ORANGE.getColor(),
				Colors.GREY.getColor(), Colors.BLUE.getColor()));
		t.add(new Tile(Colors.BLUE.getColor(), Colors.BLUE.getColor(),
				Colors.GREY.getColor(), Colors.ORANGE.getColor()));
		t.add(new Tile(Colors.PINK.getColor(), Colors.GREY.getColor(),
				Colors.GREY.getColor(), Colors.BLUE.getColor()));
		this.tileList = t;
	}

	// --- FIRST HEURISTIC ---//

	private Integer firstHeuristic(E2State e) {
		int consecutive = 0;
		int totalTiles = dimension * dimension;
		int hValue = e.getSupposedWeight();

		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {

				Tile currentTile = e.getBoard().getTiles()[i][j];

				if (currentTile != null) {
					consecutive++;
					if ((i == 0 || i == dimension - 1)
							|| (j == 0 || j == dimension - 1)) {
						if ((i == 0 || i == dimension - 1)
								&& (j == 0 || j == dimension - 1)) {
							if (!correctCorner(currentTile, i, j)) {
								return Integer.MAX_VALUE / 2;
							}
						} else if (!(correctBorder(currentTile, i, j))) {
							return Integer.MAX_VALUE / 2;
						}
						if (!hasWalls(currentTile)) {
							return Integer.MAX_VALUE / 2;
						}
					} else {
						if (hasWalls(currentTile)) {
							return Integer.MAX_VALUE / 2;
						}
					}
				} else { // it isn't consecutive
					if (e.getRemainingTiles().size() + consecutive == totalTiles)
						return hValue;
					else
						return Integer.MAX_VALUE / 2;

				}
			}
		}
		return hValue;
	}

	private boolean correctBorder(Tile currentTile, int i, int j) {
		if (hasOneWall(currentTile)) {
			if (i == 0 && currentTile.getColor(Direction.NORTH) == wallColor)
				return true;
			if (i == dimension - 1
					&& currentTile.getColor(Direction.SOUTH) == wallColor)
				return true;
			if (j == 0 && currentTile.getColor(Direction.WEST) == wallColor)
				return true;
			if (j == dimension - 1
					&& currentTile.getColor(Direction.EAST) == wallColor)
				return true;
		}

		return false;
	}

	private int wallAmount(Tile currentTile) {
		int walls = 0;
		if (currentTile.getColor(Direction.NORTH) == wallColor)
			walls++;
		if (currentTile.getColor(Direction.SOUTH) == wallColor)
			walls++;
		if (currentTile.getColor(Direction.WEST) == wallColor)
			walls++;
		if (currentTile.getColor(Direction.EAST) == wallColor)
			walls++;
		return walls;
	}

	public boolean hasOneWall(Tile currentTile) {
		return wallAmount(currentTile) == 1;
	}

	public boolean hasWalls(Tile currentTile) {
		return wallAmount(currentTile) != 0;
	}

	private boolean correctCorner(Tile currentTile, int i, int j) {
		// arriba izq
		if (i == 0 && j == 0
				&& currentTile.getColor(Direction.NORTH) == wallColor
				&& currentTile.getColor(Direction.WEST) == wallColor)
			return true;
		// arriba der
		if (i == 0 && j == dimension - 1
				&& currentTile.getColor(Direction.NORTH) == wallColor
				&& currentTile.getColor(Direction.EAST) == wallColor)
			return true;
		// abajo izq
		if (i == dimension - 1 && j == 0
				&& currentTile.getColor(Direction.SOUTH) == wallColor
				&& currentTile.getColor(Direction.WEST) == wallColor)
			return true;
		// abajo der
		if (i == dimension - 1 && j == dimension - 1
				&& currentTile.getColor(Direction.SOUTH) == wallColor
				&& currentTile.getColor(Direction.EAST) == wallColor)
			return true;
		return false;
	}

	// --- SECOND HEURISTIC ---

	private Integer secondHeuristic(E2State e) {
		int depth = dimension % 2 == 0 ? dimension / 2 : dimension / 2 + 1;
		int totalTiles = dimension * dimension;
		int hValue = e.getSupposedWeight();
		boolean nullDetected = false;
		int consecutive = 0;

		for (int d = 0; d < depth; d++) {

			// ---->
			for (int j = d; j < dimension - 1 - d && !nullDetected; j++) {
				if (e.getBoard().getTiles()[d][j] != null) {
					consecutive++;
					if (d == 0) { // estmos en el borde
						if (j == 0
								&& !correctCorner(
										e.getBoard().getTiles()[0][0], 0, 0)) { // en
																				// la
																				// esquina
																				// izq
																				// arriba
							return Integer.MAX_VALUE / 2;
						}

						if (j != 0
								&& !correctBorder(
										e.getBoard().getTiles()[d][j], d, j)) // no
																				// es
																				// esquina
																				// pero
																				// es
																				// pared
							return Integer.MAX_VALUE / 2;
						if (j != 0 && !hasWalls(e.getBoard().getTiles()[d][j]))
							return Integer.MAX_VALUE / 2;
					} else { // chequear que no hayan walls en el medio
						if (hasWalls(e.getBoard().getTiles()[d][j])) {
							return Integer.MAX_VALUE / 2;
						}
					}
				} else {
					nullDetected = true;
				}
			}
			// |
			// |
			// v
			for (int i = d; i < dimension - 1 - d && !nullDetected; i++) {
				if (e.getBoard().getTiles()[i][dimension - 1 - d] != null) {
					consecutive++;
					if (d == 0) { // estmos en el borde
						if (i == 0
								&& !correctCorner(
										e.getBoard().getTiles()[0][dimension - 1],
										0, dimension - 1)) { // en
																// la
																// esquina
																// der
																// arriba
							hValue += 100;
						}

						if (i != 0
								&& !correctBorder(
										e.getBoard().getTiles()[i][dimension - 1],
										i, dimension - 1)) // no
															// es
															// esquina
															// pero
															// es
															// pared
							return Integer.MAX_VALUE / 2;
						if (i != 0
								&& !hasWalls(e.getBoard().getTiles()[i][dimension - 1]))
							return Integer.MAX_VALUE / 2;
					} else { // chequear que no hayan walls en el medio
						if (hasWalls(e.getBoard().getTiles()[i][dimension - 1
								- d])) {
							return Integer.MAX_VALUE / 2;
						}
					}
				} else {
					nullDetected = true;
				}
			}
			// <----
			for (int j = d; j < dimension - 1 - d && !nullDetected; j++) {
				if (e.getBoard().getTiles()[dimension - 1 - d][dimension - 1
						- j] != null) {
					consecutive++;
					if (d == 0) { // estmos en el borde
						if (j == 0
								&& !correctCorner(
										e.getBoard().getTiles()[dimension - 1][dimension - 1],
										dimension - 1, dimension - 1)) { // en
																			// la
																			// esquina
																			// der
																			// arriba
							return Integer.MAX_VALUE / 2;
						}

						if (j != 0
								&& !correctBorder(
										e.getBoard().getTiles()[dimension - 1][dimension
												- 1 - j], dimension - 1,
										dimension - 1 - j)) // no
															// es
															// esquina
															// pero
															// es
															// pared
							return Integer.MAX_VALUE / 2;
						if (j != 0
								&& !hasWalls(e.getBoard().getTiles()[dimension - 1][dimension
										- 1 - j]))
							return Integer.MAX_VALUE / 2;
					} else { // chequear que no hayan walls en el medio
						if (hasWalls(e.getBoard().getTiles()[dimension - 1 - d][dimension
								- 1 - j])) {
							return Integer.MAX_VALUE / 2;
						}
					}
				} else {
					nullDetected = true;
				}
			}
			// ^
			// |
			// |
			for (int i = d; i < dimension - 1 - d && !nullDetected; i++) {
				if (e.getBoard().getTiles()[dimension - 1 - i][d] != null) {
					consecutive++;
					if (d == 0) { // estmos en el borde
						if (i == 0
								&& !correctCorner(
										e.getBoard().getTiles()[dimension - 1][0],
										dimension - 1, 0)) { // en
																// la
																// esquina
																// der
																// arriba
							return Integer.MAX_VALUE / 2;
						}

						if (i != 0
								&& !correctBorder(
										e.getBoard().getTiles()[dimension - 1
												- i][0], dimension - 1 - i, 0)) // no
																				// es
																				// esquina
																				// pero
																				// es
																				// pared
							return Integer.MAX_VALUE / 2;
						if (i != 0
								&& !hasWalls(e.getBoard().getTiles()[dimension
										- 1 - i][0]))
							return Integer.MAX_VALUE / 2;
					} else { // chequear que no hayan walls en el medio
						if (hasWalls(e.getBoard().getTiles()[dimension - 1 - i][d])) {
							return Integer.MAX_VALUE / 2;
						}
					}
				} else {
					nullDetected = true;
				}
			}

			if (nullDetected) {
				if (e.getRemainingTiles().size() + consecutive == totalTiles)
					return hValue;
				return Integer.MAX_VALUE / 2;
			}
		}

		return hValue;
	}

}
