package com.gikdew.gameobjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.gikdew.gameworld.GameWorld;

public class ScoreHandler {

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
	private Box box;

	private boolean isScored = false;

	public ScoreHandler(GameWorld world, Box box) {
		this.world = world;
		this.position = new Vector2(10, 10);
		this.box = box;
		this.radius = 15;
		circle = new Circle(10, 10, 15);
		angle = box.getAngle();
		distanceToCenter = 60 + 50 + 10;

	}

	public void update(float delta) {
		angle += box.getAngularVelocity() * delta;
		angle = box.getAngle();
		position = new Vector2(calculatePosition());
		circle = new Circle(position, radius);

	}

	private Vector2 calculatePosition() {
		float cx = world.gameWidth / 2;
		float cy = world.gameHeight / 2;
		Vector2 pos = new Vector2((float) (cx + distanceToCenter
				* Math.sin(Math.toRadians(-angle))),
				(float) (cy + distanceToCenter
						* Math.cos(Math.toRadians(-angle))));

		return pos;
	}

	public boolean collidesWithDot(Dot dot) {
		if (Intersector.overlaps(getCircle(), dot.getCircle())) {
			if (!isScored) {
				world.addScore(1);
				isScored = true;
				return true;
			}
			return false;

		} else {
			isScored = false;
			return false;
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
