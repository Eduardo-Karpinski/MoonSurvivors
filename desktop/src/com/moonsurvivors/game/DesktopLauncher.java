package com.moonsurvivors.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.moonsurvivors.game.screen.MoonSurvivors;

// https://deepdivegamestudio.itch.io/magical-asset-pack
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		config.setForegroundFPS(120);
		config.setTitle("Moon Survivors");
		config.setWindowIcon("ui/ico.jpg");
		new Lwjgl3Application(new MoonSurvivors(), config);
	}
}