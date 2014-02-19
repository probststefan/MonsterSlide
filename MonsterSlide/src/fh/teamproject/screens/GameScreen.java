package fh.teamproject.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;

import fh.teamproject.controller.player.android.SwipeController;
import fh.teamproject.entities.Player;
import fh.teamproject.entities.World;
import fh.teamproject.hud.Hud;
import fh.teamproject.utils.CameraManager;
import fh.teamproject.utils.CameraManager.Mode;
import fh.teamproject.utils.Settings;
import fh.teamproject.utils.debug.DebugDrawer;
import fh.teamproject.utils.debug.DebugInfoPanel;
import fh.teamproject.utils.debug.DebugInputController;

public class GameScreen implements Screen {

	public static CameraManager camManager;
	public static Settings settings;

	// DEBUG
	private final boolean showFps = true;
	public boolean isPaused = true;
	public DebugDrawer debugDrawer;
	// Controller
	public SwipeController swipeController;

	// Logic
	public World world;
	public Player player;

	private SpriteBatch spriteBatch;
	private BitmapFont font;

	public Hud hud;
	public Game game;

	public GameScreen(Game game) {
		this.game = game;
		GameScreen.settings = new Json().fromJson(Settings.class,
				Gdx.files.internal("settings.json"));
		world = new World(this);
		player = (Player) world.getPlayer();

		GameScreen.camManager = new CameraManager(this);
		GameScreen.camManager.setMode(Mode.FREE);

		// Debug
		debugDrawer = new DebugDrawer(this);

		hud = new Hud(this);
		hud.generateSlideOverview();

		// Input
		DebugInputController debugInput = new DebugInputController(this);
		InputMultiplexer gameInputMul = new InputMultiplexer();
		InputMultiplexer debugInputMul = new InputMultiplexer();
		InputMultiplexer allInputs = new InputMultiplexer();
		debugInputMul.addProcessor(debugInput);
		debugInputMul.addProcessor(DebugInfoPanel.stage);
		debugInputMul.addProcessor((InputProcessor) GameScreen.camManager
				.getController(Mode.FREE));

		// gameInputMul.addProcessor(this.player.inputHandling);
		gameInputMul.addProcessor(hud.stage);
		allInputs.addProcessor(gameInputMul);
		allInputs.addProcessor(debugInputMul);
		Gdx.input.setInputProcessor(allInputs);

		if (showFps) {
			// Wird zur Zeit genutzt um die fps anzuzeigen.
			spriteBatch = new SpriteBatch();
			font = new BitmapFont();
		}

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		/* UPDATE */
		if (!isPaused) {
			world.update();
		}

		if (!world.checkIsPlayerOnSlide() && !isPaused) {
			this.game.setScreen(new MenuScreen(this.game));
			this.dispose();
		} else {
			GameScreen.camManager.update();
			hud.update();
			hud.setPoints((int) world.getSlide().getSlidedDistance());

			/* RENDER */
			world.render();
			hud.render();

			if (DebugDrawer.isDebug) {
				debugDrawer.render();
			}

			showFPS();
		}
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
			font.draw(spriteBatch, Gdx.graphics.getFramesPerSecond() + " fps, Bullet: "
					+ (int) (world.performanceCounter.load.value * 100f) + "%", 10,
					Gdx.graphics.getHeight() - 10);
			spriteBatch.end();
		}
	}
}
