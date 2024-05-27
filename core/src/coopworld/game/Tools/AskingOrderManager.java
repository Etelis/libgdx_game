package coopworld.game.Tools;

import java.util.HashMap;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.Enums;

public class AskingOrderManager {
    public static HashMap<Integer, Enums.Player> firstHelperMap = new HashMap<Integer, Enums.Player>();

    public AskingOrderManager() {
        if(Constants.IS_INTERVENTION){
            // TODO 2022 - HARD CODED!!! (7 levels)
            for(int i = 1; i <= 7; i++){
                firstHelperMap.put(i, Enums.Player.Human);
            }

            for (int i = 0; i < Constants.INTERVENTION_LEVELS_EXTENDED.size(); i++) {
                firstHelperMap.put((Integer) Constants.INTERVENTION_LEVELS_EXTENDED.get(i), Enums.Player.Virtual);
            }
        }
        else{
            for(int i = 1; i <= CooperativeGame.levelsPath.size(); i++){
                firstHelperMap.put(i, Enums.Player.Human);
            }
            //firstHelperMap.put(1, Enums.Player.Human);
            firstHelperMap.put(Constants.LEVELS_IN_SESSION + 1, Enums.Player.Virtual);
        }
    }

    public Enums.Player getFirstHelper(int levelNum){
         return firstHelperMap.get(levelNum);
     }
}