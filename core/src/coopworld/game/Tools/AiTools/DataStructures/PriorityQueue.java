package coopworld.game.Tools.AiTools.DataStructures;

import java.util.ArrayList;

/**
 * Created by Eva on 10/30/2016.
 */
public class PriorityQueue<T>
{
    private ArrayList<MyTuple> elements = new ArrayList<MyTuple>();

    public int count()
    {
        return elements.size();
    }

    public void enqueue(T item, double priority)
    {
        elements.add(new MyTuple(item,priority));
    }

    public T dequeue()
    {
        int bestIndex = 0;

        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getPriority() < elements.get(bestIndex).getPriority()) {
                bestIndex = i;
            }
        }

        T bestItem = (T)elements.get(bestIndex).getItem();
        elements.remove(bestIndex);
        return bestItem;
    }
}