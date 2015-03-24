package model;

import gps.controllers.GPSEngine;
import gps.controllers.GPSNode;
import gps.controllers.SearchStrategy;

public class E2Engine extends GPSEngine {

	public E2Engine(SearchStrategy strategy) {
		engine(new E2Problem(), strategy);
	}
	
	@Override
	public void addNode(GPSNode node) {
		switch (strategy) {
			case BFS:
				open.add(node);
				break;
			case DFS:
				open.add(0, node);
				break;
			case AStar:			
				Integer hValue = problem.getHValue(node.getState());
				Integer hCost = node.getCost();
				Integer totalValue = hValue + hCost;
				int i = 0;
				while (i < open.size() && totalValue > problem.getHValue(open.get(i).getState()) + open.get(i).getCost())
					i++;
				if (problem.getHValue(node.getState()) < 100)
					open.add(i, node);
				break;
			default:
				break;
		}
	}
	
}
