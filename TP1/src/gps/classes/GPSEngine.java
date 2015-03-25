package gps.classes;

import gps.API.GPSProblem;
import gps.API.GPSRule;
import gps.API.GPSState;
import gps.exceptions.NotAppliableException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class GPSEngine {

	private List<GPSNode> open = new LinkedList<GPSNode>();

	private List<GPSNode> closed = new ArrayList<GPSNode>();

	private GPSProblem problem;

	// Use this variable in the addNode implementation
	private SearchStrategy strategy;

	public List<GPSNode> getClosed() {
		return closed;
	}

	public List<GPSNode> getOpen() {
		return open;
	}

	public GPSProblem getProblem() {
		return problem;
	}

	protected SearchStrategy getStrategy() {
		return strategy;
	}

	public void engine(GPSProblem myProblem, SearchStrategy myStrategy) {

		problem = myProblem;
		strategy = myStrategy;

		GPSNode rootNode = new GPSNode(problem.getInitState(), 0, 0);
		boolean finished = false;
		boolean failed = false;
		long explosionCounter = 0;

		open.add(rootNode);
		while (!failed && !finished) {
			if (open.size() <= 0) {
				failed = true;
			} else {
				GPSNode currentNode = open.get(0);
				System.out.println(currentNode);
				closed.add(currentNode);
				open.remove(0);
				if (isGoal(currentNode)) {
					finished = true;
					System.out.println(currentNode.getSolution());
					System.out.println("Expanded nodes: " + explosionCounter);
					System.out.println("Depth: " + currentNode.getDepth());
					System.out.println("Frontier nodes: " + open.size());
					System.out.println("Total States: " + (open.size()
							+ closed.size()));
				} else {
					explosionCounter++;
					explode(currentNode);

				}
			}
		}

		if (finished) {
			System.out.println("OK! solution found!");
		} else if (failed) {
			System.err.println("FAILED! solution not found!");
		}
	}

	private boolean isGoal(GPSNode currentNode) {
		return currentNode.getState() != null
				&& problem.getGoalState().compare(currentNode.getState());
	}

	protected boolean explode(GPSNode node) {
		if (problem.getRules() == null) {
			System.err.println("No rules!");
			return false;
		}

		for (GPSRule rule : problem.getRules()) {
			GPSState newState = null;
			try {
				newState = rule.evalRule(node.getState());
				// if (newState != null)
				// System.out.println(newState);
			} catch (NotAppliableException e) {
				// Do nothing
			}
			if (newState != null
					&& !checkBranch(node, newState)
					&& !checkOpenAndClosed(
							node.getCost() + rule.getCost(), newState)) {
				int newCost = node.getCost() + rule.getCost();
				GPSNode newNode = new GPSNode(newState, newCost, node.getDepth() + 1);
				newNode.setParent(node);
				addNode(newNode);
			}
		}
		return true;
	}

	private boolean checkOpenAndClosed(Integer cost, GPSState state) {
		for (GPSNode openNode : open) {
			if (openNode.getState().compare(state) && openNode.getCost() < cost) {
				return true;
			}
		}
		for (GPSNode closedNode : closed) {
			if (closedNode.getState().compare(state)
					&& closedNode.getCost() < cost) {
				return true;
			}
		}
		return false;
	}

	private boolean checkBranch(GPSNode parent, GPSState state) {
		if (parent == null) {
			return false;
		}
		return checkBranch(parent.getParent(), state)
				|| state.compare(parent.getState());
	}

	public abstract void addNode(GPSNode node);

}
