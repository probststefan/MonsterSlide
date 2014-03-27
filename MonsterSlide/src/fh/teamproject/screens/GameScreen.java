package fh.teamproject.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.Json;

import fh.teamproject.MonsterSlide;
import fh.teamproject.controller.player.android.SwipeController;
import fh.teamproject.game.World;
import fh.teamproject.game.hud.Hud;
import fh.teamproject.utils.CameraManager;
import fh.teamproject.utils.CameraManager.Mode;
import fh.teamproject.utils.Settings;
import fh.teamproject.utils.debug.DebugDrawer;
import fh.teamproject.utils.debug.DebugInfoPanel;
import fh.teamproject.utils.debug.DebugInputController;

public class GameScreen implements Screen {

	public static CameraManager camManager;
	public static Settings settings;
	public static InputMultiplexer allInputs;
	// DEBUG
	private final boolean showFps = true;
	public boolean isPaused = false;
	public DebugDrawer debugDrawer;
	public World world;
	private SpriteBatch spriteBatch;
	private BitmapFont font;
	public Hud hud;
	public MonsterSlide game;

	public GameScreen(Game game) {
		allInputs = new InputMultiplexer();
		this.game = (MonsterSlide) game;
		GameScreen.settings = new Json().fromJson(Settings.class,
				Gdx.files.internal("settings.json"));
		world = new World(this);

		GameScreen.camManager = new CameraManager(this);
		GameScreen.camManager.setMode(Mode.SMOOTH);

		// Debug
		debugDrawer = new DebugDrawer(this);

		hud = new Hud(world);
		hud.generateSlideOverview();

		// Input
		DebugInputController debugInput = new DebugInputController(this);
		InputMultiplexer gameInputMul = new InputMultiplexer();
		InputMultiplexer debugInputMul = new InputMultiplexer();
		debugInputMul.addProcessor(debugInput);
		debugInputMul.addProcessor(DebugInfoPanel.stage);
		debugInputMul.addProcessor((InputProcessor) GameScreen.camManager
				.getController(Mode.FREE));

		gameInputMul.addProcessor(new GestureDetector(new SwipeController(world
				.getPlayer())));
		gameInputMul.addProcessor(hud.stage);
		allInputs.addProcessor(debugInputMul);
		allInputs.addProcessor(gameInputMul);
		Gdx.input.setInputProcessor(allInputs);

		if (showFps) {
			// Wird zur Zeit genutzt um die fps anzuzeigen.
			spriteBatch = new SpriteBatch();
			font = new BitmapFont();
		}

	}

	@Override
	public void render(float delta) {
		// Blauen Himmel aus FEZ zeichnen.
		Gdx.gl.glClearColor(0.18431f, 0.85882f, 1.0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		/* UPDATE */
		if (!isPaused) {
			world.update();
		}
		GameScreen.camManager.update();
		hud.update();

		/* RENDER */
		world.render();
		hud.render();
		if (DebugDrawer.isDebug) {
			debugDrawer.render();
		}
		showFPS();
	}

	@Override
	public void resize(int width, int height) {
		GameScreen.camManager.setViewport(width, height);
		hud.setViewport(width, height);

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		camManager.dispose();
		world.dispose();
	}

	public void showFPS() {
		if (showFps) {
			// FPS anzeigen.
			spriteBatch.begin();
			font.draw(
					spriteBatch,
					Gdx.graphics.getFramesPerSecond()
							+ " fps, Bullet: "
							+ (int) (world.getPhysixManager().getPerformanceCounter().load.value * 100f)
							+ "%", 10, Gdx.graphics.getHeight() - 10);
			spriteBatch.end();
		}
	}

	public AssetManager getAssets() {
		return game.getAssets();
	}

	public MonsterSlide getGame() {
		return game;
	}

	public World getWorld() {
		return world;
	}
}