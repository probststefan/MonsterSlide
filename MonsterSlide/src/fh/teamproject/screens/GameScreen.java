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
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw.DebugDrawModes;

import fh.teamproject.entities.Player;
import fh.teamproject.entities.World;
import fh.teamproject.interfaces.ISlidePart;
import fh.teamproject.utils.DebugDrawer;

public class GameScreen implements Screen {
	// Den Debug-Modus von Bullet ein- und ausschalten.
	private final boolean debuggerOn = false;
	private final boolean showFps = true;
	public DebugDrawer debugDrawer = null;

	PerspectiveCamera camera;
	CameraInputController controller;

	ModelBatch batch;
	Environment lights;

	World world;
	Player player;

	private SpriteBatch spriteBatch;
	private BitmapFont font;

	public GameScreen() {
		this.camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		this.camera.translate(0f, 0f, 10f);
		this.controller = new CameraInputController(this.camera);

		this.world = new World();
		this.player = (Player) this.world.getPlayer();

		this.batch = new ModelBatch();

		this.lights = new Environment();
		this.lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f,
				1f));
		this.lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		InputMultiplexer inputMul = new InputMultiplexer();
		inputMul.addProcessor(this.controller);

		Gdx.input.setInputProcessor(inputMul);

		// Elemente der Rutsche zur Bullet-Welt hinzufuegen.
		for (ISlidePart slidePart : this.world.getSlide().getSlideParts()) {
			this.world.addRigidBody(slidePart.getRigidBody());
		}

		int i = 0;
		for (ISlidePart slidePart : this.world.getSlide().getSlideParts()) {

			if (i == 1) {
				slidePart.releaseAll();
			}
			i++;
		}

		// Spieler zur Bullet-Welt hinzufuegen.
		this.world.addRigidBody(this.player.getRigidBody());

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

		this.debugger();

		/* UPDATE */
		// this.camera.lookAt(this.player.position);
		this.camera.update();
		this.world.update();
		this.world.getSlide().update(this.player.getPosition());

		/* RENDER */
		this.batch.begin(this.camera);
		// Player rendern.
		this.batch.render(this.player.instance, this.lights);
		// Rutschelemente rendern.
		for (ISlidePart slidePart : this.world.getSlide().getSlideParts()) {
			if (slidePart.getAliveState() == false) {
				// SlidePart aus der Welt entfernen.
				world.removeRigidBody(slidePart.getRigidBody());
				slidePart.getRigidBody().dispose();
				slidePart.releaseAll();
				slidePart = null;
			} else {
				if (slidePart instanceof btDynamicsWorld == false) {
					world.addRigidBody(slidePart.getRigidBody());
				}

				this.batch.render(slidePart.getModelInstance(), this.lights);
			}
		}
		this.batch.end();

		this.showFPS();
		this.player.update();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	public void debugger() {
		if (this.debuggerOn) {
			// Stellt die Kollisionsobjekte von Bullet grafisch dar.
			Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
			this.setDebugMode(DebugDrawModes.DBG_DrawWireframe, this.camera.combined);
			Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);

			if ((this.debugDrawer != null) && (this.debugDrawer.getDebugMode() > 0)) {
				this.debugDrawer.begin();
				this.world.getWorld().debugDrawWorld();
				this.debugDrawer.end();
			}
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

	public void showFPS() {
		if (this.showFps) {
			// FPS anzeigen.
			this.spriteBatch.begin();
			this.font.draw(this.spriteBatch, Gdx.graphics.getFramesPerSecond() + " fps", 10,
					Gdx.graphics.getHeight() - 10);
			this.spriteBatch.end();
		}
	}
}
