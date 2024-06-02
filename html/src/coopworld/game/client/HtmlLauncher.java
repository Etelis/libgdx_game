package coopworld.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.user.client.Window;

import coopworld.game.CrossPlatform.CrossPlatformObjects;
import coopworld.game.CooperativeGame;
import coopworld.game.Logs.EntranceData;
import coopworld.game.Logs.Enums;
import coopworld.game.Logs.GameLanguage;
import coopworld.game.Tools.Constants;
import coopworld.game.Tools.Utils;

public class HtmlLauncher extends GwtApplication {
        @Override
        public GwtApplicationConfiguration getConfig () {
                float scale = 0.85f;
                float reducingFactor = 0.95f;
                float ratio = 1.666667f;
                float clientWidth, clientHeight;
                int gameWidth, gameHeight;

                clientWidth = scale * Window.getClientWidth();
                clientHeight = scale * Window.getClientHeight();

                gameHeight = (int)(clientHeight);
                gameWidth = (int)(ratio * gameHeight);

                while(gameWidth > clientWidth){
                        gameWidth *= reducingFactor;
                        gameHeight *= reducingFactor;
                }

                //gameHeight = 648;
                //gameWidth = 1080;
                return new GwtApplicationConfiguration(gameWidth, gameHeight);
                // old - 1080, 648

                //float ratio = 0.85f;
                //return new GwtApplicationConfiguration((int)(ratio * 1280), (int)(ratio * 728));

                //return new GwtApplicationConfiguration(864, 567);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                // user id
                String value = com.google.gwt.user.client.Window.Location.getParameter("val");
                Constants.USER_ID_WEB = value;

                // language
                String language = com.google.gwt.user.client.Window.Location.getParameter("language");
                if(language != null){
                        GameLanguage gameLanguage = Utils.getGameLanguage(language);
                        if(gameLanguage != null){
                                Constants.GAME_LANGUAGE = gameLanguage;
                        }
                }

                // amt?
                String setting = com.google.gwt.user.client.Window.Location.getParameter("setting");
                if(setting != null){
                        if(setting.equals("prolific")) {
                                Constants.IS_PROLIFIC = true;
                                Constants.GAME_LANGUAGE = new GameLanguage(Enums.Language.English, Enums.LanguageDirection.LTR); // AMT works only in English.
                        }
                }

                // master access (for testing and demo)
                String isMaster = com.google.gwt.user.client.Window.Location.getParameter("master");
                if(isMaster != null){
                        if(isMaster.equals("true")) {
                                Constants.MASTER_ACCESS = true;
                        }
                }

                CrossPlatformObjects cpo = new CrossPlatformObjects(new WebInput(), new WebUtils());
                CooperativeGame cooperativeGame = new CooperativeGame(cpo);

                EntranceData entranceData = new EntranceData();
                try{
                        entranceData.setUser_id(Constants.USER_ID_WEB);
                        entranceData.setTime_stamp(cooperativeGame.
                                crossPlatformObjects.getUtils().getTimeStamp());
                        entranceData.setApp_code_name(Window.Navigator.getAppCodeName());
                        entranceData.setApp_name(Window.Navigator.getAppName());
                        entranceData.setApp_version(Window.Navigator.getAppVersion());
                        entranceData.setPlatform(Window.Navigator.getPlatform());
                        entranceData.setIs_cookies_enabled(Window.Navigator.isCookieEnabled());
                        entranceData.setIs_java_enabled(Window.Navigator.isJavaEnabled());
                        entranceData.setClient_width(Window.getClientWidth());
                        entranceData.setClient_height(Window.getClientHeight());
                        entranceData.setScroll_left(Window.getScrollLeft());
                        entranceData.setScroll_top(Window.getScrollTop());
                        entranceData.setQuery_string(Window.Location.getQueryString());
                }
                catch (Exception e){
                        entranceData.setException(e.toString());
                }
                Constants.ENTRANCE_DATA = entranceData;

                return cooperativeGame;
        }
}