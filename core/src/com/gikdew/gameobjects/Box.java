package com.gikdew.gameobjects;

import C.C;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.gikdew.gameworld.GameWorld;
import com.gikdew.tweenaccessors.Value;
import com.gikdew.tweenaccessors.ValueAccessor;

public class Box {
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
	private Value angValue = new Value();
	private Value distance = new Value();
	private TweenCallback cb;
	private int cicleDuration = 20;
	private int targetVel = 350;

	public Box(final GameWorld world, float x, float y, int radius,
			float angleVel, float angAcc, CenterCircle centerCircle) {
		this.world = world;
		this.centerCircle = centerCircle;
		this.position = new Vector2(x, y);
		this.setRadius(radius);
		circle = new Circle(x, y, radius);
		angle = 0;
		this.angVel = angleVel;
		this.angAcc = angAcc;
		distanceToCenter = centerCircle.getRadius() + radius - 5;
		angValue.setValue(110);
		distance.setValue(centerCircle.getRadius() + radius - 5);

		Tween.registerAccessor(Value.class, new ValueAccessor());
		manager = new TweenManager();
		cb = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				angValue.setValue(110);
				cicleDuration = (int) (Math.random() * 10 + 1);
				targetVel = (int) (Math.random() * 300 + 150 + world.getScore());
				Gdx.app.log("calculate", targetVel + "");
				Tween.to(angValue, -1, cicleDuration).target(targetVel)
						.repeatYoyo(1, 0).setCallback(cb)
						.setCallbackTriggers(TweenCallback.COMPLETE)
						.ease(TweenEquations.easeInOutBounce).start(manager);

			}

		};

		Tween.to(angValue, -1, cicleDuration).target(350).repeatYoyo(1, 0)
				.setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE)
				.ease(TweenEquations.easeInOutBounce).start(manager);

	}

	public void update(float delta) {
		angVel += angAcc * delta;
		angle += angVel * delta;
		rotation = angle;
		position = new Vector2(calculatePosition());
		circle.set(position, radius);
		angVel = angValue.getValue();
		distanceToCenter = distance.getValue()
				+ (centerCircle.getRadius() - C.radiusCenterCircle);

		manager.update(delta);

		// Gdx.app.log("Box vel:", angVel + "");

	}

	public boolean collidesWithDot(Dot dot) {
		if (Intersector.overlaps(getCircle(), dot.getCircle())) {
			world.setScore0();
			return true;
		} else {
			return false;
		}
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

	public float getAngle() {
		return angle;
	}

}
