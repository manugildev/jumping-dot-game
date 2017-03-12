package com.gikdew.gameobjects;

import C.C;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.gikdew.gameworld.GameWorld;
import com.gikdew.helpers.AssetLoader;
import com.gikdew.tweenaccessors.Value;
import com.gikdew.tweenaccessors.ValueAccessor;

public class Dot {
	private Vector2 position;
	private Vector2 velocity;
	private Vector2 acceleration;

	private int radius;
	float distanceToCenter;
	private float rotation;

	private float angle;
	private float angVel;
	private float angAcc;

	private GameWorld world;
	private Circle circle;
	private CenterCircle centerCircle;
	private TweenManager manager;
	private Value distance = new Value();
	private float jumpDuration;
	private TweenCallback cb;

	public Dot(GameWorld world, float x, float y, int radius, float angleVel,
			float angAcc, final CenterCircle centerCircle) {
		this.world = world;
		this.centerCircle = centerCircle;
		this.position = new Vector2(x, y);
		this.setRadius(radius);
		circle = new Circle(x, y, radius);
		angle = 0;
		this.angVel = angleVel;
		this.angAcc = angAcc;
		distanceToCenter = centerCircle.getRadius() + radius;
		distance.setValue(centerCircle.getRadius() + radius);
		jumpDuration = .3f;
		Tween.registerAccessor(Value.class, new ValueAccessor());
		manager = new TweenManager();
		cb = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				distance.setValue(C.radiusCenterCircle + 15);
			}
		};

	}

	public void update(float delta) {
		angVel += angAcc * delta;
		angle += angVel * delta;
		position = new Vector2(calculatePosition());
		distanceToCenter = distance.getValue()
				+ (centerCircle.getRadius() - C.radiusCenterCircle);
		circle.set(position, radius);
		manager.update(delta);

	}

	private Vector2 calculatePosition() {
		float cx = world.gameWidth / 2;
		float cy = world.gameHeight / 2;
		return new Vector2((float) (cx + distanceToCenter
				* Math.sin(Math.toRadians(-angle))),
				(float) (cy + distanceToCenter
						* Math.cos(Math.toRadians(-angle))));
	}

	public void onClick() {

		if (distance.getValue() < centerCircle.getRadius() + 16) {
			AssetLoader.jump.play();
			centerCircle.jump();
			Tween.to(distance, -1, jumpDuration).target(120).repeatYoyo(1, 0)
					.setCallback(cb)
					.setCallbackTriggers(TweenCallback.COMPLETE)
					.ease(TweenEquations.easeOutSine).start(manager);
		}

	}

	public Vector2 getPosition() {
		return new Vector2(position.x - radius, position.y - radius);
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getAngularVelocity() {
		return angVel;
	}

	public void setAngularVelocity(float angularVelocity) {
		this.angVel = angularVelocity;
	}

	public float getAngularAcceleration() {
		return angAcc;
	}

	public void setAngularAcceleration(float angularAcceleration) {
		this.angAcc = angularAcceleration;
	}

	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}
}
