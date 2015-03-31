package edu.itba.sia.solver;

import java.util.List;

import edu.itba.sia.API.GPSProblem;
import edu.itba.sia.classes.GPSEngine;
import edu.itba.sia.classes.GPSNode;
import edu.itba.sia.classes.SearchStrategy;

public class Engine extends GPSEngine
{
    private static int INITIAL_DEPTH = 1;
    private static int MAX_DEPTH;
    private static int GREEDY_FACTOR = 100;

    public Engine(SearchStrategy strategy)
    {
        engine(new Problem(), strategy);
        MAX_DEPTH = INITIAL_DEPTH;
    }

    @Override
    protected boolean explode(GPSNode node)
    {

        if (getStrategy().equals(SearchStrategy.IDFS))
        {
            if (node.getDepth() >= MAX_DEPTH)
            {
                MAX_DEPTH += 1;
                System.out.println("Depth up to: " + MAX_DEPTH);

                getClosed().clear();
                getOpen().clear();
                getOpen().add(new GPSNode(getProblem().getInitState(), 0, 0));

                return true;
            }
        }
        return super.explode(node);
    }

    @Override
    public void addNode(GPSNode node)
    {
        final GPSProblem problem = getProblem();
        List<GPSNode> open = getOpen();
        switch (getStrategy())
        {
            case BFS:
                open.add(node);
                break;
            case DFS:
            case IDFS:
                open.add(0, node);
                break;
            case Greedy:
                for (int counter = 0; counter < open.size() / GREEDY_FACTOR; counter++)
                {
                    open.remove(open.size() - 1);
                }
                open.add(0, node);
                break;
            case AStar:
                Integer total = getProblem().getHValue(node.getState()) + node.getCost();

                int i = 0;
                for (GPSNode openNode : open)
                {
                    if (total > problem.getHValue(openNode.getState()) + openNode.getCost())
                    {
                        i++;
                    }
                }
                getOpen().add(i, node);
                break;


            default:
                break;
        }
    }
}
