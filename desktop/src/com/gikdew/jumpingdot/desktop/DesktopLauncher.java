package com.gikdew.jumpingdot.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gikdew.jumpingdot.JumpingDotGame;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Jumping Dot";
		config.width = 320;
		config.height = 480;
		new LwjglApplication(new JumpingDotGame(new ActionResolverDesktop()),
				config);
	}
}
