package coopworld.game.Logs;

/**
 * Created by Chen on 11/04/2017.
 */

public class GenerationDelays {
    private float coins_delay;
    private float virtual_coins_delay;
    private float blocked_coins_delay;
    private float blocked_virtual_coins_delay;

    public void setCoins_generation_delay(float coins_generation_delay) {
        this.coins_delay = coins_generation_delay;
    }

    public void setVirtual_coins_generation_delay(float virtual_coins_generation_delay) {
        this.virtual_coins_delay = virtual_coins_generation_delay;
    }

    public void setBlocked_coins_generation_delay(float blocked_coins_generation_delay) {
        this.blocked_coins_delay = blocked_coins_generation_delay;
    }

    public void setBlocked_virtual_coins_generation_delay(float blocked_virtual_coins_generation_delay) {
        this.blocked_virtual_coins_delay = blocked_virtual_coins_generation_delay;
    }

    public float getCoins_delay() {
        return coins_delay;
    }

    public float getVirtual_coins_delay() {
        return virtual_coins_delay;
    }

    public float getBlocked_coins_delay() {
        return blocked_coins_delay;
    }

    public float getBlocked_virtual_coins_delay() {
        return blocked_virtual_coins_delay;
    }
}
