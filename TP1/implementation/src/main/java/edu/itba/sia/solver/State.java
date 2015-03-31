package edu.itba.sia.solver;

import java.util.List;

import edu.itba.sia.API.GPSState;
import edu.itba.sia.model.Direction;
import edu.itba.sia.model.Board;
import edu.itba.sia.model.Tile;

public class State implements GPSState {

	private Board board;
	private List<Tile> remainingTiles;
	private int hValue = -1;
	public int[][] lookUpTableState;
	private int wallColor;

	public List<Tile> getRemainingTiles() {
		return remainingTiles;
	}

	public State(Board board, List<Tile> remainingTiles,
			int[][] lookUpTableState, int wallcolor) {
		this.board = board;
		this.remainingTiles = remainingTiles;
		this.lookUpTableState = lookUpTableState;
		this.wallColor = wallcolor;
	}

	public Board getBoard() {
		return board;
	}

	@Override
	public boolean compare(GPSState state) {
		if (state == null) {
			return false;
		}
		State state2 = (State) state;
		return (board.isSameBoardAs(state2.getBoard()) && remainingTiles.size() == state2
				.getRemainingTiles().size());
	}

	public String toString() {
		return board.toString();
	}

	public int getEstimatedWeight() {
		int total = 0;
		for (Tile t : remainingTiles) {
			total += getWeight(t);
		}
		return total;
	}

	public Integer getWeight(Tile t) {
		return 1;
	}

	// ------- HEURISTICS
	// ---------------------------------------------------------

