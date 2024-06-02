package coopworld.game.Tools;

/**
 * Created by User on 03/03/2017.
 */
public class StarsLogic {
    int numOfStars;
    int score;
    int displayedCoins;

    public StarsLogic(int score, int displayedCoins){
        this.score = score;
        this.displayedCoins = displayedCoins;
    }

    public int getNumOfStars(){
        float completionRate = (float) (this.score + 1) / (float) this.displayedCoins;

        if(completionRate > 0.6f) {
            this.numOfStars = 3;
        }
        else if(completionRate > 0.4f) {
            this.numOfStars = 2;
        }
        else {
            this.numOfStars = 1;
        }

        return numOfStars;
    }
}
