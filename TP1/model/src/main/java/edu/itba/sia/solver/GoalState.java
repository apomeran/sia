package edu.itba.sia.solver;
import java.util.List;

import edu.itba.sia.API.GPSState;
import edu.itba.sia.model.Board;
import edu.itba.sia.model.Tile;

public class GoalState extends State
{

	public GoalState(Board board, List<Tile> remainingTiles,
                     int[][] lookUpTableState, int wallColor) {
		super(board, remainingTiles, lookUpTableState, wallColor);
	}

	@Override
	public boolean compare(GPSState state) {
		if (state == null)
			return false;
		State state2 = (State) state;
		return state2.getRemainingTiles().isEmpty();
	}

}
