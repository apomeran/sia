package edu.itba.sia.solver;

import java.util.List;

import edu.itba.sia.API.GPSProblem;
import edu.itba.sia.classes.GPSEngine;
import edu.itba.sia.classes.GPSNode;
import edu.itba.sia.classes.SearchStrategy;

public class E2Engine extends GPSEngine {
	final static int initialDepth = 1;

	private int MAXIDFSDEPTH;

	public E2Engine(SearchStrategy strategy) {
		engine(new E2Problem(), strategy);
		MAXIDFSDEPTH = initialDepth;
	}

	@Override
	protected boolean explode(GPSNode node) {

		if (getStrategy().equals(SearchStrategy.IDFS)) {
			if (node.getDepth() >= MAXIDFSDEPTH) {
				getOpen().clear();
				getClosed().clear();
				getOpen().add(new GPSNode(getProblem().getInitState(), 0, 0));
				MAXIDFSDEPTH += 1;
				System.out.println("Current depth: " + MAXIDFSDEPTH);
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
			Integer hValue = getProblem().getHValue(node.getState());
			Integer hCost = node.getCost();
			Integer totalValue = hValue + hCost;
			int i = 0;
			while (i < getOpen().size()
					&& totalValue > problem.getHValue(getOpen().get(i)
							.getState()) + getOpen().get(i).getCost()) {
				i++;
			}
			getOpen().add(i, node);
			break;
		case Greedy:
			Integer hValueGreedy = getProblem().getHValue(node.getState());
			int a = open.size();
			int factor = 100;
			for (int counter = 0; counter < a / factor; counter++) {
				open.remove(open.size() - 1);
			}
			open.add(0, node);
			break;
		case IDFS:
			open.add(0, node);
		default:
			break;
		}
	}
}
