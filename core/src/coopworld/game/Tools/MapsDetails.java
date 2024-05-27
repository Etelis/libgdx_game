package coopworld.game.Tools;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

public class MapsDetails {
    // Inner class - players positions. hold the first positions of the players.
    public class PlayersPositions {
        private Vector2 humanPlayerPosition;
        private Vector2 virtualPlayerPosition;

        public PlayersPositions(Vector2 humanPlayerPosition, Vector2 virtualPlayerPosition) {
            this.humanPlayerPosition = humanPlayerPosition;
            this.virtualPlayerPosition = virtualPlayerPosition;
        }

        public Vector2 getHumanPlayerPosition() {
            return humanPlayerPosition;
        }

        public Vector2 getVirtualPlayerPosition() {
            return virtualPlayerPosition;
        }
    }

    // hash map to hold the mapping between them tmx file name (String- key), and the correspond
    // first players positions in the start of the level.
    public HashMap<String, PlayersPositions> mapsDetails;

    public MapsDetails() {
        // initialize the hash map.
        mapsDetails = new HashMap<String, PlayersPositions>();
    }

    /*
    This method sets the initial positions for both human and virtual players.
    In the start of the level - the players will be places in that defined positions.
    We use PlayersPositions object to define position. the first argument is human player position,
    and the second is virtual player position.
    Vector2 - first argument is x, second argument is y.
    */

    public void setInitialPositions(){
        this.mapsDetails.put("beach_level_1.tmx", new PlayersPositions(new Vector2(8, 5),
                new Vector2(10, 5)));

        this.mapsDetails.put("colored_forest_level_2.tmx", new PlayersPositions(new Vector2(8, 5),
                new Vector2(10, 5)));




        this.mapsDetails.put("colored_forest_level.tmx", new PlayersPositions(new Vector2(8, 5),
                new Vector2(10, 5)));

        this.mapsDetails.put("beach_level_2.tmx", new PlayersPositions(new Vector2(8, 5),
                new Vector2(11, 5)));
        /*
        this.mapsDetails.put("desert_level.tmx", new PlayersPositions(new Vector2(10, 3),
                new Vector2(6, 3)));
        */
        this.mapsDetails.put("desert_level2.tmx", new PlayersPositions(new Vector2(10, 3),
                new Vector2(6, 3)));

        this.mapsDetails.put("candy_level_1.tmx", new PlayersPositions(new Vector2(8, 5),
                new Vector2(10, 5)));
        this.mapsDetails.put("candy_level_2.tmx", new PlayersPositions(new Vector2(8, 3),
                new Vector2(10, 3)));
        this.mapsDetails.put("Level4.tmx", new PlayersPositions(new Vector2(8, 3),
                new Vector2(10, 3)));
        this.mapsDetails.put("Level4-ice.tmx", new PlayersPositions(new Vector2(8, 1),
                new Vector2(10, 1)));
        this.mapsDetails.put("regular_level_1.tmx", new PlayersPositions(new Vector2(10, 3),
                new Vector2(6, 3))); // 6, 3
        this.mapsDetails.put("Level7-ice.tmx", new PlayersPositions(new Vector2(10, 3),
                new Vector2(6, 3)));
        this.mapsDetails.put("map1b.tmx", new PlayersPositions(new Vector2(8, 2),
                new Vector2(11, 2)));
        this.mapsDetails.put("Level1.tmx", new PlayersPositions(new Vector2(8, 2),
                new Vector2(11, 2)));
        this.mapsDetails.put("Level1_combined.tmx", new PlayersPositions(new Vector2(8, 2),
                new Vector2(11, 2)));
        this.mapsDetails.put("Level1-ice.tmx", new PlayersPositions(new Vector2(8, 2),
                new Vector2(11, 2)));
        this.mapsDetails.put("map6.tmx", new PlayersPositions(new Vector2(6, 2),
                new Vector2(10, 2)));
        this.mapsDetails.put("Level5.tmx", new PlayersPositions(new Vector2(7, 3),
                new Vector2(10, 3)));
        this.mapsDetails.put("Level5-ice.tmx", new PlayersPositions(new Vector2(7, 3),
                new Vector2(10, 3)));
        this.mapsDetails.put("map11.tmx", new PlayersPositions(new Vector2(13, 1),
                new Vector2(10, 1)));
        /*
        this.mapsDetails.put("9.tmx", new PlayersPositions(new Vector2(6, 1),
                new Vector2(12, 1)));
        */
        this.mapsDetails.put("map8.tmx", new PlayersPositions(new Vector2(8, 1),
                new Vector2(11, 1)));
        this.mapsDetails.put("map10.tmx", new PlayersPositions(new Vector2(8, 3),
                new Vector2(11, 3)));
        this.mapsDetails.put("map12.tmx", new PlayersPositions(new Vector2(8, 3),
                new Vector2(11, 3)));
        this.mapsDetails.put("Level6.tmx", new PlayersPositions(new Vector2(8, 3),
                new Vector2(11, 3)));
        this.mapsDetails.put("colored_forest_level.tmx", new PlayersPositions(new Vector2(8, 5),
                new Vector2(11, 5)));
        this.mapsDetails.put("colored_forest_level_1.tmx", new PlayersPositions(new Vector2(8, 5),
                new Vector2(11, 5)));
        this.mapsDetails.put("beach_level_2.tmx", new PlayersPositions(new Vector2(8, 5),
                new Vector2(11, 5)));
        this.mapsDetails.put("Level6-ice.tmx", new PlayersPositions(new Vector2(8, 3),
                new Vector2(11, 3)));
        this.mapsDetails.put("map77.tmx", new PlayersPositions(new Vector2(8, 2),
                new Vector2(11, 3)));
        this.mapsDetails.put("map1c.tmx", new PlayersPositions(new Vector2(4, 5),
                new Vector2(16, 5)));
        this.mapsDetails.put("map4.tmx", new PlayersPositions(new Vector2(8, 1),
                new Vector2(11, 1)));
        this.mapsDetails.put("Level3.tmx", new PlayersPositions(new Vector2(8, 1),
                new Vector2(11, 1)));
        this.mapsDetails.put("Level3-ice.tmx", new PlayersPositions(new Vector2(8, 1),
                new Vector2(11, 1)));
    }

    public HashMap<String, PlayersPositions> getMapsDatails(){
        return mapsDetails;
    }
}
