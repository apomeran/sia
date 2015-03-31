package edu.itba.sia.rules;

import java.util.LinkedList;

import edu.itba.sia.solver.GlobalState;
import edu.itba.sia.solver.State;
import edu.itba.sia.API.GPSRule;
import edu.itba.sia.API.GPSState;
import edu.itba.sia.model.Direction;
import edu.itba.sia.exceptions.NotAppliableException;
import edu.itba.sia.model.Board;
import edu.itba.sia.model.Tile;

public class InsertTile implements GPSRule {
	Tile tile;
	int rotateTimes;
	int row;
	int col;
	int wallColor;
	int dimension = GlobalState.SIZE;

	public InsertTile(Tile tile, int rotateTimes, int row, int col,
			int wallcolor) {
		this.tile = tile;
		this.rotateTimes = rotateTimes;
		this.row = row;
		this.col = col;
		this.wallColor = wallcolor;
	}

	public Tile getInsertTile() {
		return tile;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	@Override
	public Integer getCost() {
		return 1;
	}

	@Override
	public String getName() {
		return "Insert Tile";
	}

	@Override
	public GPSState evalRule(GPSState state) throws NotAppliableException {
		State e = (State) state;

		if (!e.getRemainingTiles().contains(tile)) {
			// do not add to open if tile is already in the board.
			return null;
		}

		LinkedList<Tile> remTiles = new LinkedList<Tile>();
		for (Tile t : e.getRemainingTiles()) {
			remTiles.add(new Tile(t.getCompressedColors()));
		}
		State result = new State(new Board(e.getBoard()), remTiles, null,
				wallColor);

		tile.rotate(rotateTimes);
		if (result.getBoard().insert(tile, row, col)) {
			if (!isOk(result)) {
				return null;
			}
			result.getRemainingTiles().remove(tile);
			return result;
		}

		return null;
	}

	public boolean isOk(State e) {
		dimension = e.getBoard().getSize();
		int depth = dimension % 2 == 0 ? dimension / 2 : dimension / 2 + 1;
		int totalTiles = dimension * dimension;
		int hValue = e.getEstimatedWeight();
		boolean nullDetected = false;
		int consecutive = 0;

		if (e.getBoard().getTiles()[0][0] != null) {
			Tile t = e.getBoard().getTiles()[0][0];
			if (t.getColor(Direction.NORTH) != wallColor
					|| t.getColor(Direction.WEST) != wallColor) {
				return false;
			}
		}
		for (int i = 1; i < dimension - 1; i++) {
			if (e.getBoard().getTiles()[0][i] != null) {
				Tile t = e.getBoard().getTiles()[0][i];
				if (t.getColor(Direction.NORTH) != wallColor) {
					return false;
				}
			}
		}
		for (int i = 1; i < dimension - 1; i++) {
			if (e.getBoard().getTiles()[dimension - 1][i] != null) {
				Tile t = e.getBoard().getTiles()[dimension - 1][i];
				if (t.getColor(Direction.SOUTH) != wallColor) {
					return false;
				}
			}
		}

		if (e.getBoard().getTiles()[0][dimension - 1] != null) {
			Tile t = e.getBoard().getTiles()[0][dimension - 1];
			if (t.getColor(Direction.NORTH) != wallColor
					|| t.getColor(Direction.EAST) != wallColor) {
				return false;
			}
		}
		if (e.getBoard().getTiles()[dimension - 1][0] != null) {
			Tile t = e.getBoard().getTiles()[dimension - 1][0];
			if (t.getColor(Direction.SOUTH) != wallColor
					|| t.getColor(Direction.WEST) != wallColor) {
				return false;
			}
		}
		for (int i = 1; i < dimension - 1; i++) {
			if (e.getBoard().getTiles()[i][0] != null) {
				Tile t = e.getBoard().getTiles()[i][0];
				if (t.getColor(Direction.WEST) != wallColor) {
					return false;
				}
			}
		}
		for (int i = 1; i < dimension - 1; i++) {
			if (e.getBoard().getTiles()[i][dimension - 1] != null) {
				Tile t = e.getBoard().getTiles()[i][dimension - 1];
				if (t.getColor(Direction.EAST) != wallColor) {
					return false;
				}
			}
		}

		if (e.getBoard().getTiles()[dimension - 1][dimension - 1] != null) {
			Tile t = e.getBoard().getTiles()[dimension - 1][dimension - 1];
			if (t.getColor(Direction.SOUTH) != wallColor
					|| t.getColor(Direction.EAST) != wallColor) {
				return false;
			}
		}

		return true;

	}

}
