package coopworld.game.Configuration;

/**
 * Created by Chen on 01/06/2017.
 */

public class Cheats {
    boolean clock;
    boolean locked_levels_available;
    boolean back_buttons;

    public boolean getClock() {
        return clock;
    }

    public boolean getLocked_levels_available() {
        return locked_levels_available;
    }

    public void setLocked_levels_available(boolean locked_levels_available) {
        this.locked_levels_available = locked_levels_available;
    }

    public boolean isBack_buttons() {
        return back_buttons;
    }

    public void setBack_buttons(boolean back_buttons) {
        this.back_buttons = back_buttons;
    }
}