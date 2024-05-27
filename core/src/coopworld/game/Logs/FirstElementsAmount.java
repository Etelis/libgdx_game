package coopworld.game.Logs;

public class FirstElementsAmount {
    private int coins_displayed;
    private int virtual_coins_displayed;
    private int nature_obstacles_displayed;
    private int ice_obstacles_displayed;

    public void setCoins_displayed(int coins_displayed) {
        this.coins_displayed = coins_displayed;
    }

    public void setVirtual_coins_displayed(int virtual_coins_displayed) {
        this.virtual_coins_displayed = virtual_coins_displayed;
    }

    public void setNature_obstacles_displayed(int nature_obstacles_displayed) {
        this.nature_obstacles_displayed = nature_obstacles_displayed;
    }

    public void setIce_obstacles_displayed(int ice_obstacles_displayed) {
        this.ice_obstacles_displayed = ice_obstacles_displayed;
    }

    public int getCoins_displayed() {
        return coins_displayed;
    }

    public int getVirtual_coins_displayed() {
        return virtual_coins_displayed;
    }

    public int getNature_obstacles_displayed() {
        return nature_obstacles_displayed;
    }

    public int getIce_obstacles_displayed() {
        return ice_obstacles_displayed;
    }
}
