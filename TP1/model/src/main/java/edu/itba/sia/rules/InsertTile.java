package edu.itba.sia.rules;

import java.util.LinkedList;

import edu.itba.sia.E2GlobalState;
import edu.itba.sia.E2State;
import edu.itba.sia.API.GPSRule;
import edu.itba.sia.API.GPSState;
import edu.itba.sia.exceptions.NotAppliableException;
import edu.itba.sia.model.Tile;

public class InsertTile implements GPSRule {

	Tile insertTile;
	int rotateTimes;
	int row;
	int col;

	public InsertTile(Tile insertTile, int rotateTimes, int row, int col) {
		this.insertTile = insertTile;
		this.rotateTimes = rotateTimes;
		this.row = row;
		this.col = col;
	}

	@Override
	public Integer getCost() {
		return 1;
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
			remTiles.add(new Tile(t.upColor(), t.rightColor(), t.downColor(), t
					.leftColor()));

		Tile[][] oldBoard = e.getBoard();
		Tile[][] newBoard = new Tile[E2GlobalState.SIZE][E2GlobalState.SIZE];

		for (int i = 0; i < E2GlobalState.SIZE; i++) {
			for (int j = 0; j < E2GlobalState.SIZE; j++) {
				newBoard[i][j] = oldBoard[i][j];
			}
		}
		E2State result = new E2State(newBoard, e.lookUpTableState, remTiles);
		insertTile.rotate(rotateTimes);

		if (result.insertTile(insertTile, row, col)) {
			result.getRemainingTiles().remove(insertTile);
			return result;
		}

		return null;
	}
}
