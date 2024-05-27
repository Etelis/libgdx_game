package coopworld.game.Logs;

public class OnOffEvent {
    boolean turn_on;
    String time_stamp;

    public OnOffEvent(boolean turn_on, String time_stamp) {
        this.turn_on = turn_on;
        this.time_stamp = time_stamp;
    }
}
