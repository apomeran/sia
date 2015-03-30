package edu.itba.sia;

import java.util.List;

import edu.itba.sia.API.GPSState;
import edu.itba.sia.model.Board;
import edu.itba.sia.model.Tile;

public class E2State implements GPSState {

	private Board board;
	private List<Tile> remainingTiles;
	private int hValue = -1;
	public int[][] lookUpTableState;

	public List<Tile> getRemainingTiles() {
		return remainingTiles;
	}

	public E2State(Board board, List<Tile> remainingTiles,
			int[][] lookUpTableState) {
		this.board = board;
		this.remainingTiles = remainingTiles;
		this.lookUpTableState = lookUpTableState;
	}

	@Override
	public boolean compare(GPSState state) {
		if (state == null)
			return false;
		E2State state2 = (E2State) state;
		return (board.compareBoard(state2.getBoard()) && remainingTiles.size() == state2
				.getRemainingTiles().size());
	}

	public Board getBoard() {
		return board;
	}

	public String toString() {
		return board.toString();
	}

	public int gethValue() {
		return hValue;
	}

	public void sethValue(int hValue) {
		this.hValue = hValue;
	}

	public boolean hasNoHValue() {
		return hValue == -1;
	}

	public int getSupposedWeight() {
		int total = 0;
		for (Tile t : remainingTiles)
			total += getWeight(t);
		return total;
	}

	public Integer getWeight(Tile t) {
		switch (t.getType()) {
		case CORNER:
			return 1;
		case WALL:
			return 2;
		case INNER:
			return 3;
		default:
			return null;
		}
	}

	// ------- HEURISTICS
	// ---------------------------------------------------------

	// Check if any of the missing tiles in the board has a pattern that no
	// remaining tile can match
	public int firstHeuristic() {
		return 0;
	}

	// Open edges
	public int openEdgesHeuristic() {
		int heuristicValue = 0;
		int size = board.getDimension();

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
	public int remainingColorsHeuristic() {
		int[][] lookUpTableState = null;
		return E2GlobalState.getRemainingColors(lookUpTableState);
	}

	// Manhattan distance for every tile
	public int manhattanDistanceHeuristic() {

		int result = -1;

		double factor = 1.0;
		int complete = 0;
		int actual = 0;
		int n = board.getDimension();
		int constant = n / 2;
		for (int i = 0; i < board.getDimension(); i++) {
			for (int j = 0; j < board.getDimension(); j++) {
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

	// all 5 heuristics combined
	public int combinationHeuristic() {
		if (getRemainingTiles().size() > getBoard().getDimension()
				* getBoard().getDimension() / 2) {
			return manhattanDistanceHeuristic();
		} else {
			return openEdgesHeuristic();
		}
	}
	
	public int spiralColocationHeuristic(){
		return 0;
	}
}
