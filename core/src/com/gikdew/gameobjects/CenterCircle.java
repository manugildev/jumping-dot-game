package com.gikdew.gameobjects;

import C.C;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.math.Vector2;
import com.gikdew.gameworld.GameWorld;
import com.gikdew.tweenaccessors.Value;
import com.gikdew.tweenaccessors.ValueAccessor;

public class CenterCircle {

	public Vector2 position;
	public float radius;

	private TweenManager manager;
	private Value distance = new Value();
	private TweenCallback cb;

	private int radiusObjective = 60;
	private GameWorld world;

	public CenterCircle(GameWorld world) {
		this.world = world;
		position = new Vector2(world.gameWidth / 2, world.gameHeight / 2);

		radius = C.radiusCenterCircle;
		distance.setValue(radius);
		Tween.registerAccessor(Value.class, new ValueAccessor());
		manager = new TweenManager();
		cb = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {

			}

		};

	}

	public void update(float delta) {
		if (world.isRunning()) {
			manager.update(delta);
		}

		radius = distance.getValue();
	}

	public float getRadius() {
		return radius;
	}

	public void jump() {
		Tween.to(distance, -1, .3f).target(radiusObjective).repeatYoyo(1, 0)
				.setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE)
				.ease(TweenEquations.easeInOutBounce).start(manager);
	}

}
