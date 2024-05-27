package coopworld.game.Tools;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;

import coopworld.game.Logs.FirstElementsAmount;
import coopworld.game.Tools.AiTools.MatrixBuilder;

/**
 * Created by Chen on 12/04/2017.
 */

public class MapElementsCounter {

    public FirstElementsAmount getAmounts(String tiledMapName){
        FirstElementsAmount firstElementsAmount = new FirstElementsAmount();
        TmxMapLoader tmxMapLoader = new TmxMapLoader();
        String tiledMapPath = "tiled_maps/" + tiledMapName;
        // initialize variables.
        int coinsInMap = 0;
        int virtualCoinsInMap = 0;
        int natureObstaclesInMap = 0;
        int iceObstaclesInMap = 0;

        // try to open tiled map in order to parse it.
        try {
            FileHandle tmxFile = tmxMapLoader.resolve(tiledMapPath);
            XmlReader xml = new XmlReader();
            XmlReader.Element root = xml.parse(tmxFile);

            // run over the .tmx file and count elements.
            Array<XmlReader.Element> objectgroupElements = root.getChildrenByName("objectgroup");
            for (XmlReader.Element groundObject : objectgroupElements) {
                if (groundObject.getName().equals("objectgroup")) {

                    String name = groundObject.getAttribute("name", null);

                    if (name.equals(MatrixBuilder.cellType.virtualcoins.name())
                        || (name.equals(MatrixBuilder.cellType.natureObstacles.name()))
                        || (name.equals(MatrixBuilder.cellType.iceObstacles.name()))
                        || (name.equals(MatrixBuilder.cellType.coins.name())))

                    {

                        for (XmlReader.Element objectElement : groundObject.getChildrenByName("object")) {
                            if(name.equals(MatrixBuilder.cellType.virtualcoins.name()))
                            {
                                if(!(objectElement.get("visible","true").equals("false")))
                                {
                                    virtualCoinsInMap += 1;
                                }
                            }

                            else if(name.equals(MatrixBuilder.cellType.coins.name()))
                            {
                                if(!(objectElement.get("visible","true").equals("false")))
                                {
                                    coinsInMap += 1;
                                }
                            }

                            else if(name.equals(MatrixBuilder.cellType.iceObstacles.name()))
                            {
                                if(!(objectElement.get("visible","true").equals("false")))
                                {
                                    iceObstaclesInMap += 1;
                                }
                            }

                            else if(name.equals(MatrixBuilder.cellType.natureObstacles.name()))
                            {
                                if(!(objectElement.get("visible","true").equals("false")))
                                {
                                    natureObstaclesInMap += 1;
                                }
                            }
                        }
                    }
                }
            }

        }
        catch (Exception ex) {
            throw new GdxRuntimeException("Couldn't count elements of tilemap '" + tiledMapPath
                    + "'", ex);
        }

        // set variables in FirstElementsAmount object.
        firstElementsAmount.setCoins_displayed(coinsInMap);
        firstElementsAmount.setVirtual_coins_displayed(virtualCoinsInMap);
        firstElementsAmount.setNature_obstacles_displayed(natureObstaclesInMap);
        firstElementsAmount.setIce_obstacles_displayed(iceObstaclesInMap);

        return firstElementsAmount;
    }
}
