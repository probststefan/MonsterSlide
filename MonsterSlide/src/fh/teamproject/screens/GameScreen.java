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
import com.badlogic.gdx.math.Vector3;

import fh.teamproject.controller.player.android.SwipeController;
import fh.teamproject.entities.BezierSlidePart;
import fh.teamproject.entities.Player;
import fh.teamproject.entities.World;
import fh.teamproject.hud.Hud;
import fh.teamproject.utils.CameraManager;
import fh.teamproject.utils.CameraManager.Mode;
import fh.teamproject.utils.debug.DebugDrawer;
import fh.teamproject.utils.debug.DebugInfoPanel;
import fh.teamproject.utils.debug.DebugInputController;

public class GameScreen implements Screen {
	// DEBUG
	private final boolean showFps = true;
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

	BezierSlidePart bezPart;
	public GameScreen(Game game) {
		this.game = game;
		world = new World(this);
		player = (Player) world.getPlayer();

		// this.swipeController = new SwipeController(this.player);
		camManager = new CameraManager(this);
		camManager.setMode(Mode.CHASE);

		batch = new ModelBatch();

		lights = new Environment();
		lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f,
				1f));
		lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		// Debug
		debugDrawer = new DebugDrawer(this);

		hud = new Hud(this);

		// Input
		DebugInputController debugInput = new DebugInputController(this);
		InputMultiplexer gameInputMul = new InputMultiplexer();
		InputMultiplexer debugInputMul = new InputMultiplexer();
		InputMultiplexer allInputs = new InputMultiplexer();
		debugInputMul.addProcessor(debugInput);
		debugInputMul.addProcessor(DebugInfoPanel.stage);
		debugInputMul.addProcessor((InputProcessor) camManager
				.getController(Mode.FREE));

		// gameInputMul.addProcessor(new GestureDetector(this.swipeController));
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

		// Tryouts
		Vector3 start = new Vector3(-10.0f, -3.0f, -40.0f);
		Vector3 control1 = new Vector3(0.0f, 10.0f, 10.0f);
		Vector3 control2 = new Vector3(0.0f, -15.0f, 15.0f);
		Vector3 end = new Vector3(0.0f, -40.0f, 200.0f);
		bezPart = new BezierSlidePart(start, end, control1, control2, 0.1f);
		world.getWorld().addRigidBody(bezPart.rigidBody);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		if (!isPaused) {
			/* UPDATE */
			world.update();
		}
		camManager.update();
		hud.update();

		/* RENDER */
		batch.begin(camManager.getActiveCamera());
		// Player rendern.
		batch.render(player.instance, lights);
		// Rutschelemente rendern.
		// for (ISlidePart slidePart : world.getSlide().getSlideParts()) {
		// batch.render(slidePart.getModelInstance(), lights);
		// }
		batch.render(bezPart.instance, lights);
		batch.end();
		hud.render();
		if (DebugDrawer.isDebug) {
			debugDrawer.render();
		}
		showFPS();

	}

	@Override
	public void resize(int width, int height) {
		camManager.setViewport(width, height);
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
		System.out.println("Dispose");
		world.dispose();
	}

	public void showFPS() {
		if (showFps) {
			// FPS anzeigen.
			spriteBatch.begin();
			font.draw(spriteBatch, Gdx.graphics.getFramesPerSecond()
					+ " fps, Bullet: "
					+ (int) (world.performanceCounter.load.value * 100f) + "%", 10,
					Gdx.graphics.getHeight() - 10);
			spriteBatch.end();
		}
	}
}
