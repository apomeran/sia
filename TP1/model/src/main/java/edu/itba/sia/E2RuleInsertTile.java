package edu.itba.sia;

import edu.itba.sia.API.GPSRule;
import edu.itba.sia.API.GPSState;
import edu.itba.sia.exceptions.NotAppliableException;

public class E2RuleInsertTile implements GPSRule {

	// TODO: colors removed heuristic...
	private int colorsRemoved = 0;
	
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
		E2State e2State = (E2State) state;
		int[][] board = e2State.board;
		
		int maxDefined = 0, curPattern = 0, lastI = 0, lastJ = 0;
		for (int i=0; i<board.length ;i++) {
			for (int j=0; j<board.length ;j++) {
				curPattern = calculatePatternFor(i, j, e2State.board);
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
		int pattern = calculatePatternFor(i, j, state.board);
		int up= pattern & 0xFF000000, right= pattern & 0x00FF0000, down= pattern & 0x0000FF00, left= pattern & 0x000000FF;
		
		short[] matchingTiles = E2GlobalState.getTilesMatching(up, right, down, left, state.lookUpTableState);
		
		if (matchingTiles == null) { // No tiles match the search criteria...
			throw new NotAppliableException();
		} else {
			short selectedTile = matchingTiles[0];
			Tile tile = E2GlobalState.TILES[(selectedTile & 0xFF00) >>> 8];
			state.addTile(tile, i, j, (selectedTile & 0x000F));
		}		
	}
	
	private int calculatePatternFor(int i, int j, int[][] board) {
		int up, right, down, left;
		up    =                   (i-1 < 0)? 0 : ((board[i-1][j] & 0x0000FF00) <<  16);
		right = (j+1 == E2GlobalState.SIZE)? 0 : ((board[i][j+1] & 0x000000FF) <<  16);
		down  = (i+1 == E2GlobalState.SIZE)? 0 : ((board[i+1][j] & 0xFF000000) >>> 16);
		left  =                   (j-1 < 0)? 0 : ((board[i][j-1] & 0x00FF0000) >>> 16);
		return (up | right | down | left);
	}

}
