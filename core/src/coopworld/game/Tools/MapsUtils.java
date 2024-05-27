package coopworld.game.Tools;

public class MapsUtils {

    public static String getMapType(String mapName){
        if(mapName.contains("combined") || mapName.contains("regular")){
            PersistentData.LEVEL_TYPE = "regular";
        }
        else if(mapName.contains("forest")) {
            PersistentData.LEVEL_TYPE = "orange";
        }
        else if(mapName.contains("ice")) {
            PersistentData.LEVEL_TYPE = "ice";
        }
        else if(mapName.contains("beach")) {
            PersistentData.LEVEL_TYPE = "beach";
        }
        else if(mapName.contains("desert")) {
            PersistentData.LEVEL_TYPE = "desert";
        }
        else if(mapName.contains("candy")) {
            PersistentData.LEVEL_TYPE = "candy";
        }
        return PersistentData.LEVEL_TYPE;
    }
}
