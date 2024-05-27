package coopworld.game.Tools.AiTools.DataStructures;

/**
 * Created by Eva on 10/30/2016.
 */
public class MyTuple<T> {
    private T item;
    private double priority;

    public MyTuple(T item, double priority) {
        this.item = item;
        this.priority = priority;
    }

    public T getItem() {
        return item;
    }

    public double getPriority() {
        return priority;
    }
}
