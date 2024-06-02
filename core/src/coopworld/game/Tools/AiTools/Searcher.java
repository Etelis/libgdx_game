package coopworld.game.Tools.AiTools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import coopworld.game.Tools.AiTools.DataStructures.Node;

/**
 * Created by Eva on 10/30/2016.
 */
public abstract class Searcher {

    protected abstract LinkedList<Node> Search(SearchableMatrix searchable, Node start, Node goal);
    Node targetedBlockedCoin;

    /**
     * Returns linked list of nodes from search point to reach point
     * @param n reach point
     * @return linked list of nodes from search point to reach point
     */
    protected LinkedList<Node> Backtrace(Node n)
    {
        LinkedList<Node> path = new LinkedList<Node>();
        Node onPath = n;
        path.add(n);
        while (onPath.getParent() != null) // backtrace all the way up to root
        {
            path.addFirst(onPath.getParent());
            onPath = onPath.getParent();
        }
        return path;
    }


    protected int getClosestCoinIndexFromList(Node start,ArrayList<Node> nodeArrayList)
    {
        int i = 0;
        int currentMinIndex = 0;
        int dist;
        int minPathLength = 0;
        for (Node coinNode : nodeArrayList) {
            if (coinNode.isRandomlyGenerated()) // give priority to random generated coin
            {
                currentMinIndex = i;
                break;
            }
            dist = start.computeManhattanDistance(coinNode);
            if ((i == 0) || (dist < minPathLength)) {
                minPathLength = dist;
                currentMinIndex = i;
            }
            i++;


           /* LinkedList<Node> pathToCoin = this.Search(searchable,start,coinNode);
            dist = pathToCoin.size();
          //  System.out.println("GOAL :");

          //  System.out.println("Size:" + Integer.toString(dist));
            if ((i == 0) || (dist < minPathLength)){
                minPath = pathToCoin;
                minPathLength = minPath.size();
            }
            paths.add(this.Search(searchable,start,coinNode));
            i++;
            */

        }
        return currentMinIndex;
    }
    /**
     * Get best next step
     * @param
     * @return
     */

    public Node GetBestNextStepToVirtualCoin(SearchableMatrix searchable,Node start)
    {
        if(start == null)
        {
            //System.out.println("start is null");
            return null;
        }
        if (searchable.getVirtualCoinsList().isEmpty())
            return start;

      //  ArrayList<LinkedList<Node>> paths = new ArrayList<LinkedList<Node>>();
       // LinkedList<Node> minPath = null;
       // int minPathLength = 0;
      //  int dist;

        int currentMinIndex = getClosestCoinIndexFromList(start,searchable.getVirtualCoinsList());

        /*
        System.out.println("GOAL :" + searchable.getVirtualCoinsList().get(currentMinIndex).getMatrixCell().getX()+
                " , " + searchable.getVirtualCoinsList().get(currentMinIndex).getMatrixCell().getY() );
        */
        // the index i is the best next step
        LinkedList<Node> pathToCoin = this.Search(searchable,start
                ,searchable.getVirtualCoinsList().get(currentMinIndex));
        //System.out.println("*****************PATH**************");
        if (pathToCoin != null) {
            /*
            for (Node n : pathToCoin) {
                n.print();
            }
            */

            if (pathToCoin.size() > 1)
                return pathToCoin.get(1); //first actual step in path
            else if (pathToCoin.size() == 1)
                return pathToCoin.get(0); // current position
        }
        return null;
    }


    // Used to get virtual player to go to the blocked coin, if we use the previous function
    // since there is no path to that spot the virtual player won't go there.
    public Node GetBestNextStepToBlockedVirtualCoin(SearchableMatrix searchable,Node start)
    {
        if(start == null)
        {
            //System.out.println("start is null");
            return null;
        }
        if (searchable.getBlockedVirtualCoinsList().isEmpty())
            return start;

        int i = 0;
        int currentMinIndex = 0;
        int dist;
        int minPathLength = 0;
        Collection<ArrayList<Node>> possibleSpotsArrayLists = searchable.getBlockedVirtualCoinsList().values();
        ArrayList<Node> allOptions = new ArrayList<Node>();
        for (ArrayList<Node> arrayList : possibleSpotsArrayLists) {
            for(Node n : arrayList)
            {
                allOptions.add(n);
            }
        }
        for(Node n : allOptions) {
            if (n.isRandomlyGenerated()) // give priority to random generated coin
            {
                currentMinIndex = i;
                break;
            }
            dist = start.computeManhattanDistance(n);
            if ((i == 0) || (dist < minPathLength)) {
                minPathLength = dist;
                currentMinIndex = i;
            }
            i++;
        }

        for (Map.Entry<Node, ArrayList<Node>> entry : searchable.getBlockedVirtualCoinsList().entrySet()) {
            ArrayList<Node> possibleSpotsInEntry = entry.getValue();
            if (possibleSpotsInEntry.contains(allOptions.get(currentMinIndex)))
            {
                targetedBlockedCoin = entry.getKey();
            }
        }

        //System.out.println("GOAL :" + searchable.getVirtualCoinsList().get(currentMinIndex).getMatrixCell().getX()+
        //        " , " + searchable.getVirtualCoinsList().get(currentMinIndex).getMatrixCell().getY() );

        // the index i is the best next step
        LinkedList<Node> pathToCoin = this.Search(searchable,start
                ,allOptions.get(currentMinIndex));
        if(allOptions.get(currentMinIndex).equals(start))
        {
            return start;
        }

        System.out.println("*****************PATH**************");
        if (pathToCoin != null) {
            /*
            for (Node n : pathToCoin) {
                n.print();
            }
            */

            if (pathToCoin.size() > 1)
                return pathToCoin.get(1); //first actual step in path
            else if (pathToCoin.size() == 1)
                return pathToCoin.get(0); // current position
        }
        return null;
    }

    public Node GetBestNextStepToGoal(SearchableMatrix searchable,Node start,Node goal)
    {
        LinkedList<Node> pathToCoin = this.Search(searchable,start,goal);

        if(pathToCoin.size() > 1)
            return pathToCoin.get(1); //first actual step in path
        else if (pathToCoin.size() == 1)
            return pathToCoin.get(0); // current position
        return null;
    }

    public Node getNatureObstacleNode(){
        return targetedBlockedCoin;
    }
}
