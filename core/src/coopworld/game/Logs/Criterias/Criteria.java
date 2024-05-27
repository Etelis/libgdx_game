package coopworld.game.Logs.Criterias;

/**
 * Created by User on 13/03/2017.
 */
public abstract class Criteria {
    public Criteria(int coins_to_collect) {
        this.coins_to_collect = coins_to_collect;
    }

    public int coins_to_collect;
    public abstract boolean criteriaSatisfied();

}
