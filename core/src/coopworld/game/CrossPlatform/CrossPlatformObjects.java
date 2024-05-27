package coopworld.game.CrossPlatform;

/**
 * Created by Chen on 13/10/2017.
 */

public class CrossPlatformObjects {
    public Input input;
    public GeneralUtils utils;

    public CrossPlatformObjects() {
    }

    public CrossPlatformObjects(Input input, GeneralUtils utils) {
        this.input = input;
        this.utils = utils;
    }

    public Input getInput() {
        return input;
    }

    public GeneralUtils getUtils() {
        return utils;
    }
}
