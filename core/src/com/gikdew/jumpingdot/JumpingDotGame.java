package com.gikdew.jumpingdot;

import com.badlogic.gdx.Game;
import com.gikdew.helpers.AssetLoader;
import com.gikdew.screens.SplashScreen;

public class JumpingDotGame extends Game {

	private ActionResolver actionResolver;

	public JumpingDotGame(ActionResolver actionResolver) {
		this.actionResolver = actionResolver;

	}

	@Override
	public void create() {
		AssetLoader.load();
		setScreen(new SplashScreen(this, actionResolver));
	}

	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}

}
