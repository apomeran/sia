package edu.itba.sia;
import java.util.List;

import edu.itba.sia.API.GPSState;
import edu.itba.sia.model.Board;
import edu.itba.sia.model.Tile;

public class E2GoalState extends E2State {

	public E2GoalState(Board board, List<Tile> remainingTiles,
			int[][] lookUpTableState) {
		super(board, remainingTiles, lookUpTableState);
	}

	@Override
	public boolean compare(GPSState state) {
		if (state == null)
			return false;
		E2State state2 = (E2State) state;
		return state2.getRemainingTiles().isEmpty();
	}

}