	public int spiralHeuristic() {
		int dimension = getBoard().getSize();
		int depth = dimension % 2 == 0 ? dimension / 2 : dimension / 2 + 1;
		int totalTiles = dimension * dimension;
		int hValue = getEstimatedWeight();
		boolean nullDetected = false;
		int consecutive = 0;
		State e = this;
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

	public int spiralHeuristic2() {
		int consecutive = 0;
		int dimension = board.getSize();
		int totalTiles = board.getSize() * board.getSize();
		int hValue = getEstimatedWeight();

		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {

				Tile currentTile = board.getTiles()[i][j];

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
					if (getRemainingTiles().size() + consecutive == totalTiles) {
						return hValue;
					} else {
						return Integer.MAX_VALUE / 2;
					}

				}
			}
		}
		return hValue;
	}

	// Open edges
	public int openEdgesHeuristic() {
		int heuristicValue = 0;
		int size = board.getSize();
		
		// Corners
		heuristicValue += (board.getTiles()[0][1] == null ? 1 : 0);
		heuristicValue += (board.getTiles()[1][0] == null ? 1 : 0);
		heuristicValue += (board.getTiles()[size - 1][1] == null ? 1 : 0);
		heuristicValue += (board.getTiles()[size - 2][0] == null ? 1 : 0);
		heuristicValue += (board.getTiles()[1][size - 1] == null ? 1 : 0);
		heuristicValue += (board.getTiles()[0][size - 2] == null ? 1 : 0);
		heuristicValue += (board.getTiles()[size - 2][size - 1] == null ? 1 : 0);
		heuristicValue += (board.getTiles()[size - 1][size - 2] == null ? 1 : 0);

		// Edges
		for (int i = 1; i < size - 1; i++) {
			heuristicValue += (board.getTiles()[0][i - 1] == null ? 1 : 0);
			heuristicValue += (board.getTiles()[0][i + 1] == null ? 1 : 0);
			heuristicValue += (board.getTiles()[1][i] == null ? 1 : 0);
		}
		for (int i = 1; i < size - 1; i++) {
			heuristicValue += (board.getTiles()[i - 1][0] == null ? 1 : 0);
			heuristicValue += (board.getTiles()[i + 1][0] == null ? 1 : 0);
			heuristicValue += (board.getTiles()[i][1] == null ? 1 : 0);
		}
		for (int i = size - 2; i > 0; i--) {
			heuristicValue += (board.getTiles()[size - 1][i - 1] == null ? 1
					: 0);
			heuristicValue += (board.getTiles()[size - 1][i + 1] == null ? 1
					: 0);
			heuristicValue += (board.getTiles()[size - 2][1] == null ? 1 : 0);
		}
		for (int i = size - 2; i > 0; i--) {
			heuristicValue += (board.getTiles()[i - 1][size - 1] == null ? 1
					: 0);
			heuristicValue += (board.getTiles()[i + 1][size - 1] == null ? 1
					: 0);
			heuristicValue += (board.getTiles()[i][size - 2] == null ? 1 : 0);
		}

		// Inner Board
		for (int i = 1; i < size - 1; i++) {
			for (int j = 1; j < size - 1; j++) {
				if (board.getTiles()[i][j] == null) {
					heuristicValue += (board.getTiles()[i - 1][j] == null ? 1
							: 0);
					heuristicValue += (board.getTiles()[i + 1][j] == null ? 1
							: 0);
					heuristicValue += (board.getTiles()[i][j - 1] == null ? 1
							: 0);
					heuristicValue += (board.getTiles()[i][j + 1] == null ? 1
							: 0);
				}
			}
		}
		return heuristicValue;
	}

	// remaining colors 
	public int combinedOpenEdgesWithColocation() {
		int[][] lookUpTableState = null;
		return GlobalState.getRemainingColors(lookUpTableState);
	}

	// Manhattan distance for every tile
	public int manhattanDistanceHeuristic() {

		int result = -1;

		double factor = 1.0;
		int complete = 0;
		int actual = 0;
		int n = board.getSize();
		int constant = n / 2;
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				int distance = Math.abs(constant - i) + Math.abs(constant - j);
				if (!(board.getTiles()[i][j] == null)) {
					actual += distance;
				}
				complete += distance;
			}
		}
		result = (complete - actual) * (n * n);
		return (int) (result * factor);
	}

	// 2 heuristics combined
	public int combinationHeuristic() {
		if (getRemainingTiles().size() > getBoard().getSize()
				* getBoard().getSize() / 2) {
			return spiralHeuristic();
		} else {
			return openEdgesHeuristic();
		}
	}
	
	// 2 heuristics combined
	public int anotherCombinationHeuristic() {
		if (getRemainingTiles().size() > getBoard().getSize()
				* getBoard().getSize() / 2) {
			return spiralHeuristic();
		} else {
			return spiralHeuristic2();
		}
	}

	public boolean hasOneWall(Tile currentTile) {
		return wallAmount(currentTile) == 1;
	}

	public boolean hasWalls(Tile currentTile) {
		return wallAmount(currentTile) != 0;
	}

	private boolean correctCorner(Tile currentTile, int i, int j) {
		int dimension = board.getSize();
		// arriba izq
		if (i == 0 && j == 0
				&& currentTile.getColor(Direction.NORTH) == wallColor
				&& currentTile.getColor(Direction.WEST) == wallColor) {
			return true;
		}
		// arriba der
		if (i == 0 && j == dimension - 1
				&& currentTile.getColor(Direction.NORTH) == wallColor
				&& currentTile.getColor(Direction.EAST) == wallColor) {
			return true;
		}
		// abajo izq
		if (i == dimension - 1 && j == 0
				&& currentTile.getColor(Direction.SOUTH) == wallColor
				&& currentTile.getColor(Direction.WEST) == wallColor) {
			return true;
		}
		// abajo der
		if (i == dimension - 1 && j == dimension - 1
				&& currentTile.getColor(Direction.SOUTH) == wallColor
				&& currentTile.getColor(Direction.EAST) == wallColor) {
			return true;
		}
		return false;
	}

	private boolean correctBorder(Tile currentTile, int i, int j) {
		int dimension = board.getSize();
		if (hasOneWall(currentTile)) {
			if (i == 0 && currentTile.getColor(Direction.NORTH) == wallColor) {
				return true;
			}
			if (i == dimension - 1
					&& currentTile.getColor(Direction.SOUTH) == wallColor) {
				return true;
			}
			if (j == 0 && currentTile.getColor(Direction.WEST) == wallColor) {
				return true;
			}
			if (j == dimension - 1
					&& currentTile.getColor(Direction.EAST) == wallColor) {
				return true;
			}
		}

		return false;
	}

	private int wallAmount(Tile currentTile) {
		int walls = 0;
		if (currentTile.getColor(Direction.NORTH) == wallColor) {
			walls++;
		}
		if (currentTile.getColor(Direction.SOUTH) == wallColor) {
			walls++;
		}
		if (currentTile.getColor(Direction.WEST) == wallColor) {
			walls++;
		}
		if (currentTile.getColor(Direction.EAST) == wallColor) {
			walls++;
		}
		return walls;
	}
}
