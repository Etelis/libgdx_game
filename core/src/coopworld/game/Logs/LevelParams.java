package coopworld.game.Logs;


import java.util.LinkedHashMap;

import coopworld.game.CooperativeGame;
import coopworld.game.Logs.Criterias.Criteria;
import coopworld.game.Screens.GameScreens.GameScreen;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.InstructionElement;
import coopworld.game.Tools.MapElementsCounter;
import coopworld.game.Tools.Paths;

/**
 * Created by User on 21/02/2017.
 */

public class LevelParams {
    private int level_num;

    private int level_minimal_length; // Level length (sec).
    private TiledMap tiled_map;

    private InstructionElement instructions = null; // Instruction elements.

    public LevelParams(int levelNum){
        this.level_num = levelNum;
        // for internal use.
        MapElementsCounter mapElementsCounter = new MapElementsCounter();
        String mapPath;
        LinkedHashMap<String,String> viewAndAudioInstructions = null;

        switch(levelNum) {
            case 1:
                level_minimal_length = 1;
                viewAndAudioInstructions = new LinkedHashMap<String, String>();
                viewAndAudioInstructions.put(Paths.FIRST_INST_VIEW, Paths.FIRST_INST_VOICE);
                instructions = new InstructionElement(viewAndAudioInstructions,
                        Enums.LevelDescription.OnlyCoins, new Criteria(1000) {
                    // TODO - not the same in the Combined mode!
                    @Override
                    public boolean criteriaSatisfied() {
                        return GameScreen.humanScore >= coins_to_collect;
                    }
                });
                tiled_map = new TiledMap();
                mapPath = CooperativeGame.levelsPath.get(levelNum).toString();
                tiled_map.setTmx_file_name(CooperativeGame.levelsPath.get(levelNum).toString());
                tiled_map.setFirst_element_amount(mapElementsCounter.getAmounts(mapPath));

                break;

            case 2:
                level_minimal_length = Constants.LEVEL_LENGTH;
                viewAndAudioInstructions = new LinkedHashMap<String, String>();
                if(CooperativeGame.gameData.getGame_params().getInstructions_method() == Enums.InstructionsMethod.Combined) {
                    instructions = null;
                }
                else{
                    instructions = new InstructionElement(viewAndAudioInstructions,
                            Enums.LevelDescription.CoinsAndIceCubes, new Criteria(15) {
                        @Override
                        public boolean criteriaSatisfied() {
                            return GameScreen.humanScore >= coins_to_collect;
                        }
                    });
                }

                tiled_map = new TiledMap();
                mapPath = CooperativeGame.levelsPath.get(levelNum).toString();
                tiled_map.setTmx_file_name(CooperativeGame.levelsPath.get(levelNum).toString());
                tiled_map.setFirst_element_amount(mapElementsCounter.getAmounts(mapPath));

                break;

            case 3:
                level_minimal_length = Constants.LEVEL_LENGTH;
                viewAndAudioInstructions = new LinkedHashMap<String, String>();
                if(CooperativeGame.gameData.getGame_params().getInstructions_method() == Enums.InstructionsMethod.Combined) {
                    instructions = null;
                }
                else{
                    instructions = new InstructionElement(viewAndAudioInstructions,
                            Enums.LevelDescription.HumanRequests, new Criteria(20) {
                        @Override
                        public boolean criteriaSatisfied() {
                            return GameScreen.humanScore >= coins_to_collect;
                        }
                    });
                }
                tiled_map = new TiledMap();
                mapPath = CooperativeGame.levelsPath.get(levelNum).toString();
                tiled_map.setTmx_file_name(CooperativeGame.levelsPath.get(levelNum).toString());
                tiled_map.setFirst_element_amount(mapElementsCounter.getAmounts(mapPath));

                break;

            case 4:
                // TODO - HARD CODED!!!
                if(Constants.IS_INTERVENTION){
                    level_minimal_length = Constants.LEVEL_LENGTH_EXTENDED;
                }
                else{ // intervention
                    level_minimal_length = Constants.LEVEL_LENGTH;
                }

                viewAndAudioInstructions = new LinkedHashMap<String, String>();

                if(CooperativeGame.gameData.getGame_params().getInstructions_method() == Enums.InstructionsMethod.Combined) {
                    instructions = null;
                }
                else{
                    instructions = new InstructionElement(viewAndAudioInstructions,
                            Enums.LevelDescription.VirtualRequests, new Criteria(20) {
                        @Override
                        public boolean criteriaSatisfied() {
                            return GameScreen.humanScore >= coins_to_collect;
                        }
                    });
                }

                tiled_map = new TiledMap();
                mapPath = CooperativeGame.levelsPath.get(levelNum).toString();
                tiled_map.setTmx_file_name(CooperativeGame.levelsPath.get(levelNum).toString());
                tiled_map.setFirst_element_amount(mapElementsCounter.getAmounts(mapPath));

                break;

            case 5:
                viewAndAudioInstructions = new LinkedHashMap<String, String>();
                // TODO - HARDCODED.
                if(!Constants.IS_INTERVENTION){
                    level_minimal_length = Constants.LEVEL_LENGTH_EXTENDED;
                }
                else{ // intervention
                    level_minimal_length = Constants.LEVEL_LENGTH;
                }
                instructions = null;

                tiled_map = new TiledMap();
                mapPath = CooperativeGame.levelsPath.get(levelNum).toString();
                tiled_map.setTmx_file_name(CooperativeGame.levelsPath.get(levelNum).toString());
                tiled_map.setFirst_element_amount(mapElementsCounter.getAmounts(mapPath));

                break;

            case 6:
                // TODO - HARD CODED!!!
                if(Constants.IS_INTERVENTION){
                    level_minimal_length = Constants.LEVEL_LENGTH_EXTENDED;
                }
                else{ // intervention
                    level_minimal_length = Constants.LEVEL_LENGTH;
                }
                instructions = null;
                tiled_map = new TiledMap();
                mapPath = CooperativeGame.levelsPath.get(levelNum).toString();
                tiled_map.setTmx_file_name(CooperativeGame.levelsPath.get(levelNum).toString());
                tiled_map.setFirst_element_amount(mapElementsCounter.getAmounts(mapPath));

                break;

            case 7:
                level_minimal_length = Constants.LEVEL_LENGTH;
                instructions = null;
                tiled_map = new TiledMap();
                mapPath = CooperativeGame.levelsPath.get(levelNum).toString();
                tiled_map.setTmx_file_name(CooperativeGame.levelsPath.get(levelNum).toString());
                tiled_map.setFirst_element_amount(mapElementsCounter.getAmounts(mapPath));

                break;

            case 8:
                level_minimal_length = Constants.LEVEL_LENGTH;
                instructions = null;
                tiled_map = new TiledMap();
                mapPath = CooperativeGame.levelsPath.get(levelNum).toString();
                tiled_map.setTmx_file_name(CooperativeGame.levelsPath.get(levelNum).toString());
                tiled_map.setFirst_element_amount(mapElementsCounter.getAmounts(mapPath));

                break;

            case 9:
                level_minimal_length = Constants.LEVEL_LENGTH;
                instructions = null;
                tiled_map = new TiledMap();
                mapPath = CooperativeGame.levelsPath.get(levelNum).toString();
                tiled_map.setTmx_file_name(CooperativeGame.levelsPath.get(levelNum).toString());
                tiled_map.setFirst_element_amount(mapElementsCounter.getAmounts(mapPath));

                break;

            case 10:
                level_minimal_length = Constants.LEVEL_LENGTH;
                instructions = null;
                tiled_map = new TiledMap();
                mapPath = CooperativeGame.levelsPath.get(levelNum).toString();
                tiled_map.setTmx_file_name(CooperativeGame.levelsPath.get(levelNum).toString());
                tiled_map.setFirst_element_amount(mapElementsCounter.getAmounts(mapPath));

                break;

            case 11:
                level_minimal_length = Constants.LEVEL_LENGTH;
                instructions = null;
                tiled_map = new TiledMap();
                mapPath = CooperativeGame.levelsPath.get(levelNum).toString();
                tiled_map.setTmx_file_name(CooperativeGame.levelsPath.get(levelNum).toString());
                tiled_map.setFirst_element_amount(mapElementsCounter.getAmounts(mapPath));

                break;

            case 12:
                level_minimal_length = Constants.LEVEL_LENGTH;
                instructions = null;
                tiled_map = new TiledMap();
                mapPath = CooperativeGame.levelsPath.get(levelNum).toString();
                tiled_map.setTmx_file_name(CooperativeGame.levelsPath.get(levelNum).toString());
                tiled_map.setFirst_element_amount(mapElementsCounter.getAmounts(mapPath));

                break;

            default:
                level_minimal_length = Constants.LEVEL_LENGTH;
                instructions = null;
                tiled_map = new TiledMap();
                mapPath = CooperativeGame.levelsPath.get(levelNum).toString();
                tiled_map.setTmx_file_name(CooperativeGame.levelsPath.get(levelNum).toString());
                tiled_map.setFirst_element_amount(mapElementsCounter.getAmounts(mapPath));

                break;
        }
    }

    public void setLevel_num(int level_num) {
        this.level_num = level_num;
    }

    public void setInstructions(InstructionElement instructions) {
        this.instructions = instructions;
    }

    public int getLevel_num() {
        return level_num;
    }

    public InstructionElement getInstructions() {
        return instructions;
    }

    public TiledMap getTiled_map() {
        return tiled_map;
    }

    public int getLevel_minimal_length() {
        return level_minimal_length;
    }
}
