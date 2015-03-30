package edu.itba.sia.rules;

import java.util.LinkedList;

import edu.itba.sia.E2GlobalState;
import edu.itba.sia.E2State;
import edu.itba.sia.API.GPSRule;
import edu.itba.sia.API.GPSState;
import edu.itba.sia.enums.Direction;
import edu.itba.sia.exceptions.NotAppliableException;
import edu.itba.sia.model.Board;
import edu.itba.sia.model.Tile;

public class InsertTile implements GPSRule {
	Tile insertTile;
	int rotateTimes;
	int row;
	int col;
	int wallColor;
	int dimension = E2GlobalState.SIZE;

	public InsertTile(Tile insertTile, int rotateTimes, int row, int col) {
		this.insertTile = insertTile;
		this.rotateTimes = rotateTimes;
		this.row = row;
		this.col = col;
	}

	@Override
	public Integer getCost() {
		switch (insertTile.getType()) {
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

	@Override
	public String getName() {
		return "insert tile";
	}

	@Override
	public GPSState evalRule(GPSState state) throws NotAppliableException {
		E2State e = (E2State) state;

		if (!e.getRemainingTiles().contains(insertTile)) {
			// do not add to open if insertTile is already in the board.
			return null;
		}

		LinkedList<Tile> remTiles = new LinkedList<Tile>();
		for (Tile t : e.getRemainingTiles())
			remTiles.add(new Tile(t.getPattern()));
		E2State result = new E2State(new Board(e.getBoard()), remTiles, null);

		insertTile.rotate(rotateTimes);

		if (result.getBoard().insert(insertTile, row, col)) {
			if (!isOk(result)) {
				return null;
			}
			result.getRemainingTiles().remove(insertTile);
			return result;
		}

		return null;
	}

	public Tile getInsertTile() {
		return insertTile;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean isOk(E2State e) {
		dimension = e.getBoard().getDimension();
		int depth = dimension % 2 == 0 ? dimension / 2 : dimension / 2 + 1;
		int totalTiles = dimension * dimension;
		int hValue = e.getSupposedWeight();
		boolean nullDetected = false;
		int consecutive = 0;

		if (e.getBoard().getTiles()[0][0] != null) {
			Tile t = e.getBoard().getTiles()[0][0];
			if (t.getColor(Direction.NORTH) != wallColor
					|| t.getColor(Direction.WEST) != wallColor) {
				return false;
			}
		}
		if (e.getBoard().getTiles()[0][dimension - 1] != null) {
			Tile t = e.getBoard().getTiles()[0][dimension - 1];
			if (t.getColor(Direction.NORTH) != wallColor
					|| t.getColor(Direction.EAST) != wallColor) {
				return false;
			}
		}
		if (e.getBoard().getTiles()[dimension-1][0] != null) {
			Tile t = e.getBoard().getTiles()[dimension-1][0];
			if (t.getColor(Direction.SOUTH) != wallColor
					|| t.getColor(Direction.WEST) != wallColor) {
				return false;
			}
		}
		if (e.getBoard().getTiles()[dimension - 1][dimension - 1] != null) {
			Tile t = e.getBoard().getTiles()[dimension - 1][dimension - 1];
			if (t.getColor(Direction.SOUTH) != wallColor
					|| t.getColor(Direction.EAST) != wallColor) {
				return false;
			}
		}

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
							return false;
						}

						if (j != 0
								&& !correctBorder(
										e.getBoard().getTiles()[d][j], d, j)) // no
																				// es
																				// esquina
																				// pero
																				// es
																				// pared
							return false;
						if (j != 0 && !hasWalls(e.getBoard().getTiles()[d][j]))
							return false;
					} else { // chequear que no hayan walls en el medio
						if (hasWalls(e.getBoard().getTiles()[d][j])) {
							return false;
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
							return false;
						if (i != 0
								&& !hasWalls(e.getBoard().getTiles()[i][dimension - 1]))
							return false;
					} else { // chequear que no hayan walls en el medio
						if (hasWalls(e.getBoard().getTiles()[i][dimension - 1
								- d])) {
							return false;
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
							return false;
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
							return false;
						if (j != 0
								&& !hasWalls(e.getBoard().getTiles()[dimension - 1][dimension
										- 1 - j]))
							return false;
					} else { // chequear que no hayan walls en el medio
						if (hasWalls(e.getBoard().getTiles()[dimension - 1 - d][dimension
								- 1 - j])) {
							return false;
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
							return false;
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
							return false;
						if (i != 0
								&& !hasWalls(e.getBoard().getTiles()[dimension
										- 1 - i][0]))
							return false;
					} else { // chequear que no hayan walls en el medio
						if (hasWalls(e.getBoard().getTiles()[dimension - 1 - i][d])) {
							return false;
						}
					}
				} else {
					nullDetected = true;
				}
			}

			if (nullDetected) {
				if (e.getRemainingTiles().size() + consecutive == totalTiles)
					return true;
				return false;
			}
		}
		return true;

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
}
