package gps.controllers;

import gps.model.GPSState;

public class GPSNode {

	private GPSState state;

	private GPSNode parent;

	private int cost;

	public GPSNode(GPSState state, int cost) {
		super();
		this.state = state;
		this.cost = cost;
	}

	public GPSNode getParent() {
		return parent;
	}

	public void setParent(GPSNode parent) {
		this.parent = parent;
	}

	public GPSState getState() {
		return state;
	}

	public int getCost() {
		return cost;
	}

	@Override
	public String toString() {
		return state.toString();
	}

	public String getSolution() {
		if (this.parent == null) {
			return this.state.toString();
		}
		return this.parent.getSolution() + "\n" + this.state;
	}
}
