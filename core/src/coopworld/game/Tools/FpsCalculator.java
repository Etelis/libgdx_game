package coopworld.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;

import coopworld.game.CooperativeGame;
import coopworld.game.CrossPlatform.GeneralUtils;

public class FpsCalculator {
    Stage stage;
    long currTime;
    long elapsedTime;
    long lastUpdateTime;

    private long UPDATE_DELAY;
    private ArrayList<Integer> fps_list;

    Label fps_label;
    private int currFps;

    private GeneralUtils generalUtils;

    public FpsCalculator(Stage stage, float seconds){
        this.generalUtils = ((CooperativeGame) Gdx.app.getApplicationListener()).
                crossPlatformObjects.getUtils();
        this.stage = stage;
        this.UPDATE_DELAY = (long)seconds * 1000000000L;
        this.currTime = generalUtils.getNanoTime();
        this.lastUpdateTime = this.currTime;
        this.elapsedTime = 0;
        this.fps_list = new ArrayList<>();

        if(Constants.IS_CHEAT) {
            createLabel();
            stage.addActor(fps_label);
        }
    }

    public void render(){
        currTime = generalUtils.getNanoTime();

        elapsedTime = currTime - lastUpdateTime;
        if (elapsedTime >= UPDATE_DELAY) {
            currFps = Gdx.graphics.getFramesPerSecond();
            if(Constants.IS_CHEAT) {
                updateLabel(String.valueOf(currFps));
            }
            updateList(Gdx.graphics.getFramesPerSecond());
            //updateList(GameScreen.humanAskState.toString());

            lastUpdateTime = generalUtils.getNanoTime();
        }
    }

    public void updateLabel(String fps){
        fps_label.setText(fps);
    }

    public void updateList(int fps){
        fps_list.add(fps);
    }

    public void createLabel(){
        fps_label = GraphicTools.getLabelByText("Calculating fps...");
    }

    public double getAvgFps(){
        Integer sum = 0;

        for (Integer fps : fps_list) {
            sum += fps;
        }
        return Utils.round(sum.doubleValue() / fps_list.size(), 2);
    }

    public FpsInfo getFpsInfo(){
        ArrayList<Integer> list = new ArrayList<Integer>();
        /*
        histogram.put("0-9", 0);
        histogram.put("10-19", 0);
        histogram.put("20-29", 0);
        histogram.put("30-39", 0);
        histogram.put("40-49", 0);
        histogram.put("50-59", 0);
        histogram.put("60-69", 0);
        histogram.put("70+", 0);
        histogram.put("minus", 0);

        // error.
        */
        for(int i = 0; i < 10; i++){
            list.add(0);
        }

        int index = 0;
        int size = fps_list.size();
        double avg;
        int sum = 0;


        for(Integer fps : fps_list){
            sum += fps;
            if(fps >= 0 && fps <= 10){
                index = 0;
            }
            else if(fps > 10 && fps <= 20){
                index = 1;
            }
            else if(fps > 20 && fps <= 30){
                index = 2;
            }
            else if(fps > 30 && fps <= 40){
                index = 3;
            }
            else if(fps > 40 && fps <= 50){
                index = 4;
            }
            else if(fps > 50 && fps <= 60){
                index = 5;
            }
            else if(fps > 60 && fps <= 70){
                index = 6;
            }
            else if(fps > 70){
                index = 7;
            }
            else if(fps < 0){
                index = 8;
            }
            else{
                index = 9;
            }

            list.set(index, list.get(index) + 1);
        }
        avg = Utils.round((double)sum / (double)fps_list.size(), 2);
        String listStr = Utils.arrayListToArr(list);
        FpsInfo fpsInfo = new FpsInfo(avg, listStr);

        return fpsInfo;
    }
}