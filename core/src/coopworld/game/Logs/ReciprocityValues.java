package coopworld.game.Logs;

public class ReciprocityValues {
    // Values.
    int regular_coin_value;
    int locked_coin_value;
    int regular_virtual_coin_value;
    int locked_virtual_coin_value;
    // Costs.
    int help_providing_cost_human;
    int help_providing_cost_virtual;

    public ReciprocityValues() {
    }

    public int getRegular_coin_value() {
        return regular_coin_value;
    }

    public int getLocked_coin_value() {
        return locked_coin_value;
    }

    public int getRegular_virtual_coin_value() {
        return regular_virtual_coin_value;
    }

    public int getLocked_virtual_coin_value() {
        return locked_virtual_coin_value;
    }

    public int getHelp_providing_cost_human() {
        return help_providing_cost_human;
    }

    public int getHelp_providing_cost_virtual() {
        return help_providing_cost_virtual;
    }

    public void setRegular_coin_value(int regular_coin_value) {
        this.regular_coin_value = regular_coin_value;
    }

    public void setLocked_coin_value(int locked_coin_value) {
        this.locked_coin_value = locked_coin_value;
    }

    public void setRegular_virtual_coin_value(int regular_virtual_coin_value) {
        this.regular_virtual_coin_value = regular_virtual_coin_value;
    }

    public void setLocked_virtual_coin_value(int locked_virtual_coin_value) {
        this.locked_virtual_coin_value = locked_virtual_coin_value;
    }

    public void setHelp_providing_cost_human(int help_providing_cost_human) {
        this.help_providing_cost_human = help_providing_cost_human;
    }

    public void setHelp_providing_cost_virtual(int help_providing_cost_virtual) {
        this.help_providing_cost_virtual = help_providing_cost_virtual;
    }
}
