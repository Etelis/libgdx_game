package coopworld.game.Tools.AiTools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;

import coopworld.game.Tools.AiTools.DataStructures.Node;

/**
 * Created by Eva on 10/28/2016.
 */

/// Best first search algorithm
/// https://en.wikipedia.org/wiki/Best-first_search
public class BestFirstSearchAlgorithm extends Searcher{
    private SortedSet<Node> openList;
    private int evaluatedNodes;


    public BestFirstSearchAlgorithm() {
        this.openList = new TreeSet<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.equals(o2)) {
                    return 0;
                }
                int compareRes = (o1.getDistance() - o2.getDistance());
                // if distance is equal, return 1 and not 0, because 0 wont add the item to the list
                if (compareRes == 0) {
                    return 1;
                }
                return compareRes;
            }
        });
        this.evaluatedNodes = 0;

    }


    public SortedSet<Node> getOpenList() {
        return openList;
    }

    public int getEvaluatedNodes() {
        return evaluatedNodes;
    }

    private void AddToOpenList(Node n) {
        if (!this.openList.add(n)) {
            System.out.println("Error in adding node to open list - node already exists");
        }
    }

    private Node PopOpenList() {
        this.evaluatedNodes++;
        Node max = this.openList.last();
        this.openList.remove(max);
        return max;
    }

    /**
     * Best first search implementation - the returned path is always a straight path or must run "orthogonally".
     * @param searchable
     * @param
     * @param
     * @return
     */

    //TODO: Test this function
    protected LinkedList<Node> Search(SearchableMatrix searchable, Node start, Node goal) {
        this.openList.clear();
        this.evaluatedNodes = 0;
        searchable.ResetMazeDistance();
        // start node initialization
        start.setDistance(0);
        start.setParent(null);
        AddToOpenList(start);
        HashSet<Node> closed = new HashSet<Node>();

        if ((start == null) || (goal == null))
        {
            return null; // Problem or Collected all the coins nowhere to go next!
        }
        while (openList.size() > 0) {
            Node n = PopOpenList();
            closed.add(n);
            if (n.equals(goal))
                return Backtrace(n); // private method, back traces through the parents

            ArrayList<Node> successors = searchable.getAllNextStepOptions(n);
            for (Node s : successors) {
                int newPath = n.getDistance() + 1; // vertex cost is 1

                if (!(closed.contains(s)) && (!(openList.contains(s)))) {
                    s.setParent(n);
                    s.setDistance(n.getDistance() + 1);
                    AddToOpenList(s);
                } else if (newPath < s.getDistance()) {
                    if (!openList.contains(s))
                        AddToOpenList(s);
                    else {
                        openList.remove(s);
                        s.setDistance(newPath);
                        s.setParent(n);
                        AddToOpenList(s);
                    }
                }
            }
        }
        return null;

    }


}
