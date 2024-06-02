package coopworld.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import coopworld.game.CooperativeGame;
import coopworld.game.CrossPlatform.CrossPlatformObjects;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		CrossPlatformObjects cpo = new CrossPlatformObjects(new AndroidInputt(), new AndroidUtils());
		initialize(new CooperativeGame(cpo), config);
	}
}
