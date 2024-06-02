package coopworld.game.Logs;

import coopworld.game.Tools.Constants;

public class EntranceData {
    String user_id;
    String query_string;
    String time_stamp;
    String app_code_name;
    String app_name;
    String app_version;
    String platform;
    String user_agent;
    boolean is_cookies_enabled;
    boolean is_java_enabled;
    int client_width;
    int client_height;
    int scroll_left;
    int scroll_top;
    String exception;

    String infrastructure;

    public EntranceData() {
        this.infrastructure = Constants.INFRASTRUCTURE;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setQuery_string(String query_string) {
        this.query_string = query_string;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public void setApp_code_name(String app_code_name) {
        this.app_code_name = app_code_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public void setIs_cookies_enabled(boolean is_cookies_enabled) {
        this.is_cookies_enabled = is_cookies_enabled;
    }

    public void setIs_java_enabled(boolean is_java_enabled) {
        this.is_java_enabled = is_java_enabled;
    }

    public void setClient_width(int client_width) {
        this.client_width = client_width;
    }

    public void setClient_height(int client_height) {
        this.client_height = client_height;
    }

    public void setScroll_left(int scroll_left) {
        this.scroll_left = scroll_left;
    }

    public void setScroll_top(int scroll_top) {
        this.scroll_top = scroll_top;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
