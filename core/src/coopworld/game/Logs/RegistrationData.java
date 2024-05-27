package coopworld.game.Logs;

import coopworld.game.Tools.Constants;

public class RegistrationData {
    String user_id;
    String registration_time;
    Enums.Gender human_gender;
    Enums.Gender virtual_gender;
    String infrastructure;

    public RegistrationData() {
        this.infrastructure = Constants.INFRASTRUCTURE;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setRegistration_time(String registration_time) {
        this.registration_time = registration_time;
    }

    public void setHuman_gender(Enums.Gender human_gender) {
        this.human_gender = human_gender;
    }

    public void setVirtual_gender(Enums.Gender virtual_gender) {
        this.virtual_gender = virtual_gender;
    }
}
