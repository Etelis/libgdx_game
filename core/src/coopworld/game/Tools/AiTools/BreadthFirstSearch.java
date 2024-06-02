package coopworld.game.Tools.AiTools;


import com.badlogic.gdx.utils.Queue;

import java.util.LinkedList;

import coopworld.game.Tools.AiTools.DataStructures.Node;

/**
 * Not in use!
 */
public class BreadthFirstSearch extends Searcher {
    private Queue<Node> nodesQueue;
    private int _evaluatedNodes;

    public BreadthFirstSearch() {
        nodesQueue = new Queue<Node>();
        _evaluatedNodes = 0;
    }


    public LinkedList<Node> Search(SearchableMatrix searchable, Node start, Node goal) {
        searchable.ResetMazeDistance();
        start.setDistance(0); // set entry point to distance 0
        start.setParent(null);
        this.nodesQueue.addLast(start);

        while (this.nodesQueue.size > 0) {
            Node current = this.nodesQueue.removeFirst();
            for (Node n : searchable.getAllNextStepOptions(current)) {
                if (n.getDistance() == 9999999) // not visited yet
                {
                    n.setDistance(current.getDistance() + 1);
                    n.setParent(current);
                    if (n.equals(goal)) {
                        return Backtrace(n);
                    }
                    this.nodesQueue.addLast(n);
                }
            }
        }
        return null; // could'nt reach goal point
    }
}

