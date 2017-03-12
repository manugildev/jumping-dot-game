package com.gikdew.gameworld;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.gikdew.gameobjects.Box;
import com.gikdew.gameobjects.CenterCircle;
import com.gikdew.gameobjects.Dot;
import com.gikdew.gameobjects.Menu;
import com.gikdew.gameobjects.ScoreHandler;
import com.gikdew.helpers.AssetLoader;
import com.gikdew.helpers.ColorManager;
import com.gikdew.jumpingdot.ActionResolver;
import com.gikdew.tweenaccessors.Value;
import com.gikdew.tweenaccessors.ValueAccessor;

public class GameWorld {

	private Dot dot;
	private Box box;
	private ScoreHandler scoreHandler;
	private Menu menu;
	private CenterCircle centerCircle;
	public float sW = Gdx.graphics.getWidth();
	public float sH = Gdx.graphics.getHeight();
	public float gameWidth = 320;
	public float gameHeight = sH / (sW / gameWidth);
	private int score = 0;

	private TweenCallback cb, cb1;
	private TweenManager manager;
	private Value distance = new Value();
	private Value distance1 = new Value();

	public enum GameState {
		READY, RUNNING, GAMEOVER, MENU
	}

	public GameState currentState;
	public ColorManager colorManager;

	public ActionResolver actionResolver;

	public GameWorld(final ActionResolver actionResolver) {
		this.actionResolver = actionResolver;
		menu = new Menu(this, 0, 0, gameWidth, gameHeight);
		centerCircle = new CenterCircle(this);

		dot = new Dot(this, gameWidth / 2, gameHeight / 2 - 50 - 15, 15, -180,
				0, centerCircle);
		box = new Box(this, gameWidth / 2 - 50 - 15, gameHeight / 2, 15, 130,
				0, centerCircle);
		scoreHandler = new ScoreHandler(this, box);
		colorManager = new ColorManager();
		currentState = GameState.MENU;
		distance.setValue(0);
		Tween.registerAccessor(Value.class, new ValueAccessor());
		manager = new TweenManager();
		cb = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				actionResolver.showOrLoadInterstital();
				distance.setValue(0);
			}

		};
		cb1 = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				actionResolver.submitScore(getScore());

			}

		};

	}

	public void update(float delta) {
		manager.update(delta);
		switch (currentState) {
		case READY:
			dot.update(delta);
			box.update(delta);
			centerCircle.update(delta);
			colorManager.update(delta);
			menu.update(delta);
			break;

		case RUNNING:
			dot.update(delta);
			box.update(delta);
			menu.update(delta);
			centerCircle.update(delta);
			colorManager.update(delta);
			scoreHandler.update(delta);
			// Collisions
			if (scoreHandler.collidesWithDot(dot) || box.collidesWithDot(dot)) {

			}

			break;
		default:
			dot.update(delta);
			box.update(delta);
			menu.update(delta);
			centerCircle.update(delta);
			colorManager.update(delta);

			break;
		}

	}

	public Dot getDot() {
		return dot;
	}

	public Box getBox() {
		return box;
	}

	public Menu getMenu() {
		return menu;
	}

	public ScoreHandler getScoreHandler() {
		return scoreHandler;
	}

	public CenterCircle getCenterCircle() {
		return centerCircle;
	}

	public ColorManager getColorManager() {
		return colorManager;
	}

	public int getScore() {
		return score;
	}

	public void setScore0() {
		AssetLoader.dead.play();
		menu.goUp();
		currentState = GameState.GAMEOVER;
		Tween.to(distance1, -1, .7f).target(1).repeatYoyo(0, 0)
				.setCallback(cb1).setCallbackTriggers(TweenCallback.COMPLETE)
				.ease(TweenEquations.easeInOutBounce).start(manager);
		if (score > AssetLoader.getHighScore()) {
			AssetLoader.setHighScore(score);

		} else {
			if (Math.random() < 0.5f) {
				Tween.to(distance, -1, .5f).target(1).repeatYoyo(0, 0)
						.setCallback(cb)
						.setCallbackTriggers(TweenCallback.COMPLETE)
						.ease(TweenEquations.easeInOutBounce).start(manager);
			}
		}

	}

	public void restart() {
		score = 0;
	}

	public void addScore(int increment) {
		score += increment;
		// ACHIEVEMENTS
		if (actionResolver.isSignedIn()) {
			if (score >= 10)
				actionResolver.unlockAchievementGPGS("CgkItqKa2ZoTEAIQAg");
			if (score >= 25)
				actionResolver.unlockAchievementGPGS("CgkItqKa2ZoTEAIQAw");
			if (score >= 50)
				actionResolver.unlockAchievementGPGS("CgkItqKa2ZoTEAIQBA");
			if (score >= 100)
				actionResolver.unlockAchievementGPGS("CgkItqKa2ZoTEAIQBQ");
			if (score >= 200)
				actionResolver.unlockAchievementGPGS("CgkItqKa2ZoTEAIQBg");
		}
	}

	public boolean isReady() {
		return currentState == GameState.READY;
	}

	public boolean isGameover() {
		return currentState == GameState.GAMEOVER;
	}

	public boolean isRunning() {
		return currentState == GameState.RUNNING;
	}

	public boolean isMenu() {
		return currentState == GameState.MENU;
	}

}
