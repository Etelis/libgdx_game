package coopworld.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import coopworld.game.CooperativeGame;
import coopworld.game.Scenes.AnimatedImage;

/**
 * Created by Chen on 22/06/2017.
 */

public class GraphicTools {

    public static Label getUserIdLabel() {
        float fontSize = Constants.VIEWPORT_WIDTH * (1.5f / 1280f);
        /* stopwatch and display scores */
        Label.LabelStyle textStyle = new Label.LabelStyle();
        textStyle.fontColor = new Color(32f/255f, 4f/255f, 110f/255f, 1);
        if(Constants.MASTER_ACCESS){
            textStyle.fontColor = new Color(204f/255f, 31f/255f, 31f/255f, 1);
        }
        textStyle.font = new BitmapFont(Gdx.files.internal("fonts/myFont.fnt"), false);
        textStyle.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);

        Label userIdLabel = new Label(CooperativeGame.gameData.getUser().getUser_id(), textStyle);
        userIdLabel.setPosition((float) 0.02 * Constants.VIEWPORT_WIDTH,
                (float) 0.025 * Constants.VIEWPORT_HEIGHT);
        userIdLabel.setFontScale(fontSize);

        return userIdLabel;
    }

    public static Label getVerNumLabel(String verNum) {
        float fontSize = Constants.VIEWPORT_WIDTH * (1.5f / 1280f);
        /* stopwatch and display scores */
        Label.LabelStyle textStyle = new Label.LabelStyle();
        textStyle.fontColor = new Color(87f/255f, 103f/255f, 139f/255f, 1);
        textStyle.font = new BitmapFont(Gdx.files.internal("fonts/myFont.fnt"), false);
        textStyle.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);

        Label userIdLabel = new Label("ver. " + verNum + ", " + PersistentData.GAME_GROUP + " Group", textStyle);
        userIdLabel.setPosition((float) 0.01 * Constants.VIEWPORT_WIDTH,
                (float) 0.01 * Constants.VIEWPORT_HEIGHT);
        userIdLabel.setFontScale(fontSize);

        return userIdLabel;
    }

    public static Label getUserNameLabel() {
        float fontSize = Constants.VIEWPORT_WIDTH * (1.4f / 1280f);
        /* stopwatch and display scores */
        Label.LabelStyle textStyle = new Label.LabelStyle();
        textStyle.fontColor = new Color(87f/255f, 103f/255f, 139f/255f, 1);
        textStyle.font = new BitmapFont(Gdx.files.internal("fonts/myFont.fnt"), false);
        textStyle.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);

        //Label userIdLabel = new Label(CooperativeGame.gameData.getUser().getName(), textStyle);
        Label userIdLabel = new Label(CooperativeGame.gameData.getUser().getName(), textStyle);

        userIdLabel.setPosition((float) 0.02 * Constants.VIEWPORT_WIDTH,
                (float) 0.01 * Constants.VIEWPORT_HEIGHT);
        userIdLabel.setFontScale(fontSize);

        return userIdLabel;
    }

    public static Label getLabelByText(String text) {
        float fontSize = Constants.VIEWPORT_WIDTH * (1.2f / 1280f);
        /* stopwatch and display scores */
        Label.LabelStyle textStyle = new Label.LabelStyle();
        textStyle.fontColor = new Color(87f/255f, 103f/255f, 139f/255f, 1);
        textStyle.font = new BitmapFont(Gdx.files.internal("fonts/myFont.fnt"), false);
        textStyle.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);

        textStyle.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest,
                Texture.TextureFilter.Nearest);
        Label label = new Label(text, textStyle);

        label.setFontScale(fontSize);

        return label;
    }

    public static TextureAtlas getTextureAtlas(String path){
        TextureAtlas textureAtlas;

        if(CooperativeGame.loader.getAssetManager().isLoaded(path)) {
            // texture is available, let's fetch it and do something interesting
            textureAtlas = CooperativeGame.loader.getAssetManager().get(path, TextureAtlas.class);
        }
        else{
            textureAtlas = new TextureAtlas(path);
        }
        for(Texture texture : textureAtlas.getTextures()){
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        return textureAtlas;
    }

    public static Texture getTexture(String path){
        Texture texture;
        boolean error = false, loaded = true;
        if(!CooperativeGame.loader.getAssetManager().isLoaded(path)) {
            try {
                loaded = false;
                CooperativeGame.loader.getAssetManager().load(path, Texture.class);
                CooperativeGame.loader.getAssetManager().finishLoading();
            } catch (Exception e) {
                error = true;
                // TODO COOP2 - SEND ERROR LOG. ALSO, CONSIDER IF REGULAR TEXTURE CREATION IS OK.
            }
        }

        if(!CooperativeGame.loader.getAssetManager().isLoaded(path) || error){ // problem with loading or still not loaded.
            texture = new Texture(path);
        }
        else{ // loaded successfully
            texture = CooperativeGame.loader.getAssetManager().get(path, Texture.class);
        }
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return texture;
    }

    public static Image getButtonPNG(String path){
        Skin skin = new Skin();
        Texture texture = getTexture(path);

        skin.add("button", texture);
        Image button = new Image(skin, "button");

        return button;
    }

    public static TextButton getButtonPack(String path, String drawableName, String drawableNameUp){
        Skin skin = new Skin();
        BitmapFont font = new BitmapFont();
        TextButton button;
        TextureAtlas texture;
        if(CooperativeGame.loader.getAssetManager().isLoaded(path)) {
            // texture is available, let's fetch it and do something interesting
            texture = CooperativeGame.loader.getAssetManager().get(path, TextureAtlas.class);
        }
        else{
            texture = new TextureAtlas(path);
        }
        skin.add("button", texture);
        skin.addRegions(texture);
        // button's style
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = skin.getDrawable(drawableName);
        style.down = skin.getDrawable(drawableNameUp);
        style.font = font;
        button = new TextButton("", style);

        return button;
    }

    /*
    public static BasicAnimatedImage getBasicAnimatedImage(String packPath, float frameDuration,
                                                           String name, int times){
        TextureAtlas atlas;
        atlas = GraphicTools.getTextureAtlas(packPath);

        Animation<TextureRegion> animation;
        animation = new Animation<TextureRegion>(frameDuration,
                atlas.findRegions(name), Animation.PlayMode.NORMAL);
        BasicAnimatedImage bai = new BasicAnimatedImage(animation, times);
        return bai;
    }
     */

    public static AnimatedImage getAnimatedImage(String packPath, float frameDuration,
                                                           String name){
        TextureAtlas atlas;
        /* open pack image og ice girl player*/
        if(CooperativeGame.loader.getAssetManager().isLoaded(packPath)) {
            // texture is available, let's fetch it and do something interesting
            atlas = CooperativeGame.loader.getAssetManager().get(packPath, TextureAtlas.class);
        }
        else{
            atlas = new TextureAtlas(Gdx.files.internal(packPath));
        }

        Animation<TextureRegion> animation;
        animation = new Animation<TextureRegion>(frameDuration,
                atlas.findRegions(name), Animation.PlayMode.NORMAL);
        AnimatedImage ai = new AnimatedImage(animation);
        return ai;
    }

    public static Label getLabel(String text, Color color, float xPos, float yPos, float fontScale){
        Label.LabelStyle textStyle = new Label.LabelStyle();
        textStyle.fontColor = color;
        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/myFont.fnt"), false);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        textStyle.font = font;

        Label scoreRealLabel = new Label(text, textStyle);
        scoreRealLabel.setPosition(xPos, yPos);
        scoreRealLabel.setFontScale(fontScale);

        return scoreRealLabel;
    }

    public static SelectBox<String> getSelectBoxImages(String[] items){
        SelectBox<String> selectBox;

        Table table = new Table();
        table.setFillParent(true);
        table.row();

        Skin uiskin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        uiskin.getFont("default-font").getData().setScale(4f / 1920 * Constants.VIEWPORT_WIDTH);
        selectBox = new SelectBox<String>(uiskin);
        selectBox.setItems(items);
        /* pack -  set the select box size (width and height). Match to the text length. */
        selectBox.pack();

        return selectBox;
    }

    public static Image getNoWiFi(){
        Image noWifi = GraphicTools.getButtonPNG("wifi_obligation/no-wifi.png");
        noWifi.setPosition(0.9f * Constants.VIEWPORT_WIDTH, 0.01f * Constants.VIEWPORT_HEIGHT);
        noWifi.setSize(0.08f * Constants.VIEWPORT_WIDTH, 0.08f * Constants.VIEWPORT_WIDTH);
        return noWifi;
    }

    public static float getHeight(Image img, float width){
        return (width * img.getDrawable().getMinHeight()) / img.getDrawable().getMinWidth();
    }
}
