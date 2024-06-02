package coopworld.game.Tools.AiTools;

import java.util.HashMap;
import java.util.LinkedList;

import coopworld.game.Tools.AiTools.DataStructures.Node;
import coopworld.game.Tools.AiTools.DataStructures.PriorityQueue;

/**
 * Created by Eva on 10/30/2016.
*/
public class AStar extends Searcher
{
    public HashMap<Node, Node> cameFrom = new HashMap<Node, Node>();
    public HashMap<Node, Integer> costSoFar = new HashMap<Node, Integer>();


    static public double Heuristic(Node a, Node b)
    {
        return Math.abs(a.getMatrixCell().getX() - b.getMatrixCell().getX()) + Math.abs(a.getMatrixCell().getY() - b.getMatrixCell().getY());
    }

    public LinkedList<Node> Search(SearchableMatrix searchable, Node start, Node goal) {
        // more goals!
        if (goal == null) {
            return null;
        }
        cameFrom.clear();
        costSoFar.clear();
        searchable.ResetMazeDistance();

        PriorityQueue frontier = new PriorityQueue<Node>();
        frontier.enqueue(start, 0);

        start.setParent(null);
        start.setDistance(0);

        cameFrom.put(start, start);
        costSoFar.put(start, new Integer(0));

        while (frontier.count() > 0) {
            Node current = (Node) frontier.dequeue();

            if (current.equals(goal)) {
                return Backtrace(current);
            }

            for (Node next : searchable.getAllNextStepOptions(current)) {
                int newCost = costSoFar.get(current) + searchable.cost(current, next);
                if (!(costSoFar.containsKey(next)) || (newCost < costSoFar.get(next))) {
                    next.setDistance(newCost);
                    costSoFar.put(next, newCost);
                    double priority = newCost + Heuristic(next, goal);
                    frontier.enqueue(next, priority);
                    next.setParent(current);
                    cameFrom.put(next, current);
                }
            }
        }

        return null;
    }
}
