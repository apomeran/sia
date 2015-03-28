package edu.itba.sia.rules;

import java.util.LinkedList;

import edu.itba.sia.E2State;
import edu.itba.sia.Tile;
import edu.itba.sia.API.GPSRule;
import edu.itba.sia.API.GPSState;
import edu.itba.sia.enums.TileRotation;
import edu.itba.sia.exceptions.NotAppliableException;

public class InsertTile implements GPSRule {

	Tile tile;
	TileRotation rotation;
	int row;
	int col;

	public InsertTile(Tile tile, TileRotation rotation, int row, int col) {
		this.tile = tile;
		this.rotation = rotation;
		this.row = row;
		this.col = col;
	}

	@Override
	public Integer getCost() {
		return 1;
	}

	@Override
	public String getName() {
		return "Classic Insert Tile";
	}

	@Override
	public GPSState evalRule(GPSState state) throws NotAppliableException {
		E2State e = (E2State) state;

		if (!e.getRemainingTiles().contains(tile)) {
			// do not add to open if insertTile is already in the board.
			return null;
		}
		LinkedList<Tile> remTiles = new LinkedList<Tile>();
		for (Tile t : e.getRemainingTiles())
			remTiles.add(new Tile(t.upColor(), t.rightColor(), t.downColor(), t
					.leftColor()));
		E2State result = new E2State(e.getBoard(), e.lookUpTableState, remTiles);

		tile.setCurrentRotation(rotation);

		if (result.insertTile(tile, row, col)) {
			result.getRemainingTiles().remove(tile);
			return result;
		}

		return null;
	}

}
