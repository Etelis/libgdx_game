package coopworld.game.Logs;

import java.util.TreeMap;

/**
 * Created by User on 14/01/2017.
 */

public class User {
    public String user_id; // Primary Key. Unique!
    public String name;
    private String human_gender, virtual_gender; // Female/Male.
    int levels_played;
    int high_score;
    String registration_time;
    String was_registered;
    String experimenter;

    private transient Enums.Gender humanGender, virtualGender;

    public User() {
        //user_id = String.valueOf(new Random().nextInt(100000));
    }

    public void setGenders(){
        // GENDER BASED.
        Enums.Gender virtualGender = null;
        Enums.Gender humanGender = null;

        if(virtual_gender != null){
            if(virtual_gender.toLowerCase().equals("female")){
                virtualGender = Enums.Gender.Female;
            }
            else{
                virtualGender = Enums.Gender.Male;
            }
        }

        if (human_gender != null) {
            if (human_gender.toLowerCase().equals("female")) {
                humanGender = Enums.Gender.Female;
            } else {
                humanGender = Enums.Gender.Male;
            }
        }

        this.virtualGender = virtualGender;
        this.humanGender = humanGender;
    }

    public void setHuman_gender(String gender) {
        this.human_gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHigh_score(int high_score) {
        this.high_score = high_score;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public int getHigh_score() {
        return high_score;
    }

    public Enums.Gender getHuman_gender() {
        return humanGender;
    }

    public TreeMap<Integer, String> getTextual_responses() {
        /*
        TreeMap<Integer, String> tr = new TreeMap<Integer, String>();
        HashMap<Integer, LevelLocalDetails> l = this.getLocal_levels_details();

        for (Map.Entry<Integer, LevelLocalDetails> pair : this.getLocal_levels_details().entrySet()) {
            //Integer i = pair.getKey();
            String levelNumStr = String.valueOf(pair.getKey());
            tr.put(Integer.parseInt(levelNumStr), pair.getValue().getTextual_output());
        }
        return tr;
         */
        return null;
    }

    public void setVirtual_gender(String virtual_gender) {
        this.virtual_gender = virtual_gender;
    }

    public Enums.Gender getVirtual_gender() {
        return virtualGender;
    }

    public int getLevels_played() {
        return levels_played;
    }

    public void setRegistration_time(String registration_time) {
        this.registration_time = registration_time;
    }

    public void setWas_registered(String was_registered) {
        this.was_registered = was_registered;
    }

    public String getWas_registered() {
        return was_registered;
    }

    public void setLevels_played(int levels_played) {
        this.levels_played = levels_played;
    }

    public void reset() {
        this.levels_played = 0;
        this.human_gender = null;
        this.virtual_gender = null;
        this.levels_played = 0;
        this.high_score = 0;
        this.registration_time = null;
        this.was_registered = "No";
    }

    public String getExperimenter() {
        return experimenter;
    }
}
