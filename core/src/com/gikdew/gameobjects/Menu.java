package com.gikdew.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gikdew.gameworld.GameWorld;
import com.gikdew.helpers.AssetLoader;
import com.gikdew.ui.SimpleButton;

public class Menu {
	private Vector2 position;
	private Vector2 velocity;
	private Vector2 acceleration;

	private GameWorld world;
	private Rectangle rectangle;

	private boolean goingUp = false;
	private boolean goingDown = false;
	private boolean isSet = true;

	private ArrayList<SimpleButton> menuButtons;

	private SimpleButton playButton, rankButton, shareButton, achieveButton;

	public Menu(GameWorld world, float x, float y, float width, float height) {
		this.world = world;
		rectangle = new Rectangle(x, y, width, height);
		position = new Vector2(x, y);
		velocity = new Vector2();
		acceleration = new Vector2();

		// MENU BUTTONS
		menuButtons = new ArrayList<SimpleButton>();
		playButton = new SimpleButton(world, rectangle.width / 2
				- AssetLoader.playButtonDown.getRegionWidth() / 4,
				rectangle.height / 2 - 30, 100 / 2, 100 / 2,
				AssetLoader.playButtonUp, AssetLoader.playButtonDown);
		rankButton = new SimpleButton(world, rectangle.width / 2 - 90,
				rectangle.height / 2 - 30, 100 / 2, 100 / 2,
				AssetLoader.rankButtonUp, AssetLoader.rankButtonDown);

		shareButton = new SimpleButton(world, rectangle.width / 2 + 40,
				rectangle.height / 2 - 30, 100 / 2, 100 / 2,
				AssetLoader.shareButtonUp, AssetLoader.shareButtonDown);

		achieveButton = new SimpleButton(world, rectangle.width / 2
				- AssetLoader.achieveButtonDown.getRegionWidth() / 4,
				rectangle.height / 2 - 30 + 90, 100 / 2, 100 / 2,
				AssetLoader.achieveButtonUp, AssetLoader.achieveButtonDown);

		menuButtons.add(playButton);
		menuButtons.add(rankButton);
		menuButtons.add(shareButton);
		menuButtons.add(achieveButton);
	}

	public void update(float delta) {
		velocity.add(acceleration.cpy().scl(delta));
		position.add(velocity.cpy().scl(delta));

		rectangle.x = position.x;
		rectangle.y = position.y;

		if (position.y <= 0) {
			rectangle.x = 0;
			rectangle.y = 0;
			if (goingUp) {
				stop();
			}
		} else if (position.y >= world.gameHeight) {
			rectangle.x = 0;
			rectangle.y = world.gameHeight;
			if (goingDown) {
				stop();
			}
		}

		playButton.setY(rectangle.y + rectangle.height / 2 + world.gameHeight
				/ 7);
		rankButton.setY(rectangle.y + rectangle.height / 2 + world.gameHeight
				/ 7 + 25 + 5);
		shareButton.setY(rectangle.y + rectangle.height / 2 + world.gameHeight
				/ 7 + 25 + 5);
		achieveButton.setY(rectangle.y + rectangle.height / 2
				+ world.gameHeight / 7 + 50 + 10);
		playButton.update(delta);
		rankButton.update(delta);
		shareButton.update(delta);
		achieveButton.update(delta);
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void goUp() {
		goingUp = true;
		velocity.y = -2000;
		isSet = true;
	}

	public void goDown() {
		goingDown = true;
		velocity.y = +2000;
		isSet = false;
	}

	public void stop() {
		goingUp = false;
		goingDown = false;
		velocity.y = 0;
	}

	public boolean isMoving() {
		return goingDown || goingUp;
	}

	public ArrayList<SimpleButton> getMenuButtons() {
		return menuButtons;
	}
}
