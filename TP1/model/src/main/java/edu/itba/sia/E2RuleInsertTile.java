package edu.itba.sia;

import edu.itba.sia.API.GPSRule;
import edu.itba.sia.API.GPSState;
import edu.itba.sia.exceptions.NotAppliableException;

public class E2RuleInsertTile implements GPSRule {

	// TODO: colors removed heuristic...
	private int colorsRemoved = 0;

    private short tileToInsert;
    private int i;
    private int j;

    public E2RuleInsertTile(short tile, int i, int j) {
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
        e2state.addTile(tile, i, j, (tileToInsert & 0x000F));
        return e2state;
	}
}
