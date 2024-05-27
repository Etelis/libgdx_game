package coopworld.game.Logs;

/**
 * Created by Chen on 11/04/2017.
 */

// a tiled map - how the level looks, elements first position etc.
public class TiledMap {
    private String tmx_file_name;
    private FirstElementsAmount first_element_amount;

    public TiledMap() {
    }

    public void setTmx_file_name(String tmx_file_name) {
        this.tmx_file_name = tmx_file_name;
    }

    public void setFirst_element_amount(FirstElementsAmount first_element_amount) {
        this.first_element_amount = first_element_amount;
    }
}
