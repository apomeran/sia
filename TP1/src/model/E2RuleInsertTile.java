package model;

import gps.API.GPSRule;
import gps.API.GPSState;
import gps.exceptions.NotAppliableException;

public class E2RuleInsertTile implements GPSRule {

	// TODO: colors removed heuristic...
	private int colorsRemoved;
	
	@Override
	public Integer getCost() {
		return 1 + colorsRemoved;
	}

	@Override
	public String getName() {
		return "Insert Tile";
	}

	@Override
	public GPSState evalRule(GPSState state) throws NotAppliableException {
		E2State e2State = (E2State) state;
		int[][] board = e2State.board;
		
		int maxDefined = 0, curPattern = 0, lastI = 0, lastJ = 0;
		for (int i=0; i<board.length ;i++) {
			for (int j=0; j<board.length ;j++) {
				curPattern = calculatePatternFor(i, j);
				int curDefined = (((curPattern & 0xFF000000) != 0)? 1:0) + (((curPattern & 0x00FF0000) != 0)? 1:0) + (((curPattern & 0x0000FF00) != 0)? 1:0) + (((curPattern & 0x000000FF) != 0)? 1:0);
				if (board[i][j] == 0) {
					if (curDefined <= maxDefined)
						addTilesFor(lastI, lastJ, e2State);
					else {
						lastI = i;
						lastJ = j;
						maxDefined = curDefined;
					}
				}
			}
		}
		return null;
	}
	
	private void addTilesFor(int i, int j, E2State state) throws NotAppliableException {
		int pattern = calculatePatternFor(i, j);
		int up= pattern & 0xFF000000, right= pattern & 0x00FF0000, bot= pattern & 0x0000FF00, left= pattern & 0x000000FF;
		
		short[] matchingTiles = E2GlobalState.getTilesMatching(up, left, state.lookUpTableState);
		
		if (matchingTiles == null) { // No tiles match the search criteria...
			throw new NotAppliableException();
		} else {
			if ( bot == 0  && right == 0 ) {
				short selectedTile = matchingTiles[0];
				Tile tile = E2GlobalState.TILES[(selectedTile & 0xFF00) >>> 8];
				state.addTile(tile, i, j, (selectedTile & 0x000F));
			} else {
				// TODO: search inside the matchingTiles for one where and add it:
				// ( (bot == 0 || bot == (rotatedTile & 0x0000FF00) ) && (right == 0 || right == (rotatedTile & 0x00FF0000) ) )
				// if none exist => NotAppliableException...
			}
				

		}		
	}
	
	private int calculatePatternFor(int i, int j) {
		// TODO:
		return 0;
	}

}
