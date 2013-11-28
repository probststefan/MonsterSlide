package fh.teamproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;

import fh.teamproject.controller.camera.ChaseCameraController;
import fh.teamproject.controller.camera.DebugCameraController;
import fh.teamproject.controller.camera.DebugInputController;
import fh.teamproject.entities.Player;
import fh.teamproject.entities.World;
import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.utils.CameraManager;
import fh.teamproject.utils.CameraManager.Mode;
import fh.teamproject.utils.DebugDrawer;

public class GameScreen implements Screen {
	// Den Debug-Modus von Bullet ein- und ausschalten.
	private final boolean showFps = true;
	public DebugDrawer debugDrawer = null;
	public ShapeRenderer lineRenderer = new ShapeRenderer();

	public CameraManager camManager;
	ModelBatch batch;
	Environment lights;

	World world;
	Player player;

	private SpriteBatch spriteBatch;
	private BitmapFont font;

	public GameScreen() {

		this.world = new World();
		this.player = (Player) this.world.getPlayer();

		DebugCameraController controller = new DebugCameraController(
				new PerspectiveCamera(67, Gdx.graphics.getWidth(),
						Gdx.graphics.getHeight()));
		ChaseCameraController chaseCamContr = new ChaseCameraController(
				new PerspectiveCamera(67, Gdx.graphics.getWidth(),
						Gdx.graphics.getHeight()), this.player);
		this.camManager = new CameraManager();
		this.camManager.addCamera(controller, Mode.FREE);
		this.camManager.addCamera(chaseCamContr, Mode.CHASE);
		this.camManager.setMode(Mode.CHASE);
		this.batch = new ModelBatch();

		this.lights = new Environment();
		this.lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f,
				1f));
		this.lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		DebugInputController processor = new DebugInputController(this);
		InputMultiplexer inputMul = new InputMultiplexer();
		inputMul.addProcessor(processor);
		inputMul.addProcessor(controller);
		Gdx.input.setInputProcessor(inputMul);

		if (this.showFps) {
			// Wird zur Zeit genutzt um die fps anzuzeigen.
			this.spriteBatch = new SpriteBatch();
			this.font = new BitmapFont();
		}

		// Debug
		this.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe,
				this.camManager.getActiveCamera().combined);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		this.camManager.update();
		/* UPDATE */
		this.world.update();

		/* RENDER */
		this.batch.begin(this.camManager.getActiveCamera());
		// Player rendern.
		this.batch.render(this.player.instance, this.lights);
		// Rutschelemente rendern.
		for (ISlidePart slidePart : this.world.getSlide().getSlideParts()) {
			this.batch.render(slidePart.getModelInstance(), this.lights);
		}

		if ((this.debugDrawer != null) && (this.debugDrawer.getDebugMode() > 0)) {
			this.debugDrawer.begin();
			this.world.getWorld().debugDrawWorld();
			this.debugDrawer.end();

			Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
			this.setDebugMode(this.getDebugMode(),
					this.camManager.getActiveCamera().combined);
			Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
		}
		this.batch.end();

		this.showFPS();
	}

	@Override
	public void resize(int width, int height) {
		this.camManager.setViewport(width, height);

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

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

	public void setDebugMode(final int mode, final Matrix4 projMatrix) {
		if ((mode == btIDebugDraw.DebugDrawModes.DBG_NoDebug) && (this.debugDrawer == null)) {
			return;
		}
		if (this.debugDrawer == null) {
			this.world.getWorld().setDebugDrawer(this.debugDrawer = new DebugDrawer());
		}
		this.debugDrawer.lineRenderer.setProjectionMatrix(projMatrix);
		this.debugDrawer.setDebugMode(mode);
	}

	public int getDebugMode() {
		return (this.debugDrawer == null) ? 0 : this.debugDrawer.getDebugMode();
	}
}