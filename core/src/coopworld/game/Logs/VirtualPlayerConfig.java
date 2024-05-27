package coopworld.game.Logs;

/**
 * Created by User on 29/01/2017.
 */
public class VirtualPlayerConfig {
    private Enums.VirtualPlayerStrategy strategy; // Instant/Delay/TitForTat/Random/NoResponse
    private Enums.TimingRequestLogic timing_request_logic;

    public VirtualPlayerConfig() {
    }

    public void setStrategy(Enums.VirtualPlayerStrategy strategy) {
        this.strategy = strategy;
    }

    public void setTiming_request_logic(Enums.TimingRequestLogic timing_request_logic) {
        this.timing_request_logic = timing_request_logic;
    }

    public Enums.TimingRequestLogic getTiming_request_logic() {
        return timing_request_logic;
    }

    public Enums.VirtualPlayerStrategy getStrategy() {
        return strategy;
    }

    // private String stuckWhenNeedsHelp; // "Yes"/"No" (Yes - stand next to the blocked ice cube,
    // No - continue picking for other ice cubes).
}
