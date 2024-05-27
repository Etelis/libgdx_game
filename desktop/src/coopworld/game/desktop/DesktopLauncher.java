package coopworld.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import coopworld.game.CrossPlatform.CrossPlatformObjects;
import coopworld.game.CooperativeGame;

// not in use
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		CrossPlatformObjects cpo = new CrossPlatformObjects(new DesktopInput(), new DesktopUtils());
		new LwjglApplication(new CooperativeGame(cpo), config);
	}
}