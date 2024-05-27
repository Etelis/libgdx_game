package coopworld.game.Tools;

import java.util.LinkedHashMap;

import coopworld.game.Logs.Criterias.Criteria;
import coopworld.game.Logs.Enums;

/**
 * Created by User on 01/03/2017.
 */
public class InstructionElement {
    private Enums.LevelDescription levelDescription; // Describe level characteristics.
    // visualView & audioPath
    public static LinkedHashMap<String, String> viewAndAudio = new LinkedHashMap<String, String>();

    // criteria - each level ends if X seconds has passed and a criteria is satisfied.
    // Here it is a default criteria (always false). It will be override in the training (first)
    // levels.
    private Criteria criteria;

    public InstructionElement(LinkedHashMap<String, String> viewAndAudio,
                              Enums.LevelDescription levelDescription, Criteria criteria) {
        this.viewAndAudio = viewAndAudio;
        this.levelDescription = levelDescription;
        this.criteria = criteria;
    }

    public Enums.LevelDescription getLevelDescription() {
        return levelDescription;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public LinkedHashMap<String, String> getViewAndAudio() {
        return viewAndAudio;
    }
}
