package coopworld.game.Logs.HelpRequests;

import coopworld.game.Logs.Enums;

/**
 * Created by User on 18/01/2017.
 */
public class HelpRequest {
    private Enums.Player help_asker; // Human for child, Virtual for virtual.
    private boolean was_accepted; // Yes for yes, No for no.

    private int[][] board; // Screen-shot of the board (int mapping - 18*30) when
                          // the request was created.

    private String asking_time;
    private String answer_time;
    private String remaining_time_in_clock;

    private String appearing_time;

    private int human_score_before;

    private Enums.ChosenStrategy chosen_virtual_strategy;

    public HelpRequest() {
    }

    public void setHelpAsker(Enums.Player helpAsker) {
        this.help_asker = helpAsker;
    }

    public void setWasAccepted(boolean wasAccepted) {
        this.was_accepted = wasAccepted;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public void setAsking_time(String asking_time) {
        this.asking_time = asking_time;
    }

    public void setAnswer_time(String answer_time) {
        this.answer_time = answer_time;
    }

    public boolean getWasAccepted() {
        return was_accepted;
    }

    public Enums.Player getHelp_asker() {
        return help_asker;
    }

    public String getAsking_time() {
        return asking_time;
    }

    public String getAnswer_time() {
        return answer_time;
    }

    public void setChosen_virtual_strategy(Enums.ChosenStrategy chosen_virtual_strategy) {
        this.chosen_virtual_strategy = chosen_virtual_strategy;
    }
    public void setHuman_score_before(int human_score_before) {
        this.human_score_before = human_score_before;
    }

    public void setAppearing_time(String appearing_time) {
        this.appearing_time = appearing_time;
    }

    public void setRemaining_time_in_clock(String remaining_time_in_clock) {
        this.remaining_time_in_clock = remaining_time_in_clock;
    }
}
