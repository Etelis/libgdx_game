package coopworld.game.Logs;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import coopworld.game.CooperativeGame;


public class ElementCollectionList {
    private ArrayList<ElementCollection> collectionList;

    public ElementCollectionList() {
        collectionList = new ArrayList<>();
    }

    public void addElementCollection(String elementName) {
        ElementCollection elementCollection = new ElementCollection(elementName);
        collectionList.add(elementCollection);
    }

    public ArrayList<ElementCollection> getCollectionList() {
        return collectionList;
    }

    // Definition of ElementCollection class
    public static class ElementCollection {
        String element_name;
        String collection_time;

        public ElementCollection(String elementName) {
            this.element_name = elementName;
            this.collection_time = ((CooperativeGame) Gdx.app.getApplicationListener()).
                    crossPlatformObjects.getUtils().getTimeStamp();
        }
    }
}

