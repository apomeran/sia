package edu.itba.sia.rules;

import edu.itba.sia.E2GlobalState;
import edu.itba.sia.E2State;
import edu.itba.sia.API.GPSRule;
import edu.itba.sia.API.GPSState;
import edu.itba.sia.exceptions.NotAppliableException;
import edu.itba.sia.model.Tile;

public class LoriInsertTile implements GPSRule {

	// TODO: colors removed heuristic...
	private int colorsRemoved = 0;

    private short tileToInsert;
    private int i;
    private int j;

    public LoriInsertTile(short tile, int i, int j) {
        this.tileToInsert = tile;
        this.i = i;
        this.j = j;
    }
	
	@Override
	public Integer getCost() {
		return 1 + colorsRemoved;
	}

	@Override
	public String getName() {
		return "Insert Tile";
	}

	@Override
	public GPSState evalRule(GPSState state) throws NotAppliableException
    {
        E2State e2state = (E2State) state;
        Tile tile = E2GlobalState.TILES[(tileToInsert & 0xFF00) >>> 8];
     //   e2state.addTile(tile, i, j, (tileToInsert & 0x000F));
        return e2state;
	}
}
