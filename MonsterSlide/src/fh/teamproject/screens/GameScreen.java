package fh.teamproject.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

import fh.teamproject.controller.player.android.SwipeController;
import fh.teamproject.entities.Player;
import fh.teamproject.entities.World;
import fh.teamproject.hud.Hud;
import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.utils.CameraManager;
import fh.teamproject.utils.CameraManager.Mode;
import fh.teamproject.utils.debug.DebugDrawer;
import fh.teamproject.utils.debug.DebugInfoPanel;
import fh.teamproject.utils.debug.DebugInputController;

public class GameScreen implements Screen {
	// DEBUG
	private final boolean showFps = true;
	public static boolean isDebug = false;
	public boolean isPaused = false;
	public DebugDrawer debugDrawer;
	// Controller
	public SwipeController swipeController;
	public CameraManager camManager;

	// Rendering
	public ModelBatch batch;
	public Environment lights;

	// Logic
	public World world;
	public Player player;

	private SpriteBatch spriteBatch;
	private BitmapFont font;

	public Hud hud;
	public Game game;

	public GameScreen(Game game) {
		this.game = game;
		this.world = new World(this);
		this.player = (Player) this.world.getPlayer();

		// this.swipeController = new SwipeController(this.player);
		this.camManager = new CameraManager(this);
		this.camManager.setMode(Mode.CHASE);

		this.batch = new ModelBatch();

		this.lights = new Environment();
		this.lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f,
				1f));
		this.lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		// Debug
		this.debugDrawer = new DebugDrawer(this);

		this.hud = new Hud(this);

		// Input
		DebugInputController debugInput = new DebugInputController(this);
		InputMultiplexer gameInputMul = new InputMultiplexer();
		InputMultiplexer debugInputMul = new InputMultiplexer();
		InputMultiplexer allInputs = new InputMultiplexer();
		debugInputMul.addProcessor(debugInput);
		debugInputMul.addProcessor(DebugInfoPanel.stage);
		debugInputMul.addProcessor((InputProcessor) this.camManager
				.getController(Mode.FREE));

		// gameInputMul.addProcessor(new GestureDetector(this.swipeController));
		// gameInputMul.addProcessor(this.player.inputHandling);
		gameInputMul.addProcessor(this.hud.stage);
		allInputs.addProcessor(gameInputMul);
		allInputs.addProcessor(debugInputMul);
		Gdx.input.setInputProcessor(allInputs);

		if (this.showFps) {
			// Wird zur Zeit genutzt um die fps anzuzeigen.
			this.spriteBatch = new SpriteBatch();
			this.font = new BitmapFont();
		}

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		if (!this.isPaused) {
			/* UPDATE */
			this.world.update();
		}
		this.camManager.update();
		this.hud.update();

		/* RENDER */
		this.batch.begin(this.camManager.getActiveCamera());
		// Player rendern.
		this.batch.render(this.player.instance, this.lights);
		// Rutschelemente rendern.
		for (ISlidePart slidePart : this.world.getSlide().getSlideParts()) {
			this.batch.render(slidePart.getModelInstance(), this.lights);
		}
		this.batch.end();
		this.hud.render();
		if (GameScreen.isDebug) {
			this.debugDrawer.render();
		}
		this.showFPS();

	}

	@Override
	public void resize(int width, int height) {
		this.camManager.setViewport(width, height);
		this.hud.setViewport(width, height);

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
		System.out.println("Dispose");
		this.world.dispose();
	}

	public void showFPS() {
		if (this.showFps) {
			// FPS anzeigen.
			this.spriteBatch.begin();
			this.font.draw(this.spriteBatch, Gdx.graphics.getFramesPerSecond()
					+ " fps, Bullet: "
					+ (int) (this.world.performanceCounter.load.value * 100f) + "%", 10,
					Gdx.graphics.getHeight() - 10);
			this.spriteBatch.end();
		}
	}
}
