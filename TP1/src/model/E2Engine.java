package model;

import gps.API.GPSProblem;
import gps.classes.GPSEngine;
import gps.classes.GPSNode;
import gps.classes.SearchStrategy;

import java.util.List;

public class E2Engine extends GPSEngine {
	final static int initialDepth = 1;

	private int maxIDFSdepth;

	public E2Engine(SearchStrategy strategy) {
		engine(new E2Problem(), strategy);
		maxIDFSdepth = initialDepth;
	}

	@Override
	protected boolean explode(GPSNode node) {

		if (getStrategy().equals(SearchStrategy.IDFS)) {
			if (node.getDepth() >= maxIDFSdepth) {
				getOpen().clear();
				getClosed().clear();
				getOpen().add(new GPSNode(getProblem().getInitState(), 0, 0));
				maxIDFSdepth += 1;
				System.out.println("Current depth: " + maxIDFSdepth);
				return true;
			}
		}
		return super.explode(node);

	}

	@Override
	public void addNode(GPSNode node) {
		final GPSProblem problem = getProblem();
		List<GPSNode> open = getOpen();
		switch (getStrategy()) {
		case BFS:
			open.add(node);
			break;
		case DFS:
			open.add(0, node);
			break;
		case AStar:			
			Integer heuristicValue = getProblem().getHValue(node.getState());
			Integer hCost = node.getCost();
			Integer totalValue = heuristicValue + hCost;
			int i = 0;
			while (i < getOpen().size()
					&& totalValue > problem.getHValue(getOpen().get(i).getState())
							+ getOpen().get(i).getCost()) {
				i++;
			}
//			if (problem.getHValue(node.getState()) < 1000)
				getOpen().add(i, node);
			break;
		case Greedy:
			Integer greedyHeuristicValue = getProblem().getHValue(node.getState());
			int j = 0;
			while (j < getOpen().size()
					&& greedyHeuristicValue > problem.getHValue(getOpen().get(j).getState())) {
				j++;
			}
				open.add(j, node);
			break;
		case IDFS:
			open.add(0, node);
		default:
			break;

		}
	}

}
