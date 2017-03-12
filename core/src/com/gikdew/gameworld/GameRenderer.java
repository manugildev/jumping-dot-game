package com.gikdew.gameworld;

import java.util.ArrayList;

import C.C;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.gikdew.gameobjects.Box;
import com.gikdew.gameobjects.CenterCircle;
import com.gikdew.gameobjects.Dot;
import com.gikdew.gameobjects.Menu;
import com.gikdew.gameobjects.ScoreHandler;
import com.gikdew.helpers.AssetLoader;
import com.gikdew.helpers.ColorManager;
import com.gikdew.ui.SimpleButton;

public class GameRenderer {

	private GameWorld world;
	private OrthographicCamera cam;
	private Dot dot, dot1;
	private Box box;
	private Menu menu;
	private CenterCircle cCircle;
	private ColorManager cManager;
	private ScoreHandler scoreHandler;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private TextureRegion dotT, boxT, menuT;
	private ArrayList<SimpleButton> menuButtons;
	ShaderProgram fontShader;

	public GameRenderer(GameWorld world, int gameWidth, int gameHeight) {
		this.world = world;
		cam = new OrthographicCamera();
		cam.setToOrtho(true, gameWidth, gameHeight);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		initAssets();
		initObjects();

		fontShader = new ShaderProgram(Gdx.files.internal("font.vert"),
				Gdx.files.internal("font.frag"));
		if (!fontShader.isCompiled()) {
			Gdx.app.error("fontShader",
					"compilation failed:\n" + fontShader.getLog());
		}
	}

	private void initObjects() {
		dot = world.getDot();
		box = world.getBox();
		scoreHandler = world.getScoreHandler();
		menu = world.getMenu();
		menuButtons = world.getMenu().getMenuButtons();
		cCircle = world.getCenterCircle();
		cManager = world.getColorManager();

	}

	private void initAssets() {
		dotT = AssetLoader.dot;
		boxT = AssetLoader.box;
		menuT = AssetLoader.menuBg;

	}

	public void render(float delta, float runTime) {
		if (C.backgroundColor.equals("")) {
			Gdx.gl.glClearColor(cManager.getColor().r, cManager.getColor().g,
					cManager.getColor().b, cManager.getColor().a);
		} else {
			Gdx.gl.glClearColor(Color.valueOf(C.backgroundColor).r,
					Color.valueOf(C.backgroundColor).g,
					Color.valueOf(C.backgroundColor).b,
					Color.valueOf(C.backgroundColor).a);
		}

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.valueOf(C.secondCenterCircle));
		shapeRenderer.circle(world.gameWidth / 2, world.gameHeight / 2,
				cCircle.getRadius()
						+ (world.gameWidth / 2 - 8 - C.radiusCenterCircle),
				1500);
		shapeRenderer.end();

		batch.begin();
		if (world.isRunning() || world.isReady()) {
			drawDot();
			drawBox();
			drawScore();
			drawCCircle();
			drawMenu();
		} else if (world.isGameover()) {
			drawDot();
			drawBox();
			drawCCircle();
			drawMenu();
			drawHScore();
		} else {
			drawDot();
			drawBox();
			drawCCircle();
			drawMenu();
		}

		batch.end();

	}

	private void drawTapToStart() {
		batch.setShader(fontShader);

		AssetLoader.font.draw(batch, "Tap to Jump", (world.gameWidth / 2)
				- (12 * (("Tap to Jump").length() - 0.9f)),
				menu.getRectangle().height - 65);

		batch.setShader(null);
	}

	private void drawCCircle() {
		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		if (C.centerCircleColor.equals("")) {
			shapeRenderer.setColor(cManager.getColor());
		} else {
			shapeRenderer.setColor(Color.valueOf(C.centerCircleColor));
		}
		shapeRenderer.circle(world.gameWidth / 2, world.gameHeight / 2,
				cCircle.getRadius(), 1000);

		shapeRenderer.setColor(Color.BLUE);

		shapeRenderer.end();
		batch.begin();
	}

	private void drawHScore() {
		batch.setShader(fontShader);
		AssetLoader.font1
				.draw(batch,
						"Score: " + world.getScore(),
						(world.gameWidth / 2)
								- (7.4f * (("Score: " + world.getScore())
										.length() - 0.9f)),
						menu.getRectangle().y
								+ (world.gameHeight / 2 - C.radiusCenterCircle / 1.7f));
		AssetLoader.font1.draw(
				batch,
				"Highscore: " + AssetLoader.getHighScore(),
				(world.gameWidth / 2)
						- (7.4f * (("Highscore: " + AssetLoader.getHighScore())
								.length() - 0.9f)), menu.getRectangle().y
						+ (world.gameHeight / 2));

		batch.setShader(null);
	}

	private void drawMenu() {
		batch.draw(menuT, menu.getRectangle().x, menu.getRectangle().y,
				menu.getRectangle().width, menu.getRectangle().height);
		drawButtons();
		batch.setShader(fontShader);

		AssetLoader.font.draw(batch, C.gameName, (world.gameWidth / 2)
				- (12 * (C.gameName.length() - 0.9f)), menu.getRectangle().y
				+ (world.gameHeight - world.gameWidth / 2) / 2 - 25);

		batch.setShader(null);

	}

	private void drawButtons() {
		menuButtons.get(0).draw(batch);
		menuButtons.get(1).draw(batch);
		menuButtons.get(2).draw(batch);
		menuButtons.get(3).draw(batch);
	}

	private void drawScore() {
		batch.setShader(fontShader);
		if (world.getScore() > 9 && world.getScore() < 20) {
			AssetLoader.font.draw(
					batch,
					"" + world.getScore(),
					(world.gameWidth / 2)
							- (10 * (world.getScore() + "").length() - 1),
					world.gameHeight / 2 - (world.gameWidth / 2 + 50));
		} else if (world.getScore() == 0) {
			AssetLoader.font1.draw(batch, "Tap to Jump", (world.gameWidth / 2)
					- (7.4f * ("Tap to Jump").length()), world.gameHeight / 2
					- (world.gameWidth / 2 + 50));
		} else {
			AssetLoader.font.draw(
					batch,
					"" + world.getScore(),
					(world.gameWidth / 2)
							- (13 * (world.getScore() + "").length() - 1),
					world.gameHeight / 2 - (world.gameWidth / 2 + 50));
		}

		batch.setShader(null);
	}

	private void drawBox() {
		if (C.boxColor.equals("")) {
			batch.setColor(cManager.getColor());
		} else {
			batch.setColor(Color.valueOf(C.boxColor));
		}
		batch.draw(boxT, box.getPosition().x, box.getPosition().y,
				box.getRadius(), box.getRadius(), box.getRadius() * 2,
				box.getRadius() * 2, 1, 1, box.getRotation());
		batch.setColor(Color.WHITE);

	}

	private void drawDot() {
		if (C.ballColor.equals("")) {
			batch.setColor(cManager.getColor());
		} else {
			batch.setColor(Color.valueOf(C.ballColor));
		}
		batch.draw(dotT, dot.getPosition().x, dot.getPosition().y,
				dot.getRadius() * 2, dot.getRadius() * 2);
		batch.setColor(Color.WHITE);
	}
}
